package org.firstinspires.ftc.teamcode.main.utils.helpers;

import org.firstinspires.ftc.teamcode.main.utils.interactions.groups.InteractionGroup;
import org.firstinspires.ftc.teamcode.main.utils.interactions.items.StandardDistanceSensor;
import org.firstinspires.ftc.teamcode.main.utils.interactions.items.StandardMotor;
import org.firstinspires.ftc.teamcode.main.utils.interactions.items.StandardServo;

public class ElevatorDriver {

    private final StandardMotor RIGHT_MOTOR, LEFT_MOTOR;
    private final StandardServo RIGHT_SERVO, LEFT_SERVO;
    private final StandardDistanceSensor DISTANCE;

    private int handGrabbingPositionRight = 30;
    private int handGrabbingPositionLeft = 55;
    private int handReleasingPositionRight = 60;
    private int handReleasingPositionLeft = 30;
    private int distanceSensorDistance = 120;
    private int handTurningGrabbingPosition = 20;
    private int handTurningDefaultPosition = 23;
    private int handTurningBottomBallPosition = 36;
    private int handTurningMiddleBallPosition = 36;
    private int handTurningTopBallPosition = 36;
    private int handTurningBottomBlockPosition = 38;
    private int handTurningMediumBlockPosition = 38;
    private int handTurningTopBlockPosition = 38;
    private int handTurningSafePosition = 33;
    private int elevatorSafePosition = -500;
    private int elevatorLowerBallPosition = -20;
    private int elevatorMiddleBallPosition = -350;
    private int elevatorTopBallPosition = -700;
    private int elevatorLowerBlockPosition = -150;
    private int elevatorMiddleBlockPosition = -575;
    private int elevatorTopBlockPosition = -1000;

    private int step = 0;

    /**
     * Whether the robot is stable or not. This should only be true if the robot is not moving and in its default position, {@link #step} is 0, and/or when all the "isPos*" boolean values are false besides {@link #isPosDefault}.
     */
    private boolean isStable = false;

    private boolean isPosIntake = false;
    private boolean isPosDefault = false;
    private boolean isPosLowBall = false;
    private boolean isPosMedBall = false;
    private boolean isPosTopBall = false;
    private boolean isPosLowBlock = false;
    private boolean isPosMedBlock = false;
    private boolean isPosTopBlock = false;

    /**
     * This creates an ElevatorDriver with two elevator motors, two hand grabber servos, and a distance sensor to determine when the servos should grab the hand. It uses the default configuration for each motor, servo, and sensor, which at the time of writing is best for our 2021-2022 season robot.
     * @param rightMotor The right motor
     * @param leftMotor The left motor
     * @param rightServo The right grabber servo
     * @param leftServo The left grabber servo
     * @param distanceSensor The distance sensor
     */
    public ElevatorDriver(StandardMotor rightMotor, StandardMotor leftMotor, StandardServo rightServo, StandardServo leftServo, StandardDistanceSensor distanceSensor) {
        RIGHT_MOTOR = rightMotor;
        LEFT_MOTOR = leftMotor;
        RIGHT_SERVO = rightServo;
        LEFT_SERVO = leftServo;
        DISTANCE = distanceSensor;
    }

