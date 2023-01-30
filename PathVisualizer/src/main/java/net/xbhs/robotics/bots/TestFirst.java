package net.xbhs.robotics.bots;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import net.xbhs.robotics.utils.Bot;
import net.xbhs.robotics.utils.Environment;

public class TestFirst extends Bot {

    @Override
    public RoadRunnerBotEntity bot() {
        return new DefaultBotBuilder(Environment.MEEP)
            .setColorScheme(new ColorSchemeBlueDark())
            .setConstraints(30, 30, 3.5, 3.5, 13.7795)
            .followTrajectorySequence(drive ->
                drive.trajectorySequenceBuilder(new Pose2d(-36.00, 62.00, Math.toRadians(-90.00)))
                    .splineTo(new Vector2d(-35.96, 27.28), Math.toRadians(-88.97))
                    .splineTo(new Vector2d(-30.12, 10.93), Math.toRadians(-66.61))
                    .splineTo(new Vector2d(-44.13, 11.10), Math.toRadians(167.29))
                    .splineTo(new Vector2d(-64.16, 12.43), Math.toRadians(180.00))
//                    .splineTo(new Vector2d(-54.15, 12.43), Math.toRadians(-180.00))
                    .splineTo(new Vector2d(-39.80, 10.76), Math.toRadians(-13.47))
//                    .splineToSplineHeading(new Pose2d(-30.95, 6.76, Math.toRadians(-35.31)), Math.toRadians(-14.31))
                    .build());
    }

}
