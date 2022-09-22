package org.firstinspires.ftc.teamcode.utils.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.TouchSensor
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.utils.hardware.data.TouchSensorData

class TouchSensorRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private val SENSOR: TouchSensor

    init {
        SENSOR = hardwareMap.get(TouchSensor::class.java, name)
        SENSOR.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        return TouchSensorData(SENSOR.isPressed, Range.clip(SENSOR.value * 100, 0.0, 100.0))
    }

    override fun getOutputType(): Class<*> {
        return TouchSensorData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}