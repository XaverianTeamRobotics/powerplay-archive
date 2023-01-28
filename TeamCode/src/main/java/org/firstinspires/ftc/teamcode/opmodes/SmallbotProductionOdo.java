package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class SmallbotProductionOdo extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new MecanumDrivetrain(true, false));
//        registerFeature(new FourMotorArm());
//        registerFeature(new Hand());

        // TODO: opmode to reset position
        // TODO: button to reset position
    }

    @Override
    public void run() {

    }

}
