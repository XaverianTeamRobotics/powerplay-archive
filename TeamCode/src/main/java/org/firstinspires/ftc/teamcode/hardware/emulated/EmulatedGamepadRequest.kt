package org.firstinspires.ftc.teamcode.hardware.emulated

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.hardware.HardwareGetter
import org.firstinspires.ftc.teamcode.hardware.physical.GamepadRequest

class EmulatedGamepadRequest(name: String): ScriptParameters.Request(name) {
    init {
        val variable = ScriptParameters.GlobalVariable<GamepadEmulatedValue>("emulatedGamepad$name")
        variable.value = GamepadEmulatedValue()
        HardwareGetter.jloopingRunner!!.scriptParametersGlobal.addGlobalVariable(variable)
    }

    data class GamepadEmulatedValue(   var left_stick_x         : Double = 0.0,
                                       var left_stick_y         : Double = 0.0,
                                       var right_stick_x        : Double = 0.0,
                                       var right_stick_y        : Double = 0.0,
                                       var dpad_up              : Double = 0.0,
                                       var dpad_down            : Double = 0.0,
                                       var dpad_left            : Double = 0.0,
                                       var dpad_right           : Double = 0.0,
                                       var a                    : Double = 0.0,
                                       var b                    : Double = 0.0,
                                       var x                    : Double = 0.0,
                                       var y                    : Double = 0.0,
                                       var left_bumper          : Double = 0.0,
                                       var right_bumper         : Double = 0.0,
                                       var left_trigger         : Double = 0.0,
                                       var right_trigger        : Double = 0.0,
                                       var back                 : Double = 0.0,
                                       var start                : Double = 0.0,
                                       var left_stick_button    : Double = 0.0,
                                       var right_stick_button   : Double = 0.0,
                                       var guide                : Double = 0.0,
                                       var circle               : Double = 0.0,
                                       var cross                : Double = 0.0,
                                       var square               : Double = 0.0,
                                       var triangle             : Double = 0.0,
                                       var share                : Double = 0.0,
                                       var options              : Double = 0.0,
                                       var touchpad             : Double = 0.0,
                                       var touchpad_finger_1    : Double = 0.0,
                                       var touchpad_finger_2    : Double = 0.0,
                                       var touchpad_finger_1_x  : Double = 0.0,
                                       var touchpad_finger_1_y  : Double = 0.0,
                                       var touchpad_finger_2_x  : Double = 0.0,
                                       var touchpad_finger_2_y  : Double = 0.0,
                                       var ps                   : Double = 0.0)

    override fun issueRequest(p0: Any?): Any {
        val gamepad: GamepadEmulatedValue = HardwareGetter.jloopingRunner!!.scriptParametersGlobal.getGlobalVariable("emulatedGamepad$name").value as GamepadEmulatedValue
        when (p0 as GamepadRequest.GamepadRequestInput) {
            GamepadRequest.GamepadRequestInput.LEFT_STICK_X ->         return gamepad.left_stick_x
            GamepadRequest.GamepadRequestInput.LEFT_STICK_Y ->         return gamepad.left_stick_y
            GamepadRequest.GamepadRequestInput.RIGHT_STICK_X ->        return gamepad.right_stick_x
            GamepadRequest.GamepadRequestInput.RIGHT_STICK_Y ->        return gamepad.right_stick_y
            GamepadRequest.GamepadRequestInput.DPAD_UP ->              return gamepad.dpad_up
            GamepadRequest.GamepadRequestInput.DPAD_DOWN ->            return gamepad.dpad_down
            GamepadRequest.GamepadRequestInput.DPAD_LEFT ->            return gamepad.dpad_left
            GamepadRequest.GamepadRequestInput.DPAD_RIGHT ->           return gamepad.dpad_right
            GamepadRequest.GamepadRequestInput.A ->                    return gamepad.a
            GamepadRequest.GamepadRequestInput.B ->                    return gamepad.b
            GamepadRequest.GamepadRequestInput.X ->                    return gamepad.x
            GamepadRequest.GamepadRequestInput.Y ->                    return gamepad.y
            GamepadRequest.GamepadRequestInput.LEFT_BUMPER ->          return gamepad.left_bumper
            GamepadRequest.GamepadRequestInput.RIGHT_BUMPER ->         return gamepad.right_bumper
            GamepadRequest.GamepadRequestInput.LEFT_TRIGGER ->         return gamepad.left_trigger
            GamepadRequest.GamepadRequestInput.RIGHT_TRIGGER ->        return gamepad.right_trigger
            GamepadRequest.GamepadRequestInput.BACK ->                 return gamepad.back
            GamepadRequest.GamepadRequestInput.START ->                return gamepad.start
            GamepadRequest.GamepadRequestInput.LEFT_STICK_BUTTON ->    return gamepad.left_stick_button
            GamepadRequest.GamepadRequestInput.RIGHT_STICK_BUTTON ->   return gamepad.right_stick_button
            GamepadRequest.GamepadRequestInput.GUIDE ->                return gamepad.guide
            GamepadRequest.GamepadRequestInput.CIRCLE ->               return gamepad.circle
            GamepadRequest.GamepadRequestInput.CROSS ->                return gamepad.cross
            GamepadRequest.GamepadRequestInput.SQUARE ->               return gamepad.square
            GamepadRequest.GamepadRequestInput.TRIANGLE ->             return gamepad.triangle
            GamepadRequest.GamepadRequestInput.SHARE ->                return gamepad.share
            GamepadRequest.GamepadRequestInput.OPTIONS ->              return gamepad.options
            GamepadRequest.GamepadRequestInput.TOUCHPAD ->             return gamepad.touchpad
            GamepadRequest.GamepadRequestInput.TOUCHPAD_FINGER_1 ->    return gamepad.touchpad_finger_1
            GamepadRequest.GamepadRequestInput.TOUCHPAD_FINGER_2 ->    return gamepad.touchpad_finger_2
            GamepadRequest.GamepadRequestInput.TOUCHPAD_FINGER_1_X ->  return gamepad.touchpad_finger_1_x
            GamepadRequest.GamepadRequestInput.TOUCHPAD_FINGER_1_Y ->  return gamepad.touchpad_finger_1_y
            GamepadRequest.GamepadRequestInput.TOUCHPAD_FINGER_2_X ->  return gamepad.touchpad_finger_2_x
            GamepadRequest.GamepadRequestInput.TOUCHPAD_FINGER_2_Y ->  return gamepad.touchpad_finger_2_y
            GamepadRequest.GamepadRequestInput.PS ->                   return gamepad.ps
        }
    }

    override fun getOutputType(): Class<*> {
        return Double::class.java
    }

    override fun getInputType(): Class<*> {
        return GamepadRequest.GamepadRequestInput::class.java
    }
}