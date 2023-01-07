package org.firstinspires.ftc.teamcode.internals.image;

import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging;
import org.opencv.core.KeyPoint;

public class ConeStackDetectorTest extends OperationMode implements AutonomousOperation {
    ConeStackTracker detector;
    @Override
    public void construct() {
        detector = new ConeStackTracker(true, true);
        detector.startStreaming();
    }

    @Override
    public void run() {
        // Print every detected cone
        for (KeyPoint keypoint : detector.getDetectedCones().toArray()) {
            Logging.logText(keypoint.pt.toString());
            Logging.logText(String.valueOf(detector.getAngleToTarget(keypoint)));
        }
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return null;
    }
}