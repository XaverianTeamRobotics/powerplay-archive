package org.firstinspires.ftc.teamcode.utils.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.CRServo
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.utils.hardware.data.ContinousServoInput
import org.firstinspires.ftc.teamcode.utils.hardware.data.ContinousServoOptions

class ContinousServo(var name: String) {

    private val request: ScriptParameters.Request = HardwareGetter.makeContinousServoRequest(name)

    val servo: CRServo
        get() {
            return HardwareGetter.getContinousServoFromRequest(name)
        }

    var power: Double = 0.0
        get() {
            return HardwareGetter.issueContinousServoRequest(name, ContinousServoInput(field, ContinousServoOptions.GET))
        } set(value) {
            field = value
            HardwareGetter.issueContinousServoRequest(name, ContinousServoInput(value, ContinousServoOptions.SET))
        }



}