package org.firstinspires.ftc.teamcode.internals.motion.odometry.utils;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

public class ResetPose extends OperationMode {

    @Override
    public void construct() {
        PoseBucket.forceSetPose(new Pose2d(0, 0, 0));
    }

    @Override
    public void run() {
        requestOpModeStop();
    }

}
