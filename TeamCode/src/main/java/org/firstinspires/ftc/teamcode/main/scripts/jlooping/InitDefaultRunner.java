package org.firstinspires.ftc.teamcode.main.scripts.jlooping;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.ScriptRunner;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.main.scripts.jlooping.requests.imgproc.NavTargetDetectionRequest;
import org.firstinspires.ftc.teamcode.main.scripts.jlooping.requests.imgproc.ObjectDetectionRequest;
import org.firstinspires.ftc.teamcode.main.scripts.jlooping.requests.imgproc.StartingPositionRequest;
import org.firstinspires.ftc.teamcode.main.scripts.jlooping.scripts.GamepadInputHandlerScript;
import org.firstinspires.ftc.teamcode.main.utils.autonomous.image.ObjectDetector;

public class InitDefaultRunner {
    public static ScriptRunner generateRunner(LinearOpMode opMode, boolean addControllerDriving) {
        ScriptRunner runner = new ScriptRunner();
        try {
            if (addControllerDriving) {
                runner.addScript(new GamepadInputHandlerScript("inputDriver", opMode));
            }
            ScriptParameters.GlobalVariable<Boolean> driveEnabled = new ScriptParameters.GlobalVariable<>("driveEnabled");
            driveEnabled.setValue(true);
            runner.scriptParametersGlobal.addGlobalVariable(driveEnabled);
        } catch (ScriptRunner.DuplicateScriptException e) {
            e.printStackTrace();
        }
        return runner;
    }

    public static void addImageProc(ScriptRunner runner, ObjectDetector detector) {
        runner.addRequest(new NavTargetDetectionRequest(detector));
        runner.addRequest(new ObjectDetectionRequest(detector));
        runner.addRequest(new StartingPositionRequest(detector));
    }
}
