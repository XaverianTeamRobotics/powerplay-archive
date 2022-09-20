package org.firstinspires.ftc.teamcode.utils.hardware.physical.request;

import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.inputs.GyroData;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.inputs.GyroData;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.inputs.GyroData;

public class GyroRequest extends ScriptParameters.Request {

    private final GyroSensor SENSOR;

    public GyroRequest(String name, HardwareMap hardwareMap) {
        super(name);
        SENSOR = hardwareMap.get(GyroSensor.class, name);
        SENSOR.resetDeviceConfigurationForOpMode();
        try {
            SENSOR.calibrate();
            while(SENSOR.isCalibrating());
        } catch(UnsupportedOperationException ignored) {}
    }

    @Override
    public Object issueRequest(Object o) {
        return new GyroData(SENSOR.getHeading(), new int[] { SENSOR.rawX(), SENSOR.rawY(), SENSOR.rawZ() },
            SENSOR.status());
    }

    @Override
    public Class getOutputType() {
        return GyroData.class;
    }

    @Override
    public Class getInputType() {
        return Object.class;
    }

}
