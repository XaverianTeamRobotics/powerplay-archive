package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.ArmFeature;
import org.firstinspires.ftc.teamcode.features.MecanumDrivetrainFeature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.DrivetrainMapMode;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;


public class ArmMecanumDemo extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        Devices.initializeArmMotors();
        Devices.initializeControlHubMotors();
        MecanumDrivetrainFeature drivetrain = new MecanumDrivetrainFeature(DrivetrainMapMode.FR_BR_FL_BL, false);
        ArmFeature arm = new ArmFeature();
        registerFeature(drivetrain);
        registerFeature(arm);
    }

    @Override
    public void run() {

    }
}