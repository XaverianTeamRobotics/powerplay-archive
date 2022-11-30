package org.firstinspires.ftc.teamcode.internals.remote_debugger

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.expansion_motor0
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.expansion_motor1
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.expansion_motor2
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.expansion_motor3
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.motor0
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.motor1
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.motor2
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.motor3
import java.text.DateFormat.getDateTimeInstance
import java.util.*

/*
 * A list of functions that return a JSON string to be sent to the client.
 */

/*
 * Send any string content to the client while marking it as human-readable and not a command (using JSON via the GSON library).
 */
fun plainTextMessage(text: String): String {
    val json = JsonObject()
    json.addProperty("type", "text")
    json.addProperty("text", text)
    return Gson().toJson(json)
}

/*
 * Send a simple "Welcome" message to the client in addition to the current date and time.
 */
fun welcomeMessage(): String {
    val json = JsonObject()
    val formatter = getDateTimeInstance()
    val time = formatter.format(Date())
    return plainTextMessage("Welcome to the Remote Debugger! The current time is $time.")
}

/*
 * Send a message to the client indicating that a motor is availiable for debugging.
 */
fun motorEnableMessage(motor: Int, enabled: Boolean): String {
    val json = JsonObject()
    json.addProperty("type", "enable-motor")
    json.addProperty("motor", motor.toString())
    json.addProperty("enabled", enabled)
    return Gson().toJson(json)
}

fun motorEnableMessage(motor: Int): String {
    return motorEnableMessage(motor, true)
}

fun motorDisableMessage(motor: Int): String {
    return motorEnableMessage(motor, false)
}

/*
 * Send a message indicating a motor's power
 */
fun motorPowerMessage(motor: Int, power: Double): String {
    val json = JsonObject()
    json.addProperty("type", "motor-power")
    json.addProperty("motor", motor.toString())
    json.addProperty("power", power)
    return Gson().toJson(json)
}




// ================== CALLBACKS ================== //

/*
 * A callback for a motor's power.
 */
fun motorPowerCallback(motor: Int, power: Double) {
    when (motor) {
        0 -> motor0             .power = power
        1 -> motor1             .power = power
        2 -> motor2             .power = power
        3 -> motor3             .power = power
        4 -> expansion_motor0   .power = power
        5 -> expansion_motor1   .power = power
        6 -> expansion_motor2   .power = power
        7 -> expansion_motor3   .power = power
    }
}