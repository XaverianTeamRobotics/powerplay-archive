package org.firstinspires.ftc.teamcode.utils.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.utils.hardware.data.ContinousServoInput
import org.firstinspires.ftc.teamcode.utils.hardware.data.ContinousServoOptions

class ContinousServoRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private val SERVO: CRServo

    init {
        SERVO = hardwareMap.get(CRServo::class.java, name)
        SERVO.resetDeviceConfigurationForOpMode()
        SERVO.direction = DcMotorSimple.Direction.FORWARD
    }

    override fun issueRequest(o: Any): Any {
        val input = o as ContinousServoInput
        return if (input.type === ContinousServoOptions.GET) {
            SERVO.power * 100
        } else {
            var `val` = input.power
            `val` = Range.clip(`val`, -100.0, 100.0)
            SERVO.power = `val` / 100
            0
        }
    }

    override fun getOutputType(): Class<*>? {
        return Double::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return ContinousServoInput::class.java
    }
}