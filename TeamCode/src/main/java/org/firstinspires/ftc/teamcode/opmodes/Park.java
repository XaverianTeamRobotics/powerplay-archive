package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.SleeveDetectionFeature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging;

import java.util.ArrayList;

public class Park extends OperationMode implements AutonomousOperation {

    private ArrayList<Integer> spots = new ArrayList<>();
    private SleeveDetectionFeature sleeveDetectionFeature = null;

    @Override
    public void construct() {
        Devices.initializeCamera0();
        Devices.initializeControlHubMotors();
        sleeveDetectionFeature = new SleeveDetectionFeature();
        registerFeature(sleeveDetectionFeature);
        spots.set(0, 0);
        spots.set(1, 0);
        spots.set(2, 0);
    }

    @Override
    public void run() {
        int spot = sleeveDetectionFeature.spot;
        if(spot != 0) {
            spots.set(spot, spots.get(spot) + 1);
        }
        if(getRuntime() >= 3000) {
            Integer num = Math.max(spots.get(0), Math.max(spots.get(1), spots.get(2)));
            Logging.logText("The robot has decided it wants to go to spot " + spots.indexOf(num) + ", with the rankings " + spots.get(0) + " " + spots.get(1) + " " + spots.get(2) + ".");
        }
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return null;
    }

}
