package org.firstinspires.ftc.teamcode.utils.hardware.physical.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

class LaserDistanceSensorRequest(name: String?, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private val SENSOR: DistanceSensor

    init {
        SENSOR = hardwareMap.get(DistanceSensor::class.java, name)
        SENSOR.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        return SENSOR.getDistance(o as DistanceUnit)
    }

    override fun getOutputType(): Class<*>? {
        return Double::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return DistanceUnit::class.java
    }
}