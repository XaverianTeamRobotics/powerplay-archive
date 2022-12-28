package org.firstinspires.ftc.teamcode.internals.telemetry

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.vuforia.Vuforia.isInitialized
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.isEmulated

class Logging {
    companion object {

        @JvmStatic
        lateinit var driverTelemetry: Telemetry
        var dashboardPacket = TelemetryPacket()
        private lateinit var _telemetry: Telemetry;

        @JvmStatic
        val telemetry: Telemetry
            get() {
                if(!::driverTelemetry.isInitialized) {
                    throw UninitializedPropertyAccessException("driverTelemetry must be initialized to access telemetry!")
                }
                if(!::_telemetry.isInitialized) {
                    _telemetry = MultipleTelemetry(driverTelemetry, FtcDashboard.getInstance().telemetry)
                }
                return telemetry
            }

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
                driverTelemetry.addLine(text)
                dashboardPacket.addLine(text)
            }
        }

        @JvmStatic
        fun logData(key: String, value: Any) {
            if (isEmulated) {
                println("$key: $value")
            } else {
                driverTelemetry.addData(key, value)
                dashboardPacket.addLine(" ")
                dashboardPacket.put(key, value)
                dashboardPacket.put("$key (key)", "$key:")
            }
        }

        @JvmStatic
        fun updateLog() {
            if (!isEmulated) {
                driverTelemetry.update()
                FtcDashboard.getInstance().sendTelemetryPacket(dashboardPacket)
                dashboardPacket = TelemetryPacket()
            }
        }

        @JvmStatic
        fun clear() {
            if (!isEmulated) {
                driverTelemetry.clear()
                FtcDashboard.getInstance().clearTelemetry()
                dashboardPacket = TelemetryPacket()
            }
        }
    }
}