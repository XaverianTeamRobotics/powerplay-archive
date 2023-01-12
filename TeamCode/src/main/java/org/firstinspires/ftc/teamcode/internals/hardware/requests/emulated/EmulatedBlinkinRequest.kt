package org.firstinspires.ftc.teamcode.internals.hardware.requests.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.internals.hardware.data.BlinkinInput
import org.firstinspires.ftc.teamcode.internals.hardware.data.BlinkinOptions

class EmulatedBlinkinRequest(name: String) : ScriptParameters.Request(name) {
    private var pos = 0.0

    override fun issueRequest(p0: Any?): Any {
        val (id, type) = p0 as BlinkinInput
        return if (type === BlinkinOptions.GET) {
            pos
        } else {
            pos = id
            println("Set ID to $id.")
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