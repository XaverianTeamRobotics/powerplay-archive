package org.firstinspires.ftc.teamcode.internals.motion.odometry.utils;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public class PoseBucket {

    private static Pose2d pose = new Pose2d(0, 0, 0);

    public static void setPose(Pose2d pose) {
        PoseBucket.pose = pose;
    }

    public static Pose2d getPose() {
        return pose;
    }

}
