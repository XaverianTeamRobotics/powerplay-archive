package org.firstinspires.ftc.teamcode.utils.hardware.emulated

import android.media.MediaCodec.MetricsConstants.ENCODER
import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.utils.hardware.physical.data.EncoderInput

class EmulatedEncoderRequest(name: String) : ScriptParameters.Request(name) {

    override fun issueRequest(p0: Any?): Any {
        val input = p0 as EncoderInput
        if(input == EncoderInput.GET) {
            println("Encoder position reset.")
        }
        return 0
    }

    override fun getOutputType(): Class<Int>? {
        return Int::class.javaPrimitiveType
    }

    override fun getInputType(): Class<*> {
        return EncoderInput::class.java
    }

}