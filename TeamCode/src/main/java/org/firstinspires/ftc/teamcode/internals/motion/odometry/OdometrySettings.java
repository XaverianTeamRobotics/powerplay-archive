package org.firstinspires.ftc.teamcode.internals.motion.odometry;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.LocalizationType;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.OdoEncoder;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.EncoderConfig;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.MotorConfig;

/**
 * Settings for an odometry implementation using three odometry pods or two odometry pods and an intertial measurement unit. Tuning this is incredibly important for SLAM applications like autonomous driving or field-centric driving. Errors in this implementation compound continously, so it's important to make sure these values are as precise as possible.
 * <br><br>
 * This document makes numerous references to the Strafer v5 chassis by goBILDA. Its SKU is 3209-0001-0005.
 */
public class OdometrySettings {

    /**
     * The type of localization in use.
     */
    public static LocalizationType LOCALIZATION_TYPE = LocalizationType.POD;

    /**
     * The name and direction of the front right motor.
     */
    public static MotorConfig DRIVE_FRONT_RIGHT = new MotorConfig("motor0", DcMotorSimple.Direction.FORWARD);

    /**
     * The name and direction of the back right motor.
     */
    public static MotorConfig DRIVE_BACK_RIGHT = new MotorConfig("motor1", DcMotorSimple.Direction.FORWARD);

    /**
     * The name and direction of the front left motor.
     */
    public static MotorConfig DRIVE_FRONT_LEFT = new MotorConfig("motor2", DcMotorSimple.Direction.REVERSE);

    /**
     * The name and direction of the back left motor.
     */
    public static MotorConfig DRIVE_BACK_LEFT = new MotorConfig("motor3", DcMotorSimple.Direction.REVERSE);

    /**
     * The name and direction of the right encoder.
     */
    public static EncoderConfig ENCODER_RIGHT = new EncoderConfig("motor3", OdoEncoder.Direction.FORWARD);

    /**
     * The name and direction of the left encoder.
     */
    public static EncoderConfig ENCODER_LEFT = new EncoderConfig("motor0", OdoEncoder.Direction.FORWARD);

    /**
     * The name and direction of the middle encoder.
     */
    public static EncoderConfig ENCODER_MIDDLE = new EncoderConfig("motor7", OdoEncoder.Direction.FORWARD);

    /**
     * The name of the IMU or gyroscope replacing the right encoder if in use.
     */
    public static String IMU = "imu";

    /**
     * The ticks per revolution of the internal encoder inside your drive motors. This should come from the motor's specsheet or a similar specification document. For example, the Strafer v5's motors run count 537.7 ticks per revolution.
     */
    public static double TICKS_PER_REV = 537.7;

    /**
     * The maximum rotations per minute of your drive motors. This should come from the motor's specsheet or a similar specification document. For example, the Strafer v5's motors run at 312 RPM.
     */
    public static double MAX_RPM = 312;

    /**
     * The radius of the driving wheels in inches. This should come from the wheel's specsheet or a similar specification document. For example, the Strafer v5's mecanum wheels have a radius of 1.8898 inches.
     */
    public static double WHEEL_RADIUS = 1.8898;

    /**
     * The gear ratio between the output (wheel) speed and the input (motor) speed. For example, the Strafer v5 has a gear ratio of 1:1, or just 1.
     */
    public static double GEAR_RATIO = 1;

    /**
     * The track width of the driving wheels in inches. Track width is the lateral distance from the center of one wheel to the center of another wheel. Only a rough estimate is needed originally as it will be tuned later. For example, the Strafer v5's track width is ≈16 inches.
     */
    public static double TRACK_WIDTH = 13.7795;

    /**
     * Maximum experimental velocity of your bot. Calculate it using the equation:
     * <br><br>
     * <kbd>
     *  ((MAX_RPM / 60) * GEAR_RATIO * WHEEL_RADIUS * 2 * Math.PI) * 0.75;
     * </kbd>
     * <br><br>
     * This calculated value is actually only 75% of the theoretical maximum velocity of the bot. This is because what is theoretically true is never experimentally true. You can increase this value once your bot is tuned properly and follows paths accurately. Go no higher than 85% of the bot's theoretical maximum velocity unless necessary.
     * <br><br>
     * I recommend lowering this value as low as possible until the extra velocity is necessary. Basically, run your bot as slow as you can without sacrificing points.
     * <br><br>
     * If you don't have the time to calculate the slowest possible maximum velocity, 30 should suffice for most robots.
     */
    public static double MAX_VEL = 30;

