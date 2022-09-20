package org.firstinspires.ftc.teamcode.utils.hardware.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.Colors

class EmulatedColorSensorRequest(name: String?) : ScriptParameters.Request(name) {
    override fun issueRequest(p0: Any?): Any {
        return Colors(intArrayOf(0, 0, 0, 0), doubleArrayOf(0.0, 0.0, 0.0), 0, 0.0, 0)
    }

    override fun getOutputType(): Class<*> {
        return Colors::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}