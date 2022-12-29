package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDriver;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.constants.*;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.dw.EncoderForwardOffsetExperimentalTuner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.dw.EncoderTrackWidthExperimentalTuner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.ff.AutoFeedforwardTuner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.ff.LateralMultiplierTuner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.ff.ManualFeedforwardTuner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.xpconstraints.MaxVelocityTuner;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;

public class AutoTuning extends OperationMode implements TeleOperation {

    private MenuManager menuManager = null;
    private AutonomousDriver driver = null;

    public enum InitialTesting {
        TEST,
        RETUNE,
        COMPLETE
    }

    @Override
    public void construct() {
        registerFeature(new MotorConfigSetup());
        registerFeature(new RPMTuner());
        registerFeature(new TickTuner());
        registerFeature(new WheelRadiusTuner());
        registerFeature(new GearRatioTuner());
        registerFeature(new TrackWidthEstimateTuner());
        registerFeature(new BaseConstraintEstimateTuner());
        registerFeature(new EncoderTickTuner());
        registerFeature(new EncoderWheelRadiusTuner());
        registerFeature(new EncoderGearRatioTuner());
        registerFeature(new EncoderTrackWidthEstimateTuner());
        registerFeature(new EncoderForwardOffsetEstimateTuner());
        registerFeature(new MaxVelocityTuner());
        registerFeature(new EncoderTrackWidthExperimentalTuner());
        registerFeature(new EncoderForwardOffsetExperimentalTuner());
        registerFeature(new AutoFeedforwardTuner());
        registerFeature(new ManualFeedforwardTuner());
        registerFeature(new LateralMultiplierTuner());
    }

    @Override
    public void run() {
        if(State.beginningDirections == Affair.PRESENT) {
            // first we go through all the constants
            Questions.ask(new Menu.MenuBuilder().setDescription("This OpMode will go through a series of tuning stages to set up your odometry system. First, navigate to http://192.168.43.1:8080/dash and open the OdometrySettings dropdown. You'll need to edit those values throughout the tuning process. The values you set there will be saved at the end of the tuning process. Place your bot on an empty square of field tiles. Make sure you're using a freshly-charged battery. Connect two controllers to the Driver Station. All interactions with the menu will be made with the first gamepad, and all interactions with the robot will be made with the second gamepad. When you're ready, select Start Tuning.").addItem("Start Tuning").build(), Devices.controller1);
            State.beginningDirections = Affair.PAST;
            State.constantDirections = Affair.PRESENT;
        }else if(State.beginPhysicalTuning == Affair.PRESENT) {
            // now we need to start doing physical tuning, but first we need to test the constant motor and encoder values are right
            Questions.ask(new Menu.MenuBuilder().setDescription("You're done defining the initial values! Soon we will begin physically tuning the robot. Before that though, we're going to do a test to make sure your values are correct. When you're ready, select Ok.").addItem("Ok").build(), Devices.controller1);
            switch(State.initialTesting) {
                case TEST:
                    // so, we let the user run the robot around a bit and if they think something needs to be retuned, they can retune it
                    // we first init the fancy async menu (needs to be async so they can control the bot at the same time as the menu)
                    if(menuManager == null) {
                        menuManager = Questions.askAsync(Devices.controller1, "You should be able to drive your robot around with the second gamepad. Drive the bot and check the dashboard's field viewer. If the bot is driving correctly (in real life) and the \"bot\" drawn on the dashboard seems to roughly line up with the physical bot, select Continue. Otherwise, select Reconfigure and change the names/directions of your motors and encoders.", "Continue", "Reconfigure");
                    }
                    menuManager.runOnce();
                    // then the drivetrain
                    if(driver == null) {
                        driver = new AutonomousDriver(HardwareGetter.getHardwareMap());
                        driver.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    }
                    // and they can drive it
                    driver.setWeightedDrivePower(
                        new Pose2d(
                            -Compressor.compress(Devices.controller2.getLeftStickY()),
                            -Compressor.compress(Devices.controller2.getLeftStickX()),
                            -Compressor.compress(Devices.controller2.getRightStickX())
                        )
                    );
                    driver.update();
                    // and lastly they can choose what to do next, while they're driving!
                    Item answer = menuManager.runOnce();
                    if(answer != null) {
                        if(answer.equals("Continue")) {
                            State.initialTesting = InitialTesting.COMPLETE;
                        }else{
                            State.initialTesting = InitialTesting.RETUNE;
                        }
                        menuManager = null;
                        driver.setMotorPowers(0, 0, 0, 0);
                        driver = null;
                    }
                    break;
                case RETUNE:
                    // we dont need to do as much stuff here, just let the user do their things
                    Questions.ask(new Menu.MenuBuilder().setDescription("Update your drive and encoder motor names and directions in the settings, then select Ok.").addItem("Ok").build(), Devices.controller1);
                    State.initialTesting = InitialTesting.TEST;
                    break;
            }
            // now lets start the actual physical tuning
            Questions.ask(new Menu.MenuBuilder().setDescription("Ok, now that you've tested your initial values we can begin physical tuning! Select Start Tuning when you're ready.").addItem("Start Tuning").build(), Devices.controller1);
            State.beginPhysicalTuning = Affair.PAST;
            State.maxVelocityTuner = Affair.PRESENT;
        }
    }

}
