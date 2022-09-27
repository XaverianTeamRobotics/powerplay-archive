package org.firstinspires.ftc.teamcode.features;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.ScriptRunner;
import org.firstinspires.ftc.teamcode.utils.hardware.Devices;
import org.firstinspires.ftc.teamcode.utils.hardware.accessors.GlobalMotorAccess;
import org.firstinspires.ftc.teamcode.utils.registration.OperationMode;

import static org.firstinspires.ftc.teamcode.utils.hardware.Devices.*;

public class MecanumDrivetrainFeature extends BlankFeature {
    public MecanumDrivetrainFeature() {
        super("MecanumOpMode", false);
    }

    @Override
    public void run(ScriptParameters scriptParameters) {
        double y = -gamepad1.getLeftStickY(); // Remember, this is reversed!
        double x = gamepad1.getRightStickX() * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.getRightStickX();

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        motor0.setPower(backLeftPower);
        motor1.setPower(frontLeftPower);
        motor2.setPower(backRightPower);
        motor3.setPower(frontRightPower);
    }

    @Override
    public void addToOpMode(OperationMode opMode) throws ScriptRunner.DuplicateScriptException {
        opMode.environment.runner.addScript(this);
    }
}

