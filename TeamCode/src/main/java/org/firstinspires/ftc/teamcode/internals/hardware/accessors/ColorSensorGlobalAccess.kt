package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.hardware.data.Colors

class ColorSensorGlobalAccess(var name: String) {

    private val request: ScriptParameters.Request = HardwareGetter.makeColorSensorRequest(name)

    val sensor: NormalizedColorSensor
        get() {
            return HardwareGetter.getColorSensorFromRequest(name).sanitized
        }

    val rawSensor: ColorSensor
        get() {
            return HardwareGetter.getColorSensorFromRequest(name).raw
        }

    private val cdata: Colors
        get() {
            return HardwareGetter.getColorSensorData(name)
        }

    val rgb: IntArray
        get() {
            return cdata.rgba.copyOfRange(0, 3)
        }

    val rgba: IntArray
        get() {
            return cdata.rgba
        }

    val hsv: DoubleArray
        get() {
            return cdata.hsv
        }

    val gsv: Int
        get() {
            return cdata.gsv
        }

    val gain: Double
        get() {
            return cdata.gain
        }

    val raw: Int
        get() {
            return cdata.raw
        }

}