package org.firstinspires.ftc.teamcode.utils.hardware.physical.requests;

import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class VoltageSensorRequest extends ScriptParameters.Request {

    private final VoltageSensor SENSOR;

    public VoltageSensorRequest(String name, HardwareMap hardwareMap) {
        super(name);
        SENSOR = hardwareMap.get(VoltageSensor.class, name);
        SENSOR.resetDeviceConfigurationForOpMode();
    }

    @Override
    public Object issueRequest(Object o) {
        return SENSOR.getVoltage();
    }

    @Override
    public Class getOutputType() {
        return double.class;
    }

    @Override
    public Class getInputType() {
        return Object.class;
    }

}
