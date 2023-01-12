package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.MecanumDrivetrainFeature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.DrivetrainMapMode;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging;

public class ProductionSmallbot extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new MecanumDrivetrainFeature(DrivetrainMapMode.FR_BR_FL_BL));
    }

    @Override
    public void run() {
        Logging.log(Devices.motor0.getName());
//        Logging.log(Devices.camera0.getDeviceName());
        Logging.updateLog();
    }

}
