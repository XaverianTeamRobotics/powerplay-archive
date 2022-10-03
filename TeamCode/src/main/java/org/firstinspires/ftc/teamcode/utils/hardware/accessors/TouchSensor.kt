package org.firstinspires.ftc.teamcode.utils.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter

class TouchSensor(var name: String) {

    private val request: ScriptParameters.Request = HardwareGetter.makeTouchSensorRequest(name)

    val sensor: TouchSensor
        get() {
            return HardwareGetter.getTouchSensorFromRequest(name)
        }

    fun isPressed(): Boolean {
        return HardwareGetter.getTouchSensorData(name).pressed
    }

    val force: Double
        get() {
            return HardwareGetter.getTouchSensorData(name).force
        }

}
