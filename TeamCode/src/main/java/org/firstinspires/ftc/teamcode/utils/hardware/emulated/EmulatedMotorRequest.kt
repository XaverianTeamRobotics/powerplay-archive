package org.firstinspires.ftc.teamcode.utils.hardware.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.utils.hardware.physical.inputs.StandardMotorParameters

class EmulatedMotorRequest(name: String?) : ScriptParameters.Request(name) {
    override fun issueRequest(o: Any?): Any {
        var input = o as StandardMotorParameters
        println("Emulated motor set to ${input.value} with mode ${input.operation}")
        return 0
    }

    override fun getOutputType(): Class<*>? {
        return Int::class.java
    }

    override fun getInputType(): Class<*> {
        return StandardMotorParameters::class.java
    }
}