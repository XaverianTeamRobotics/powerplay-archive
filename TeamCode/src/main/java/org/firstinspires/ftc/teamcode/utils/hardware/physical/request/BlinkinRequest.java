package org.firstinspires.ftc.teamcode.utils.hardware.physical.request;

import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.inputs.BlinkinInput;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.inputs.BlinkinOptions;

public class BlinkinRequest extends ScriptParameters.Request {

    private final Servo SERVO;

    public BlinkinRequest(String name, HardwareMap hardwareMap) {
        super(name);
        SERVO = hardwareMap.get(Servo.class, name);
        SERVO.resetDeviceConfigurationForOpMode();
    }

    @Override
    public Object issueRequest(Object o) {
        BlinkinInput input = (BlinkinInput) o;
        if(input.getType() == BlinkinOptions.GET) {
            return SERVO.getPosition();
        }else{
            SERVO.setPosition(Range.clip(input.getId(), 0.2525, 0.7475));
            return 0;
        }
    }

    @Override
    public Class getOutputType() {
        return double.class;
    }

    @Override
    public Class getInputType() {
        return BlinkinInput.class;
    }

}
