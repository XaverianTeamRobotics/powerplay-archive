package org.firstinspires.ftc.teamcode.utils.hardware.physical.requests;

import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.LightSensorData;

public class LightSensorRequest extends ScriptParameters.Request {

    private final LightSensor SENSOR;
    private boolean led = true;

    public LightSensorRequest(String name, HardwareMap hardwareMap) {
        super(name);
        SENSOR = hardwareMap.get(LightSensor.class, name);
        SENSOR.enableLed(true);
    }

    @Override
    public Object issueRequest(Object o) {
        boolean change = (boolean) o;
        if(change) {
            SENSOR.enableLed(!led);
            led = !led;
        }
        return new LightSensorData(SENSOR.getRawLightDetected(), SENSOR.getRawLightDetectedMax(),
            SENSOR.getLightDetected(), SENSOR.status());
    }

    @Override
    public Class getOutputType() {
        return LightSensorData.class;
    }

    @Override
    public Class getInputType() {
        return boolean.class;
    }

}
