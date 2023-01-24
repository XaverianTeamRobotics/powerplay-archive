package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.controller1

class FourMotorArmFeature: Feature(), Buildable {
    override fun loop() {
        // Motor config: 4 - TL, 5 - BL, 6 - TR, 7 - BR
        val power0 = controller1.rightTrigger - (controller1.leftTrigger * 50)
        Devices.motor4.speed = power0
        Devices.motor5.speed = -power0
        Devices.motor6.speed = power0
        Devices.motor7.speed = -power0
    }

    override fun build() {
        // LEAVE EMPTY
    }
}