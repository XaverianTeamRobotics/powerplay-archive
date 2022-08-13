package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.hardware.physical.GamepadRequestInput;
import org.firstinspires.ftc.teamcode.hardware.physical.MotorOperation;
import org.firstinspires.ftc.teamcode.hardware.physical.StandardMotorParameters;
import org.firstinspires.ftc.teamcode.utils.opModeRegistration.OperationMode;
import org.firstinspires.ftc.teamcode.utils.opModeRegistration.TeleOperation;

public class GamepadTestOpMode extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        HardwareGetter.makeMotorRequest("Motor1");
    }

    @Override
    public void run() {
        double power = HardwareGetter.getGamepadValue("gamepad1", GamepadRequestInput.RIGHT_TRIGGER);
        HardwareGetter.setMotorValue("Motor1", new StandardMotorParameters(power,
            MotorOperation.POWER));
    }
}
