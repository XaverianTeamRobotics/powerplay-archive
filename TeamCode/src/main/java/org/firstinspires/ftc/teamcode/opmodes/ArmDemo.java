package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.ArmFeature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class ArmDemo extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        Devices.initializeArmMotors();
        registerFeature(new ArmFeature());
    }

    @Override
    public void run() {

    }

}
