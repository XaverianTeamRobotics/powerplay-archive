package org.firstinspires.ftc.teamcode.hardware

import com.michaell.looping.ScriptParameters
import com.michaell.looping.ScriptRunner
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.hardware.emulated.EmulatedGamepadRequest
import org.firstinspires.ftc.teamcode.hardware.emulated.EmulatedMotorRequest
import org.firstinspires.ftc.teamcode.hardware.physical.GamepadRequest
import org.firstinspires.ftc.teamcode.hardware.physical.GamepadRequestInput
import org.firstinspires.ftc.teamcode.hardware.physical.MotorRequest
import org.firstinspires.ftc.teamcode.hardware.physical.StandardMotorParameters
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
         * Check if a motor is currently busy. This can be used in autonomous programming to track distances.
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
    }
}