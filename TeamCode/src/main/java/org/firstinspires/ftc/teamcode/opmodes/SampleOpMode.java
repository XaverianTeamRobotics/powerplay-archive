package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.utils.hardware.Devices;
import org.firstinspires.ftc.teamcode.utils.registration.OperationMode;
import org.firstinspires.ftc.teamcode.utils.registration.TeleOperation;

public class SampleOpMode extends OperationMode implements TeleOperation {
    @Override
    public void construct() {

    }

    @Override
    public void run() {
        Devices.getMotor0().setPower(1.0);
    }
}
