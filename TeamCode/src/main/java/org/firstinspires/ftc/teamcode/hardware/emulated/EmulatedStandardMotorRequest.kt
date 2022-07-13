package org.firstinspires.ftc.teamcode.hardware.emulated

import com.michaell.looping.ScriptParameters
import com.michaell.looping.ScriptRunner
import org.firstinspires.ftc.teamcode.hardware.physical.StandardMotorRequest

class EmulatedStandardMotorRequest(name: String?) : ScriptParameters.Request(name) {
    override fun issueRequest(o: Any?): Any {
        val power = (o as StandardMotorRequest.StandardMotorParameters).power
        println("Emulated motor $name set to $power")
        return 0
    }

    override fun getOutputType(): Class<*>? {
        return Int::class.java
    }

    override fun getInputType(): Class<*> {
        return StandardMotorRequest.StandardMotorParameters::class.java
    }
}