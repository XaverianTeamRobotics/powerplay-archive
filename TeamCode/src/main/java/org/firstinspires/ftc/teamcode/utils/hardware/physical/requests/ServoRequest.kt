package org.firstinspires.ftc.teamcode.utils.hardware.physical.requests;

import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.ServoInput;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.ServoOptions;

public class ServoRequest extends ScriptParameters.Request {

    private final Servo SERVO;

    public ServoRequest(String name, HardwareMap hardwareMap) {
        super(name);
        SERVO = hardwareMap.get(Servo.class, name);
        SERVO.resetDeviceConfigurationForOpMode();
        SERVO.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public Object issueRequest(Object o) {
        ServoInput vals = (ServoInput) o;
        if(vals.getType() == ServoOptions.GET) {
            return SERVO.getPosition() * 100;
        }else{
            double val = vals.getPosition();
            val = Range.clip(val, 0, 100);
            SERVO.setPosition(val / 100);
            return 0;
        }
    }

    @Override
    public Class getOutputType() {
        return double.class;
    }

    @Override
    public Class getInputType() {
        return ServoInput.class;
    }

}
