package org.firstinspires.ftc.teamcode.main.scripts.jlooping.scripts;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.builtin.ConditionalScriptTemplate;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class OpModeStopper extends ConditionalScriptTemplate {
    LinearOpMode opMode;
    public OpModeStopper(String name, LinearOpMode opMode) {
        super(name, true);
        this.opMode = opMode;
    }

    @Override
    public void init(ScriptParameters parameters) {
        needsInit = false;
    }

    @Override
    public void toRun(ScriptParameters scriptParameters) {
        scriptParameters.runner.setShouldExit(true);
    }

    @Override
    public boolean shouldRun(ScriptParameters scriptParameters) {
        return opMode.isStopRequested();
    }
}
