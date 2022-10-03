package org.firstinspires.ftc.teamcode.utils.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.GyroSensor
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter

class Gyroscope(var name: String) {

    private val request: ScriptParameters.Request = HardwareGetter.makeGyroscopeRequest(name)

    val gyroscope: GyroSensor
        get() {
            return HardwareGetter.getGyroscopeFromRequest(name)
        }

    val heading: Int
        get() {
            return HardwareGetter.getGyroData(name).heading
        }

    val raw: IntArray
        get() {
            return HardwareGetter.getGyroData(name).raw
        }

    val status: String
        get() {
            return HardwareGetter.getGyroData(name).status
        }

}