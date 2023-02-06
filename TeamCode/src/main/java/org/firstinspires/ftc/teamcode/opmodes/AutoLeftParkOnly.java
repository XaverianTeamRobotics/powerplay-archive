package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.features.FourMotorArm;
import org.firstinspires.ftc.teamcode.features.Hand;
import org.firstinspires.ftc.teamcode.features.SleeveDetector;
import org.firstinspires.ftc.teamcode.internals.data.ArmCommand;
import org.firstinspires.ftc.teamcode.internals.data.ArmHeight;
import org.firstinspires.ftc.teamcode.internals.data.DriveCommand;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.trajectories.TrajectorySequence;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.time.Clock;
import org.firstinspires.ftc.teamcode.internals.time.Timer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class AutoLeftParkOnly extends OperationMode implements AutonomousOperation {

    private AutonomousDrivetrain drivetrain;
    private ArmCommand currentArmCommand = null;
    private ArmCommand lastArmCommand = null;
    private DriveCommand currentDriveCommand = null;
    private Queue<DriveCommand> driveCommands = new LinkedList<>();
    private Queue<ArmCommand> armCommands = new LinkedList<>();
    private Hand hand;
    private Timer handTimer;
    private SleeveDetector sleeve;
    private int spot;

    @Override
    public void construct() {

        // inits
        String clockStr = UUID.randomUUID().toString();
        handTimer = Clock.make(clockStr);
        hand = new Hand(true);
        registerFeature(hand);
        registerFeature(new FourMotorArm());
        sleeve = new SleeveDetector();
        registerFeature(sleeve);
        armCommands.add(ArmCommand.CONE_LOW);
        armCommands.add(ArmCommand.JNCT_HIGH);
        armCommands.add(ArmCommand.OPEN);
        armCommands.add(ArmCommand.ALIGN);
        armCommands.add(ArmCommand.CONE_LOW);
        armCommands.add(ArmCommand.CLOSE);
        armCommands.add(ArmCommand.JNCT_HIGH);
        armCommands.add(ArmCommand.OPEN);
        armCommands.add(ArmCommand.ALIGN);
        armCommands.add(ArmCommand.CONE_LOW);
        armCommands.add(ArmCommand.ALIGN);
        drivetrain = new AutonomousDrivetrain();
        drivetrain.setPoseEstimate(new Pose2d(39.25, 61.50, Math.toRadians(-90.00)));



        driveCommands.add(new DriveCommand.Wait(this::findSleeve));



        TrajectorySequence seq0 = drivetrain.trajectorySequenceBuilder(new Pose2d(36.29, 61.50, Math.toRadians(-90.00)))
            // leave wall
            .lineToConstantHeading(new Vector2d(36.29, 58))
            .build();
        driveCommands.add(new DriveCommand.Drive(seq0));
        driveCommands.add(new DriveCommand.Do(() -> {
            lastArmCommand = currentArmCommand;
            currentArmCommand = armCommands.poll();
        }));
        driveCommands.add(new DriveCommand.Wait(FourMotorArm::autoComplete));



        TrajectorySequence seq1 = drivetrain.trajectorySequenceBuilder(seq0.end())
            // to straight
            .lineToConstantHeading(new Vector2d(14.5, 58))
            .turn(Math.toRadians(5))
            .lineToConstantHeading(new Vector2d(14, 12.50))
            .build();
        driveCommands.add(new DriveCommand.Drive(seq1));
//        driveCommands.add(new DriveCommand.Wait(FourMotorArm::autoComplete));
//
//
//
////        TrajectorySequence seq1 = drivetrain.trajectorySequenceBuilder(seq0.end())
////            // dropoff
////            .lineToConstantHeading(new Vector2d(13, 0))
////            .build();
////        driveCommands.add(new DriveCommand.Drive(seq1));
//        driveCommands.add(new DriveCommand.Do(() -> {
//            lastArmCommand = currentArmCommand;
//            currentArmCommand = armCommands.poll();
//        }));
//        driveCommands.add(new DriveCommand.Wait(() -> handTimer.elapsed(1)));
//        driveCommands.add(new DriveCommand.Do(() -> {
//            lastArmCommand = currentArmCommand;
//            currentArmCommand = armCommands.poll();
//        }));
//        driveCommands.add(new DriveCommand.Wait(FourMotorArm::autoComplete));
//
//
//
//        TrajectorySequence seq2 = drivetrain.trajectorySequenceBuilder(seq1.end())
//            // go to straight to pickup
//            .lineToConstantHeading(new Vector2d(14, 12.50))
//            .turn(Math.toRadians(45))
//            .build();
//        driveCommands.add(new DriveCommand.Drive(seq2));
//        driveCommands.add(new DriveCommand.Do(() -> {
//            lastArmCommand = currentArmCommand;
//            currentArmCommand = armCommands.poll();
//        }));
//        driveCommands.add(new DriveCommand.Wait(FourMotorArm::autoComplete));
// end







//        TrajectorySequence seq5 = drivetrain.trajectorySequenceBuilder(seq2.end())
//            // park 3
//            .lineToConstantHeading(new Vector2d(14, 12.50))
//            .turn(Math.toRadians(50))
//            .build();
//        driveCommands.add(new DriveCommand.Drive(seq5));



        TrajectorySequence sleeveOne = drivetrain.trajectorySequenceBuilder(seq1.end())
            .lineToConstantHeading(new Vector2d(61, 12.50))
            .build();
        TrajectorySequence sleeveTwo = drivetrain.trajectorySequenceBuilder(seq1.end())
            .lineToConstantHeading(new Vector2d(37, 12.50))
            .build();
         processSleeve(sleeveOne, sleeveTwo);



        driveCommands.add(new DriveCommand.Wait(FourMotorArm::autoComplete));
        driveCommands.add(new DriveCommand.Do(() -> {
            ArmHeight.setHeight(new double[] { Devices.encoder5.getPosition(), Devices.encoder6.getPosition() });
        }));



        currentDriveCommand = driveCommands.poll();



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
                Hand.manualOpen();
                handTimer.reset();
                break;
            case CLOSE:
                Hand.manualClose();
                handTimer.reset();
                break;
            case ALIGN:
                FourMotorArm.autoLevelArm();
        }
        lastArmCommand = currentArmCommand;
    }

    private void processSleeve(TrajectorySequence one, TrajectorySequence two) {
        driveCommands.add(new DriveCommand.Do(() -> {
            switch(spot) {
                case 1:
                    driveCommands.add(new DriveCommand.Drive(one));
                    break;
                case 2:
                    driveCommands.add(new DriveCommand.Drive(two));
            }
        }));
    }

    private boolean findSleeve() {
        spot = sleeve.getSpot();
        return spot != 0;
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return SmallbotProduction.class;
    }

}
