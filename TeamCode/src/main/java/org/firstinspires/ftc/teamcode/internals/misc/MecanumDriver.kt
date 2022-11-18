package org.firstinspires.ftc.teamcode.internals.misc

import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class MecanumDriver(val drivetrainMapMode: DrivetrainMapMode, val useExpansionHub: Boolean, val fieldCentric: Boolean) {
    constructor() : this(DrivetrainMapMode.FR_BR_FL_BL, false, false)
    constructor(drivetrainMapMode: DrivetrainMapMode) : this(drivetrainMapMode, false, false)

    fun runMecanum(x: Double, y: Double, rot: Double) {
        val backLeftPower: Double
        val frontLeftPower: Double
        val backRightPower: Double
        val frontRightPower: Double

        when (fieldCentric) {
            false -> {
                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]
                val denominator = max(abs(rot) + abs(x) + abs(y), 0.8)
                frontLeftPower = (y + x + rot) / denominator
                backLeftPower = (y - x + rot) / denominator
                frontRightPower = -(y - x - rot) / denominator
                backRightPower = -(y + x - rot) / denominator
            }
            true -> {
                // Read inverse IMU heading, as the IMU heading is CW positive
                val botHeading: Double = -Devices.integrated_imu.orientation.x

                Logging.logData("Bot Heading", botHeading)
                Logging.logData("   X", Devices.integrated_imu.orientation.x)
                Logging.logData("   Y", Devices.integrated_imu.orientation.y)
                Logging.logData("   Z", Devices.integrated_imu.orientation.z)
                Logging.updateLog()

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
                frontRightPower = -(rotY - rotX - rot) / denominator
                backRightPower = -(rotY + rotX - rot) / denominator
            }
        }

        when (useExpansionHub) {
            false -> {
                when (drivetrainMapMode) {
                    DrivetrainMapMode.FR_BR_FL_BL -> {
                        Devices.motor0.power = frontRightPower
                        Devices.motor1.power = backRightPower
                        Devices.motor2.power = frontLeftPower
                        Devices.motor3.power = backLeftPower
                    }

                    DrivetrainMapMode.BL_FL_BR_FR -> {
                        Devices.motor0.power = backLeftPower
                        Devices.motor1.power = frontLeftPower
                        Devices.motor2.power = backRightPower
                        Devices.motor3.power = frontRightPower
                    }
                }
            }

            true -> {
                when (drivetrainMapMode) {
                    DrivetrainMapMode.FR_BR_FL_BL -> {
                        Devices.expansion_motor0.power = frontRightPower
                        Devices.expansion_motor1.power = backRightPower
                        Devices.expansion_motor2.power = frontLeftPower
                        Devices.expansion_motor3.power = backLeftPower
                    }

                    DrivetrainMapMode.BL_FL_BR_FR -> {
                        Devices.expansion_motor0.power = backLeftPower
                        Devices.expansion_motor1.power = frontLeftPower
                        Devices.expansion_motor2.power = backRightPower
                        Devices.expansion_motor3.power = frontRightPower
                    }
                }
            }
        }
    }

    fun getMecanumFormulaOutput(x: Double, y: Double, rot: Double): MecanumOutput {
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
                val botHeading: Double = -Devices.integrated_imu.orientation.x

                Logging.logData("Bot Heading", botHeading)
                Logging.logData("   X", Devices.integrated_imu.orientation.x)
                Logging.logData("   Y", Devices.integrated_imu.orientation.y)
                Logging.logData("   Z", Devices.integrated_imu.orientation.z)
                Logging.updateLog()

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
                frontRightPower = -(rotY - rotX - rot) / denominator
                backRightPower = -(rotY + rotX - rot) / denominator
            }
        }
        return MecanumOutput(frontLeftPower, backLeftPower, frontRightPower, backRightPower)
    }
}

enum class DrivetrainMapMode {
    BL_FL_BR_FR, FR_BR_FL_BL
}

data class MecanumOutput(
    val frontLeftPower: Double,
    val backLeftPower: Double,
    val frontRightPower: Double,
    val backRightPower: Double
)