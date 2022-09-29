package org.firstinspires.ftc.teamcode.features

import com.michaell.looping.ScriptParameters
import com.michaell.looping.ScriptRunner.DuplicateScriptException
import org.firstinspires.ftc.teamcode.utils.hardware.Devices
import org.firstinspires.ftc.teamcode.utils.registration.OperationMode
import kotlin.math.abs
import kotlin.math.max

class MecanumDrivetrainFeature : BlankFeature("MecanumOpMode", false) {
    override fun run(scriptParameters: ScriptParameters) {
        val y = -Devices.controller1.leftStickY // Remember, this is reversed!
        val x = Devices.controller1.rightStickX * 1.1 // Counteract imperfect strafing
        val rx = Devices.controller1.rightStickX

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        val denominator = max(abs(y) + abs(x) + abs(rx), 1.0)
        val frontLeftPower = (y + x + rx) / denominator
        val backLeftPower = (y - x + rx) / denominator
        val frontRightPower = (y - x - rx) / denominator
        val backRightPower = (y + x - rx) / denominator
        Devices.motor0.power = backLeftPower
        Devices.motor1.power = frontLeftPower
        Devices.motor2.power = -backRightPower
        Devices.motor3.power = -frontRightPower
    }
}