    /**
     * This creates an ElevatorDriver with two elevator motors, two hand grabber servos, and a distance sensor to determine when the servos should grab the hand. It uses a custom configuration for the motors, servos, and sensor.
     * @param rightMotor The right motor
     * @param leftMotor The left motor
     * @param rightServo The right servo
     * @param leftServo The left servo
     * @param distanceSensor The distance sensor
     * @param handGrabbingPositionRight The position of the right hand grabber servo when it closes down on the object being held
     * @param handGrabbingPositionLeft The position of the left hand grabber servo when it closes down on the object being held
     * @param handReleasingPositionRight The position of the right hand grabber servo when it stops holding the object
     * @param handReleasingPositionLeft The position of the left hand grabber servo when it stops holding the object
     * @param distanceSensorDistance The distance from an object as reported by the distance sensor in which the object is in the holding position
     * @param handTurningGrabbingPosition The position of the hand's turning servo when the hand is in its grabbing position, or intake position
     * @param handTurningDefaultPosition The position of the hand's turning servo when the hand is in its default position
     * @param handTurningBottomBallPosition The position of the hand's turning servo when the hand is in the position it should be in when dispensing a ball to the bottom level
     * @param handTurningMiddleBallPosition The position of the hand's turning servo when the hand is in the position it should be in when dispensing a ball to the middle level
     * @param handTurningTopBallPosition The position of the hand's turning servo when the hand is in the position it should be in when dispensing a ball to the top level
     * @param handTurningBottomBlockPosition The position of the hand's turning servo when the hand is in the position it should be in when dispensing a block to the bottom level
     * @param handTurningMediumBlockPosition The position of the hand's turning servo when the hand is in the position it should be in when dispensing a block to the middle level
     * @param handTurningTopBlockPosition The position of the hand's turning servo when the hand is in the position it should be in when dispensing a block to the top level
     * @param handTurningSafePosition The position of the hand's turning servo when it's in its safe position. This is the position in which the hand should be in while the absolute value of the elevator position is greater than the absolute value of the elevators safe point and the hand is not currently in the position to dispense a block
     * @param elevatorLowerBallPosition The position of the left elevator motor's encoder value at the position to dispense a ball at the lower level
     * @param elevatorMiddleBallPosition The position of the left elevator motor's encoder value at the position to dispense a ball at the middle level
     * @param elevatorTopBallPosition The position of the left elevator motor's encoder value at the position to dispense a ball at the top level
     * @param elevatorLowerBlockPosition The position of the left elevator motor's encoder value at the position to dispense a block at the lower level
     * @param elevatorMiddleBlockPosition The position of the left elevator motor's encoder value at the position to dispense a block at the middle level
     * @param elevatorTopBlockPosition The position of the left elevator motor's encoder value at the position to dispense a block at the top level
     * @param elevatorSafePosition The position of the elevator when it reaches a point where the hand has enough space to turn 360 degrees without hitting anything
     */
    public ElevatorDriver(StandardMotor rightMotor, StandardMotor leftMotor, StandardServo rightServo, StandardServo leftServo, StandardDistanceSensor distanceSensor, int handGrabbingPositionRight, int handGrabbingPositionLeft, int handReleasingPositionRight, int handReleasingPositionLeft, int distanceSensorDistance, int handTurningGrabbingPosition, int handTurningDefaultPosition, int handTurningBottomBallPosition, int handTurningMiddleBallPosition, int handTurningTopBallPosition, int handTurningBottomBlockPosition, int handTurningMediumBlockPosition, int handTurningTopBlockPosition, int handTurningSafePosition, int elevatorLowerBallPosition, int elevatorMiddleBallPosition, int elevatorTopBallPosition, int elevatorLowerBlockPosition, int elevatorMiddleBlockPosition, int elevatorTopBlockPosition, int elevatorSafePosition) {
        this.RIGHT_MOTOR = rightMotor;
        this.LEFT_MOTOR = leftMotor;
        this.RIGHT_SERVO = rightServo;
        this.LEFT_SERVO = leftServo;
        this.DISTANCE = distanceSensor;
        this.handGrabbingPositionRight = handGrabbingPositionRight;
        this.handGrabbingPositionLeft = handGrabbingPositionLeft;
        this.handReleasingPositionRight = handReleasingPositionRight;
        this.handReleasingPositionLeft = handReleasingPositionLeft;
        this.distanceSensorDistance = distanceSensorDistance;
        this.handTurningGrabbingPosition = handTurningGrabbingPosition;
        this.handTurningDefaultPosition = handTurningDefaultPosition;
        this.handTurningBottomBallPosition = handTurningBottomBallPosition;
        this.handTurningMiddleBallPosition = handTurningMiddleBallPosition;
        this.handTurningTopBallPosition = handTurningTopBallPosition;
        this.handTurningBottomBlockPosition = handTurningBottomBlockPosition;
        this.handTurningMediumBlockPosition = handTurningMediumBlockPosition;
        this.handTurningTopBlockPosition = handTurningTopBlockPosition;
        this.handTurningSafePosition = handTurningSafePosition;
        this.elevatorLowerBallPosition = elevatorLowerBallPosition;
        this.elevatorMiddleBallPosition = elevatorMiddleBallPosition;
        this.elevatorTopBallPosition = elevatorTopBallPosition;
        this.elevatorLowerBlockPosition = elevatorLowerBlockPosition;
        this.elevatorMiddleBlockPosition = elevatorMiddleBlockPosition;
        this.elevatorTopBlockPosition = elevatorTopBlockPosition;
        this.elevatorSafePosition = elevatorSafePosition;
    }

    /**
     * Runs the elevator. This method will run whatever needs to be ran for the elevator to reach the position it needs to. It should be called in a loop until the elevator has completed its task.
     */
    public void run() {

    }

    /**
     * Tells the driver to attempt to drive to the intake position if possible.
     */
    public void setToIntakePosition() {
        if(isStable()) {
            unstabalize();
            isPosIntake = true;
        }
    }

    /**
     * Tells the driver to attempt to drive to the lower ball position if possible.
     */
    public void setToLowerBallPosition() {

    }

