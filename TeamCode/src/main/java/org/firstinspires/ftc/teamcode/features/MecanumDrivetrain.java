package org.firstinspires.ftc.teamcode.features;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.PoseBucket;
import org.firstinspires.ftc.teamcode.internals.motion.pid.constrained.SlewRateLimiter;
import org.jetbrains.annotations.NotNull;

/**
 * A mecanum drivetrain. This relies on odometry. Use {@link NativeMecanumDrivetrain} if you don't have odometry.
 */
public class MecanumDrivetrain extends Feature implements Buildable {

    private final boolean FIELD_CENTRIC;
    private final boolean DRIVER_ASSIST;
    private AutonomousDrivetrain drivetrain;
    private double mX = 1, mY = 1, mR = 1, mS = 1;
    private SlewRateLimiter sX = new SlewRateLimiter(mS);
    private SlewRateLimiter sY = new SlewRateLimiter(mS);
    private SlewRateLimiter sR = new SlewRateLimiter(mS);

    public MecanumDrivetrain(boolean fieldCentric, boolean driverAssist) {
        FIELD_CENTRIC = fieldCentric;
        DRIVER_ASSIST = driverAssist;
    }

    @Override
    public void build() {
        drivetrain = new AutonomousDrivetrain();
        drivetrain.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drivetrain.setPoseEstimate(PoseBucket.getPose());
    }

    @Override
    public void loop() {
        // Read current pose
        Pose2d poseEstimate = drivetrain.getPoseEstimate();
        // Get gamepad inputs
        double x = Devices.controller1.getLeftStickX() * mX;
        double y = Devices.controller1.getLeftStickY() * mY;
        double r = Devices.controller1.getRightStickX() * mR;
        boolean reset = Devices.controller1.getTouchpad();
        if(reset) {
            drivetrain.setPoseEstimate(new Pose2d(0, 0, 0));
        }
        // dampen if assist is enabled
        if(DRIVER_ASSIST) {
            double[] v = dampen(x / 100, y / 100, r / 100);
            x = Range.clip(v[0] * 100, -100, 100);
            y = Range.clip(v[1] * 100, -100, 100);
            r = Range.clip(v[2] * 100, -100, 100);
        }
        // Create a vector from the gamepad x/y inputs
        // Then, rotate that vector by the inverse of that heading if we're using field centric--otherwise we'll just assume the heading is 0
        Vector2d input;
        if(FIELD_CENTRIC) {
            input = new Vector2d(
                -Compressor.compress(y),
                -Compressor.compress(x)
            ).rotated(-poseEstimate.getHeading());
        }else{
            input = new Vector2d(
                -Compressor.compress(y),
                -Compressor.compress(x)
            );
        }
        // Pass in the rotated input + right stick value for rotation
        // Rotation is not part of the rotated input thus must be passed in separately
        drivetrain.setWeightedDrivePower(
            new Pose2d(
                input.getX(),
                input.getY(),
                -Compressor.compress(r)
            )
        );
        // Update everything. Odometry. Etc.
        drivetrain.update();
    }

    private double @NotNull [] dampen(double x, double y, double r) {
        return new double[] { sX.calculate(x), sY.calculate(y), sR.calculate(r) };
    }

}
