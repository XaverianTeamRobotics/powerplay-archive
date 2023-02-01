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
            .followTrajectorySequence(drive ->
                drive.trajectorySequenceBuilder(new Pose2d(39.25, 61.50, Math.toRadians(-90.00)))
                .lineTo(new Vector2d(36.00, 61.25))
                .lineTo(new Vector2d(34.78, 47.28))
                .lineTo(new Vector2d(37.49, 34.78))
                .lineToLinearHeading(new Pose2d(33.96, 19.56, Math.toRadians(230.00)))
                .lineTo(new Vector2d(32.00, 13.00))
                .lineTo(new Vector2d(29.50, 10.00))
                .lineTo(new Vector2d(34.23, 13.31))
                .lineToLinearHeading(new Pose2d(42.66, 14.13, Math.toRadians(0.00)))
                .lineToLinearHeading(new Pose2d(53.00, 10.00, Math.toRadians(354.00)))
                .lineToSplineHeading(new Pose2d(38.31, 14.94, Math.toRadians(0.00)))
                .splineToSplineHeading(new Pose2d(29.89, 10.32, Math.toRadians(226.00)), Math.toRadians(226.00))
                .lineToSplineHeading(new Pose2d(35.59, 17.93, Math.toRadians(61.70)))
                .splineTo(new Vector2d(35.00, 39.00), Math.toRadians(90.00))
                .build());
    }

}
