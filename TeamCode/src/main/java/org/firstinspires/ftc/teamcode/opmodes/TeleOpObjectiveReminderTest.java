package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.TeleOpNextObjectiveReminder;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class TeleOpObjectiveReminderTest extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new TeleOpNextObjectiveReminder());
    }

    @Override
    public void run() {

    }
}
