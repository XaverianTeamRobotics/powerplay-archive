package org.firstinspires.ftc.teamcode.utils.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.AccelerationSensor
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter

class AccelerometerGlobalAccess(var name: String) {

    private val request: ScriptParameters.Request = HardwareGetter.makeAccelerometerRequest(name)

    val accelerometer: AccelerationSensor
        get() = HardwareGetter.getAccelerometerFromRequest(name)

    val acceleration: Acceleration
        get() = HardwareGetter.getAccelerometerData(name).force

    val status: String
        get() = HardwareGetter.getAccelerometerData(name).status

}