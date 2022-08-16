package org.firstinspires.ftc.teamcode.opmodes;

import com.michaell.looping.ScriptParameters;
import org.firstinspires.ftc.teamcode.hardware.Devices;
import org.firstinspires.ftc.teamcode.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.hardware.physical.MotorOperation;
import org.firstinspires.ftc.teamcode.hardware.physical.StandardMotorParameters;
import org.firstinspires.ftc.teamcode.utils.opModeRegistration.OperationMode;
import org.firstinspires.ftc.teamcode.utils.opModeRegistration.TeleOperation;

public class SampleOpMode extends OperationMode implements TeleOperation {
    @Override
    public void construct() {

    }

    @Override
    public void run() {
        Devices.getMotor0().setPower(1.0);
    }
}
