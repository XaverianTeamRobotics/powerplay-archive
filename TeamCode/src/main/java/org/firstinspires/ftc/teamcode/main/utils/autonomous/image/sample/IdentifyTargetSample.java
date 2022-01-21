package org.firstinspires.ftc.teamcode.main.utils.autonomous.image.sample;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.main.utils.autonomous.image.TFLITE_Wrapper;

@TeleOp(name = "Identify Image Sample")
public class IdentifyTargetSample extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        TFLITE_Wrapper tflite_wrapper = new TFLITE_Wrapper(hardwareMap);

        tflite_wrapper.init();
        tflite_wrapper.activate();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Visible Target", tflite_wrapper.getVisibleVuforiaTarget().trackableName);
            telemetry.update();
            sleep(100);
        }
    }
}
