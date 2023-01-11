package org.firstinspires.ftc.teamcode.internals.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;

public class SafeLogging {

    public static final MultipleTelemetry TELEMETRY = new MultipleTelemetry(HardwareGetter.getOpMode().telemetry, FtcDashboard.getInstance().getTelemetry());

    public static void update() {
        TELEMETRY.update();
    }

    public static void clear() {
        TELEMETRY.clear();
    }

    public static void logData(String key, Object value) {
        TELEMETRY.addData(key, value);
    }

    public static void logText(String msg) {
        TELEMETRY.addLine(msg);
    }

    public static void log(String msg) {
        logText(msg);
    }

    public static void log(String key, Object value) {
        logData(key, value);
    }

}
