package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.ff;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.OdometrySettings;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDriver;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.State;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;

public class ManualFeedforwardTuner extends Feature implements Conditional {

    private final double DISTANCE = 72; // in
    private FtcDashboard dashboard = FtcDashboard.getInstance();
    private MenuManager menuManager = null;
    private AutonomousDriver driver = null;

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

    private MotionProfile generateProfile(boolean movingForward) {
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
                        -Devices.controller2.getLeftStickY(),
                        -Devices.controller2.getLeftStickX(),
                        -Devices.controller2.getRightStickX()
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
                Questions.askC1("We're going to manually tune and refine kA and kV respectively now. In the Dashboard, graph the measuredVelocity and targetVelocity values now. Your goal is to make the red (measured) velocity match the green (target) velocity as best as you can. Visit bit.ly/graphtuning for some general information. I also highly recommend playing around with the simulator at bit.ly/fftuning and watching bit.ly/fftutorials before starting. If you need to manually reposition the robot during tuning, you can toggle driver control by pressing the touchpad on either controller. When you're ready to begin, select Ok.", "Ok");
                step = Step.MANUAL;
                break;
                // todo: make target velo green and measured velo red
                // todo: touchpad toggle driver control
        }
    }

}
