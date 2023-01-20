package org.firstinspires.ftc.teamcode.opmodes

import org.firstinspires.ftc.teamcode.features.FourMotorArmFeature
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation

class FourMotorArmTest: OperationMode(), TeleOperation {
    override fun construct() {
        registerFeature(FourMotorArmFeature())
    }

    override fun run() {

    }
}