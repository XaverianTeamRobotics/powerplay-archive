package org.firstinspires.ftc.teamcode.internals.hardware.accessors

import org.firstinspires.ftc.teamcode.internals.Vector3
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter

class IMUGlobalAccess(var name: String) {
    private val request = HardwareGetter.makeIMURequest(name)

    val orientation: Vector3
        get() = HardwareGetter.getIMUData(name).orientation

    val acceleration: Vector3
        get() = HardwareGetter.getIMUData(name).acceleration
}