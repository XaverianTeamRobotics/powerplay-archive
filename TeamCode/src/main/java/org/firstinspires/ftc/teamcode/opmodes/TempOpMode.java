package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "robot_in_two_weeks")
public class TempOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        DcMotor arm = hardwareMap.dcMotor.get("arm");
        DcMotor hand = hardwareMap.dcMotor.get("hand");
        DcMotor freedom = hardwareMap.dcMotor.get("freedom");


        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;
            double h1 = gamepad1.a ? 0.5 : 0;
            double h2 = gamepad1.b ? -0.5 : 0;
            double h = h1 - h2;
            double a1 = gamepad1.right_trigger;
            double a2 = -gamepad1.left_trigger;
            double a = a1 - a2;
            double f1 = gamepad1.right_bumper ? 0.5 : 0;
            double f2 = gamepad1.left_bumper ? -0.5 : 0;
            double f = f1 - f2;

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
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

            arm.setPower(a);
            hand.setPower(h);
            freedom.setPower(f);
        }
    }
}
