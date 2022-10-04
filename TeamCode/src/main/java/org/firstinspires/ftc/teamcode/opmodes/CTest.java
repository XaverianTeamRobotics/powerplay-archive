package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.utils.hardware.Devices.*;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.utils.hardware.Devices;
import org.firstinspires.ftc.teamcode.utils.registration.OperationMode;
import org.firstinspires.ftc.teamcode.utils.registration.TeleOperation;

// fl2 fr0 bl3 br1 a1-0 a2-1 h1-2 freedom-3
@TeleOp(name="Mecanum Op-Mode", group="TeleOp")
public class CTest extends OperationMode implements TeleOperation {

    double x, y, rx, h, a, f;

    @Override
    public void construct() {
        motor2.getMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor3.getMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motor3.getMotor().setDirection(DcMotorSimple.Direction.REVERSE); //front right
        motor2.getMotor().setDirection(DcMotorSimple.Direction.REVERSE); // back right

    }

    @Override
    public void run() {
        y = Devices.getGamepad1().getLeftStickY();
        x = -Devices.getGamepad1().getLeftStickX();
        rx = Devices.getGamepad1().getRightStickX();
        double h1 = Devices.getGamepad1().getA() ? 0.3 : 0;
        double h2 = Devices.getGamepad1().getB() ? -0.7 : 0;
        h = h1 + h2;
        a = Devices.getGamepad1().getRightTrigger() - Devices.getGamepad1().getLeftTrigger();
        double f1 = gamepad1.right_bumper ? 0.2 : 0;
        double f2 = gamepad1.left_bumper ? -0.5 : 0;
        f = f1 + f2;

        double fineMotorMod = gamepad1.x ? 0.2 : 1;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]

        // ugh that explanation above makes NO sense so here it is in simple thomas ricci is not smart enough to
        // understand it terms: I have NO idea what going on he- no just kidding.
        //
        // basically since we're working with ratios the denom is the highest motor value we possibly can have so
        // nothing is out of alignment

        // (for example, if you were driving at full speed forward but only half to the side, we need to
        // calculate the side as a fraction of the forward speed or else the robot would assume both sides are
        // going at full speed and That Is Bad.
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        motor1.setPower(((y + x + rx) / denominator) * fineMotorMod);
        motor0.setPower(((y - x + rx) / denominator) * fineMotorMod);
        motor3.setPower(((y - x - rx) / denominator) * fineMotorMod);
        motor2.setPower(((y + x - rx) / denominator) * fineMotorMod);

        motor0.setPower(a * fineMotorMod);
        motor1.setPower(-a * fineMotorMod) ;
        motor2.setPower(h * fineMotorMod);
        motor3.setPower(f * fineMotorMod);
    }
}