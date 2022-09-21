package org.firstinspires.ftc.teamcode.utils.hardware.emulated

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.AccelerometerData

class EmulatedAccelerometerRequest(name: String) : ScriptParameters.Request(name) {

    override fun issueRequest(p0: Any?): Any {
        return AccelerometerData(Acceleration(), "")
    }

    override fun getOutputType(): Class<*> {
        return AccelerometerData::class.java
    }

    override fun getInputType(): Class<*> {
        return Any::class.java
    }


}