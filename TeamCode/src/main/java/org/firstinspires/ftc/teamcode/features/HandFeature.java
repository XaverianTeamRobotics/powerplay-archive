package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.Clock;

public class HandFeature extends Feature implements Buildable {

    private boolean open = true;

    @Override
    public void build() {
        Devices.servo0.setPosition(50);
        Devices.servo1.setPosition(50);
        Clock.make("hand");
    }

    @Override
    public void loop() {
        if(open && Devices.distanceSensor.getDistance() < 33 && Clock.get("hand").elapsed(2)) {
            Devices.servo0.setPosition(88);
            Devices.servo1.setPosition(12);
            open = false;
        }else if(!open && Devices.controller1.getA()) {
            Devices.servo0.setPosition(50);
            Devices.servo1.setPosition(50);
            open = true;
            Clock.get("hand").reset();
        }
    }

}
