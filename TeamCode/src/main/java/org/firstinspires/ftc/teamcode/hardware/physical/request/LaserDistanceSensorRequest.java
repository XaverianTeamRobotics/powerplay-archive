package org.firstinspires.ftc.teamcode.hardware.physical.request;

import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class LaserDistanceSensorRequest extends ScriptParameters.Request {

    private final DistanceSensor SENSOR;

    public LaserDistanceSensorRequest(String name, HardwareMap hardwareMap) {
        super(name);
        SENSOR = hardwareMap.get(DistanceSensor.class, name);
        SENSOR.resetDeviceConfigurationForOpMode();
    }

    @Override
    public Object issueRequest(Object o) {
        return SENSOR.getDistance((DistanceUnit) o);
    }

    @Override
    public Class getOutputType() {
        return double.class;
    }

    @Override
    public Class getInputType() {
        return DistanceUnit.class;
    }

}
