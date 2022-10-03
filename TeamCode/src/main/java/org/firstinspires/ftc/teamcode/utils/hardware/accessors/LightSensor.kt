package org.firstinspires.ftc.teamcode.utils.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.LightSensor
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.utils.hardware.data.RawBrightness

class LightSensor(var name: String) {

    private val request: ScriptParameters.Request = HardwareGetter.makeLightSensorRequest(name)

    val sensor: LightSensor
        get() {
            return HardwareGetter.getLightSensorFromRequest(name)
        }

    private var led = true

    fun enableLED() {
        led = true
        HardwareGetter.issueLightSensorRequest(name, true)
    }

    fun disableLED() {
        led = false
        HardwareGetter.issueLightSensorRequest(name, false)
    }

    val brightness: Double
        get() {
            return HardwareGetter.issueLightSensorRequest(name, led).brightness
        }

    val raw: RawBrightness
        get() {
            val req = HardwareGetter.issueLightSensorRequest(name, led)
            return RawBrightness(req.rawBrightness, req.maxRawBrightness)
        }

    val status: String
        get() {
            return HardwareGetter.issueLightSensorRequest(name, led).status
        }

}