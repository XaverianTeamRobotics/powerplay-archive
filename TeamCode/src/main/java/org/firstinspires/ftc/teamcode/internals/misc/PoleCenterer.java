package org.firstinspires.ftc.teamcode.internals.misc;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.image.PoleNavigator;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.pid.constrained.ImageFeedbackController;

import java.util.Objects;

@Config
public class PoleCenterer {

    public static double kP = 0.35, kI = 0, kD = 0, mV = 0.00001, mA = 0.00001, sp = 0, ctol = 0.1, etol = 0.2;

    private AutonomousDrivetrain drivetrain = null;
    private final ImageFeedbackController controller;

    public PoleCenterer() {
        controller = new ImageFeedbackController(new PoleNavigator(), kP, kI, kD, mV, mA, sp, ctol, etol);
    }

    public PoleCenterer(int index) {
        controller = new ImageFeedbackController(new PoleNavigator(index), kP, kI, kD, mV, mA, sp, ctol, etol);
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
        return xy[2] == 0;
    }

    public void center() {
        if(drivetrain == null) {
            throw new NullPointerException("Drivetrain can't be null!");
        }
        boolean done = false;
        while(!done && Objects.requireNonNull(HardwareGetter.getOpMode()).opModeIsActive()) {
            done = loop();
        }
        drivetrain.setWeightedDrivePower(new Pose2d(0, 0, 0));
    }

    public void setDrivetrain(AutonomousDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

}
