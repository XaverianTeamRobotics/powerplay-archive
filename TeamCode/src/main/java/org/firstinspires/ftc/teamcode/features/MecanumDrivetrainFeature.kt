package org.firstinspires.ftc.teamcode.features

import org.firstinspires.ftc.teamcode.internals.features.Buildable
import org.firstinspires.ftc.teamcode.internals.features.Feature
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.controller1
import org.firstinspires.ftc.teamcode.internals.hardware.Devices.Companion.controller2
import org.firstinspires.ftc.teamcode.internals.misc.DrivetrainMapMode
import org.firstinspires.ftc.teamcode.internals.misc.MecanumDriver

/**
* This is a feature that allows for a mecanum robot to be powered by a gamepad.
* @param drivetrainMapMode The motor layout of the drivetrain.
 * @param useExpansionHub Whether or not to use the expansion hub to get the motors. Requires that the expansion hub motors be initialized before use
 * @param fieldCentric Whether or not to use field centric controls. The imu must be initialized prior to use.
 */
class MecanumDrivetrainFeature(private var drivetrainMapMode: DrivetrainMapMode, private var useExpansionHub: Boolean, private var fieldCentric: Boolean, private var isRotInverted: Boolean) : Feature(),
    Buildable {

    private var mecanumDriver: MecanumDriver? = null

    constructor() : this(DrivetrainMapMode.FR_BR_FL_BL, false, false, false)
    constructor(drivetrainMapMode: DrivetrainMapMode) : this(drivetrainMapMode, false, false, false)
    constructor(drivetrainMapMode: DrivetrainMapMode, useExpansionHub: Boolean) : this(drivetrainMapMode, useExpansionHub, false, false)

    override fun build() {

        mecanumDriver = MecanumDriver(drivetrainMapMode, useExpansionHub, fieldCentric)
    }

    override fun loop() {
        /*
        Right joystick controlls rotation
        Left joystick controlls forward/backward/left/right movement

        Controller 1 has full power
        Controller 2 has reduced power (for finer control)
         */

        val rot: Double = if (!isRotInverted) {
            controller1.rightStickX + 0.4 * controller2.rightStickX // 0.4/0.3 are scaling constants for controller 2
        } else {
            -(controller1.rightStickX + 0.4 * controller2.rightStickX)
        }

        val x: Double = -1 * (controller1.leftStickX + 0.3 * controller2.leftStickX)
        val y: Double = (controller1.leftStickY + 0.3 * controller2.leftStickY)
        mecanumDriver!!.runMecanum(x, y, rot)
    }
}
