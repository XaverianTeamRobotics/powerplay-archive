package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.features.SleeveDetectionFeature;
import org.firstinspires.ftc.teamcode.internals.misc.Clock;
import org.firstinspires.ftc.teamcode.internals.odometry.drivers.AutonomousDriver;
import org.firstinspires.ftc.teamcode.internals.odometry.trajectories.TrajectorySequence;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

public class AutoProduction extends OperationMode implements AutonomousOperation {

    private final SleeveDetectionFeature sleeveDetectionFeature = new SleeveDetectionFeature();
    private boolean driving = false;
    private boolean first = true;
    TrajectorySequence trajectory1;
    TrajectorySequence trajectory2;
    TrajectorySequence trajectory3;
    TrajectorySequence[] trajectories = new TrajectorySequence[3];
    AutonomousDriver drive;

    @Override
    public void construct() {
        drive = new AutonomousDriver(hardwareMap);
        Pose2d startPose = new Pose2d(-58, 35, Math.toRadians(0));
        drive.setPoseEstimate(startPose);
        trajectory1 = drive.trajectorySequenceBuilder(new Pose2d(-58, 35, 0))
            .splineTo(new Vector2d(-46, 35), 0)
            .lineToSplineHeading(new Pose2d(-34, 35, Math.toRadians(90)))
            .build();
        trajectory2 = drive.trajectorySequenceBuilder(new Pose2d(-58, 35, 0))
            .splineTo(new Vector2d(-46, 35), 0)
            .lineToSplineHeading(new Pose2d(-40, 35, Math.toRadians(90)))
            .splineToConstantHeading(new Vector2d(-34, 52), Math.toRadians(90))
            .lineToSplineHeading(new Pose2d(-34, 59, Math.toRadians(0)))
            .build();
        trajectory3 = drive.trajectorySequenceBuilder(new Pose2d(-58, 35, 0))
            .splineTo(new Vector2d(-46, 35), 0)
            .lineToSplineHeading(new Pose2d(-34, 35, Math.toRadians(90)))
            .lineToSplineHeading(new Pose2d(-34, 15, Math.toRadians(90)))
            .build();
        registerFeature(sleeveDetectionFeature);
        trajectories[0] = trajectory1;
        trajectories[1] = trajectory2;
        trajectories[2] = trajectory3;
        Clock.make("auto");
    }

    @Override
    public void run() {
        if(first) {
            Clock.get("auto").reset();
            first = false;
        }else if(Clock.get("auto").elapsed(5) && !driving) {
            int spot = sleeveDetectionFeature.getSpot();
            if(spot > 0) {
                driving = true;
                drive.followTrajectorySequence(trajectories[spot - 1]);
            }
        }
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return Production.class;
    }

}
