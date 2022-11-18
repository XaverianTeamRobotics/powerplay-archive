package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class ArmFeature extends Feature {

    public boolean useExpansionHub = false;

    @Override
    public void loop() {
        double power = (Devices.controller1.getRightTrigger() - Devices.controller1.getLeftTrigger()) * 100;
        if (useExpansionHub) {
            Devices.expansion_motor0.setPower(power);
            Devices.expansion_motor1.setPower(power);
        } else {
            Devices.motor0.setSpeed(power);
            Devices.motor1.setSpeed(power);
        }
        int offset = Devices.gyroscope.getHeading() - 90;
        if(offset > 0) {

        }
    }
}
