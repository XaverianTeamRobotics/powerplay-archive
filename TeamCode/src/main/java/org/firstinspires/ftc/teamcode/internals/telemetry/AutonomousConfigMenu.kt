package org.firstinspires.ftc.teamcode.internals.telemetry

import org.firstinspires.ftc.teamcode.internals.telemetry.UserConfiguredConstants.showTeleOpObjectiveReminder
import org.firstinspires.ftc.teamcode.internals.telemetry.UserConfiguredConstants.startingCode

/*
 * Display a prompt to configure different options
 * Both returns the values AND sets some static variables
 */
fun autoConfigMenu(): AutoConfigResults {
    val menu = TelemetryMenu()
    val startingCodeMsg = "Starting Code"
    val showTeleOpObjectiveReminderMsg = "Show TeleOp Objective Reminder"
    menu.addMenuItem(MenuItem(startingCodeMsg, MenuItemType.INT, true))
    menu.addMenuItem(MenuItem(showTeleOpObjectiveReminderMsg, MenuItemType.BOOLEAN, true))
    menu.addMenuItem(MenuItem("Exit", MenuItemType.MENU, true))

    while (true) {
        menu.listenForControllerInput()
        if (menu.currentMenu.name == "Exit") {
            break
        }
    }

    // Set the static variables
    startingCode = menu.getMenuItem("Starting Code")!!.value as Int
    showTeleOpObjectiveReminder = menu.getMenuItem("Show TeleOp Objective Reminder")!!.value as Boolean

    return AutoConfigResults(startingCode, showTeleOpObjectiveReminder)
}

data class AutoConfigResults(val startingCode: Int, val showTeleOpObjectiveReminder: Boolean)

object UserConfiguredConstants {
    var startingCode: Int = 1
    var showTeleOpObjectiveReminder: Boolean = true
}