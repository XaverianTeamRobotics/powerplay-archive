package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class ArmFeature extends Feature {

    @Override
    public void loop() {
        double power = (Devices.controller1.getRightTrigger() - Devices.controller1.getLeftTrigger()) * 100;
        Devices.motor0.setSpeed(power);
    }
    
}
