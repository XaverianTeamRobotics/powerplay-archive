package org.firstinspires.ftc.teamcode.utils.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter

class LaserDistanceSensor(var name: String) {

    private val request: ScriptParameters.Request = HardwareGetter.makeLaserDistanceSensorRequest(name)

    val sensor: DistanceSensor
        get() {
            return HardwareGetter.getLaserDistanceSensorFromRequest(name)
        }

    var unit: DistanceUnit = DistanceUnit.MM
        get() {
            return field
        } set(value) {
            field = value
        }

    val distance: Double
        get() {
            return HardwareGetter.issueLaserDistanceSensorRequest(name, unit)
        }

}