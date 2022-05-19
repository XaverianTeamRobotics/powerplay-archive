package org.firstinspires.ftc.teamcode.main.scripts.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "DriveIt")
public class DriveIt extends LinearOpMode {

    private DcMotor FrontRight;
    private DcMotor BackRight;
    private DcMotor FrontLeft;
    private DcMotor BackLeft;
    private int driveDirection = 0;

    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        float vertical;
        float horizontal;
        float pivot;

        FrontRight = hardwareMap.get(DcMotor.class, "Front Right");
        BackRight = hardwareMap.get(DcMotor.class, "Back Right");
        FrontLeft = hardwareMap.get(DcMotor.class, "Front Left");
        BackLeft = hardwareMap.get(DcMotor.class, "Back Left");

        // Put initialization blocks here.
        FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        BackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            resetDriveSwitch();
            while (opModeIsActive()) {
                // Put loop blocks here.
                vertical = gamepad1.right_stick_y;
                horizontal = -gamepad1.right_stick_x;
                pivot = -gamepad1.left_stick_x;
                FrontRight.setPower(-pivot + (vertical - horizontal));
                BackRight.setPower(-pivot + vertical + horizontal);
                FrontLeft.setPower(pivot + vertical + horizontal);
                BackLeft.setPower(pivot + (vertical - horizontal));
                telemetry.addData("Vert", vertical);
                telemetry.addData("Horiz", horizontal);
                telemetry.addData("Pivot", pivot);
                telemetry.addData("Drive Direction", driveDirection);
                telemetry.update();
            }
        }
    }

    Runnable switchDriveDirection = new Runnable() {
        @Override
        public void run() {
            //
            while (true) {
                if (gamepad1.right_bumper) {
                    resetDriveSwitch();
                    while (gamepad1.right_bumper);
                    break;
                }
            }
        }

    };

    private void resetDriveSwitch() {
        sleep(500);
        new Thread(switchDriveDirection).start();
    }
}