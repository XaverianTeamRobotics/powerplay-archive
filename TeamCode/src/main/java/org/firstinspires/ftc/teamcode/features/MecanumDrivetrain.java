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
    private AutonomousDrivetrain drivetrain;

    public MecanumDrivetrain(boolean fieldCentric) {
        FIELD_CENTRIC = fieldCentric;
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
        // Create a vector from the gamepad x/y inputs
        // Then, rotate that vector by the inverse of that heading if we're using field centric--otherwise we'll just assume the heading is 0
        Vector2d input;
        if(FIELD_CENTRIC) {
            input = new Vector2d(
                -Compressor.compress(Devices.controller1.getLeftStickY()),
                -Compressor.compress(Devices.controller1.getLeftStickX())
            ).rotated(-poseEstimate.getHeading());
        }else{
            input = new Vector2d(
                -Compressor.compress(Devices.controller1.getLeftStickY()),
                -Compressor.compress(Devices.controller1.getLeftStickX())
            );
        }
        // Pass in the rotated input + right stick value for rotation
        // Rotation is not part of the rotated input thus must be passed in separately
        drivetrain.setWeightedDrivePower(
            new Pose2d(
                input.getX(),
                input.getY(),
                -Compressor.compress(Devices.controller1.getRightStickX())
            )
        );
        // Update everything. Odometry. Etc.
        drivetrain.update();
    }

}
