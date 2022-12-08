package org.firstinspires.ftc.teamcode.features;

import com.acmerobotics.roadrunner.util.NanoClock;
import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class HandFeature extends Feature implements Buildable {

    private boolean open = true;
    private double second = 0;

    @Override
    public void build() {
        Devices.servo0.setPosition(50);
        Devices.servo1.setPosition(50);
    }

    @Override
    public void loop() {
        if(open && Devices.distanceSensor.getDistance() < 33 && NanoClock.system().seconds() > second) {
            Devices.servo0.setPosition(88);
            Devices.servo1.setPosition(12);
            open = false;
        }else if(!open && Devices.controller1.getA()) {
            Devices.servo0.setPosition(50);
            Devices.servo1.setPosition(50);
            open = true;
            second = NanoClock.system().seconds() + 2;
        }
    }

}
