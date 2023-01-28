package org.firstinspires.ftc.teamcode.internals.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.internals.hardware.InitializedDCDevices
import org.firstinspires.ftc.teamcode.internals.hardware.data.EncoderInput

class EncoderRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request("encoder$name") {
    val encoder: DcMotor
    private var offset: Int

    init {
        encoder = hardwareMap.get(DcMotor::class.java, name)
        if(!InitializedDCDevices.has(name)) {
            encoder.resetDeviceConfigurationForOpMode()
            encoder.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            encoder.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            encoder.power = 0.0
            offset = encoder.currentPosition
            InitializedDCDevices.add(name)
        }else{
            offset = encoder.currentPosition
        }
    }

    override fun issueRequest(o: Any): Any {
        val input = o as EncoderInput
        return if (input === EncoderInput.GET) {
            encoder.currentPosition - offset
        } else {
            offset = encoder.currentPosition
            0
        }
    }

    override fun getOutputType(): Class<*> {
        return Int::class.java
    }

    override fun getInputType(): Class<*> {
        return EncoderInput::class.java
    }
}