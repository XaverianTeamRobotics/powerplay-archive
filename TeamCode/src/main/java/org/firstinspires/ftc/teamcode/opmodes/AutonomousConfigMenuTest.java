package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.registration.AutonomousOperation;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging;
import org.firstinspires.ftc.teamcode.internals.telemetry.UserConfiguredConstants;

import static org.firstinspires.ftc.teamcode.internals.telemetry.AutonomousConfigMenuKt.autoConfigMenu;

public class AutonomousConfigMenuTest extends OperationMode implements AutonomousOperation {
    @Override
    public void construct() {
        autoConfigMenu();
    }

    @Override
    public void run() {
        Logging.logData("Starting Code", UserConfiguredConstants.getStartingCode());
        Logging.logData("Show TeleOp Objective Reminder", UserConfiguredConstants.getShowTeleOpObjectiveReminder());
        Logging.updateLog();
    }

    @Override
    public Class<? extends OperationMode> getNext() {
        return ArmMecanumSmallRobot.class;
    }
}
