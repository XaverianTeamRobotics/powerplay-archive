package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class ArmFeature extends Feature implements Buildable {

    @Override
    public void build() {
        Devices.initializeArmMotors();
    }

    public boolean useExpansionHub = false;

    @Override
    public void loop() {
        boolean power0a0 = Devices.controller1.getRightTrigger() > 0;
        boolean power1a0 = Devices.controller1.getLeftTrigger() > 0;
        double power0 = (power0a0 ? 100 : 0) - (Devices.controller1.getRightBumper() ? 100 : 0);
        double power1 = (power1a0 ? 100 : 0) - (Devices.controller1.getLeftBumper() ? 100 : 0);
        if (useExpansionHub) {
            Devices.expansion_motor0.setPower(-power0);
            Devices.expansion_motor1.setPower(power1);
        } else {
            Devices.motor0.setSpeed(-power0);
            Devices.motor1.setSpeed(power1);
        }
        int offset = Devices.gyroscope.getHeading() - 90;
        if(offset > 0) {

        }
    }

}
