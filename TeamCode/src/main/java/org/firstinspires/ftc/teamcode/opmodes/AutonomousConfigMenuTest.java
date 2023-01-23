package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.telemetry.UserConfiguredConstants;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging;

import static org.firstinspires.ftc.teamcode.internals.telemetry.AutonomousConfigMenuKt.autoConfigMenu;

public class AutonomousConfigMenuTest extends OperationMode implements AutonomousOperation {
    @Override
    public void construct() {
        autoConfigMenu();
    }

    @Override
    public void run() {
        AdvancedLogging.logData("Starting Code", UserConfiguredConstants.getStartingCode());
        AdvancedLogging.logData("Show TeleOp Objective Reminder", UserConfiguredConstants.getShowTeleOpObjectiveReminder());
        AdvancedLogging.updateLog();
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return null;
    }
}
