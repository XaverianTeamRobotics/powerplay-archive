package org.firstinspires.ftc.teamcode.internals.motion.odometry.pathing;

import org.firstinspires.ftc.teamcode.internals.data.DriveCommand;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;

import java.util.Queue;

public class AutoRunnerAlt {

    private final Auto auto, one, three;
    private final Queue<DriveCommand> driveCommands;
    private final AutonomousDrivetrain drivetrain;
    private DriveCommand currentDriveCommand = null;

    public AutoRunnerAlt(Auto auto, AutonomousDrivetrain drivetrain, Auto one, Auto three) {
        this.auto = auto;
        driveCommands = this.auto.path();
        this.drivetrain = drivetrain;
        this.one = one;
        this.three = three;
        this.currentDriveCommand = driveCommands.poll();
    }

    public void processSleeve(int result) {
        beeeeekaaaayhaveityourway(result);
    }

    private void beeeeekaaaayhaveityourway(int spot) {
        switch(spot) {
            case 1:
                driveCommands.addAll(one.path());
                break;
            case 3:
                driveCommands.addAll(three.path());
        }
    }


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
    }

}
