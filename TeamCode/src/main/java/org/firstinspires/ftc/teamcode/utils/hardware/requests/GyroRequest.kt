package org.firstinspires.ftc.teamcode.utils.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.GyroSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.utils.hardware.data.GyroData

class GyroRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private val SENSOR: GyroSensor

    init {
        SENSOR = hardwareMap.get(GyroSensor::class.java, name)
        SENSOR.resetDeviceConfigurationForOpMode()
        try {
            SENSOR.calibrate()
            while (SENSOR.isCalibrating);
        } catch (ignored: UnsupportedOperationException) {
        }
    }

    override fun issueRequest(o: Any): Any {
        return GyroData(
            SENSOR.heading, intArrayOf(SENSOR.rawX(), SENSOR.rawY(), SENSOR.rawZ()),
            SENSOR.status()
        )
    }

    override fun getOutputType(): Class<*> {
        return GyroData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}