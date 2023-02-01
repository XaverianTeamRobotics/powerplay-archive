package net.xbhs.robotics.bots;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import net.xbhs.robotics.utils.Bot;
import net.xbhs.robotics.utils.Environment;

public class Prod3 extends Bot {

    @Override
    public RoadRunnerBotEntity bot() {
        return new DefaultBotBuilder(Environment.MEEP)
            .setColorScheme(new ColorSchemeBlueDark())
            .setConstraints(30, 30, 3, 3, 13.90)
            .setDimensions(14, 14)
            .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(39.25, 61.50, Math.toRadians(-90.00)))
                    .splineTo(new Vector2d(36.96, 56.48), Math.toRadians(250.00))
                    .splineTo(new Vector2d(35.62, 23.94), Math.toRadians(-88.75))
                    .splineTo(new Vector2d(32.00, 13.00), Math.toRadians(230.00))
                    .splineTo(new Vector2d(29.50, 10.00), Math.toRadians(230.00))
                    .splineToLinearHeading(new Pose2d(32, 13.00, Math.toRadians(230)), Math.toRadians(230))
                    .lineToLinearHeading(new Pose2d(59.00, 10.00, Math.toRadians(354)))
                    .lineToSplineHeading(new Pose2d(32, 13.00, Math.toRadians(230)))
                    .splineToConstantHeading(new Vector2d(29.50, 10.00), Math.toRadians(230.00))
                    .splineToLinearHeading(new Pose2d(32, 13.00, Math.toRadians(230)), Math.toRadians(230))
                    .lineToSplineHeading(new Pose2d(36, 25, Math.toRadians(-90)))
                    // 2
                    .splineToConstantHeading(new Vector2d(36, 35),  Math.toRadians(60))
                    // 3
                    .lineToLinearHeading(new Pose2d(12.04, 35, Math.toRadians(90)))
                    // 1
                    .lineToLinearHeading(new Pose2d(58.04, 35, Math.toRadians(180)))
                    .build());
    }

}
