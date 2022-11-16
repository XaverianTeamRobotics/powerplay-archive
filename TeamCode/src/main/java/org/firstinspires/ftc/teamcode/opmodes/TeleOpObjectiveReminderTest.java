package org.firstinspires.ftc.teamcode.opmodes;

import com.michaell.looping.ScriptRunner;
import org.firstinspires.ftc.teamcode.internals.features.TeleOpNextObjectiveReminder;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

public class TeleOpObjectiveReminderTest extends OperationMode {
    @Override
    public void construct() {
        try {
            registerFeature(new TeleOpNextObjectiveReminder());
        } catch (ScriptRunner.DuplicateScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

    }
}
