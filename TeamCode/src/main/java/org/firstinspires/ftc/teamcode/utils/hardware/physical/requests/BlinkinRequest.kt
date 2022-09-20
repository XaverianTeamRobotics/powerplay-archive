package org.firstinspires.ftc.teamcode.utils.hardware.physical.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.BlinkinInput
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.BlinkinOptions

class BlinkinRequest(name: String?, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private val SERVO: Servo

    init {
        SERVO = hardwareMap.get(Servo::class.java, name)
        SERVO.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        val (id, type) = o as BlinkinInput
        return if (type === BlinkinOptions.GET) {
            SERVO.position
        } else {
            SERVO.position = Range.clip(id.toDouble(), 0.2525, 0.7475)
            0
        }
    }

    override fun getOutputType(): Class<*>? {
        return Double::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return BlinkinInput::class.java
    }
}