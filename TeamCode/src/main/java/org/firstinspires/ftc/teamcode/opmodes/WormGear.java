package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class WormGear extends OperationMode implements TeleOperation {
    @Override
    public void construct() {

    }

    @Override
    public void run() {
        double lpower = Devices.controller1.getLeftTrigger() - Devices.controller2.getLeftTrigger();
        double rpower = Devices.controller1.getRightTrigger() - Devices.controller2.getRightTrigger();
        Devices.motor0.setPower(lpower);
        Devices.motor1.setPower(-lpower);
        Devices.motor2.setPower(rpower);
        Devices.motor3.setPower(-rpower);
    }
}
