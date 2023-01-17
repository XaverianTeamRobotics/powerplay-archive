package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.MecanumDrivetrainFeature;
import org.firstinspires.ftc.teamcode.internals.misc.DrivetrainMapMode;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class KidsDriveThisOneExclamationPointEmoji extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new MecanumDrivetrainFeature(DrivetrainMapMode.FR_BR_FL_BL, false, false, false));
    }

    @Override
    public void run() {
        
    }

}
