package org.firstinspires.ftc.teamcode.utils.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.AccelerationSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.utils.hardware.data.AccelerometerData

class AccelerometerRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private val SENSOR: AccelerationSensor

    init {
        SENSOR = hardwareMap.get(AccelerationSensor::class.java, name)
        SENSOR.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        return AccelerometerData(
            SENSOR.acceleration,
            SENSOR.status()
        )
    }

    override fun getOutputType(): Class<*> {
        return AccelerometerData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}