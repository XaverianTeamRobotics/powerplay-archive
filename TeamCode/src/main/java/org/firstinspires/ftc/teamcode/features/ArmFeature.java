package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

public class ArmFeature extends Feature {

    @Override
    public void loop() {

        /*
        Right trigger on both controllers raises the elevator
        Left trigger on both controllers lowers the elevator

        Right bumper on controller 1 raises one side
        Left bumper on controller 1 lowers one side
        (to correct misalignment)
         */

        double rightTrigger1 = Devices.controller1.getRightTrigger();
        double rightTrigger2 = Devices.controller2.getRightTrigger();
        double leftTrigger1 = Devices.controller1.getLeftTrigger();
        double leftTrigger2 = Devices.controller2.getLeftTrigger();

        boolean rightBumper = Devices.controller1.getRightBumper();
        boolean leftBumper = Devices.controller1.getLeftBumper();

        double powerAll = 0.0;
        double power0 = 0.0;
        double power1 = 0.0;
        final double FRICTION = 0.0; // Issue with worm gears causes friction to be ignored

        double rightTriggerSum = rightTrigger1 + (rightTrigger2 / 2.0);
        double leftTriggerSum = leftTrigger1 + (leftTrigger2 / 2.0);

        // Sets powerAll based on trigger input
        if (rightTriggerSum > 0) {
            powerAll = 100;
        }
        else if (leftTriggerSum > 0) {
            powerAll = -100;
        }
        else {
            powerAll = 0;
        }

        // Sets the two individual powers based on powerAll
        if (powerAll > 0) {
            power0 = powerAll - FRICTION;
            power1 = powerAll;
        }
        else if (powerAll < 0) {
            power0 = powerAll + FRICTION;
            power1 = powerAll;
        }
        else {
            power0 = 0;
            power1 = 0;
        }

        // Changes power0 if bumpers are pressed
        if (rightBumper) {
            power0 = 100;
            power1 = 0;
        }
        else if (leftBumper) {
            power0 = -100;
            power1 = 0;
        }

        Devices.motor4.setSpeed(-power0); // Negative is up
        Devices.motor5.setSpeed(power1); // Positive is up
    }

}
