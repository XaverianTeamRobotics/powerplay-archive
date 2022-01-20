package org.firstinspires.ftc.teamcode.main.scripts.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.main.utils.gamepads.GamepadManager;
import org.firstinspires.ftc.teamcode.main.utils.interactions.items.StandardMotor;
import org.firstinspires.ftc.teamcode.main.utils.interactions.items.StandardServo;
import org.firstinspires.ftc.teamcode.main.utils.io.InputSpace;
import org.firstinspires.ftc.teamcode.main.utils.io.OutputSpace;
import org.firstinspires.ftc.teamcode.main.utils.locations.DuckMotorLocation;
import org.firstinspires.ftc.teamcode.main.utils.locations.ElevatorBottomLimitSwitchLocation;
import org.firstinspires.ftc.teamcode.main.utils.locations.ElevatorLeftLiftMotorLocation;
import org.firstinspires.ftc.teamcode.main.utils.locations.ElevatorRightLiftMotorLocation;
import org.firstinspires.ftc.teamcode.main.utils.locations.HandSpinningServoLocation;
import org.firstinspires.ftc.teamcode.main.utils.locations.IntakeLiftingServoLocation;
import org.firstinspires.ftc.teamcode.main.utils.locations.IntakeSpinningMotorLocation;
import org.firstinspires.ftc.teamcode.main.utils.locations.TankDrivetrainLocation;
import org.firstinspires.ftc.teamcode.main.utils.scripting.TeleOpScript;

import java.util.HashMap;

public class FullTeleOpScript extends TeleOpScript {

    // TODO: Full manual control. This includes toggling between manual and automatic control, and the ability to escape automatic routines once manual mode is activated. This is needed on the elevator, hand, and intake.

    private GamepadManager gamepadManager;
    private double timeAsOfLastIntakeMovement, timeAsOfLastFullLiftMovement;
    private int intakeLowerPos, intakeUpperPos, step;
    private boolean intakeShouldBeDown, intakeIsAtPosition, manualMode;
    private boolean isMovingToLBall, isMovingToMBall, isMovingToTBall, isMovingToLBlock, isMovingToMBlock, isMovingToTBlock, isMovingToBasePos;
    private InputSpace inputSpace;
    private OutputSpace outputSpace;

    public FullTeleOpScript(LinearOpMode opMode) {
        super(opMode);
        // set fields and calibrate robot
        assignValues();
        calibrateElevator();
        calibrateIntake();
    }

    @Override
    public void main() {
        // control robot
        controlDrivetrain();
        controlIntakeLifter();
        // debug
        intakeShouldBeDown = true; intakeIsAtPosition = true;
        controlIntake();
        // these methods are for manual control of the lift. currently, they do not work with the controlEntireLiftAutonomously() method that well. they technically function but it's not idea. for now, comment that method out if you uncomment these two
//        controlElevator();
//        controlHand();
        controlEntireLiftAutonomously();
        controlDuck();
        // debug
        debug();
    }

    private void assignValues() {
        inputSpace = new InputSpace(getOpMode().hardwareMap);
        outputSpace = new OutputSpace(getOpMode().hardwareMap);
        gamepadManager = new GamepadManager(getOpMode().gamepad1, getOpMode().gamepad1, getOpMode().gamepad1, getOpMode().gamepad1, getOpMode().gamepad1, getOpMode().gamepad1);
        timeAsOfLastIntakeMovement = 0;
        intakeLowerPos = 10;
        intakeUpperPos = 30;
        intakeShouldBeDown = false;
        intakeIsAtPosition = false;
        manualMode = false;
        isMovingToLBall = false;
        isMovingToMBall = false;
        isMovingToTBall = false;
        isMovingToLBlock = false;
        isMovingToMBlock = false;
        isMovingToTBlock = false;
        isMovingToBasePos = false;
        timeAsOfLastIntakeMovement = 0;
        step = 0;
    }

