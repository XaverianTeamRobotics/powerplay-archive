package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.FourMotorArmFeature;
import org.firstinspires.ftc.teamcode.features.NativeMecanumDrivetrain;
import org.firstinspires.ftc.teamcode.internals.misc.DrivetrainMapMode;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class SmallBotProduction extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new NativeMecanumDrivetrain(
            DrivetrainMapMode.FR_BR_FL_BL,
            false, true,
            false, false));
        registerFeature(new FourMotorArmFeature());
    }

    @Override
    public void run() {

    }

}
