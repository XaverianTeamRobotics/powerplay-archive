package org.firstinspires.ftc.teamcode.other;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.main.utils.interactions.items.StandardColorSensor;

import java.util.Arrays;

@Autonomous(name = "ColorTest")
public class ColorTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        StandardColorSensor sensor = new StandardColorSensor(hardwareMap, "color");
        sensor.powerLED();
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("RGBA: ", Arrays.toString(sensor.getRGBA()));
            telemetry.addData("HSV: ", Arrays.toString(sensor.getHSV()));
            telemetry.addData("ARGB: ", sensor.getARGB());
        }
    }

}
