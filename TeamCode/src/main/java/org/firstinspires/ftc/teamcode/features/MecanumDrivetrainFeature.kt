package org.firstinspires.ftc.teamcode.features

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.controller1
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.expansion_motor0
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.expansion_motor1
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.expansion_motor2
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.expansion_motor3
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.motor0
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.motor1
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.motor2
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.motor3
import kotlin.math.abs
import kotlin.math.max

class MecanumDrivetrainFeature(var drivetrainMapMode: DrivetrainMapMode, var useExpansionHub: Boolean) :
    BlankFeature("MecanumOpMode", false) {

    constructor() : this(DrivetrainMapMode.FR_BR_FL_BL, true)

    override fun run(scriptParameters: ScriptParameters) {
        //val y = gamepad1.left_stick_y.toDouble()
        //val x = gamepad1.left_stick_x.toDouble()
        //val rx = gamepad1.right_stick_x.toDouble()
        val rot: Double = controller1.leftStickX
        val x: Double = controller1.rightStickX
        val y: Double = -controller1.rightStickY
        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        val denominator = max(abs(rot) + abs(x) + abs(y), 1.0)
        val frontLeftPower: Double = (y + x + rot) / denominator
        val backLeftPower: Double = (y - x + rot) / denominator
        val frontRightPower: Double = -(y - x - rot) / denominator
        val backRightPower: Double = -(y + x - rot) / denominator

        when (useExpansionHub) {
            false -> {
                when (drivetrainMapMode) {
                    DrivetrainMapMode.FR_BR_FL_BL -> {
                        motor0.power = frontRightPower
                        motor1.power = backRightPower
                        motor2.power = frontLeftPower
                        motor3.power = backLeftPower
                    }

                    DrivetrainMapMode.BL_FL_BR_FR -> {
                        motor0.power = backLeftPower
                        motor1.power = frontLeftPower
                        motor2.power = backRightPower
                        motor3.power = frontRightPower
                    }
                }
            }

            true -> {
                when (drivetrainMapMode) {
                    DrivetrainMapMode.FR_BR_FL_BL -> {
                        expansion_motor0.power = frontRightPower
                        expansion_motor1.power = backRightPower
                        expansion_motor2.power = frontLeftPower
                        expansion_motor3.power = backLeftPower
                    }

                    DrivetrainMapMode.BL_FL_BR_FR -> {
                        expansion_motor0.power = backLeftPower
                        expansion_motor1.power = frontLeftPower
                        expansion_motor2.power = backRightPower
                        expansion_motor3.power = frontRightPower
                    }
                }
            }
        }
    }
}

enum class DrivetrainMapMode {
    BL_FL_BR_FR, FR_BR_FL_BL
}