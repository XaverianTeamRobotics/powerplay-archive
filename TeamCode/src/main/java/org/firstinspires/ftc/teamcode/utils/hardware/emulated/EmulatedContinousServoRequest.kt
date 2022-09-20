package org.firstinspires.ftc.teamcode.utils.hardware.emulated

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.ContinousServoInput
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.ContinousServoOptions

class EmulatedContinousServoRequest(name: String?) : ScriptParameters.Request(name) {
    private var power = 0.0

    override fun issueRequest(p0: Any?): Any {
        val input = p0 as ContinousServoInput
        return if (input.type === ContinousServoOptions.GET) {
            power
        } else {
            var `val` = input.power
            `val` = Range.clip(`val`, -100.0, 100.0)
            power = `val`
            println("Power set to $power.")
            0
        }
    }

    override fun getOutputType(): Class<*> {
        return Double::class.java
    }

    override fun getInputType(): Class<*> {
        return ContinousServoInput::class.java
    }
}