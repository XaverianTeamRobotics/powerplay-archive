package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class HandFeature extends Feature implements Buildable {

    @Override
    public void build() {
        Devices.initializeHandServos();
    }// code is good - sposored by raid shadow legends

    @Override
    public void loop() {

    }
}
