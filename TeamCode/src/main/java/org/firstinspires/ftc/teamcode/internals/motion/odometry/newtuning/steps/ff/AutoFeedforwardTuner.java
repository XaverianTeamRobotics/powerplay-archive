package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.ff;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.util.NanoClock;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.OdometrySettings;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDriver;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.ConstantUtils;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.State;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.LoggingUtil;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.RegressionUtil;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Shrinker;
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;

import java.util.ArrayList;
import java.util.List;

public class AutoFeedforwardTuner extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDriver driver = null;

    private final double MAX_POWER = 0.7;
    private final double DISTANCE = 100; // in
    String result;

    private enum Step {
        ALIGN_AUTO,
        AUTO,
        SHOW,
        NEXT
    }

    private Step step = Step.ALIGN_AUTO;

    @Override
    public boolean when() {
        return State.autoFeedforwardTuner == Affair.PRESENT;
    }

    @Override
    public void loop() {
        switch(step) {
            case ALIGN_AUTO:
                // first, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, "We're going to automatically tune the kV and kStatic feedforward values. First, use the second controller to drive your bot to the start of a " + Math.ceil(DISTANCE / 24.0) + " (over " + DISTANCE + " inch) tile long stretch of field tiles facing forward towards the stretch, then select Ok.", "Ok");
                }
                menuManager.runOnce();
                // we let them drive to the right spot
                if(driver == null) {
                    driver = new AutonomousDriver(HardwareGetter.getHardwareMap());
                    driver.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                driver.setWeightedDrivePower(
                    new Pose2d(
                        -Shrinker.shrink(Devices.controller2.getLeftStickY()),
                        -Shrinker.shrink(Devices.controller2.getLeftStickX()),
                        -Shrinker.shrink(Devices.controller2.getRightStickX())
                    )
                );
                driver.update();
                Item answer = menuManager.runOnce();
                // then we stop if the user's done
                if(answer != null) {
                    if(answer.equals("Ok")) {
                        step = Step.AUTO;
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                }
                break;
            case AUTO:
                // i would rather this be async, buuuuuuut the logic works out a bit easier this way and tbqh accuracy is so important here im a bit worried that pausing execution for other vthreads would be bad
                // therefore, synchronous!
                Logging.log("Tuning kV and kStatic...");
                Logging.updateLog();
                // inits
                driver = new AutonomousDriver(HardwareGetter.getHardwareMap());
                NanoClock clock = NanoClock.system();
                double maxVel = ConstantUtils.rpmToVelocity(OdometrySettings.MAX_RPM);
                double finalVel = MAX_POWER * maxVel;
                double accel = (finalVel * finalVel) / (2.0 * DISTANCE);
                double rampTime = Math.sqrt(2.0 * DISTANCE / accel);
                List<Double> timeSamples = new ArrayList<>();
                List<Double> positionSamples = new ArrayList<>();
                List<Double> powerSamples = new ArrayList<>();
                // begin driving
                driver.setPoseEstimate(new Pose2d());
                double startTime = clock.seconds();
                while(true) {
                    // slowly speed up and take measurements along the way
                    double elapsedTime = clock.seconds() - startTime;
                    if (elapsedTime > rampTime) {
                        break;
                    }
                    double vel = accel * elapsedTime;
                    double power = vel / maxVel;
                    timeSamples.add(elapsedTime);
                    positionSamples.add(driver.getPoseEstimate().getX());
                    powerSamples.add(power);
                    driver.setDrivePower(new Pose2d(power, 0.0, 0.0));
                    driver.updatePoseEstimate();
                }
                // stop and calculate the regression. this is beyond my math skills. i dont really know whats happening here
                driver.setDrivePower(new Pose2d(0.0, 0.0, 0.0));
                RegressionUtil.RampResult rampResult = RegressionUtil.fitRampData(
                    timeSamples, positionSamples, powerSamples, true,
                    LoggingUtil.getLogFile(Misc.formatInvariant(
                        "DriveRampRegression-%d.csv", System.currentTimeMillis())));
                // aaaaaaand cleanup
                result = Misc.formatInvariant("Your results are a kV of %.5f and a kStatic of %.5f (R^2 = %.2f).",
                    rampResult.kV, rampResult.kStatic, rampResult.rSquare);
                driver = null;
                step = Step.SHOW;
                break;
            case SHOW:
                Questions.askC1(result + " Set your kV and kStatic settings to these numbers, then select Continue.", "Continue");
                step = Step.NEXT;
                break;
            case NEXT:
                State.manualFeedforwardTuner = Affair.PRESENT;
                State.autoFeedforwardTuner = Affair.PAST;
        }
    }

}
