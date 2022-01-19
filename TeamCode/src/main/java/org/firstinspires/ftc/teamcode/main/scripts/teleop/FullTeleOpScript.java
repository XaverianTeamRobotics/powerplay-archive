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

    private GamepadManager gamepadManager;
    private double timeAsOfLastIntakeMovement;
    private int intakeLowerPos, intakeUpperPos;
    private boolean intakeShouldBeDown, intakeIsAtPosition, handShouldBeDown, bWasDown, manualMode;
    private HashMap<String, Double> timetable;
    private InputSpace inputSpace;
    private OutputSpace outputSpace;

    public FullTeleOpScript(LinearOpMode opMode) {
        super(opMode);
        // set fields and calibrate robot
        assignValues();
//        calibrateElevator();
        //calibrateIntake();
    }

    @Override
    public void main() {
        // control robot
        controlDrivetrain();
        //controlIntakeLifter();
        // debug
        intakeShouldBeDown = true; intakeIsAtPosition = true;
        controlIntake();
        controlElevator();
        controlHand();
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
        handShouldBeDown = false;
        bWasDown = false;
        manualMode = true;
        timetable = new HashMap<>();
        timetable.put("lastManualHandServoTime", 0D);
        timetable.put("lastManualIntakeServoTime", 0D);
    }

    private void calibrateElevator() {
        int timeAsOfLastElevatorCalibrationBegin = (int) getOpMode().time;
        while(outputSpace.receiveOutputFromElevatorBottomLimitSwitch(ElevatorBottomLimitSwitchLocation.Values.PRESSED) == 0 && timeAsOfLastElevatorCalibrationBegin > (int) getOpMode().time - 1) {
            inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_SPEED, -100);
            inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_SPEED, -100);
        }
        while(outputSpace.receiveOutputFromElevatorBottomLimitSwitch(ElevatorBottomLimitSwitchLocation.Values.PRESSED) == 0) {
            inputSpace.sendInputToElevatorLeftLift(ElevatorLeftLiftMotorLocation.Action.SET_SPEED, 10);
            inputSpace.sendInputToElevatorRightLift(ElevatorRightLiftMotorLocation.Action.SET_SPEED, 10);
        }
        ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).reset();
        ((StandardMotor) inputSpace.getElevatorRightLift().getInternalInteractionSurface()).reset();
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
        if(gamepadManager.functionOneGamepad().dpad_left && timeAsOfLastIntakeMovement + 0.25 < getOpMode().time) {
            int pos = ((StandardServo) inputSpace.getHandSpinner().getInternalInteractionSurface()).getPosition();
            inputSpace.sendInputToIntakeLifter(IntakeLiftingServoLocation.Action.SET_POSITION, Range.clip(pos + 1, 40, 80));
            timeAsOfLastIntakeMovement = getOpMode().time;
        }else if(gamepadManager.functionOneGamepad().dpad_right && timeAsOfLastIntakeMovement + 0.25 < getOpMode().time) {
            int pos = ((StandardServo) inputSpace.getHandSpinner().getInternalInteractionSurface()).getPosition();
            inputSpace.sendInputToIntakeLifter(IntakeLiftingServoLocation.Action.SET_POSITION, Range.clip(pos - 1, 40, 80));
            timeAsOfLastIntakeMovement = getOpMode().time;
        }
    }

    private void controlIntake() {
        int intakeGas = (int) Range.clip(gamepadManager.functionOneGamepad().left_trigger * 100, 0, 100);
        int intakeBrake = (int) Range.clip(gamepadManager.functionOneGamepad().right_trigger * 100, 0, 100);
        int intakeSpeed = Range.clip(intakeGas - intakeBrake, -100, 100);
        inputSpace.sendInputToIntakeSpinner(IntakeSpinningMotorLocation.Action.SET_SPEED, intakeSpeed);
    }

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
        inputSpace.sendInputToDuckMotor(DuckMotorLocation.Action.SET_SPEED, getOpMode().gamepad1.a ? -50 : 0);
    }

    private void debug() {
        getOpMode().telemetry.addData("Elevator:", ((StandardMotor) inputSpace.getElevatorLeftLift().getInternalInteractionSurface()).getDcMotor().getCurrentPosition());
        getOpMode().telemetry.update();
    }

    @Override
    public void stop() {
        inputSpace.stop();
        outputSpace.stop();
    }

}
