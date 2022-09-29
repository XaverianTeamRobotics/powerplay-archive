package org.firstinspires.ftc.teamcode.utils.hardware

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter.Companion.isEmulated

class Logging {
    companion object {
        @JvmStatic
        lateinit var telemetry: Telemetry

        @JvmStatic
        fun logText(text: String) {
            if (isEmulated) {
                println(text)
            } else {
                telemetry.addLine(text)
            }
        }

        @JvmStatic
        fun logData(key: String, value: Any) {
            if (isEmulated) {
                println("$key: $value")
            } else {
                telemetry.addData(key, value)
            }
        }

        @JvmStatic
        fun updateLog() {
            if (!isEmulated) {
                telemetry.update()
            }
        }

        @JvmStatic
        fun clear() {
            if (!isEmulated) {
                telemetry.clear()
            }
        }
    }
}