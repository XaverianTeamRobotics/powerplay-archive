package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.internals.remote_debugger.RDWebSocketServer

class WebServerFeature: Feature() {
    val webServer: RDWebSocketServer = RDWebSocketServer.initializeWebsocketServer()

    override fun loop() {
        webServer.updateData()

        if (HardwareGetter.opMode?.isStopRequested == true) {
            try {
                webServer.stop(5000)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }
    }
}