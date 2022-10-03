package org.firstinspires.ftc.teamcode.utils.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter

class VoltageSensorGlobalAccess(var name: String) {

    private val request: ScriptParameters.Request = HardwareGetter.makeVoltageSensorRequest(name)

    val sensor: VoltageSensor
        get() {
            return HardwareGetter.getVoltageSensorFromRequest(name)
        }

    val voltage: Double
        get() {
            return HardwareGetter.getVoltageSensorData(name)
        }

}
