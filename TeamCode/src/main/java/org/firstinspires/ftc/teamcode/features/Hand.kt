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
        if ((open && Devices.distanceSensor.distance < 40 && NanoClock.system().seconds() > second) || (open && Devices.controller1.y)) {
            manualClose()
        } else if (!open && Devices.controller1.a) {
            manualOpen()
        }
    }

    companion object {
        private var open = true
        private var second = 0.0
        private const val homePosLeft = 67.5
        private const val homePosRight = 37.5
        @JvmStatic
        fun manualOpen() {
            Devices.servo1.position = homePosLeft
            Devices.servo0.position = homePosRight
            open = true
            second = NanoClock.system().seconds() + 2
        }
        @JvmStatic
        fun manualClose() {
            Devices.servo1.position = homePosLeft + 7.5
            Devices.servo0.position = homePosRight - 7.5
            open = false
        }
    }
}