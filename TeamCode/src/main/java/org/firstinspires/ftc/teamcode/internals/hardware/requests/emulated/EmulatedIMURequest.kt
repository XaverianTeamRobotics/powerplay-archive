package org.firstinspires.ftc.teamcode.internals.hardware.requests.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.internals.hardware.data.IMUData

class EmulatedIMURequest(name: String) : ScriptParameters.Request(name) {
    override fun issueRequest(p0: Any?): Any {
        return IMUData(
            org.firstinspires.ftc.teamcode.internals.Vector3(0.0, 0.0, 0.0),
            org.firstinspires.ftc.teamcode.internals.Vector3(0.0, 0.0, 0.0)
        )
    }

    override fun getOutputType(): Class<*> {
        return IMUData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }
}