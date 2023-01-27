package org.firstinspires.ftc.teamcode.features;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.Compressor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.PoseBucket;

/**
 * A mecanum drivetrain. This relies on odometry. Use {@link NativeMecanumDrivetrain} if you don't have odometry.
 */
public class MecanumDrivetrain extends Feature implements Buildable {

    private final boolean FIELD_CENTRIC;
    private final boolean DRIVER_ASSIST;
    private AutonomousDrivetrain drivetrain;
    private double lx = 0, ly = 0, lr = 0;

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
        double x = Devices.controller1.getLeftStickX();
        double y = Devices.controller1.getLeftStickY();
        double r = Devices.controller1.getRightStickX();
        // dampen if assist is enabled
        if(DRIVER_ASSIST) {
            double b = 0.3;
            x = dampen(x, lx, b);
            y = dampen(y, ly, b);
            r = dampen(r, lr, b);
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
        lx = x;
        ly = y;
        lr = r;
    }

    private double dampen(double a, double la, double b) {
        double step = Math.abs(a - la) * b;
        if(a > la) {
            return la + step;
        }else if(a < la) {
            return la - step;
        }else{
            return a;
        }
    }

}
