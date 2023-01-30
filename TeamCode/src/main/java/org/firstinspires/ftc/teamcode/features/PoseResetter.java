package org.firstinspires.ftc.teamcode.features;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.PoseBucket;

public class PoseResetter extends Feature {

    @Override
    public void loop() {
        if(Devices.controller1.getDpadDown() || Devices.controller1.getDpadUp() || Devices.controller1.getDpadRight() || Devices.controller1.getDpadLeft()) {
            PoseBucket.forceSetPose(new Pose2d(0, 0, 0));
        }
    }

}
