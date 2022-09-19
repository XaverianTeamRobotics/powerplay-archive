package org.firstinspires.ftc.teamcode.hardware

import com.michaell.looping.ScriptParameters
import com.michaell.looping.ScriptRunner
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.emulated.EmulatedGamepadRequest
import org.firstinspires.ftc.teamcode.hardware.emulated.EmulatedMotorRequest
import org.firstinspires.ftc.teamcode.hardware.physical.inputs.GamepadRequestInput
import org.firstinspires.ftc.teamcode.hardware.physical.inputs.MotorOperation
import org.firstinspires.ftc.teamcode.hardware.physical.inputs.StandardMotorParameters
import org.firstinspires.ftc.teamcode.hardware.physical.request.GamepadRequest
import org.firstinspires.ftc.teamcode.hardware.physical.request.MotorRequest
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

class HardwareGetter {
    companion object {
        @JvmStatic
        var isEmulated: Boolean = false
            get() = field
            set(value) {
                field = value
            }

        @JvmStatic
        var hardwareMap: HardwareMap? = null
            get() = field
            set(value) {
                if (value == null) {
                    throw IllegalArgumentException("The global hardwareMap cannot be set to null during execution")
                } else {
                    field = value
                }
            }

        @JvmStatic
        var jloopingRunner: ScriptRunner? = null
            get() = field
            set(value) {
                if (value == null) {
                    throw IllegalArgumentException("The global ScriptRunner cannot be set to null during execution")
                } else {
                    field = value
                }
            }

        /**
         * Creates a StandardMotorRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeMotorRequest(name: String): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedMotorRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = MotorRequest(name, hardwareMap!!)
            jloopingRunner!!.addRequest(req)
            return req
        }

        /**
         * Get a motor from a previously initialized request
         */
        @JvmStatic
        fun getMotorFromRequest(name: String): DcMotor? {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    return null
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return (jloopingRunner!!.scriptParametersGlobal.getRequest(name) as MotorRequest).motor
        }

        /**
         * Creates a GamepadRequest, adds it to the global JloopingRunner, then returns it
         * Note this it returns just a generic request, so you check that it is not emulated before casting
         */
        @JvmStatic
        fun makeGamepadRequest(name: String, gamepad: Gamepad?): ScriptParameters.Request{
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    val req = EmulatedGamepadRequest(name)
                    jloopingRunner!!.addRequest(req)
                    return req
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }

            val req = GamepadRequest(gamepad!!, name)
            jloopingRunner!!.addRequest(req)
            return req
        }

        @JvmStatic
        fun getGamepadValue(name: String, gamepadRequestInput: GamepadRequestInput): Double{
            var n: String = if (isEmulated) {
                "emulatedGamepad$name"
            } else {
                name
            }

            return jloopingRunner!!.scriptParametersGlobal.issueRequest(gamepadRequestInput, jloopingRunner!!
                .scriptParametersGlobal.getRequest(n)) as Double
        }

        @JvmStatic
        fun setMotorValue(name: String, value: StandardMotorParameters) {
            try {
                jloopingRunner!!.scriptParametersGlobal.issueRequest(
                    value, jloopingRunner!!
                        .scriptParametersGlobal.getRequest(name)
                ) as Double
            } catch (_:Exception) {}
        }

        /**
         * Check if a motor is currently busy. This can be used in autonomous programming to track if a encoder is
         * done going a certain distance.
         */
        @JvmStatic
        fun isMotorBusy(name: String): Boolean {
            if (hardwareMap == null || jloopingRunner == null) {
                if (isEmulated && jloopingRunner != null) {
                    return false
                } else {
                    println("HardwareMap: $hardwareMap")
                    println("ScriptRunner: $jloopingRunner")
                    throw NullPointerException("The hardwareMap or jloopingRunner are null")
                }
            }
            return getMotorFromRequest(name)!!.isBusy
        }

