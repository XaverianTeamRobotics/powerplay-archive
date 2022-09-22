package org.firstinspires.ftc.teamcode.utils.hardware.requests.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.utils.hardware.data.TouchSensorData

class EmulatedTouchSensorRequest(name: String) : ScriptParameters.Request(name) {

    override fun issueRequest(p0: Any?): Any {
        return TouchSensorData(false, 0.0)
    }

    override fun getOutputType(): Class<*> {
        return TouchSensorData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }

}