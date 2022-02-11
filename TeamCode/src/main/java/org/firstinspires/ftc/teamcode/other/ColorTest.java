package org.firstinspires.ftc.teamcode.other;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.main.utils.interactions.items.StandardColorSensor;
import org.firstinspires.ftc.teamcode.main.utils.resources.Resources;

import java.util.Arrays;

@Autonomous(name = "ColorTest")
public class ColorTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        StandardColorSensor sensor = new StandardColorSensor(hardwareMap, Resources.Hand.Sensors.HandColor);
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("RGBA: ", Arrays.toString(sensor.getRGBA()));
            double[] hsv = sensor.getHSV();
            int[] hsvi = new int[] { (int) hsv[0], (int) (hsv[1] * 100), (int) (hsv[2] * 100) };
            telemetry.addData("HSV: ", Arrays.toString(hsvi));
            telemetry.addData("ARGB: ", sensor.getARGB());
            telemetry.addData("GSV: ", sensor.getGSV());
            // block: hue 130-
            // ball: hue 130+
            telemetry.update();
        }
    }

}
