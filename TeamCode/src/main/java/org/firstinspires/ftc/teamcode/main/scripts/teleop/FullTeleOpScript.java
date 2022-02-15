package org.firstinspires.ftc.teamcode.main.scripts.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.main.utils.autonomous.image.opencv.pipelines.StorageLocatorPipeline;
import org.firstinspires.ftc.teamcode.main.utils.gamepads.GamepadManager;
import org.firstinspires.ftc.teamcode.main.utils.helpers.elevator.ElevatorDriver;
import org.firstinspires.ftc.teamcode.main.utils.interactions.items.StandardColorSensor;
import org.firstinspires.ftc.teamcode.main.utils.interactions.items.StandardMotor;
import org.firstinspires.ftc.teamcode.main.utils.interactions.items.StandardServo;
import org.firstinspires.ftc.teamcode.main.utils.io.InputSpace;
import org.firstinspires.ftc.teamcode.main.utils.io.OutputSpace;
import org.firstinspires.ftc.teamcode.main.utils.locations.*;
import org.firstinspires.ftc.teamcode.main.utils.resources.Resources;
import org.firstinspires.ftc.teamcode.main.utils.scripting.TeleOpScript;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class FullTeleOpScript extends TeleOpScript {

    private GamepadManager gamepadManager;
    private InputSpace inputSpace;
    private OutputSpace outputSpace;
    private boolean intakeShouldBeDown = false, intakeButtonWasDown = false, isAllowedToControlElevator = false, noControlIntakeLifter = false, elevatorButtonWasDown = false, elevatorShouldBeManuallyControlled = false;
    private ElevatorDriver elevatorDriver;
    private OpenCvCamera CAMERA;
    private StorageLocatorPipeline SHIPPING_PIPELINE = new StorageLocatorPipeline();

    public FullTeleOpScript(LinearOpMode opMode) {
        super(opMode);
        // set telemetry to monospace for better text formatting
        getOpMode().telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
        /*
         * GamepadManager Functions:
         *  F1: Driving
         *  F2: Intake Motor Control, Intake Lift Control
         *  F3: Lift Control
         *  F4: Duck Spinner Control
         *  F5: Unassigned
         *  F6: Manual Intake Control
         * Users:
         *  U1: F1, F4
         *  U2: F2, F3, F6
         * */
        // setup gamepads
        gamepadManager = new GamepadManager(getOpMode().gamepad1, getOpMode().gamepad2, getOpMode().gamepad2, getOpMode().gamepad1, getOpMode().gamepad1, getOpMode().gamepad2);
        getOpMode().gamepad1.reset();
        getOpMode().gamepad2.reset();
        // setup control spaces
        inputSpace = new InputSpace(getOpMode().hardwareMap);
        outputSpace = new OutputSpace(getOpMode().hardwareMap);
        // setup elevator driver
        elevatorDriver = new ElevatorDriver(inputSpace, outputSpace, getOpMode());
        elevatorDriver.setFeedbackDestination(gamepadManager);
        elevatorDriver.setManualController(gamepadManager);
        elevatorDriver.setIntakeToggleController(gamepadManager);
        // put everything in their default positions, or auto-calibration
        inputSpace.sendInputToLeftHandGrabber(LeftHandGrabbingServoLocation.Action.SET_POSITION, 90);
        inputSpace.sendInputToRightHandGrabber(RightHandGrabbingServoLocation.Action.SET_POSITION, 37);
        inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 22);
        calibrateElevator();
        /*
         * VALUES OF INTAKE LIFTER:
         * LOW: 22
         * HIGH: 60
         * */
        inputSpace.sendInputToIntakeLifter(IntakeLiftingServoLocation.Action.SET_POSITION, 60);
        // setup camera
        // FIXME: this needs to work n stuff
        // FIXME: add lib-opencv.so or whatever it is onto robot
//        WebcamName webCam = getOpMode().hardwareMap.get(WebcamName.class, Resources.Misc.Webcam);
//        CAMERA = OpenCvCameraFactory.getInstance().createWebcam(webCam);
//        CAMERA.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
//
//            @Override
//            public void onOpened() {
//                CAMERA.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT);
//                CAMERA.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
//                CAMERA.setPipeline(SHIPPING_PIPELINE);
//            }
//
//            @Override
//            public void onError(int errorCode) {}
//
//        });
        // alert drivers robot is ready
        gamepadManager.functionOneGamepad().rumble(1000);
        gamepadManager.functionTwoGamepad().rumble(1000);
        gamepadManager.functionThreeGamepad().rumble(1000);
        gamepadManager.functionFourGamepad().rumble(1000);
        gamepadManager.functionFiveGamepad().rumble(1000);
        gamepadManager.functionSixGamepad().rumble(1000);
    }

    @Override
    public void main() {
        controlDrivetrain();
        controlIntakeLifter();
        controlIntake();
        controlEntireLiftAutonomously();
        controlDuck();
        updateLiftControlPermissions();
        // FIXME: fix this
//        controlElevatorCamera();
    }

    private void calibrateElevator() {
        // move elevator up for a second
        int timeAsOfLastElevatorCalibrationBegin = (int) getOpMode().time;
        while(outputSpace.receiveOutputFromElevatorBottomLimitSwitch(ElevatorBottomLimitSwitchLocation.Values.PRESSED) == 0 && timeAsOfLastElevatorCalibrationBegin > (int) getOpMode().time - 1) {
            inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_SPEED, -100);
            inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_SPEED, -100);
        }
        // move elevator down until it reaches the bottom
        while(outputSpace.receiveOutputFromElevatorBottomLimitSwitch(ElevatorBottomLimitSwitchLocation.Values.PRESSED) == 0) {
            inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_SPEED, 30);
            inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_SPEED, 30);
        }
        // reset the elevator
        ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).reset();
        ((StandardMotor) inputSpace.getElevatorRightLift().getInternalInteractionSurface()).reset();
    }

    private void controlDrivetrain() {
        // calculate the x and y speeds
        int left = (int) Range.clip((gamepadManager.functionOneGamepad().left_stick_y) * 75, -75, 75);
        int right = (int) Range.clip((gamepadManager.functionOneGamepad().right_stick_y) * 75, -75, 75);
        // set the defined speeds
        inputSpace.sendInputToTank(TankDrivetrainLocation.Action.SET_SPEED, -right, -left);
    }

    private void controlIntakeLifter() {
        // move the intake based on the left bumper's state
        if(gamepadManager.functionTwoGamepad().left_bumper && !noControlIntakeLifter) {
            if(!intakeButtonWasDown) {
                intakeShouldBeDown = !intakeShouldBeDown;
            }
            intakeButtonWasDown = true;
        }else{
            intakeButtonWasDown = false;
        }
        if(intakeShouldBeDown) {
            inputSpace.sendInputToIntakeLifter(IntakeLiftingServoLocation.Action.SET_POSITION, 22);
        }else{
            inputSpace.sendInputToIntakeLifter(IntakeLiftingServoLocation.Action.SET_POSITION, 60);
        }
    }

    public void updateLiftControlPermissions() {
        isAllowedToControlElevator = ((StandardServo) inputSpace.getIntakeLifter().getInternalInteractionSurface()).getPosition() != 60;
        if(!elevatorDriver.isStable()) {
            noControlIntakeLifter = true;
            intakeShouldBeDown = true;
            inputSpace.sendInputToIntakeLifter(IntakeLiftingServoLocation.Action.SET_POSITION, 22);
        }
    }

    private void controlIntake() {
        // control the intake motor based on the trigger inputs
        int intakeGas = (int) Range.clip(gamepadManager.functionTwoGamepad().right_trigger * 100, 0, 100);
        int intakeBrake = (int) Range.clip(gamepadManager.functionTwoGamepad().left_trigger * 100, 0, 100);
        int intakeSpeed = Range.clip(intakeGas - intakeBrake, -100, 100);
        inputSpace.sendInputToIntakeSpinner(IntakeSpinningMotorLocation.Action.SET_SPEED, intakeSpeed);
    }

    /**
     * This method controls all the autonomous stuff for the lift in TeleOps. Basically, it contains a bunch of routines. On every run, if no routine is running and a button is pressed to toggle a certain routine, the routine will fire. It will enable its routine, making all other routines impossible to run. During running, controllers will give feedback via vibrations to the user to let them know the elevator is performing a routine. Once a routine is complete, they will stop and the elevator will be able to run another routine once input is received.
     */
    private void controlEntireLiftAutonomously() {
        double[] hsv = ((StandardColorSensor) outputSpace.getHandColorSensor().getInternalInteractionSurface()).getHSV();
        int distance = (int) outputSpace.receiveOutputFromHandDistanceSensor();
        elevatorDriver.run();
        if(isAllowedToControlElevator) {
            // enables intake pos routine if requested
            if(gamepadManager.functionThreeGamepad().a) {
                elevatorDriver.setToIntakePosition();
            }
            // enables lower level ball routine if requested
            if(gamepadManager.functionThreeGamepad().b) {
                if(hsv[0] > 130) {
                    elevatorDriver.setToLowerBallPosition();
                }else{
                    elevatorDriver.setToLowerBlockPosition();
                }
            }
            // enables middle level ball routine routine if requested
            if(gamepadManager.functionThreeGamepad().y) {
                if(hsv[0] > 130) {
                    elevatorDriver.setToMediumBallPosition();
                }else{
                    elevatorDriver.setToMediumBlockPosition();
                }
            }
            // enables top level ball routine if requested
            if(gamepadManager.functionThreeGamepad().x) {
                if(hsv[0] > 130) {
                    elevatorDriver.setToTopBallPosition();
                }else{
                    elevatorDriver.setToTopBlockPosition();
                }
            }
            // FIXME: this is horribly broken. needs fixing or we wont be able to put our object on the top
//            // toggles manual control
//            if(gamepadManager.functionTwoGamepad().right_bumper) {
//                if(!elevatorButtonWasDown) {
//                    elevatorShouldBeManuallyControlled = !elevatorShouldBeManuallyControlled;
//                }
//                elevatorButtonWasDown = true;
//            }else{
//                elevatorButtonWasDown = false;
//            }
//            // enables/disables manual control based on toggle
//            if(elevatorShouldBeManuallyControlled) {
//                elevatorDriver.enableManualControl();
//            }else{
//                elevatorDriver.disableManualControl();
//            }
        }
    }

    private void controlDuck() {
        // turn duck motor slowly in the correct direction
        int speed = gamepadManager.functionFourGamepad().right_bumper ? -50 : 0;
        speed += gamepadManager.functionFourGamepad().left_bumper ? 50 : 0;
        inputSpace.sendInputToDuckMotor(DuckMotorLocation.Action.SET_SPEED, speed);
    }

    private void controlElevatorCamera() {
        int result = SHIPPING_PIPELINE.getResult();
        int[][] map = new int[][] {
            { 0, 0, 0 },
            { 0, 0, 0 },
            { 0, 0, 0}
        };
        int row = 2;
        if(result <= 6) {
            row = 1;
        }
        if(result <= 3) {
            row = 0;
        }
        int col = result % 3;
        map[row][col] = 1;
        String str1 = map[0][0] + " " + map[0][1] + " " + map[0][2];
        String str2 = map[1][0] + " " + map[1][1] + " " + map[1][2];
        String str3 = map[2][0] + " " + map[2][1] + " " + map[2][2];
        getOpMode().telemetry.log().clear();
        getOpMode().telemetry.log().add(str1);
        getOpMode().telemetry.log().add(str2);
        getOpMode().telemetry.log().add(str3);
        getOpMode().telemetry.update();
    }

    @Override
    public void stop() {
        inputSpace.stop();
        outputSpace.stop();
    }

    // TODO: rumbling feedback change - for the driver, the feedback they gain from rumbles should be blips instead of constant. 1 blip for lower level, 2 blips for med level, 3 blips for top level. this is so they know where to position the robot.
    // TODO: make the distance sensor stricter to force objects to be closer because currently it can detect objects 12 cm away which is too far

}
