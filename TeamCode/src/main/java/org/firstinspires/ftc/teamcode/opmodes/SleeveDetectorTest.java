package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.features.SleeveDetector;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging;
@Disabled
public class SleeveDetectorTest extends OperationMode implements AutonomousOperation {

    SleeveDetector detector;


    @Override
    public Class<? extends OperationMode> getNext() {
        return null;
    }

    @Override
    public void construct() {
        detector = new SleeveDetector();
        registerFeature(detector);
    }

    @Override
    public void run() {
        DSLogging.log(String.valueOf(detector.getSpot()));
        DSLogging.update();
    }
}
