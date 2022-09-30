package org.firstinspires.ftc.teamcode.opmodes;

import com.michaell.looping.ScriptRunner;
import org.firstinspires.ftc.teamcode.features.DrivetrainMapMode;
import org.firstinspires.ftc.teamcode.features.MecanumDrivetrainFeature;
import org.firstinspires.ftc.teamcode.utils.hardware.Devices;
import org.firstinspires.ftc.teamcode.utils.registration.OperationMode;
import org.firstinspires.ftc.teamcode.utils.registration.TeleOperation;

import static org.firstinspires.ftc.teamcode.features.BlankFeature.registerFeature;


public class FieldCentricMecanumTeleOpNoArmBigRobot extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        Devices.initializeExpansionHubMotors();
        Devices.initializeIntegratedIMU();
        MecanumDrivetrainFeature drivetrain = new MecanumDrivetrainFeature(DrivetrainMapMode.FR_BR_FL_BL, true, true);

        try {
            registerFeature(drivetrain);
        } catch (ScriptRunner.DuplicateScriptException e) {
            e.printStackTrace();
            stop();
        }
    }

    @Override
    public void run() {

    }
}