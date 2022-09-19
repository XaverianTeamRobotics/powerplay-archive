package org.firstinspires.ftc.teamcode.hardware.physical.request;

import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.hardware.physical.inputs.EncoderInput;

public class EncoderRequest extends ScriptParameters.Request {

    private final DcMotor ENCODER;
    private int offset;

    public EncoderRequest(String name, HardwareMap hardwareMap) {
        super(name);
        ENCODER = hardwareMap.get(DcMotor.class, name);
        offset = ENCODER.getCurrentPosition();
    }

    @Override
    public Object issueRequest(Object o) {
        EncoderInput input = (EncoderInput) o;
        if(input == EncoderInput.GET) {
            return ENCODER.getCurrentPosition() - offset;
        }else{
            offset = ENCODER.getCurrentPosition();
            return 0;
        }
    }

    @Override
    public Class getOutputType() {
        return Integer.class;
    }

    @Override
    public Class getInputType() {
        return EncoderInput.class;
    }

}
