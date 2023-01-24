package org.firstinspires.ftc.teamcode.features

import com.acmerobotics.roadrunner.util.NanoClock
import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices

class HandFeature : Feature(), Buildable {
    override fun build() {
        Devices.servo0.position = 100.0
        Devices.servo1.position = 0.0
    }

    override fun loop() {
        /*
        Pressing x on either controller releases the hand
         */
        if (open && Devices.distanceSensor.distance < 33 && NanoClock.system().seconds() > second) {
            Devices.servo0.position = 0.0
            Devices.servo1.position = 100.0
            open = false
        } else if (!open && Devices.controller1.a) {
            Devices.servo0.position = 100.0
            Devices.servo1.position = 0.0
            open = true
            second = NanoClock.system().seconds() + 2
        }
    }

    companion object {
        private var open = true
        private var second = 0.0
        @JvmStatic
        fun manualOpen() {
            Devices.servo0.position = 100.0
            Devices.servo1.position = 0.0
            open = true
            second = NanoClock.system().seconds() + 2
        }
    }
}