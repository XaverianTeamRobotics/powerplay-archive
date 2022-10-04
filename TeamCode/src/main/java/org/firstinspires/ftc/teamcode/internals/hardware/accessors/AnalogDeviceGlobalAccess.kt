package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import com.michaell.looping.ScriptParameters
import com.qualcomm.robotcore.hardware.AnalogInput
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter

class AnalogDeviceGlobalAccess(var name: String) {

    private val request: ScriptParameters.Request = HardwareGetter.makeAnalogDeviceRequest(name)

    val device: AnalogInput
        get() = HardwareGetter.getAnalogDeviceFromRequest(name)

    val voltage: Double
        get() = HardwareGetter.getAnalogDeviceData(name).voltage

    val maxVoltage: Double
        get() = HardwareGetter.getAnalogDeviceData(name).maxVoltage

}