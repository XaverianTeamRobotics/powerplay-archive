package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.ArmFeature;
import org.firstinspires.ftc.teamcode.features.MecanumDrivetrainFeature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class Scrimmage extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        Devices.initializeControlHubMotors();
        Devices.initializeArmMotors();
        MecanumDrivetrainFeature drivetrain = new MecanumDrivetrainFeature();
        ArmFeature arm = new ArmFeature();
        registerFeature(drivetrain);
        registerFeature(arm);
    }

    @Override
    public void run() {

    }

}
