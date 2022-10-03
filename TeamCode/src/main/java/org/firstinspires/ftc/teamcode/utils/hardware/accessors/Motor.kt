package org.firstinspires.ftc.teamcode.utils.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.utils.hardware.data.MotorOperation
import org.firstinspires.ftc.teamcode.utils.hardware.data.StandardMotorParameters

class Motor(var name: String) {

    private val motorRequest: ScriptParameters.Request = HardwareGetter.makeMotorRequest(name)

    val motor: DcMotor
        get() = HardwareGetter.getMotorFromRequest(name)

    var power: Double = 0.0
        get() {
            return motor.power
        } set(value) {
            field = value
            HardwareGetter.setMotorValue(name, StandardMotorParameters(value, MotorOperation.POWER))
        }

    var speed: Double = 0.0
        get() {
            return motor.power
        } set(value) {
            field = value
            HardwareGetter.setMotorValue(name, StandardMotorParameters(value, MotorOperation.ENCODER_POWER))
        }

    fun isBusy(): Boolean {
        return HardwareGetter.isMotorBusy(name)
    }

}
