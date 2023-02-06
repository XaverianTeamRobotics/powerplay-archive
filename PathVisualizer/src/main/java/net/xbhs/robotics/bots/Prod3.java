package net.xbhs.robotics.bots;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import net.xbhs.robotics.utils.Bot;
import net.xbhs.robotics.utils.Environment;

public class Prod3 extends Bot {

    @Override
    public RoadRunnerBotEntity bot() {

        return new DefaultBotBuilder(Environment.MEEP)
            .setColorScheme(new ColorSchemeBlueDark())
            .setConstraints(30, 30, 3, 3, 13.90)
            .setDimensions(14, 18)
            .followTrajectorySequence(drive -> {
                TrajectorySequence untitled0 = drive.trajectorySequenceBuilder(new Pose2d(36.29, 61.50, Math.toRadians(-90.00)))
                    // leave wall
                    .lineToConstantHeading(new Vector2d(36.29, 58))
                    // to straight
                    .lineToConstantHeading(new Vector2d(14.5, 58))
                    // to middle, turn
                    .lineToConstantHeading(new Vector2d(14.5, 0))
                    .turn(Math.toRadians(90))
                    // dropoff
                    .lineToConstantHeading(new Vector2d(19.11, 0.00))
                    // go to straight to pickup
                    .lineToConstantHeading(new Vector2d(8.26, 12.50))
                    // to pickup
                    .lineToConstantHeading(new Vector2d(59.5, 12.50))
                    // leave pickup
                    .lineToConstantHeading(new Vector2d(8.26, 12.50))
                    // dropoff
                    .lineToConstantHeading(new Vector2d(19.11, 0.00))
                    // leave dropoff, to park 3
                    .lineToConstantHeading(new Vector2d(8.26, 12.50))
                    // park 2
                    .lineToConstantHeading(new Vector2d(36, 12.50))
                    // park 1
                    .lineToConstantHeading(new Vector2d(56, 12.50))
                    .build();
                return untitled0;
            });
    }

}
