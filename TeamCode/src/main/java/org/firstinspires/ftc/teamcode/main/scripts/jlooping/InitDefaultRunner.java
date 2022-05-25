package org.firstinspires.ftc.teamcode.main.scripts.jlooping;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.ScriptRunner;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.main.scripts.jlooping.requests.imgproc.NavTargetDetectionRequest;
import org.firstinspires.ftc.teamcode.main.scripts.jlooping.requests.imgproc.ObjectDetectionRequest;
import org.firstinspires.ftc.teamcode.main.scripts.jlooping.requests.imgproc.StartingPositionRequest;
import org.firstinspires.ftc.teamcode.main.scripts.jlooping.scripts.MecanumDriveScript;
import org.firstinspires.ftc.teamcode.main.scripts.jlooping.scripts.OpModeStopper;
import org.firstinspires.ftc.teamcode.main.scripts.jlooping.scripts.RegularGamepadInputHandlerScript;
import org.firstinspires.ftc.teamcode.main.utils.autonomous.image.ObjectDetector;

public class InitDefaultRunner {
    public static ScriptRunner generateRunner(LinearOpMode opMode, boolean addTankControllerDriving, boolean addMecanumDriving) {
        ScriptRunner runner = new ScriptRunner();
        try {
            if (addTankControllerDriving) {
                runner.addScript(new RegularGamepadInputHandlerScript("inputDriver", opMode));
            }
            if (addMecanumDriving) {
                runner.addScript(new MecanumDriveScript("mecanumDrive", opMode));
            }
            ScriptParameters.GlobalVariable<Boolean> driveEnabled = new ScriptParameters.GlobalVariable<>("driveEnabled");
            driveEnabled.setValue(true);
            runner.scriptParametersGlobal.addGlobalVariable(driveEnabled);
        } catch (ScriptRunner.DuplicateScriptException e) {
            e.printStackTrace();
        }
        return runner;
    }

    public static void addOpModeStopper(ScriptRunner runner, LinearOpMode opMode) {
        try {
            runner.addScript(new OpModeStopper("opModeStopper", opMode));
        } catch (ScriptRunner.DuplicateScriptException e) {
            e.printStackTrace();
        }
    }

    public static void addImageProc(ScriptRunner runner, ObjectDetector detector) {
        runner.addRequest(new NavTargetDetectionRequest(detector));
        runner.addRequest(new ObjectDetectionRequest(detector));
        runner.addRequest(new StartingPositionRequest(detector));
    }
}
