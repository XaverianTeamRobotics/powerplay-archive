package org.firstinspires.ftc.teamcode.utils.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.utils.hardware.data.ServoInput
import org.firstinspires.ftc.teamcode.utils.hardware.data.ServoOptions

class ServoGlobalAccess(var name: String) {

    private val request: ScriptParameters.Request = HardwareGetter.makeServoRequest(name)

    val servo: Servo
        get() {
            return HardwareGetter.getServoFromRequest(name)
        }

    var position: Double = 0.0
        get() {
            val pos = HardwareGetter.issueServoRequest(name, ServoInput(0.0, ServoOptions.GET))
            field = pos
            return pos
        } set(value) {
            field = value
            HardwareGetter.issueServoRequest(name, ServoInput(value, ServoOptions.SET))
        }

}
