package org.firstinspires.ftc.teamcode.internals.telemetry

import com.michaell.looping.ScriptTemplate
import com.michaell.looping.builtin.ConvertToScript
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.controller1
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter

/**
 * Create a menu in the telemetry interface (using the Logging class) to allow for configuration during a match
 * Use controllers as inputs to navigate the menu
 */
class TelemetryMenu {
    var currentMenuIndex: Int = 0
    var currentMenu: MenuItem = MenuItem("Main Menu", MenuItemType.MENU, true)
    lateinit var jloopingScript: ScriptTemplate
    var allMenuItems: MutableList<MenuItem> = mutableListOf()

    fun addMenuItem(menuItem: MenuItem) {
        (currentMenu.value as MutableList<MenuItem>).add(menuItem)
        allMenuItems.add(menuItem)
    }

    fun getMenuItemInCurrentMenu(name: String): MenuItem? {
        for (menuItem in (currentMenu.value as MutableList<MenuItem>)) {
            if (menuItem.name == name) {
                return menuItem
            }
        }
        return null
    }

    fun getMenuItem(name: String): MenuItem? {
        for (menuItem in allMenuItems) {
            if (menuItem.name == name) {
                return menuItem
            }
        }
        return null
    }

    private fun previousMenuItem() {
        if (currentMenuIndex > 0) {
            currentMenuIndex--
        } else {
            currentMenuIndex = (currentMenu.value as MutableList<MenuItem>).size - 1
        }
    }

    private fun nextMenuItem() {
        if (currentMenuIndex < (currentMenu.value as MutableList<MenuItem>).size - 1) {
            currentMenuIndex++
        } else {
            currentMenuIndex = 0
        }
    }

    private fun onDPadUp() {
        previousMenuItem()
    }

    private fun onDPadDown() {
        nextMenuItem()
    }

    private fun onDPadLeft() {
        val item = (currentMenu.value as MutableList<MenuItem>)[currentMenuIndex]
        if (item.type != MenuItemType.MENU) {
            item.decrement()
        }
    }

    private fun onDPadRight() {
        val item = (currentMenu.value as MutableList<MenuItem>)[currentMenuIndex]
        if (item.type != MenuItemType.MENU) {
            item.increment()
        } else {
            currentMenu = item
            item.enterMenu(null)
        }
    }

    private fun display() {
        Logging.clear()
        Logging.logText("Telemetry Menu")
        Logging.logText(" ")
        Logging.logText("Use the D-Pad to navigate the menu")
        Logging.logText(" ")
        Logging.logText("Current Menu: ${currentMenu.name}")
        Logging.logText(" ")
        if (currentMenu.name != "Main Menu") {
            Logging.logText("Press CIRLE or B to go back")
            Logging.logText(" ")
        }
        for (i in (currentMenu.value as MutableList<MenuItem>).indices) {
            val item = (currentMenu.value as MutableList<MenuItem>)[i]
            var text = "${if (i == currentMenuIndex) "> " else "  "}${item.name}"
            if (item.type != MenuItemType.MENU) {
                text += ": ${item.value}"
            }
            if (i == currentMenuIndex) {
                Logging.logText("> ${item.name}")
            } else {
                Logging.logText("  ${item.name}")
            }
        }
        Logging.updateLog()
    }

    private fun onBackButton() {
        currentMenu = currentMenu.previousMenu!!
        currentMenu.exitMenu()
    }

    fun runInBackground() {
        HardwareGetter.jloopingRunner!!.addScript(ConvertToScript("TelemetryMenu", this, null, "listenForControllerInput"))
    }

    fun stopInBackground() {
        HardwareGetter.jloopingRunner!!.scripts.remove(jloopingScript)
    }

    fun listenForControllerInput() {
        if (controller1.dpadUp) {
            onDPadUp()
        }
        if (controller1.dpadDown) {
            onDPadDown()
        }
        if (controller1.dpadLeft) {
            onDPadLeft()
        }
        if (controller1.dpadRight) {
            onDPadRight()
        }

        if (controller1.b || controller1.circle) {
            onBackButton()
        }

        display()
    }
}

class MenuItem(val name: String, val type: MenuItemType, var isEditable: Boolean) {
    var value: Any
    var previousMenu: MenuItem? = null
    var stepSize: Double = 1.0
        set(value) {
            if (type == MenuItemType.DOUBLE || type == MenuItemType.INT) {
                if (type == MenuItemType.INT) {
                    this.value = value.toInt()
                }
            }
            field = value
        }

    init {
        value = when (type) {
            MenuItemType.BOOLEAN -> false
            MenuItemType.DOUBLE -> 0.0
            MenuItemType.INT -> 0
            MenuItemType.MENU -> mutableListOf<MenuItem>()
        }
    }

    fun increment() {
        when (type) {
            MenuItemType.INT -> {
                value = (value as Int) + 1
            }
            MenuItemType.DOUBLE -> {
                value = (value as Double) + 1
            }
            MenuItemType.BOOLEAN -> {
                value = !(value as Boolean)
            }

            else -> {}
        }
    }

    fun decrement() {
        when (type) {
            MenuItemType.INT -> {
                value = (value as Int) - 1
            }
            MenuItemType.DOUBLE -> {
                value = (value as Double) - 1
            }
            MenuItemType.BOOLEAN -> {
                value = !(value as Boolean)
            }

            else -> {}
        }
    }

    fun enterMenu(previousMenu: MenuItem?) {
        if (type == MenuItemType.MENU) {
            this.previousMenu = previousMenu
        }
    }

    fun exitMenu() {
        if (type == MenuItemType.MENU) {
            previousMenu = null
        }
    }
}

enum class MenuItemType {
    BOOLEAN, DOUBLE, INT, MENU
}