package org.firstinspires.ftc.teamcode.features;

import com.michaell.looping.ScriptRunner;
import com.michaell.looping.ScriptTemplate;
import org.firstinspires.ftc.teamcode.utils.registration.OperationMode;

public abstract class BlankFeature extends ScriptTemplate {
    public BlankFeature(String name, boolean needsInit) {
        super(name, needsInit);
    }

    public abstract void addToOpMode(OperationMode opMode) throws ScriptRunner.DuplicateScriptException;
}
