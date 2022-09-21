package org.firstinspires.ftc.teamcode.utils.hardware.physical.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.ServoInput
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.ServoOptions

class ServoRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private val SERVO: Servo

    init {
        SERVO = hardwareMap.get(Servo::class.java, name)
        SERVO.resetDeviceConfigurationForOpMode()
        SERVO.direction = Servo.Direction.FORWARD
    }

    override fun issueRequest(o: Any): Any {
        val vals = o as ServoInput
        return if (vals.type === ServoOptions.GET) {
            SERVO.position * 100
        } else {
            var `val` = vals.position
            `val` = Range.clip(`val`, 0.0, 100.0)
            SERVO.position = `val` / 100
            0
        }
    }

    override fun getOutputType(): Class<*>? {
        return Double::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return ServoInput::class.java
    }
}