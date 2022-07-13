package org.firstinspires.ftc.teamcode.hardware.physical

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

open class StandardMotorRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private var motor: DcMotor
    init {
       motor = hardwareMap.get(DcMotor::class.java, name)
    }
    override fun issueRequest(o: Any): Any {
        motor.power = (o as StandardMotorParameters).power
        return 0
    }

    override fun getOutputType(): Class<*> {
        return Int::class.java
    }

    override fun getInputType(): Class<*> {
        return StandardMotorParameters::class.java
    }

    class StandardMotorParameters(var power: Double)
}