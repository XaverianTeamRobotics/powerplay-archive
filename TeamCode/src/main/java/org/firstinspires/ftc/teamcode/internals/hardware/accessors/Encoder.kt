package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.EncoderInput

/**
 * An encoder tracks the number of rotations of a hex rod and can be used to measure distance.
 */
class Encoder(override var name: String): DeviceAccessor(name) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeEncoderRequest(name)

    /**
     * The underlying encoder managed as a DC motor being managed by the jlooping request.
     */
    val encoder: DcMotor
        get() {
            return HardwareGetter.getEncoderFromRequest(name)
        }

    /**
     * The current number of rotations, or position, of the encoder starting from 0. Forward rotations increase the number, while reverse ones decrease it. This value can be negative.
     */
    val position: Int
        get() {
            return HardwareGetter.issueEncoderRequest(name, EncoderInput.GET)
        }

    /**
     * Resets the encoder to 0.
     */
    fun reset() {
        HardwareGetter.issueEncoderRequest(name, EncoderInput.SET)
    }

}