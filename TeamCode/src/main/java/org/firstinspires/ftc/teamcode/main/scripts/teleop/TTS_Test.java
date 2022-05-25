package org.firstinspires.ftc.teamcode.main.scripts.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;

@TeleOp(name = "ttsTest", group = "ColinCode")
public class TTS_Test extends LinearOpMode {

    private AndroidTextToSpeech androidTextToSpeech = new AndroidTextToSpeech();

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Started", true);
        telemetry.update();
        androidTextToSpeech.initialize();
        androidTextToSpeech.speak("hello");
        telemetry.addData("Amogus", "True");
        while (!isStopRequested()) {
            //
        }
        androidTextToSpeech.close();
    }
}
