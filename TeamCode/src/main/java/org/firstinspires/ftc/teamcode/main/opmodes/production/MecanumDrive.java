package org.firstinspires.ftc.teamcode.main.opmodes.production;

import com.michaell.looping.ScriptRunner;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.teamcode.main.scripts.jlooping.InitDefaultRunner;

@TeleOp(name = "Mecanum Drive with jlooping")
public class MecanumDrive extends LinearOpMode {
    @Override
    public void runOpMode() {
        telemetry.addLine("Initing default runner");
        telemetry.update();
        ScriptRunner runner = InitDefaultRunner.generateRunner(this, false, true);
        telemetry.addLine("Initing OpMode Stopper");
        telemetry.update();
        InitDefaultRunner.addOpModeStopper(runner, this);
        telemetry.addLine("Running Runner");
        telemetry.update();
        runner.runConstantly();
    }
}