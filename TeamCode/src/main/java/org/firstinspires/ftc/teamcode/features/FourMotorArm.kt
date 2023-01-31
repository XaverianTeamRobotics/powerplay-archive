package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.controller1
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging

class FourMotorArm: Feature() {

    companion object {
        var run = false
    }

    override fun loop() {

        // Toggle dc on and off with B, Y
        if(controller1.b) {
            run = true
        }
        if(controller1.y) {
            run = false
        }
        if(!run) {
            return
        }

        // Motor config: 4 - TL, 5 - BL, 6 - TR, 7 - BR
        val power = controller1.rightTrigger - (controller1.leftTrigger * 0.5)
        val powerL = if (controller1.dpadLeft) -25.0 else power
        val powerR = if (controller1.dpadRight) -25.0 else power

        Devices.motor4.speed = -powerL
        Devices.motor5.speed = powerL
        Devices.motor6.speed = -powerR
        Devices.motor7.speed = powerR

        // Log the encoder values
        DSLogging.log("encoder2", Devices.encoder5.position)
        DSLogging.log("encoder1", Devices.encoder6.position)
        DSLogging.update()
    }
}