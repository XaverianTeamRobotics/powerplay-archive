package org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.OdometrySettings;

public class ConstantUtils {

    /**
     * Set RUN_USING_ENCODER to true to enable built-in hub velocity control using drive encoders.
     * Set this flag to false if drive encoders are not present and an alternative localization
     * method is in use (e.g., dead wheels).
     *
     * If using the built-in motor velocity PID, update MOTOR_VELO_PID with the tuned coefficients
     * from DriveVelocityPIDTuner.
     */
    public static final boolean RUN_USING_ENCODER = false;
    public static PIDFCoefficients MOTOR_VELO_PID = new PIDFCoefficients(0, 0, 0, getMotorVelocityF(OdometrySettings.MAX_RPM / 60 * OdometrySettings.TICKS_PER_REV));

    public static double encoderTicksToInches(double ticks) {
        return OdometrySettings.WHEEL_RADIUS * 2 * Math.PI * OdometrySettings.GEAR_RATIO * ticks / OdometrySettings.TICKS_PER_REV;
    }

    public static double rpmToVelocity(double rpm) {
        return rpm * OdometrySettings.GEAR_RATIO * 2 * Math.PI * OdometrySettings.WHEEL_RADIUS / 60.0;
    }

    public static double getMotorVelocityF(double ticksPerSecond) {
        return 32767 / ticksPerSecond;
    }

}