        @JvmStatic
        fun initAllDevices() {
            Devices.gamepad1 = GlobalGamepadAccess("gamepad1")
            Devices.gamepad2 = GlobalGamepadAccess("gamepad2")

            Devices.motor0 = GlobalMotorAccess("motor0")
            Devices.motor1 = GlobalMotorAccess("motor1")
            Devices.motor2 = GlobalMotorAccess("motor2")
            Devices.motor3 = GlobalMotorAccess("motor3")
        }
    }
}

class GlobalMotorAccess(var name: String) {
    private val motorRequest: ScriptParameters.Request = HardwareGetter.makeMotorRequest(name)

    val motor: DcMotor?
        get() = HardwareGetter.getMotorFromRequest(name)

    var power: Double = 0.0
        set(value) {
            field = value
            HardwareGetter.setMotorValue(name, StandardMotorParameters(value, runMode))
        }

    var runMode: MotorOperation = MotorOperation.POWER
        set(value) {
            field = value
            HardwareGetter.setMotorValue(name, StandardMotorParameters(power, value))
        }

    val isBusy: Boolean
        get() {
            return HardwareGetter.isMotorBusy(name)
        }
}

class GlobalGamepadAccess(var name: String) {
    var gamepadRequest: ScriptParameters.Request = HardwareGetter.jloopingRunner!!.
        scriptParametersGlobal.getRequest(name) as ScriptParameters.Request

    val leftStickX: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_STICK_X)
        }

    val leftStickY: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_STICK_Y)
        }

    val rightStickX: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_STICK_X)
        }

    val rightStickY: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_STICK_Y)
        }

    val leftTrigger: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_TRIGGER)
        }

    val rightTrigger: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_TRIGGER)
        }

    val dpadUp: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.DPAD_UP) > 0.5
        }

    val dpadDown: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.DPAD_DOWN) > 0.5
        }

    val dpadLeft: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.DPAD_LEFT) > 0.5
        }

    val dpadRight: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.DPAD_RIGHT) > 0.5
        }

    val a: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.A) > 0.5
        }

    val b: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.B) > 0.5
        }

    val x: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.X) > 0.5
        }

    val y: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.Y) > 0.5
        }

    val leftBumper: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_BUMPER) > 0.5
        }

    val rightBumper: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_BUMPER) > 0.5
        }

    val leftStickButton: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.LEFT_STICK_BUTTON) > 0.5
        }

    val rightStickButton: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.RIGHT_STICK_BUTTON) > 0.5
        }

    val back: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.BACK) > 0.5
        }

    val start: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.START) > 0.5
        }

    val guide: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.GUIDE) > 0.5
        }

    val circle: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.CIRCLE) > 0.5
        }

    val triangle: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TRIANGLE) > 0.5
        }

    val square: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.SQUARE) > 0.5
        }

    val cross: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.CROSS) > 0.5
        }

    val share: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.SHARE) > 0.5
        }

    val options: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.OPTIONS) > 0.5
        }

    val touchpad: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD) > 0.5
        }

    val touchpadFinger1: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_1) > 0.5
        }

    val touchpadFinger2: Boolean
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_2) > 0.5
        }

    val touchpadFinger1X: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_1_X)
        }

    val touchpadFinger1Y: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_1_Y)
        }

    val touchpadFinger2X: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_2_X)
        }

    val touchpadFinger2Y: Double
        get() {
            return HardwareGetter.getGamepadValue(name, GamepadRequestInput.TOUCHPAD_FINGER_2_Y)
        }
}

class Devices {
    companion object {
        @JvmStatic lateinit var gamepad1: GlobalGamepadAccess
        @JvmStatic lateinit var gamepad2: GlobalGamepadAccess

        @JvmStatic lateinit var motor0: GlobalMotorAccess
        @JvmStatic lateinit var motor1: GlobalMotorAccess
        @JvmStatic lateinit var motor2: GlobalMotorAccess
        @JvmStatic lateinit var motor3: GlobalMotorAccess
    }
}