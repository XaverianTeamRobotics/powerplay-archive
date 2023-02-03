package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.features.FourMotorArm;
import org.firstinspires.ftc.teamcode.features.Hand;
import org.firstinspires.ftc.teamcode.features.SleeveDetector;
import org.firstinspires.ftc.teamcode.internals.data.ArmCommand;
import org.firstinspires.ftc.teamcode.internals.data.DriveCommand;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequence;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.time.Clock;
import org.firstinspires.ftc.teamcode.internals.time.Timer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class BlueLeft extends OperationMode implements AutonomousOperation {

    private AutonomousDrivetrain drivetrain;
    private ArmCommand currentArmCommand = null;
    private ArmCommand lastArmCommand = null;
    private DriveCommand currentDriveCommand = null;
    private Queue<DriveCommand> driveCommands = new LinkedList<>();
    private Queue<ArmCommand> armCommands = new LinkedList<>();
    private Hand hand;
    private Timer handTimer;
    private SleeveDetector sleeve;

    @Override
    public void construct() {

        // inits
        String clockStr = UUID.randomUUID().toString();
        handTimer = Clock.make(clockStr);
        hand = new Hand(true);
        registerFeature(hand);
        registerFeature(new FourMotorArm());
        // TODO: uncomment when ready
//        sleeve = new SleeveDetector();
//        registerFeature(sleeve);
        armCommands.add(ArmCommand.JNCT_HIGH);
        armCommands.add(ArmCommand.OPEN);
        armCommands.add(ArmCommand.CONE_HIGH);
        armCommands.add(ArmCommand.CLOSE);
        armCommands.add(ArmCommand.JNCT_HIGH);
        armCommands.add(ArmCommand.OPEN);
        armCommands.add(ArmCommand.CONE_LOW);
        drivetrain = new AutonomousDrivetrain();
        drivetrain.setPoseEstimate(new Pose2d(39.25, 61.50, Math.toRadians(-90.00)));

        // begin driving to high jnct and move arm at the same time
        TrajectorySequence seq0 = drivetrain.trajectorySequenceBuilder(new Pose2d(39.25, 61.50, Math.toRadians(-90.00)))
            .addDisplacementMarker(() -> {
                lastArmCommand = currentArmCommand;
                currentArmCommand = armCommands.poll();
            })
            .splineTo(new Vector2d(36.96, 56.48), Math.toRadians(250.00))
            .splineTo(new Vector2d(35.62, 23.94), Math.toRadians(-88.75))
            .splineTo(new Vector2d(36.00, 16.00), Math.toRadians(230.00))
            .build();
        driveCommands.add(new DriveCommand.Drive(seq0));
        driveCommands.add(new DriveCommand.Wait(FourMotorArm::autoComplete));

        // drive to the cone dropoff point, then drop the cone off
        TrajectorySequence seq1 = drivetrain.trajectorySequenceBuilder(seq0.end())
            .splineToConstantHeading(new Vector2d(32, 11), Math.toRadians(230.00))
            .build();
        driveCommands.add(new DriveCommand.Drive(seq1));
        driveCommands.add(new DriveCommand.Do(() -> {
            lastArmCommand = currentArmCommand;
            currentArmCommand = armCommands.poll();
        }));
        driveCommands.add(new DriveCommand.Wait(() -> handTimer.elapsed(1)));

        // drive back where its safe to lower the arm, then begin lowering it, and drive to the cone pickup location
        TrajectorySequence seq2 = drivetrain.trajectorySequenceBuilder(seq1.end())
            .lineToLinearHeading(new Pose2d(36, 16.00, Math.toRadians(230)))
            .addDisplacementMarker(() -> {
                lastArmCommand = currentArmCommand;
                currentArmCommand = armCommands.poll();
            })
            .lineToLinearHeading(new Pose2d(59, 11.50, Math.toRadians(354)))
            .build();
        driveCommands.add(new DriveCommand.Drive(seq2));
        driveCommands.add(new DriveCommand.Wait(FourMotorArm::autoComplete));

        // close the hand once at the cone pickup location to pick up a cone
        driveCommands.add(new DriveCommand.Do(() -> {
            lastArmCommand = currentArmCommand;
            currentArmCommand = armCommands.poll();
        }));
        driveCommands.add(new DriveCommand.Wait(() -> handTimer.elapsed(1)));

        // start raising the arm again and drive to the high jnct
        TrajectorySequence seq3 = drivetrain.trajectorySequenceBuilder(seq2.end())
            .lineToSplineHeading(new Pose2d(36, 16.00, Math.toRadians(230)))
            .build();
        driveCommands.add(new DriveCommand.Drive(seq3));
        driveCommands.add(new DriveCommand.Do(() -> {
            lastArmCommand = currentArmCommand;
            currentArmCommand = armCommands.poll();
        }));
        driveCommands.add(new DriveCommand.Wait(FourMotorArm::autoComplete));

        // drive to the cone dropoff point, then drop the cone off again
        TrajectorySequence seq4 = drivetrain.trajectorySequenceBuilder(seq3.end())
            .splineToConstantHeading(new Vector2d(33, 9), Math.toRadians(230.00))
            .addDisplacementMarker(() -> {
                lastArmCommand = currentArmCommand;
                currentArmCommand = armCommands.poll();
            })
            .build();
        driveCommands.add(new DriveCommand.Drive(seq4));
        driveCommands.add(new DriveCommand.Wait(() -> handTimer.elapsed(1)));

        // drive back where its safe to lower the arm, then begin lowering it, and continue driving to the parking location
        TrajectorySequence seq5 = drivetrain.trajectorySequenceBuilder(seq4.end())
            .lineToLinearHeading(new Pose2d(36, 16.00, Math.toRadians(230)))
            .addDisplacementMarker(() -> {
                lastArmCommand = currentArmCommand;
                currentArmCommand = armCommands.poll();
            })
            .lineToSplineHeading(new Pose2d(36, 25, Math.toRadians(-90)))
            // TODO: make these their own trajectories and sleeve-based n stuff
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
        driveCommands.add(new DriveCommand.Drive(seq5));

        currentDriveCommand = driveCommands.poll();

        // find our parking spot
        // TODO: uncomment this when ready to impl sleeve
//        processSleeve(seq5.end());


    }

    @Override
    public void run() {
        if(!drivetrain.isBusy()) {
            try {
                DriveCommand.Drive command = (DriveCommand.Drive) currentDriveCommand;
                drivetrain.followTrajectorySequenceAsync(command.getSequence());
                currentDriveCommand = driveCommands.poll();
            } catch(ClassCastException e) {
                try {
                    DriveCommand.Do command = (DriveCommand.Do) currentDriveCommand;
                    command.getAction().run();
                    currentDriveCommand = driveCommands.poll();
                } catch(ClassCastException ex) {
                    DriveCommand.Wait command = (DriveCommand.Wait) currentDriveCommand;
                    if(command.getSupplier().get()) {
                        currentDriveCommand = driveCommands.poll();
                    }
                }
            }
        }
        drivetrain.update();
        processArmCommand();
    }

    private void processArmCommand() {
        if(currentArmCommand == null || lastArmCommand == currentArmCommand) return;
        switch(currentArmCommand) {
            case JNCT_GND:
                FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_GND);
                break;
            case JNCT_LOW:
                FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_LOW);
                break;
            case JNCT_MED:
                FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_MED);
                break;
            case JNCT_HIGH:
                FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.JNCT_HIGH);
                break;
            case CONE_LOW:
                FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.CONE_LOW);
                break;
            case CONE_MED:
                FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.CONE_MED);
                break;
            case CONE_HIGH:
                FourMotorArm.autoRunArm(FourMotorArm.ArmPosition.CONE_HIGH);
                break;
            case OPEN:
                hand.requestOpen();
                handTimer.reset();
                break;
            case CLOSE:
                hand.requestClose();
                handTimer.reset();
        }
        lastArmCommand = currentArmCommand;
    }

    private void processSleeve(Pose2d pose) {
        Clock.block(sleeve::isReady);
        int spot = sleeve.getAverageSpot();
        TrajectorySequence one = drivetrain.trajectorySequenceBuilder(pose)
            .splineToConstantHeading(new Vector2d(36, 37),  Math.toRadians(60))
            .lineToLinearHeading(new Pose2d(58.04, 37, Math.toRadians(180)))
            .build();
        TrajectorySequence two = drivetrain.trajectorySequenceBuilder(pose)
            .splineToConstantHeading(new Vector2d(36, 37),  Math.toRadians(60))
            .lineToLinearHeading(new Pose2d(36, 36, Math.toRadians(90)))
            .build();
        TrajectorySequence three = drivetrain.trajectorySequenceBuilder(pose)
            .splineToConstantHeading(new Vector2d(36, 37),  Math.toRadians(60))
            .lineToLinearHeading(new Pose2d(12.04, 37, Math.toRadians(90)))
            .build();
        switch(spot) {
            case 1:
                driveCommands.add(new DriveCommand.Drive(one));
                break;
            case 2:
                driveCommands.add(new DriveCommand.Drive(two));
                break;
            default:
                // using three as the default is the best option because it's the shortest distance to the alliance triangle thingy
                driveCommands.add(new DriveCommand.Drive(three));
        }
        // waiting for the arm at this point isnt really necessary but lets do it just in case
        driveCommands.add(new DriveCommand.Wait(FourMotorArm::autoComplete));
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return SmallbotProduction.class;
    }

}
