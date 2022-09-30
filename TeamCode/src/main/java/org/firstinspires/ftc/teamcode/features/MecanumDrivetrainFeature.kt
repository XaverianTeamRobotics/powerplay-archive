package org.firstinspires.ftc.teamcode.features

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.utils.hardware.Devices
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.controller1
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.expansion_motor0
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.expansion_motor1
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.expansion_motor2
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.expansion_motor3
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.integrated_imu
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.motor0
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.motor1
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.motor2
import org.firstinspires.ftc.teamcode.utils.hardware.Devices.Companion.motor3
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
* This is a feature that allows for a mecanum robot to be powered by a gamepad.
* @param drivetrainMapMode The motor layout of the drivetrain.
 * @param useExpansionHub Whether or not to use the expansion hub to get the motors. Requires that the expansion hub motors be initialized before use
 * @param fieldCentric Whether or not to use field centric controls. The imu must be initialized prior to use.
 */
class MecanumDrivetrainFeature(private var drivetrainMapMode: DrivetrainMapMode, private var useExpansionHub: Boolean, private var fieldCentric: Boolean) :
    BlankFeature("MecanumOpMode", fieldCentric) {  // Only needs to init if it is field centric

    constructor() : this(DrivetrainMapMode.FR_BR_FL_BL, false, false)
    constructor(drivetrainMapMode: DrivetrainMapMode) : this(drivetrainMapMode, false, false)
    constructor(drivetrainMapMode: DrivetrainMapMode, useExpansionHub: Boolean) : this(drivetrainMapMode, useExpansionHub, false)

    override fun run(scriptParameters: ScriptParameters) {
        //val y = gamepad1.left_stick_y.toDouble()
        //val x = gamepad1.left_stick_x.toDouble()
        //val rx = gamepad1.right_stick_x.toDouble()
        val rot: Double = controller1.leftStickX
        val x: Double = controller1.rightStickX
        val y: Double = -controller1.rightStickY

        val backLeftPower: Double
        val frontLeftPower: Double
        val backRightPower: Double
        val frontRightPower: Double

        when (fieldCentric) {
            false -> {
                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]
                val denominator = max(abs(rot) + abs(x) + abs(y), 1.0)
                frontLeftPower = (y + x + rot) / denominator
                backLeftPower = (y - x + rot) / denominator
                frontRightPower = -(y - x - rot) / denominator
                backRightPower = -(y + x - rot) / denominator
            }
            true -> {
                // Read inverse IMU heading, as the IMU heading is CW positive
                val botHeading: Double = -integrated_imu.orientation.x

                val rotX = x * cos(botHeading) - y * sin(botHeading)
                val rotY = x * sin(botHeading) + y * cos(botHeading)

                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]

                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]
                val denominator = max(abs(y) + abs(x) + abs(rot), 1.0)
                frontLeftPower = (rotY + rotX + rot) / denominator
                backLeftPower = (rotY - rotX + rot) / denominator
                frontRightPower = (rotY - rotX - rot) / denominator
                backRightPower = (rotY + rotX - rot) / denominator
            }
        }

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