package org.firstinspires.ftc.teamcode.hardware.physical

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

open class MotorRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private var motor: DcMotor
    init {
       motor = hardwareMap.get(DcMotor::class.java, name)
    }
    override fun issueRequest(o: Any): Any {
        var input = o as StandardMotorParameters
        when (input.operation) {
            MotorOperation.POWER -> {
                motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
                motor.power = input.value
            }
            MotorOperation.ENCODER_DISTANCE -> {
                motor.mode = DcMotor.RunMode.RUN_TO_POSITION
                motor.targetPosition = input.value.toInt()
            }
            MotorOperation.ENCODER_POWER -> {
                motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
                motor.power = input.value
            }
        }
        return 0
    }

    override fun getOutputType(): Class<*> {
        return Int::class.java
    }

    override fun getInputType(): Class<*> {
        return StandardMotorParameters::class.java
    }

}