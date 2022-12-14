package org.firstinspires.ftc.teamcode.internals.motion.odometry.utils;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class MotorConfig {

    public final String NAME;
    public final DcMotorSimple.Direction DIRECTION;

    public MotorConfig(String name, DcMotorSimple.Direction direction) {
        NAME = name;
        DIRECTION = direction;
    }

}