    /**
     * Tells the driver to attempt to drive to the medium ball position if possible.
     */
    public void setToMediumBallPosition() {

    }

    /**
     * Tells the driver to attempt to drive to the top ball position if possible.
     */
    public void setToTopBallPosition() {

    }

    /**
     * Tells the driver to attempt to drive to the lower block position if possible.
     */
    public void setToLowerBlockPosition() {

    }

    /**
     * Tells the driver to attempt to drive to the medium block position if possible.
     */
    public void setToMediumBlockPosition() {

    }

    /**
     * Tells the driver to attempt to drive to the top block position if possible.
     */
    public void setToTopBlockPosition() {

    }

    private void unstabalize() {
        isStable = false;
    }

    private void stabalize() {
        isStable = true;
        step = 0;
        isPosIntake = false;
        isPosDefault = false;
        isPosLowBall = false;
        isPosMedBall = false;
        isPosTopBall = false;
        isPosLowBlock = false;
        isPosMedBlock = false;
        isPosTopBlock = false;
    }

    /**
     * Attempts to run to the default position of the elevator. This should stabalize the robot.
     */
    private void reset() {

    }

    /**
     * This method determines whether the elevator is ready to do another action because it is stable. When it is stable, it is at its default position and not moving in any form.
     * @return The robot's state; true if stable and false if unstable
     */
    public boolean isStable() {
        return isStable && step == 0 && !isPosIntake && !isPosDefault && !isPosLowBall && ! isPosMedBall && !isPosTopBall && !isPosLowBlock && !isPosMedBlock && !isPosTopBlock;
    }

    public StandardMotor getRightMotor() {
        return RIGHT_MOTOR;
    }

    public StandardMotor getLeftMotor() {
        return LEFT_MOTOR;
    }

    public StandardServo getRightServo() {
        return RIGHT_SERVO;
    }

    public StandardServo getLeftServo() {
        return LEFT_SERVO;
    }

    public StandardDistanceSensor getDistance() {
        return DISTANCE;
    }

    public int getHandGrabbingPositionRight() {
        return handGrabbingPositionRight;
    }

    public int getHandGrabbingPositionLeft() {
        return handGrabbingPositionLeft;
    }

    public int getHandReleasingPositionRight() {
        return handReleasingPositionRight;
    }

    public int getHandReleasingPositionLeft() {
        return handReleasingPositionLeft;
    }

    public int getDistanceSensorDistance() {
        return distanceSensorDistance;
    }

    public int getHandTurningGrabbingPosition() {
        return handTurningGrabbingPosition;
    }

    public int getHandTurningDefaultPosition() {
        return handTurningDefaultPosition;
    }

    public int getHandTurningBottomBallPosition() {
        return handTurningBottomBallPosition;
    }

    public int getHandTurningMiddleBallPosition() {
        return handTurningMiddleBallPosition;
    }

    public int getHandTurningTopBallPosition() {
        return handTurningTopBallPosition;
    }

    public int getHandTurningBottomBlockPosition() {
        return handTurningBottomBlockPosition;
    }

    public int getHandTurningMediumBlockPosition() {
        return handTurningMediumBlockPosition;
    }

    public int getHandTurningTopBlockPosition() {
        return handTurningTopBlockPosition;
    }

    public int getHandTurningSafePosition() {
        return handTurningSafePosition;
    }

    public int getElevatorSafePosition() {
        return elevatorSafePosition;
    }

    public int getElevatorLowerBallPosition() {
        return elevatorLowerBallPosition;
    }

    public int getElevatorMiddleBallPosition() {
        return elevatorMiddleBallPosition;
    }

    public int getElevatorTopBallPosition() {
        return elevatorTopBallPosition;
    }

    public int getElevatorLowerBlockPosition() {
        return elevatorLowerBlockPosition;
    }

    public int getElevatorMiddleBlockPosition() {
        return elevatorMiddleBlockPosition;
    }

    public int getElevatorTopBlockPosition() {
        return elevatorTopBlockPosition;
    }

    public int getStep() {
        return step;
    }

    public boolean isPosIntake() {
        return isPosIntake;
    }

    public boolean isPosLowBall() {
        return isPosLowBall;
    }

    public boolean isPosMedBall() {
        return isPosMedBall;
    }

    public boolean isPosTopBall() {
        return isPosTopBall;
    }

    public boolean isPosLowBlock() {
        return isPosLowBlock;
    }

    public boolean isPosMedBlock() {
        return isPosMedBlock;
    }

    public boolean isPosTopBlock() {
        return isPosTopBlock;
    }

    public boolean isPosDefault() {
        return isPosDefault;
    }

}
