package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.features.FourMotorArm;
import org.firstinspires.ftc.teamcode.features.Hand;
import org.firstinspires.ftc.teamcode.features.SleeveDetector;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.Auto;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing.AutoRunner;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.time.Clock;
import org.firstinspires.ftc.teamcode.internals.time.Timer;

import java.util.UUID;

public class NewAutoLeft extends OperationMode implements AutonomousOperation {

    Timer time;
    int spot = 0;
    SleeveDetector sleeve;
    boolean findingSleeve = true;
    AutoRunner runner;

    @Override
    public void construct() {
        time = Clock.make(UUID.randomUUID().toString());
        Hand hand = new Hand(true);
        registerFeature(hand);
        FourMotorArm arm = new FourMotorArm();
        registerFeature(arm);
        sleeve = new SleeveDetector();
        registerFeature(sleeve);
        Pose2d start = new Pose2d(35.84, 61.50, Math.toRadians(-90.00));
        Auto auto = new Auto(start)
            .begin()
            // when we're 2 inches into the path, raise the arm. we do ths 2 inches into the path to provide adequate clearance with the wall
            .addDisplacementMarker(2, () -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH))
            // drive to the junction
            .splineTo(new Vector2d(35.14, 44.05), Math.toRadians(268.63))
            .splineTo(new Vector2d(30.58, 6.38), Math.toRadians(221.32))
            .completeTrajectory()
            // once the arm reaches the correct height, open the hand and then lower the arm to cone_high
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Hand::autoOpen)
            .appendWait(Hand::complete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.CONE_HIGH))
            .appendTrajectory()
            // drive to the cone stack
            .lineToSplineHeading(new Pose2d(38.92, 11.11, Math.toRadians(0.11)))
            .splineToConstantHeading(new Vector2d(57.52, 9.55), Math.toRadians(2.46))
            .completeTrajectory()
            // once the arm is at the right position, close in on the top cone and begin raising the arm. we also wait a fraction of a second so the arm has enough time to raise above the stack so when we drive backwards, the cone we've picked up doesnt knock over the whole stack
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Hand::autoClose)
            .appendWait(Hand::complete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH))
            .appendWait(500)
            .appendTrajectory()
            // drive back to the junction
            .lineToSplineHeading(new Pose2d(37.38, 13.95, Math.toRadians(232.36)))
            .splineToConstantHeading(new Vector2d(28.55, 7.35), Math.toRadians(232.36))
            .completeTrajectory()
            // when the arm reaches the correct height, we open the hand again and then lower the arm back down to cone_high for another cycle
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Hand::autoOpen)
            .appendWait(Hand::complete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.CONE_HIGH))
            .appendTrajectory()
            // we drive to the cone stack
            .lineToSplineHeading(new Pose2d(37.68, 9.26, Math.toRadians(1.30)))
            .splineTo(new Vector2d(57.46, 9.13), Math.toRadians(359.18))
            .completeTrajectory()
            // same as last time: we wait for the arm to lower on the top of the stack, grab a cone, raise the arm, and then a fraction of a second later we begin driving back to the junction
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Hand::autoClose)
            .appendWait(Hand::complete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH))
            .appendWait(500)
            .appendTrajectory()
            // drive back to the junction for the last time
            .lineToSplineHeading(new Pose2d(37.38, 13.95, Math.toRadians(232.36)))
            .splineToConstantHeading(new Vector2d(28.98, 5.97), Math.toRadians(228.78))
            .completeTrajectory()
            // once the arm is at the correct height, we open the hand and then lower the arm to the reset position; we're done cycling at this point and need to park
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Hand::autoOpen)
            .appendWait(Hand::complete)
            .appendAction(() -> FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.RESET))
            .appendTrajectory()
            // we drive to the second parking position and then wait for the arm to be at the reset position
            .lineToSplineHeading(new Pose2d(34.28, 11.20, Math.toRadians(267.34)))
            .completeTrajectory()
            .appendWait(FourMotorArm::autoComplete)
            .appendAction(Devices.encoder5::save)
            .appendAction(Devices.encoder6::save)
            .complete();
        // if the signal was at position 2, we're all done now! otherwise though, we need to either go to the 1st parking spot, or the 3rd spot
        Auto one = new Auto(auto.end())
            .begin()
            .lineToConstantHeading(new Vector2d(57.77, 8.17))
            .completeTrajectory()
            .complete();
        Auto three = new Auto(auto.end())
            .begin()
            .lineToSplineHeading(new Pose2d(11.21, 17.25, Math.toRadians(258.89)))
            // since all parking spots span two tiles, we attempt to drive to the closest tile to the substation for the third parking spot. we might not have enough time for this, but if we do, it just makes connor and mukes lives easier
            // also, we dont do this for the first parking spot because connor and muke like cycling the stack of cones and the first tile we hit in the first spot is already closest to the stack
            .lineTo(new Vector2d(13.96, 35.43))
            .completeTrajectory()
            .complete();
        runner = new AutoRunner(auto, auto.getDrivetrain(), one, three);
    }

    @Override
    public void run() {
        if(!findingSleeve) {
            runner.run();
        }else{
            Clock.block(this::findSleeve);
            runner.processSleeve(spot);
            findingSleeve = false;
        }
    }

    private boolean findSleeve() {
        spot = sleeve.getSpot();
        return spot != 0;
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return null;
    }

}
