package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.SleeveDetectionFeature;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

public class SleeveDetectionTest extends OperationMode implements AutonomousOperation {

    SleeveDetectionFeature detector;

    @Override
    public Class<? extends OperationMode> getNext() {
        return null;
    }

    @Override
    public void construct() {
        detector = new SleeveDetectionFeature();
        detector.setDebugEnabled(true);
        registerFeature(detector);
    }

    @Override
    public void run() {
    }
}
