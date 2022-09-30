package org.firstinspires.ftc.teamcode.utils.hardware.requests.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.utils.hardware.data.IMUData

class EmulatedIMURequest(name: String) : ScriptParameters.Request(name) {
    override fun issueRequest(p0: Any?): Any {
        return IMUData(
            org.firstinspires.ftc.teamcode.utils.Vector3(0.0, 0.0, 0.0),
            org.firstinspires.ftc.teamcode.utils.Vector3(0.0, 0.0, 0.0)
        )
    }

    override fun getOutputType(): Class<*> {
        return Any::class.java
    }

    override fun getInputType(): Class<*> {
        return IMUData::class.java
    }
}