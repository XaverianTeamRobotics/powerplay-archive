package org.firstinspires.ftc.teamcode.internals.misc;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.image.PoleNavigator;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.pid.constrained.ImageFeedbackController;

@Config
public class PoleCenterer {

    public static double kP = 0, kI = 0, kD = 0, mV = 0, mA = 0, sp = 0, tol = 0;

    private AutonomousDrivetrain drivetrain = null;
    private final ImageFeedbackController controller;

    public PoleCenterer() {
        controller = new ImageFeedbackController(new PoleNavigator(), kP, kI, kD, mV, mA, sp, tol);
    }

    public boolean loop() {
        double[] xy = controller.calculate();
        Vector2d input = new Vector2d(
            -xy[1],
            -xy[0]
        );
        drivetrain.setWeightedDrivePower(
            new Pose2d(
                input.getX(),
                input.getY(),
                0
            )
        );
        return xy[0] == 0 && xy[1] == 0;
    }

    public void center() {
        if(drivetrain == null) {
            throw new NullPointerException("Drivetrain can't be null!");
        }
        boolean done = false;
        while(!done) {
            done = loop();
        }
    }

    public void setDrivetrain(AutonomousDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

}
