package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class ArmFeature extends Feature {

    @Override
    public void loop() {

        /*
        boolean power0a0 = Devices.controller1.getRightTrigger() > 0;
        boolean power1a0 = Devices.controller1.getLeftTrigger() > 0;

        double power0 = (power0a0 ? 100 : 0) - (Devices.controller1.getRightBumper() ? 100 : 0);
        double power1 = (power1a0 ? 100 : 0) - (Devices.controller1.getLeftBumper() ? 100 : 0);

        Devices.motor4.setSpeed(-power0);
        Devices.motor5.setSpeed(power1);
         */

        double rightTrigger1 = Devices.controller1.getRightTrigger();
        double rightTrigger2 = Devices.controller2.getRightTrigger();
        double leftTrigger1 = Devices.controller1.getLeftTrigger();
        double leftTrigger2 = Devices.controller2.getLeftTrigger();

        double powerAll = 0.0;
        double power0 = 0.0;
        double power1 = 0.0;
        final double FRICTION = 0; // 5.0 was close

        double rightTriggerSum = rightTrigger1 + 0.1 * rightTrigger2; // 0.1 gives controller 2 fine control
        double leftTriggerSum = leftTrigger1 + 0.1 * leftTrigger2;

        if (rightTriggerSum > 0) {
            powerAll = 100;
        }
        else if (leftTriggerSum > 0) {
            powerAll = -100;
        }
        else {
            powerAll = 0;
        }

        if (powerAll > 0) {
            power0 = powerAll;
            power1 = powerAll - FRICTION;
        }
        else if (powerAll < 0) {
            power0 = powerAll;
            power1 = powerAll + FRICTION;
        }
        else {
            power0 = 0;
            power1 = 0;
        }

        Devices.motor4.setSpeed(-power0); // Negative is up
        Devices.motor5.setSpeed(power1); // Positive is up
    }

}
