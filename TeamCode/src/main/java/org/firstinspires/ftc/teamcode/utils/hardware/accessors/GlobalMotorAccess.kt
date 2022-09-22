package org.firstinspires.ftc.teamcode.utils.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.utils.hardware.data.MotorOperation
import org.firstinspires.ftc.teamcode.utils.hardware.data.StandardMotorParameters

class GlobalMotorAccess(var name: String) {
    private val motorRequest: ScriptParameters.Request = HardwareGetter.makeMotorRequest(name)

    val motor: DcMotor
        get() = HardwareGetter.getMotorFromRequest(name)

    var power: Double = 0.0
        set(value) {
            field = value
            HardwareGetter.setMotorValue(name, StandardMotorParameters(value, runMode))
        }

    var runMode: MotorOperation = MotorOperation.POWER
        set(value) {
            field = value
            HardwareGetter.setMotorValue(name, StandardMotorParameters(power, value))
        }

    val isBusy: Boolean
        get() {
            return HardwareGetter.isMotorBusy(name)
        }
}