    private void calibrateElevator() {
        int timeAsOfLastElevatorCalibrationBegin = (int) getOpMode().time;
        while(outputSpace.receiveOutputFromElevatorBottomLimitSwitch(ElevatorBottomLimitSwitchLocation.Values.PRESSED) == 0 && timeAsOfLastElevatorCalibrationBegin > (int) getOpMode().time - 1) {
            inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_SPEED, -100);
            inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_SPEED, -100);
        }
        while(outputSpace.receiveOutputFromElevatorBottomLimitSwitch(ElevatorBottomLimitSwitchLocation.Values.PRESSED) == 0) {
            inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_SPEED, 30);
            inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_SPEED, 30);
        }
        ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).reset();
        ((StandardMotor) inputSpace.getElevatorRightLift().getInternalInteractionSurface()).reset();
        inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 23);
    }

    private void calibrateIntake() {
        inputSpace.sendInputToIntakeLifter(IntakeLiftingServoLocation.Action.SET_POSITION, 80);
    }

    private void controlDrivetrain() {
        int left = (int) Range.clip((gamepadManager.functionOneGamepad().left_stick_y - gamepadManager.functionOneGamepad().left_stick_x) * 75, -75, 75);
        int right = (int) Range.clip((gamepadManager.functionOneGamepad().left_stick_y + gamepadManager.functionOneGamepad().left_stick_x) * 75, -75, 75);
        inputSpace.sendInputToTank(TankDrivetrainLocation.Action.SET_SPEED, -right, -left);
    }

    private void controlIntakeLifter() {
        if(gamepadManager.functionOneGamepad().right_stick_y >= 1.0) {
            inputSpace.sendInputToIntakeLifter(IntakeLiftingServoLocation.Action.SET_POSITION, 80);
        }else if(gamepadManager.functionOneGamepad().right_stick_y <= -1.0) {
            inputSpace.sendInputToIntakeLifter(IntakeLiftingServoLocation.Action.SET_POSITION, 40);
        }
        // uncomment this for manual control. For some reason it's buggy, I'll reimplement this later
//        if(gamepadManager.functionOneGamepad().dpad_left && timeAsOfLastIntakeMovement + 0.25 <= getOpMode().time) {
//            int pos = ((StandardServo) inputSpace.getHandSpinner().getInternalInteractionSurface()).getPosition();
//            inputSpace.sendInputToIntakeLifter(IntakeLiftingServoLocation.Action.SET_POSITION, Range.clip(pos + 1, 40, 80));
//            timeAsOfLastIntakeMovement = getOpMode().time;
//        }else if(gamepadManager.functionOneGamepad().dpad_right && timeAsOfLastIntakeMovement + 0.25 <= getOpMode().time) {
//            int pos = ((StandardServo) inputSpace.getHandSpinner().getInternalInteractionSurface()).getPosition();
//            inputSpace.sendInputToIntakeLifter(IntakeLiftingServoLocation.Action.SET_POSITION, Range.clip(pos - 1, 40, 80));
//            timeAsOfLastIntakeMovement = getOpMode().time;
//        }
    }

    private void controlIntake() {
        int intakeGas = (int) Range.clip(gamepadManager.functionOneGamepad().left_trigger * 100, 0, 100);
        int intakeBrake = (int) Range.clip(gamepadManager.functionOneGamepad().right_trigger * 100, 0, 100);
        int intakeSpeed = Range.clip(intakeGas - intakeBrake, -100, 100);
        inputSpace.sendInputToIntakeSpinner(IntakeSpinningMotorLocation.Action.SET_SPEED, intakeSpeed);
    }

    /**
    This method controls all the autonomous stuff for the lift in TeleOps. Basically, it contains a bunch of routines. On every run, if no routine is running and a button is pressed to toggle a certain routine, the routine will fire. It will enable its routine, making all other routines impossible to run. It also has a step counter for routines with multiple steps. All of the steps are inside the statement checking if the routine is enabled.
     */
    private void controlEntireLiftAutonomously() {
        // enables base pos routine if requested
        if(gamepadManager.functionOneGamepad().a && !isMovingToBasePos && !isMovingToLBall && !isMovingToMBall && !isMovingToTBall && !isMovingToLBlock && !isMovingToMBlock && !isMovingToTBlock) {
            isMovingToBasePos = true;
            step = 0;
        }
        // moves to base pos
        if(isMovingToBasePos) {
            if(step == 0) {
                inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 23);
                timeAsOfLastFullLiftMovement = getOpMode().time;
                step++;
            }
            if(step == 1 && timeAsOfLastFullLiftMovement + 1.5 <= getOpMode().time) {
                inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_POSITION, 0);
                inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_POSITION, 0);
                step++;
            }
            if(step == 2 && outputSpace.receiveOutputFromElevatorBottomLimitSwitch(ElevatorBottomLimitSwitchLocation.Values.PRESSED) != 0) {
                inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_SPEED, 0);
                inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_SPEED, 0);
                ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).reset();
                ((StandardMotor) inputSpace.getElevatorRightLift().getInternalInteractionSurface()).reset();
                isMovingToBasePos = false;
                step = 0;
            }
        }
        // enables lower level ball routine if requested
        if(gamepadManager.functionOneGamepad().b && !isMovingToBasePos && !isMovingToLBall && !isMovingToMBall && !isMovingToTBall && !isMovingToLBlock && !isMovingToMBlock && !isMovingToTBlock) {
            isMovingToLBall = true;
            step = 0;
        }
        // dispenses ball at lower level
        if(isMovingToLBall) {
            if(step == 0) {
                inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_POSITION, -500);
                inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_POSITION, -500);
                timeAsOfLastFullLiftMovement = getOpMode().time;
                step++;
            }
            if(step == 1 && ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).getDcMotor().getCurrentPosition() <= -500) {
                inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 33);
                timeAsOfLastFullLiftMovement = getOpMode().time;
                step++;
            }
            if(step == 2 && timeAsOfLastFullLiftMovement + 0.25 <= getOpMode().time) {
                inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_POSITION, 0);
                inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_POSITION, 0);
                step++;
            }
            if(step == 3 && ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).getDcMotor().getCurrentPosition() >= -20) {
                timeAsOfLastFullLiftMovement = getOpMode().time;
                inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 36);
                step++;
            }
            if(step == 4 && timeAsOfLastFullLiftMovement + 2 <= getOpMode().time) {
                inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 31);
                inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_POSITION, -500);
                inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_POSITION, -500);
                step++;
            }
            if(step == 5 && ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).getDcMotor().getCurrentPosition() <= -500) {
                step = 0;
                isMovingToLBall = false;
                isMovingToBasePos = true;
            }
        }
        // enables middle level ball routine routine if requested
        if(gamepadManager.functionOneGamepad().y && !isMovingToBasePos && !isMovingToLBall && !isMovingToMBall && !isMovingToTBall && !isMovingToLBlock && !isMovingToMBlock && !isMovingToTBlock) {
            isMovingToMBall = true;
            step = 0;
        }
        // dispenses ball at middle level
        if(isMovingToMBall) {
            if(step == 0) {
                inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_POSITION, -500);
                inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_POSITION, -500);
                timeAsOfLastFullLiftMovement = getOpMode().time;
                step++;
            }
            if(step == 1 && ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).getDcMotor().getCurrentPosition() <= -500) {
                inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 33);
                timeAsOfLastFullLiftMovement = getOpMode().time;
                step++;
            }
            if(step == 2 && timeAsOfLastFullLiftMovement + 0.25 <= getOpMode().time) {
                inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_POSITION, -300);
                inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_POSITION, -300);
                step++;
            }
            if(step == 3 && ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).getDcMotor().getCurrentPosition() >= -320) {
                timeAsOfLastFullLiftMovement = getOpMode().time;
                inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 36);
                step++;
            }
            if(step == 4 && timeAsOfLastFullLiftMovement + 2 <= getOpMode().time) {
                inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 31);
                inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_POSITION, -500);
                inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_POSITION, -500);
                step++;
            }
            if(step == 5 && ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).getDcMotor().getCurrentPosition() <= -500) {
                step = 0;
                isMovingToMBall = false;
                isMovingToBasePos = true;
            }
        }
        // enables top level ball routine if requested
        if(gamepadManager.functionOneGamepad().x && !isMovingToBasePos && !isMovingToLBall && !isMovingToMBall && !isMovingToTBall && !isMovingToLBlock && !isMovingToMBlock && !isMovingToTBlock) {
            isMovingToTBall = true;
            step = 0;
        }
        // dispenses ball at top level
        if(isMovingToTBall) {
            if(step == 0) {
                inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_POSITION, -700);
                inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_POSITION, -700);
                timeAsOfLastFullLiftMovement = getOpMode().time;
                step++;
            }
            if(step == 1 && ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).getDcMotor().getCurrentPosition() <= -500) {
                inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 33);
                timeAsOfLastFullLiftMovement = getOpMode().time;
                step++;
            }
            if(step == 2 && timeAsOfLastFullLiftMovement + 0.25 <= getOpMode().time && ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).getDcMotor().getCurrentPosition() <= -700) {
                inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 36);
                timeAsOfLastFullLiftMovement = getOpMode().time;
                step++;
            }
            if(step == 3 && timeAsOfLastFullLiftMovement + 2 <= getOpMode().time) {
                inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 0);
                timeAsOfLastFullLiftMovement = getOpMode().time;
                step++;
            }
            if(step == 4 && timeAsOfLastFullLiftMovement + 1 <= getOpMode().time) {
                step = 0;
                isMovingToTBall = false;
                isMovingToBasePos = true;
            }
        }
        // block pos: 38-40
    }

    /**
    This controls the elevator manually. This should only be used when manual mode is enabled.
     */
    private void controlElevator() {
        int elevatorInput = (gamepadManager.functionOneGamepad().right_bumper ? 0 : 1) + (gamepadManager.functionOneGamepad().left_bumper ? 0 : -1);
        int inputVal = Math.abs(((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).getDcMotor().getCurrentPosition()) < 500 ? Range.clip(elevatorInput * 75, -75, 25) : Range.clip(elevatorInput * 75, -75, 75);
        if(inputVal < 0 || outputSpace.receiveOutputFromElevatorBottomLimitSwitch(ElevatorBottomLimitSwitchLocation.Values.PRESSED) == 0) {
            inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_SPEED, inputVal);
            inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_SPEED, inputVal);
        }else{
            inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_SPEED, 0);
            inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_SPEED, 0);
            ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).reset();
            ((StandardMotor) inputSpace.getElevatorRightLift().getInternalInteractionSurface()).reset();
        }
    }

    /**
     * This controls the hand manually. This should only be used when manual mode is enabled.
     */
    private void controlHand() {
        if(gamepadManager.functionOneGamepad().a) {
            inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 23);
        }else if(gamepadManager.functionOneGamepad().b) {
            inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 34);
        }else if(gamepadManager.functionOneGamepad().y) {
            inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 36);
        }else if(gamepadManager.functionOneGamepad().x) {
            inputSpace.sendInputToHandSpinner(HandSpinningServoLocation.Action.SET_POSITION, 38);
        }
    }

    private void controlDuck() {
        inputSpace.sendInputToDuckMotor(DuckMotorLocation.Action.SET_SPEED, getOpMode().gamepad1.start ? -50 : 0);
    }

    private void debug() {
        getOpMode().telemetry.addData("Elevator:", ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).getDcMotor().getCurrentPosition());
        getOpMode().telemetry.addData("Intake Lift:", ((StandardServo) inputSpace.getIntakeLifter().getInternalInteractionSurface()).getPosition());
        getOpMode().telemetry.update();
    }

    @Override
    public void stop() {
        inputSpace.stop();
        outputSpace.stop();
    }

}
