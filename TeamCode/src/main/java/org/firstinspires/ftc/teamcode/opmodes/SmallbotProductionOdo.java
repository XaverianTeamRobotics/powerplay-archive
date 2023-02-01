package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.features.FourMotorArm;
import org.firstinspires.ftc.teamcode.features.Hand;
import org.firstinspires.ftc.teamcode.features.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.PoseBucket;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class SmallbotProductionOdo extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        PoseBucket.forceSetPose(new Pose2d(39.25, 61.5, Math.toRadians(-90)));
        registerFeature(new MecanumDrivetrain(false, false));
        registerFeature(new FourMotorArm());
        registerFeature(new Hand());
    }

    @Override
    public void run() {}

}
