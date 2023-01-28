package org.firstinspires.ftc.teamcode.features

import com.acmerobotics.roadrunner.util.NanoClock
import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices

class Hand : Feature(), Buildable {
    override fun build() {
        Devices.servo1.position = homePosLeft
        Devices.servo0.position = homePosRight
    }

    override fun loop() {
        /*
        Pressing x on either controller releases the hand
         */
        if ((open && Devices.distanceSensor.distance < 33 && NanoClock.system().seconds() > second) || (open && Devices.controller1.x)) {
            manualClose()
        } else if (!open && Devices.controller1.a) {
            manualOpen()
        }
    }

    companion object {
        private var open = true
        private var second = 0.0
        private val homePosLeft = 65.0
        private val homePosRight = 42.5
        @JvmStatic
        fun manualOpen() {
            Devices.servo1.position = homePosLeft
            Devices.servo0.position = homePosRight
            open = true
            second = NanoClock.system().seconds() + 2
        }
        @JvmStatic
        fun manualClose() {
            Devices.servo1.position = homePosLeft + 20
            Devices.servo0.position = homePosRight - 20
            open = false
        }
    }
}