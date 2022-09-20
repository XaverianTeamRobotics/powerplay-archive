package org.firstinspires.ftc.teamcode.utils.hardware.physical.request;

import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.inputs.AnalogData;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.inputs.AnalogData;
import org.firstinspires.ftc.teamcode.utils.hardware.physical.inputs.AnalogData;

public class AnalogDeviceRequest extends ScriptParameters.Request {

    private final AnalogInput CONTROLLER;

    public AnalogDeviceRequest(String name, HardwareMap hardwareMap) {
        super(name);
        CONTROLLER = hardwareMap.get(AnalogInput.class, name);
        CONTROLLER.resetDeviceConfigurationForOpMode();
    }

    @Override
    public Object issueRequest(Object o) {
        return new AnalogData(CONTROLLER.getVoltage(), CONTROLLER.getMaxVoltage());
    }

    @Override
    public Class getOutputType() {
        return AnalogData.class;
    }

    @Override
    public Class getInputType() {
        return Object.class;
    }

}
