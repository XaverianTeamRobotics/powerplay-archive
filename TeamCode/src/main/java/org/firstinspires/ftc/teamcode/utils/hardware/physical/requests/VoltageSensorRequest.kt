package org.firstinspires.ftc.teamcode.utils.hardware.physical.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.VoltageSensor

class VoltageSensorRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private val SENSOR: VoltageSensor

    init {
        SENSOR = hardwareMap.get(VoltageSensor::class.java, name)
        SENSOR.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        return SENSOR.voltage
    }

    override fun getOutputType(): Class<*>? {
        return Double::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}