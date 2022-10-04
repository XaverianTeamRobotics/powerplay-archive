package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.BlinkinInput
import org.firstinspires.ftc.teamcode.internals.hardware.data.BlinkinOptions

/**
 * A Blinkin manages a Blinkin-compatible LED strip.
 */
class Blinkin(var name: String) {

    /**
     * The jlooping request managing the underlying hardware.
     */
    val request: ScriptParameters.Request = HardwareGetter.makeBlinkinRequest(name)

    /**
     * The underlying Blinkin servo being managed by the jlooping request.
     */
    val driver: Servo
        get() = HardwareGetter.getBlinkinFromRequest(name)

    /**
     * The ID of the color to display in the strip. Valid IDs are numbers to the between 0.2525 and 0.7475.
     */
    var id: Double = 0.0
        get() {
            return HardwareGetter.issueBlinkinRequest(name, BlinkinInput(field, BlinkinOptions.GET))
        } set(value) {
            HardwareGetter.issueBlinkinRequest(name, BlinkinInput(value, BlinkinOptions.SET))
            field = value
        }

}