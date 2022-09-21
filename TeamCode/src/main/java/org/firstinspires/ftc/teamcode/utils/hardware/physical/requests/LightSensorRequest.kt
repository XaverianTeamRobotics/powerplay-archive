package org.firstinspires.ftc.teamcode.utils.hardware.physical.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.LightSensor
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.LightSensorData

class LightSensorRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private val SENSOR: LightSensor
    private var led = true

    init {
        SENSOR = hardwareMap.get(LightSensor::class.java, name)
        SENSOR.enableLed(true)
    }

    override fun issueRequest(o: Any): Any {
        val change = o as Boolean
        if (change) {
            SENSOR.enableLed(!led)
            led = !led
        }
        return LightSensorData(
            SENSOR.rawLightDetected, SENSOR.rawLightDetectedMax,
            SENSOR.lightDetected, SENSOR.status()
        )
    }

    override fun getOutputType(): Class<*> {
        return LightSensorData::class.java
    }

    override fun getInputType(): Class<*>? {
        return Boolean::class.javaPrimitiveType
    }
}