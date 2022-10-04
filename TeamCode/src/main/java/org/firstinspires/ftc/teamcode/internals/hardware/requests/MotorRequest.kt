package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.internals.hardware.InitializedDCDevices
import org.firstinspires.ftc.teamcode.internals.hardware.data.MotorOperation
import org.firstinspires.ftc.teamcode.internals.hardware.data.StandardMotorParameters

open class MotorRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    val motor: DcMotor
    init {
        motor = hardwareMap.get(DcMotor::class.java, name)
        if(!InitializedDCDevices.has(name)) {
            motor.resetDeviceConfigurationForOpMode()
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            InitializedDCDevices.add(name)
        }
        motor.power = 0.0
    }
    override fun issueRequest(o: Any): Any {
        val input = o as StandardMotorParameters
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