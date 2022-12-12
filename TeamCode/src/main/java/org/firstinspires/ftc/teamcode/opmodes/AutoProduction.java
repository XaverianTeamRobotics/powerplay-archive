package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import org.firstinspires.ftc.teamcode.features.SleeveDetectionFeature;
import org.firstinspires.ftc.teamcode.internals.misc.Clock;
import org.firstinspires.ftc.teamcode.internals.odometry.drivers.AutonomousDriver;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

public class AutoProduction extends OperationMode implements AutonomousOperation {

    private final SleeveDetectionFeature sleeveDetectionFeature = new SleeveDetectionFeature();
    private boolean driving = false;
    Trajectory trajectory;
    AutonomousDriver drive;

    @Override
    public void construct() {
        drive = new AutonomousDriver(hardwareMap);
        Pose2d startPose = new Pose2d(-63, 36, Math.toRadians(-90));
        drive.setPoseEstimate(startPose);
        trajectory = drive.trajectoryBuilder(startPose)
            .splineTo(new Vector2d(-36, 36), -90)
            .build();
        registerFeature(sleeveDetectionFeature);
        Clock.make("auto");
    }

    @Override
    public void run() {
        if(Clock.get("auto").elapsed(3) && !driving) {
            int spot = sleeveDetectionFeature.getSpot();
            driving = true;
            drive.followTrajectory(trajectory);
        }
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return Production.class;
    }

}
