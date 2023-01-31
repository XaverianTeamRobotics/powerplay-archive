package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.FourMotorArm;
import org.firstinspires.ftc.teamcode.features.FourMotorArmOdo;
import org.firstinspires.ftc.teamcode.features.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.features.PoseResetter;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class SmallbotProductionOdo extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new MecanumDrivetrain(true, false));
        registerFeature(new FourMotorArm());
        registerFeature(new FourMotorArmOdo());
//        registerFeature(new Hand());
        registerFeature(new PoseResetter());
    }

    @Override
    public void run() {}

}
