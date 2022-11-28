package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class HandFeature extends Feature implements Buildable {

    @Override
    public void build() {
        Devices.initializeHandMotors();
    }// code is good - sposored by raid shadow legends

    @Override
    public void loop() {
        if(Devices.controller1.getA()) {
            Devices.expansion_motor2.setPower(50);
        }else if(Devices.controller1.getB()) {
            // hi! no!! this is bad! it causes fires! the robot caught on fire. this line made the robot catch on fire. i inhaled a lot of heavy metals. fun!
            // Devices.expansion_motor2.setPower(-25);
        }else{
            Devices.expansion_motor2.setPower(0);
        }
    }
}