    /**
     * Maximum experimental acceleration of your bot. This is best found through testing, but you should be fine setting it to the same number as your maximum experimental velocity.
     * <br><br>
     * I recommend lowering this value as low as possible until the extra acceleration is necessary. Basically, run your bot as slow as you can without sacrificing points.
     */
    public static double MAX_ACCEL = MAX_VEL;

    /**
     * Maximum experimental angular (turning) velocity of your bot. Calculate it using the equation:
     * <br><br>
     * <kbd>
     *  Math.toRadians(Range.clip(MAX_VEL / TRACK_WIDTH * (180 / Math.PI), -360, 360));
     * </kbd>
     * <br><br>
     * Notice that this value is represented in radians and within the interval <kbd>[-2π, 2π]</kbd>.
     * <br><br>
     * I recommend lowering this value as low as possible until the extra velocity is necessary. Basically, run your bot as slow as you can without sacrificing points.
     * <br><br>
     * If you don't have the time to calculate the slowest possible maximum angular velocity, 3 should suffice for most robots.
     */
    public static double MAX_ANG_VEL = 2;

    /**
     * Maximum experimental angular (turning) acceleration of your bot. This is best found through testing, but you should be fine setting it to the same number as your maximum experimental angular velocity.
     * <br><br>
     * I recommend lowering this value as low as possible until the extra acceleration is necessary. Basically, run your bot as slow as you can without sacrificing points.
     */
    public static double MAX_ANG_ACCEL = MAX_ANG_VEL;

    /**
     * The ticks per revolution of the encoder of your dead wheels. This should come from the encoder's specsheet or a similar specification document. For example, the REV Through Bore Encoder (SKU REV-11-1271) counts 8192 ticks per revolution.
     */
    public static double ENCODER_TICKS_PER_REV = 8192;

    /**
     * The radius of your dead wheels in inches. For example, the Rotacaster 35mm Omni Wheel (SKU R2-0354-99/01) has a radius of 0.688976377953 inches.
     */
    public static double ENCODER_WHEEL_RADIUS = 0.688976377953;

    /**
     * The gear ratio between the output (encoder) speed and input (dead wheel) speed.
     */
    public static double ENCODER_GEAR_RATIO = 1;

    /**
     * The track width of the encoder wheels in inches. Track width is the lateral distance from the center of one wheel to the center of another wheel. Only a rough estimate is needed originally as it will be tuned later. For example, <a href="https://openodometry.weebly.com/">OpenOdometry v3</a> has a track width of ≈16 inches when using the default assembly compatible with the Strafer v5.
     */
    public static double ENCODER_TRACK_WIDTH = 9.40945;

    /**
     * The offset of the middle (strafing) encoder from the center of the robot's rotation in the Z axis (where the X axis is the track width's axis and the Y axis is vertical). The X axis offset of the middle encoder isn't important; the kinematics work the same regardless of where the middle encoder lies on the X axis. Only a rough estimate is needed originally as it will be tuned later. For example, <a href="https://openodometry.weebly.com/">OpenOdometry v3</a> has a forward offset of ≈4 inches when using the default assembly compatible with the Strafer v5.
     */
    public static double ENCODER_FORWARD_OFFSET = 0.08;

    /**
     * The PID acceleration variable. This is to be tuned by the manual feedforward tuner.
     */
    public static double kA = 0.0033;

    /**
     * The PID velocity variable. This is to be tuned by the automatic feedforward tuner.
     */
    public static double kV = 0.01578;

    /**
     * The PID static variable. This is to be tuned by the automatic feedforward tuner.
     */
    public static double kStatic = 0.06238;

    /**
     * Mecanum wheels often exhibit less torque strafing than they do going straigt. This is to be tuned by the strafe tuner.
     */
    public static double LATERAL_MULTIPLIER = 1;


    /**
     * The heading PID controller. This is to be tuned by the final back-and-forth and follower PID tuners. Standard values are a kP of 8, kI of 0, and kD of 1.
     */
    public static PIDCoefficients HEADING_PID = new PIDCoefficients(0, 0, 0);

    /**
     * The translational PID controller. This is to be tuned by the final back-and-forth and follower PID tuners. Standard values are a kP of 8, kI of 0, and kD of 1.
     */
    public static PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(0, 0, 0);

    /**
     * The X weight used in normalization. This is usually fine being set to 1.
     */
    public static double VX_WEIGHT = 1;

    /**
     * The Y weight used in normalization. This is usually fine being set to 1.
     */
    public static double VY_WEIGHT = 1;

    /**
     * The Ω weight used in normalization. This is usually fine being set to 1.
     */
    public static double OMEGA_WEIGHT = 1;

}
