package org.firstinspires.ftc.teamcode.utils.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.utils.hardware.data.EncoderInput

class EncoderGlobalAccess(var name: String) {

    private val request: ScriptParameters.Request = HardwareGetter.makeEncoderRequest(name)

    val encoder: DcMotor
        get() {
            return HardwareGetter.getEncoderFromRequest(name)
        }

    val position: Int
        get() {
            return HardwareGetter.issueEncoderRequest(name, EncoderInput.GET)
        }

    fun reset() {
        HardwareGetter.issueEncoderRequest(name, EncoderInput.SET)
    }

}