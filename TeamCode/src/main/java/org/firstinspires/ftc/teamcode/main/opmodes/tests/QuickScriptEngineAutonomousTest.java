package org.firstinspires.ftc.teamcode.main.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.main.scripts.jlooping.QuickScriptEngine;

@Autonomous(name = "QuickScriptEngineAutonomousTest", group = "tests")
public class QuickScriptEngineAutonomousTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        QuickScriptEngine engine = new QuickScriptEngine(this);
        engine.queueCommand(new QuickScriptEngine.MecanumDriveCommand(0.5f, 0, 0, 1000));
        engine.queueCommand(new QuickScriptEngine.MecanumDriveCommand(0, 0.5f, 0, 1000));
        engine.queueCommand(new QuickScriptEngine.MecanumDriveCommand(0, 0, 0.5f, 1000));
        waitForStart();
        engine.runQueue();
    }
}