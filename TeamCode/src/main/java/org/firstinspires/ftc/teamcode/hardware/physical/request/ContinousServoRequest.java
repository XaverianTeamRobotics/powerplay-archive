package org.firstinspires.ftc.teamcode.hardware.physical.request;

import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.hardware.physical.inputs.ContinousServoInput;
import org.firstinspires.ftc.teamcode.hardware.physical.inputs.ContinousServoOptions;

public class ContinousServoRequest extends ScriptParameters.Request {

    private final CRServo SERVO;

    public ContinousServoRequest(String name, HardwareMap hardwareMap) {
        super(name);
        SERVO = hardwareMap.get(CRServo.class, name);
        SERVO.resetDeviceConfigurationForOpMode();
        SERVO.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public Object issueRequest(Object o) {
        ContinousServoInput input = (ContinousServoInput) o;
        if(input.getType() == ContinousServoOptions.GET) {
            return SERVO.getPower() * 100;
        }else{
            double val = input.getPower();
            val = Range.clip(val, -100, 100);
            SERVO.setPower(val / 100);
            return 0;
        }
    }

    @Override
    public Class getOutputType() {
        return double.class;
    }

    @Override
    public Class getInputType() {
        return ContinousServoInput.class;
    }

}
