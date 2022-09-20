package org.firstinspires.ftc.teamcode.utils.hardware.physical.requests;

import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.accessors.AccelerometerGlobalAccess;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.AccelerometerData;

public class AccelerometerRequest extends ScriptParameters.Request {

    private final AccelerationSensor SENSOR;

    public AccelerometerRequest(String name, HardwareMap hardwareMap) {
        super(name);
        SENSOR = hardwareMap.get(AccelerationSensor.class, name);
        SENSOR.resetDeviceConfigurationForOpMode();
    }

    @Override
    public Object issueRequest(Object o) {
        return new AccelerometerData(SENSOR.getAcceleration(), SENSOR.status());
    }

    @Override
    public Class getOutputType() {
        return AccelerometerData.class;
    }

    @Override
    public Class getInputType() {
        return Object.class;
    }

}
