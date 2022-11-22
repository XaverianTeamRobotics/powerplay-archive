package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.controller1;
import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.expansion_motor2;

public class HandFeature extends Feature implements Buildable {

    private int holdMode = 1;
    // 1 for force hold
    // 2 for auto braking mode

    @Override
    public void build() {
        Devices.initializeHandMotors();
    }// code is good - sposored by raid shadow legends

    @Override
    public void loop() {
        if (controller1.getB())         holdMode = 1;
        else if (controller1.getA())    holdMode = 2;

        if (controller1.getA())         expansion_motor2.setSpeed( 0.50);
        else if (controller1.getY())    expansion_motor2.setSpeed(-0.25);
        else if (holdMode == 1)         expansion_motor2.setSpeed(-0.25);
        else                            expansion_motor2.setSpeed( 0.00);
    }
}
