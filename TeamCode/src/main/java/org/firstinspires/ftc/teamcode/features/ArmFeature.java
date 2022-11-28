package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class ArmFeature extends Feature implements Buildable {

    @Override
    public void build() {
        Devices.initializeArmMotors();
    }

    @Override
    public void loop() {
        boolean power0a0 = Devices.controller1.getRightTrigger() > 0;
        boolean power1a0 = Devices.controller1.getLeftTrigger() > 0;
        double power0 = (power0a0 ? 100 : 0) - (Devices.controller1.getRightBumper() ? 100 : 0);
        double power1 = (power1a0 ? 100 : 0) - (Devices.controller1.getLeftBumper() ? 100 : 0);
        Devices.expansion_motor0.setSpeed(-power0);
        Devices.expansion_motor1.setSpeed(power1);
    }

}
