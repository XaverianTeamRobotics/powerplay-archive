package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.ArmFeature;
import org.firstinspires.ftc.teamcode.features.HandFeature;
import org.firstinspires.ftc.teamcode.features.MecanumDrivetrainFeature;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class Scrimmage extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        MecanumDrivetrainFeature drivetrain = new MecanumDrivetrainFeature();
        ArmFeature arm = new ArmFeature();
        HandFeature hand = new HandFeature();
        registerFeature(drivetrain);
        registerFeature(arm);
        registerFeature(hand);
    }

    @Override
    public void run() {

    }

}
