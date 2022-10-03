package org.firstinspires.ftc.teamcode.features

import com.michaell.looping.ScriptParameters
import org.firstinspires.ftc.teamcode.utils.MecanumDriver
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
import org.firstinspires.ftc.teamcode.utils.hardware.Logging
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

    companion object {
        @JvmStatic
        var mecanumDriver: MecanumDriver? = null
    }

    constructor() : this(DrivetrainMapMode.FR_BR_FL_BL, false, false)
    constructor(drivetrainMapMode: DrivetrainMapMode) : this(drivetrainMapMode, false, false)
    constructor(drivetrainMapMode: DrivetrainMapMode, useExpansionHub: Boolean) : this(drivetrainMapMode, useExpansionHub, false)

    init {
        mecanumDriver = MecanumDriver(drivetrainMapMode, useExpansionHub, fieldCentric)
    }

    override fun run(scriptParameters: ScriptParameters) {
        //val y = gamepad1.left_stick_y.toDouble()
        //val x = gamepad1.left_stick_x.toDouble()
        //val rx = gamepad1.right_stick_x.toDouble()
        val rot: Double = controller1.leftStickX
        val x: Double = controller1.rightStickX
        val y: Double = -controller1.rightStickY

        mecanumDriver!!.runMecanum(x, y, rot)
    }
}

enum class DrivetrainMapMode {
    BL_FL_BR_FR, FR_BR_FL_BL
}