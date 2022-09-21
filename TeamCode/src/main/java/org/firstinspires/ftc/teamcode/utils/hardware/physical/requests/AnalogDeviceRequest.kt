package org.firstinspires.ftc.teamcode.utils.hardware.physical.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.AnalogInput
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.AnalogData

class AnalogDeviceRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private val CONTROLLER: AnalogInput

    init {
        CONTROLLER = hardwareMap.get(AnalogInput::class.java, name)
        CONTROLLER.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        return AnalogData(CONTROLLER.voltage, CONTROLLER.maxVoltage)
    }

    override fun getOutputType(): Class<*> {
        return AnalogData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}