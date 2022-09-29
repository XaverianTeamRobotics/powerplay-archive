package org.firstinspires.ftc.teamcode.opmodes;

import com.michaell.looping.ScriptRunner;
import org.firstinspires.ftc.teamcode.features.MecanumDrivetrainFeature;
import org.firstinspires.ftc.teamcode.utils.registration.OperationMode;
import org.firstinspires.ftc.teamcode.utils.registration.TeleOperation;

import static org.firstinspires.ftc.teamcode.features.BlankFeature.registerFeature;


public class MecanumTeleOp extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        MecanumDrivetrainFeature drivetrain = new MecanumDrivetrainFeature();

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