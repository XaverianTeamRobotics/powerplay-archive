package org.firstinspires.ftc.teamcode.utils.hardware.physical.requests;

import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.TouchSensorData;

public class TouchSensorRequest extends ScriptParameters.Request {

    private final TouchSensor SENSOR;

    public TouchSensorRequest(String name, HardwareMap hardwareMap) {
        super(name);
        SENSOR = hardwareMap.get(TouchSensor.class, name);
        SENSOR.resetDeviceConfigurationForOpMode();
    }

    @Override
    public Object issueRequest(Object o) {
        return new TouchSensorData(SENSOR.isPressed(), Range.clip(SENSOR.getValue() * 100, 0, 100));
    }

    @Override
    public Class getOutputType() {
        return TouchSensorData.class;
    }

    @Override
    public Class getInputType() {
        return Object.class;
    }

}
