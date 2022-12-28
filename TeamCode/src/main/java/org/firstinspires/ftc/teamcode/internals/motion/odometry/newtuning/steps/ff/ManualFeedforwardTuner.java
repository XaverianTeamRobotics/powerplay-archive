package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.ff;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.kinematics.Kinematics;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.acmerobotics.roadrunner.util.NanoClock;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.OdometrySettings;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDriver;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.State;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor;
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;

import java.util.Objects;

public class ManualFeedforwardTuner extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDriver driver = null;
    private NanoClock clock = null;
    private MotionProfile activeProfile = null;
    private double profileStart = 0.0;
    private boolean lastTouch = false;

    private final double DISTANCE = 72; // in
    private boolean movingForward = true;

    private enum Step {
        ALIGN,
        INSTRUCT,
        MANUAL,
        NEXT
    }

    private enum Mode {
        DRIVER_MODE,
        TUNING_MODE
    }

    private Mode mode = Mode.TUNING_MODE;

    private Step step = Step.ALIGN;

    private MotionProfile generateProfile() {
        MotionState start = new MotionState(movingForward ? 0 : DISTANCE, 0, 0, 0);
        MotionState goal = new MotionState(movingForward ? DISTANCE : 0, 0, 0, 0);
        return MotionProfileGenerator.generateSimpleMotionProfile(start, goal, OdometrySettings.MAX_VEL, OdometrySettings.MAX_ACCEL);
    }


    @Override
    public boolean when() {
        return State.manualFeedforwardTuner == Affair.PRESENT;
    }

    @Override
    public void loop() {
        switch(step) {
            case ALIGN:
                // first, the user needs to position the robot -- so lets tell them to do that
                if(menuManager == null) {
                    menuManager = Questions.askAsync(Devices.controller1, "We're going to manually tune the KA feedforward value and manually refine the kV value. First, use the second controller to drive your bot to the start of a " + Math.ceil(DISTANCE / 24.0) + " (over " + DISTANCE + " inch) tile long stretch of field tiles facing forward towards the stretch, then select Ok.", "Ok");
                }
                menuManager.runOnce();
                // we let them drive to the right spot
                if(driver == null) {
                    driver = new AutonomousDriver(HardwareGetter.getHardwareMap());
                    driver.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                driver.setWeightedDrivePower(
                    new Pose2d(
                        -Compressor.compress(Devices.controller2.getLeftStickY()),
                        -Compressor.compress(Devices.controller2.getLeftStickX()),
                        -Compressor.compress(Devices.controller2.getRightStickX())
                    )
                );
                driver.update();
                Item answer = menuManager.runOnce();
                // then we stop if the user's done
                if(answer != null) {
                    if(answer.equals("Ok")) {
                        step = Step.INSTRUCT;
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                }
                break;
            case INSTRUCT:
                Questions.askC1("We're going to tune and refine kA and kV manually. In the Dashboard, graph the measuredVelocity, error, and targetVelocity values now. Your goal is to make the red line (measured velocity) match the green line (target velocity) as best you can. Visit bit.ly/graphtuning for some general information. I also highly recommend playing around with the simulator at bit.ly/fftuning and watching bit.ly/fftutorials before starting. If you need to manually reposition the robot during tuning, you can toggle driver control by pressing down on the touchpad on either controller. When you're done tuning, press A on either controller. When you're ready to begin, select Ok.", "Ok");
                step = Step.MANUAL;
                break;
            case MANUAL:
                // inits
                if(driver == null) {
                    driver = new AutonomousDriver(HardwareGetter.getHardwareMap());
                    clock = NanoClock.system();
                    activeProfile = generateProfile();
                    profileStart = clock.seconds();
                    Logging.clear();
                    Logging.updateLog();
                    Logging.getTelemetry().clear();
                    Logging.getTelemetry().update();
                }
                switch(mode) {
                    case TUNING_MODE:
                        Logging.getTelemetry().addLine("Running. To stop and enable driver control, press down on the touchpad. When you're done tuning, press A on either controller.");
                        // toggle logic
                        if(Devices.controller1.getTouchpad() || Devices.controller2.getTouchpad() && !lastTouch) {
                            mode = Mode.DRIVER_MODE;
                            driver.setMotorPowers(0, 0, 0, 0);
                            lastTouch = true;
                            break;
                        }else if(!Devices.controller1.getTouchpad() && !Devices.controller2.getTouchpad()) {
                            lastTouch = false;
                        }
                        // switch direction if we hit the end
                        double profileTime = clock.seconds() - profileStart;
                        if(profileTime > activeProfile.duration()) {
                            // generate a new profile
                            movingForward = !movingForward;
                            activeProfile = generateProfile();
                            profileStart = clock.seconds();
                        }
                        // calculate power
                        MotionState motionState = activeProfile.get(profileTime);
                        double targetPower = Kinematics.calculateMotorFeedforward(motionState.getV(), motionState.getA(), OdometrySettings.kV, OdometrySettings.kA, OdometrySettings.kStatic);
                        // set the motor power
                        driver.setDrivePower(new Pose2d(targetPower, 0, 0));
                        driver.updatePoseEstimate();
                        Pose2d poseVelo = Objects.requireNonNull(driver.getPoseVelocity(), "poseVelocity() must not be null. Ensure that the getWheelVelocities() method has been overridden in your localizer.");
                        double currentVelo = poseVelo.getX();
                        // graph
                        Logging.getTelemetry().addData("targetVelocity", motionState.getV());
                        Logging.getTelemetry().addData("measuredVelocity", currentVelo);
                        Logging.getTelemetry().addData("error", motionState.getV() - currentVelo);
                        break;
                    case DRIVER_MODE:
                        Logging.getTelemetry().addLine("Stopped. To restart and disable driver control, press down on the touchpad. When you're done tuning, press A on either controller.");
                        // toggle logic
                        if(Devices.controller1.getTouchpad() || Devices.controller2.getTouchpad() && !lastTouch) {
                            mode = Mode.TUNING_MODE;
                            lastTouch = true;
                            driver.setMotorPowers(0, 0, 0, 0);
                            activeProfile = generateProfile();
                            profileStart = clock.seconds();
                            break;
                        }else if(!Devices.controller1.getTouchpad() && !Devices.controller2.getTouchpad()) {
                            lastTouch = false;
                        }
                        // driver control
                        driver.setWeightedDrivePower(
                            new Pose2d(
                                -Compressor.compress(Devices.controller2.getLeftStickY()),
                                -Compressor.compress(Devices.controller2.getLeftStickX()),
                                -Compressor.compress(Devices.controller2.getRightStickX())
                            )
                        );
                        // we should continue to update these values, otherwise the graph will break
                        Logging.getTelemetry().addData("targetVelocity", 0);
                        Logging.getTelemetry().addData("measuredVelocity", 0);
                        Logging.getTelemetry().addData("error", 0);
                        break;
                }
                // very very important. required for graphing
                Logging.getTelemetry().update();
                // cleanup procedure when user's completely done with tuning
                if(Devices.controller1.getA() || Devices.controller2.getA()) {
                    Logging.getTelemetry().addData("targetVelocity", 0);
                    Logging.getTelemetry().addData("measuredVelocity", 0);
                    Logging.getTelemetry().addData("error", 0);
                    Logging.getTelemetry().update();
                    Logging.getTelemetry().clear();
                    Logging.getTelemetry().update();
                    Logging.clear();
                    Logging.updateLog();
                    driver.setMotorPowers(0, 0, 0, 0);
                    driver = null;
                    mode = Mode.TUNING_MODE;
                    step = Step.NEXT;
                }
            case NEXT:
                Questions.askC1("Your kA and kV should now be tuned properly. Select Ok when you're ready to continue.", "Ok");
                State.manualFeedforwardTuner = Affair.PAST;
        }
    }

}
