package org.firstinspires.ftc.teamcode.internals.telemetry

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.isEmulated

class Logging {
    companion object {
        @JvmStatic
        lateinit var telemetry: Telemetry
        var dashboardPacket = TelemetryPacket()

        @JvmStatic
        fun log(text: String) {
            logText(text)
        }

        @JvmStatic
        fun log(key: String, value: Any) {
            logData(key, value)
        }

        @JvmStatic
        fun logText(text: String) {
            if (isEmulated) {
                println(text)
            } else {
                telemetry.addLine(text)
                dashboardPacket.addLine(text)
            }
        }

        @JvmStatic
        fun logData(key: String, value: Any) {
            if (isEmulated) {
                println("$key: $value")
            } else {
                telemetry.addData(key, value)
                dashboardPacket.addLine(" ")
                dashboardPacket.put(key, value)
                dashboardPacket.put("$key (key)", "$key:")
            }
        }

        @JvmStatic
        fun updateLog() {
            if (!isEmulated) {
                telemetry.update()
                FtcDashboard.getInstance().sendTelemetryPacket(dashboardPacket)
                dashboardPacket = TelemetryPacket()
            }
        }

        @JvmStatic
        fun clear() {
            if (!isEmulated) {
                telemetry.clear()
                FtcDashboard.getInstance().clearTelemetry()
                dashboardPacket = TelemetryPacket()
            }
        }
    }
}