package org.firstinspires.ftc.teamcode.utils.hardware.requests.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.utils.hardware.data.GyroData

class EmulatedGyroRequest(name: String) : ScriptParameters.Request(name) {

    override fun issueRequest(p0: Any?): Any {
        return GyroData(0, intArrayOf(0, 0, 0), "")
    }

    override fun getOutputType(): Class<*> {
        return GyroData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }

}