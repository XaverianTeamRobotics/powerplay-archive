package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.internals.hardware.data.BlinkinInput
import org.firstinspires.ftc.teamcode.internals.hardware.data.BlinkinOptions

class BlinkinRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val blinkin: Servo

    init {
        blinkin = hardwareMap.get(Servo::class.java, name)
        blinkin.resetDeviceConfigurationForOpMode()
    }

    override fun issueRequest(o: Any): Any {
        val (id, type) = o as BlinkinInput
        return if (type === BlinkinOptions.GET) {
            blinkin.position
        } else {
            blinkin.position = Range.clip(id, 0.2525, 0.7475)
            0.0
        }
    }

    override fun getOutputType(): Class<*>? {
        return Double::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return BlinkinInput::class.java
    }
}