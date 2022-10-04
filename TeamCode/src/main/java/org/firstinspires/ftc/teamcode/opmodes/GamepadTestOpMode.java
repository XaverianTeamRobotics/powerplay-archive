package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class GamepadTestOpMode extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
    }

    @Override
    public void run() {
        Devices.getMotor0().setPower(Devices.getController1().getRightTrigger());
    }
}
