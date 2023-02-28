package org.firstinspires.ftc.teamcode.internals.motion.pid.constrained;

import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.internals.image.PoleNavigator;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DashboardLogging;

/**
 * A profiled PID controller which attempts to center a specific pixel in an image.
 */
public class ImageFeedbackController {

    private final PoleNavigator localizer;
    private final ProfiledPIDController pidx, pidy;

    /**
     * @param localizer The image-based localizer which returns the XY coordinates of a pixel such that the center of the image is the origin.
     * @param kP The proportional gain of the controller.
     * @param kI The integral gain of the controller.
     * @param kD The derivitave gain of the controller.
     * @param mV The maximum velocity of the controller.
     * @param mA The maximum acceleration of the controller.
     * @param sp The setpoint or goal.
     * @param tol The tolerance of the setpoint.
     */
    public ImageFeedbackController(PoleNavigator localizer, double kP, double kI, double kD, double mV, double mA, double sp, double tol) {
        this(localizer, kP, kI, kD, mV, mA, sp, tol, kP, kI, kD, mV, mA, sp, tol);
    }

    public ImageFeedbackController(PoleNavigator localizer,
                                   double kPx, double kIx, double kDx, double mVx, double mAx, double spx, double tolx,
                                   double kPy, double kIy, double kDy, double mVy, double mAy, double spy, double toly) {
        this.localizer = localizer;
        pidx = new ProfiledPIDController(kPx, kIx, kDx, new TrapezoidProfile.Constraints(mVx, mAx));
        pidx.setGoal(spx);
        pidx.setTolerance(tolx);
        pidx.reset();
        pidy = new ProfiledPIDController(kPy, kIy, kDy, new TrapezoidProfile.Constraints(mVy, mAy));
        pidy.setGoal(spy);
        pidy.setTolerance(toly);
        pidy.reset();
        this.localizer.startStreaming();
    }

    /**
     * Calculates the output of the controller based on the output of the localizer.
     * @return An array of 2 elements containing the X and Y outputs (in that order).
     */
    public double[] calculate() {
        double x = localizer.getPoleDistanceX();
        double y = localizer.getPoleDistanceY();
        // this sometimes works, sometimes not :((
        if(x == Double.NaN || y == Double.NaN) {
            return new double[] { 0, 0 };
        }
        double width = localizer.getSize()[0];
        double height = localizer.getSize()[1];

        // normalize the coordinate to [-1, 1]
        double max, min;
        if(height < width) {
            min = 0 - width / 2.0;
            max = width - width / 2.0;
        }else{
            min = 0 - height / 2.0;
            max = height - height / 2.0;
        }
        x = -Range.scale(x, min, max, -1, 1);
        y = -Range.scale(y, min, max, -1, 1);

        double vx = pidx.calculate(x);
        double vy = pidy.calculate(y);
        DashboardLogging.log("x", x);
        DashboardLogging.log("y", y);
        DashboardLogging.log("width", width);
        DashboardLogging.log("height", height);
        DashboardLogging.log("vx", vx);
        DashboardLogging.log("vy", vy);
        DashboardLogging.update();
        return new double[] { vx, vy };
    }

}

