package org.firstinspires.ftc.teamcode.opmodes;

import com.michaell.looping.ScriptRunner;
import org.firstinspires.ftc.teamcode.features.DrivetrainMapMode;
import org.firstinspires.ftc.teamcode.features.MecanumDrivetrainFeature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

import static org.firstinspires.ftc.teamcode.features.BlankFeature.registerFeature;
import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.*;


public class MecanumTeleOpWithArmForBigRobot extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        Devices.initializeExpansionHubMotors();
        MecanumDrivetrainFeature drivetrain = new MecanumDrivetrainFeature(DrivetrainMapMode.FR_BR_FL_BL, true);

        try {
            registerFeature(drivetrain);
        } catch (ScriptRunner.DuplicateScriptException e) {
            e.printStackTrace();
            stop();
        }
    }

    @Override
    public void run() {
        double h1 = controller1.getA() ? 0.3 : 0;
        double h2 = controller1.getB() ? -0.7 : 0;
        double h = h1 + h2;
        double a1 = controller1.getRightTrigger();
        double a2 = -controller1.getLeftTrigger();
        double a = a1 + a2;
        double f1 = controller1.getRightBumper() ? 0.2 : 0;
        double f2 = controller1.getLeftBumper() ? -0.5 : 0;
        double f = f1 + f2;

        double fineMotorMod = controller1.getX() ? 0.2 : 1;

        motor0.setPower(a * fineMotorMod); // arm 1
        motor1.setPower(-a * fineMotorMod); // arm 2
        motor2.setPower(h * fineMotorMod); // hand
        motor3.setPower(f * fineMotorMod); // freedom
    }
}