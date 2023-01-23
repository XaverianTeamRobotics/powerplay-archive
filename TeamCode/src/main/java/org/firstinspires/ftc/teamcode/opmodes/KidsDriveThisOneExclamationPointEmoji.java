package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.ArmFeature;
import org.firstinspires.ftc.teamcode.features.HandFeature;
import org.firstinspires.ftc.teamcode.features.NativeMecanumDrivetrain;
import org.firstinspires.ftc.teamcode.internals.misc.DrivetrainMapMode;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class KidsDriveThisOneExclamationPointEmoji extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new NativeMecanumDrivetrain(
            DrivetrainMapMode.FR_BR_FL_BL,
            false, false,
            false, false));
        registerFeature(new ArmFeature());
        registerFeature(new HandFeature());
    }

    @Override
    public void run() {

    }

}
