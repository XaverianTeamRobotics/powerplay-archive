package org.firstinspires.ftc.teamcode.utils.hardware.requests

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.utils.hardware.InitializedDCDevices
import org.firstinspires.ftc.teamcode.utils.hardware.data.EncoderInput

class EncoderRequest(name: String, hardwareMap: HardwareMap) : ScriptParameters.Request(name) {
    private val ENCODER: DcMotor
    private var offset: Int

    init {
        ENCODER = hardwareMap.get(DcMotor::class.java, name)
        if(!InitializedDCDevices.has(name)) {
            ENCODER.resetDeviceConfigurationForOpMode()
            ENCODER.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            ENCODER.power = 0.0
            offset = ENCODER.currentPosition
            InitializedDCDevices.add(name)
        }else{
            offset = ENCODER.currentPosition
        }
    }

    override fun issueRequest(o: Any): Any {
        val input = o as EncoderInput
        return if (input === EncoderInput.GET) {
            ENCODER.currentPosition - offset
        } else {
            offset = ENCODER.currentPosition
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