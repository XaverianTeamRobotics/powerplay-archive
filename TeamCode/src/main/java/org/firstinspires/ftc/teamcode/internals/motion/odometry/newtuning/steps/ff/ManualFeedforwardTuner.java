package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.ff;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.kinematics.Kinematics;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
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

import java.util.ArrayList;
import java.util.Objects;

public class ManualFeedforwardTuner extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDriver driver = null;
    private NanoClock clock = null;
    private String firstMsg = "We're going to manually tune the KA feedforward value and manually refine the kV value.";

    private MotionProfile activeProfile = null;
    private double profileStart = 0.0;
    private boolean lastTouch = false;
    private final double DISTANCE = 72; // in
    private boolean movingForward = true;

    private final double TEST_DISTANCE = 60; // in

    private final ArrayList<Double> DISTANCES = new ArrayList<>();
    private boolean acceptable = false;
    private double avg = 0.0;
    private int testStep = 1;
    private boolean testStepFirstHalf = true;

    private enum Mode {
        DRIVER_MODE,
        TUNING_MODE
    }

    private enum Step {
        ALIGN,
        INSTRUCT,
        MANUAL,
        TEST,
        SHOW,
        NEXT
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
                    menuManager = Questions.askAsync(Devices.controller1, firstMsg + " First, use the second controller to drive your bot to the start of a " + Math.ceil(DISTANCE / 24.0) + " (over " + DISTANCE + " inch) tile long stretch of field tiles facing forward towards the stretch, then select Ok.", "Ok");
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
                Questions.askC1("In the Dashboard, graph the measuredVelocity, error, and targetVelocity values now. Your goal is to make the red line (measured velocity) match the green line (target velocity) as best you can. Visit bit.ly/graphtuning for some general information. I also highly recommend playing around with the simulator at bit.ly/fftuning and watching bit.ly/fftutorials before starting. If you need to manually reposition the robot during tuning, you can toggle driver control by pressing down on the touchpad on either controller. When you're done tuning, press A on either controller. When you're ready to begin, select Ok.", "Ok");
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
                    firstMsg = "We're going to manually tune your feedforward constants again.";
                    step = Step.TEST;
                }
                break;
            case TEST:
                // run the test 3 times, allowing for a loop of other vthreads in between
                switch(testStep) {
                    case 1:
                        test(true);
                        testStep++;
                        break;
                    case 2:
                    case 3:
                        test(false);
                        testStep++;
                        break;
                }
                // find the average distance when we're done and determine if its ok to continue
                if(DISTANCES.size() == 3) {
                    avg = (DISTANCES.get(0) + DISTANCES.get(1) + DISTANCES.get(2)) / 3;
                    acceptable = avg >= TEST_DISTANCE * 0.75 - TEST_DISTANCE && avg <= TEST_DISTANCE * 0.75 + TEST_DISTANCE;
                    DISTANCES.clear();
                    step = Step.SHOW;
                }
                break;
            case SHOW:
                Item ans;
                // let the user know whether they tuned well enough and that they should probably continue if it is good enough or reconfigure if it isnt
                if(acceptable) {
                    ans = Questions.askC1("Your feedforward gains seem to be accurate within 16%, with an average distance of " + avg + " inches over " + testStep + " trials when tasked with driving " + TEST_DISTANCE + " inches. You should not need to tune your feedforward values further. If you want to continue, select Continue, otherwise select Reconfigure.", "Continue", "Reconfigure");
                }else{
                    ans = Questions.askC1("Your feedforward gains seem to be inadequate for odometry, with an average distance of " + avg + " inches over " + testStep + " trials when tasked with driving " + TEST_DISTANCE + " inches, which is over the >16% of error required for proper path following. I highly recommend retuning your gains. If you want to continue without retuning, select Continue, otherwise select Reconfigure.", "Continue", "Reconfigure");
                }
                if(ans.equals("Continue")) {
                    step = Step.NEXT;
                }else{
                    step = Step.INSTRUCT;
                }
                // cleanup
                testStep = 1;
                avg = 0.0;
                acceptable = false;
            case NEXT:
                Questions.askC1("Your kA and kV should now be tuned properly. Select Ok when you're ready to continue.", "Ok");
                State.lateralMultiplierTuning = Affair.PRESENT;
                State.manualFeedforwardTuner = Affair.PAST;
                break;
        }
    }

    private void test(boolean first) {
        if(testStepFirstHalf) {
            // first, the user needs to position the robot -- so lets tell them to do that
            if(menuManager == null) {
                if(first) {
                    menuManager = Questions.askAsync(Devices.controller1, "We're going to test your feedforward constants a few times to confirm they're accurate enough. First, use the second controller to drive your bot to the start of a " + Math.ceil(TEST_DISTANCE / 24.0) + " (over " + TEST_DISTANCE + " inch) tile long stretch of field tiles facing forward towards the stretch, then select Ok.", "Ok");
                }else{
                    menuManager = Questions.askAsync(Devices.controller1, "Drive your bot back to the start of a " + Math.ceil(TEST_DISTANCE / 24.0) + " (over " + TEST_DISTANCE + " inch) tile long stretch of field tiles facing forward towards the stretch, then select Ok.", "Ok");
                }
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
                    menuManager = null;
                    driver.setMotorPowers(0, 0, 0, 0);
                    driver = null;
                    testStepFirstHalf = false;
                }
            }
        }else{
            // drive TEST_DISTANCE inches and record error
            driver = new AutonomousDriver(HardwareGetter.getHardwareMap());
            Trajectory trajectory = driver.trajectoryBuilder(new Pose2d())
                .forward(TEST_DISTANCE)
                .build();
            driver.followTrajectory(trajectory);
            DISTANCES.add(driver.getPoseEstimate().getX());
            // cleanup
            testStepFirstHalf = true;
            driver = null;
        }
    }

}
