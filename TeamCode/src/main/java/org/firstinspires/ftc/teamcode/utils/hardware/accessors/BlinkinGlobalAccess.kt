package org.firstinspires.ftc.teamcode.utils.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.utils.hardware.data.BlinkinInput
import org.firstinspires.ftc.teamcode.utils.hardware.data.BlinkinOptions

class BlinkinGlobalAccess(var name: String) {

    private val request: ScriptParameters.Request = HardwareGetter.makeBlinkinRequest(name)

    val driver: Servo
        get() = HardwareGetter.getBlinkinFromRequest(name)

    var id: Double = 0.0
        get() {
            return HardwareGetter.issueBlinkinRequest(name, BlinkinInput(field, BlinkinOptions.GET))
        } set(value) {
            HardwareGetter.issueBlinkinRequest(name, BlinkinInput(value, BlinkinOptions.SET))
            field = value
        }

}