package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.features.FourMotorArm;
import org.firstinspires.ftc.teamcode.features.Hand;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequence;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

import java.util.LinkedList;
import java.util.Queue;

public class BlueLeft extends OperationMode implements AutonomousOperation {

    private AutonomousDrivetrain drivetrain;
    private FourMotorArm.ArmPosition currentArmCommand = null;
    TrajectorySequence currentDriveCommand = null;
    private Queue<TrajectorySequence> driveCommands = new LinkedList<>();
    private Queue<FourMotorArm.ArmPosition> armCommands = new LinkedList<>();
    private Hand hand;
    private TrajectorySequence seq = null;

    @Override
    public void construct() {
//        Clock.make("o");
//        hand = new Hand();
//        registerFeature(hand);
//        armCommands.add(FourMotorArm.ArmPosition.JNCT_HIGH);
//        armCommands.add(FourMotorArm.ArmPosition.CONE_HIGH);
//        armCommands.add(FourMotorArm.ArmPosition.JNCT_HIGH);
//        armCommands.add(FourMotorArm.ArmPosition.CONE_LOW);
        drivetrain = new AutonomousDrivetrain();
        drivetrain.setPoseEstimate(new Pose2d(39.25, 61.50, Math.toRadians(-90.00)));
        seq = drivetrain.trajectorySequenceBuilder(new Pose2d(39.25, 61.50, Math.toRadians(-90.00)))
            .splineTo(new Vector2d(36.96, 56.48), Math.toRadians(250.00))
            .splineTo(new Vector2d(35.62, 23.94), Math.toRadians(-88.75))
            // drive to up, wait
            .splineTo(new Vector2d(32.00, 13.00), Math.toRadians(230.00))
            .waitSeconds(1)
            // drop
            .splineToConstantHeading(new Vector2d(29, 4), Math.toRadians(230.00))
            .waitSeconds(1)
            // drive back, lower
            .splineToLinearHeading(new Pose2d(32, 15.00, Math.toRadians(230)), Math.toRadians(230))
            // wait for lower, cone
            .lineToLinearHeading(new Pose2d(60.00, 11.50, Math.toRadians(354)))
            .waitSeconds(1)
            // drive to up, wait
            .lineToSplineHeading(new Pose2d(32, 13.00, Math.toRadians(230)))
            .waitSeconds(1)
            // drop
            .splineToConstantHeading(new Vector2d(29, 4), Math.toRadians(230.00))
            .waitSeconds(1)
            // drive to park, lower
            .splineToLinearHeading(new Pose2d(32, 15.00, Math.toRadians(230)), Math.toRadians(230))
            .lineToSplineHeading(new Pose2d(36, 25, Math.toRadians(-90)))
            // 2
            .splineToConstantHeading(new Vector2d(36, 37),  Math.toRadians(60))
            .waitSeconds(1)
            // 3
            .lineToLinearHeading(new Pose2d(12.04, 37, Math.toRadians(90)))
            .waitSeconds(1)
            // 1
            .lineToLinearHeading(new Pose2d(58.04, 37, Math.toRadians(180)))
            .waitSeconds(1)
            .build();
//        TrajectorySequence first = drivetrain.trajectorySequenceBuilder(new Pose2d(39.25, 61.50, Math.toRadians(-90.00)))
//            .addDisplacementMarker(() -> currentArmCommand = armCommands.poll())
//            .splineTo(new Vector2d(36.96, 56.48), Math.toRadians(250.00))
//            .splineTo(new Vector2d(35.62, 23.94), Math.toRadians(-88.75))
//            .splineTo(new Vector2d(32.00, 13.00), Math.toRadians(230.00))
//            .build();
//        driveCommands.add(first);
//        TrajectorySequence second = drivetrain.trajectorySequenceBuilder(first.end())
//            .splineTo(new Vector2d(29.50, 10.00), Math.toRadians(230.00))
//            .addDisplacementMarker(() -> {
//                hand.open();
//                Clock.get("o").reset();
//            })
//            .splineToLinearHeading(new Pose2d(32, 13.00, Math.toRadians(230)), Math.toRadians(230))
//            .build();
//        driveCommands.add(second);
        drivetrain.followTrajectorySequenceAsync(seq);
    }

    @Override
    public void run() {
        drivetrain.update();
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return SmallbotProduction.class;
    }

}
