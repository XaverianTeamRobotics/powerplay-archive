package org.firstinspires.ftc.teamcode.utils.hardware.emulated

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.ServoInput
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.ServoOptions

class EmulatedServoRequest(name: String) : ScriptParameters.Request(name) {

    private var pos = 0.0

    override fun issueRequest(p0: Any?): Any {
        val vals = p0 as ServoInput
        return if (vals.type === ServoOptions.GET) {
            pos
        } else {
            pos = vals.position
            println("Position set to $pos.")
            0
        }
    }

    override fun getOutputType(): Class<*> {
        return Double::class.java
    }

    override fun getInputType(): Class<*> {
        return ServoInput::class.java
    }

}