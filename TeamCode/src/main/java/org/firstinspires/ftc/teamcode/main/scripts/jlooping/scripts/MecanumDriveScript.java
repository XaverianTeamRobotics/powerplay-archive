package org.firstinspires.ftc.teamcode.main.scripts.jlooping.scripts;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.builtin.ConditionalScriptTemplate;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class MecanumDriveScript extends ConditionalScriptTemplate {
    private DcMotor FrontRight;
    private DcMotor BackRight;
    private DcMotor FrontLeft;
    private DcMotor BackLeft;
    private int driveDirection = 0;
    private LinearOpMode opMode;

    float vertical;
    float horizontal;
    float pivot;

    public MecanumDriveScript(String name, LinearOpMode opMode) {
        super(name, true);
        this.opMode = opMode;
    }

    @Override
    public void init(ScriptParameters parameters) {
        FrontRight = opMode.hardwareMap.get(DcMotor.class, "Front Right");
        BackRight = opMode.hardwareMap.get(DcMotor.class, "Back Right");
        FrontLeft = opMode.hardwareMap.get(DcMotor.class, "Front Left");
        BackLeft = opMode.hardwareMap.get(DcMotor.class, "Back Left");

        // Put initialization blocks here.
        FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        BackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        needsInit = false;
    }

    @Override
    public void toRun(ScriptParameters scriptParameters) {
        // Put loop blocks here.
        vertical = opMode.gamepad1.right_stick_y;
        horizontal = -opMode.gamepad1.right_stick_x;
        pivot = -opMode.gamepad1.left_stick_x;
        FrontRight.setPower(-pivot + (vertical - horizontal));
        BackRight.setPower(-pivot + vertical + horizontal);
        FrontLeft.setPower(pivot + vertical + horizontal);
        BackLeft.setPower(pivot + (vertical - horizontal));
        opMode.telemetry.addData("Vert", vertical);
        opMode.telemetry.addData("Horiz", horizontal);
        opMode.telemetry.addData("Pivot", pivot);
        opMode.telemetry.addData("Drive Direction", driveDirection);
        opMode.telemetry.update();
    }

    @Override
    public boolean shouldRun(ScriptParameters scriptParameters) {
        return opMode.opModeIsActive() && opMode.isStarted();
    }

    Runnable switchDriveDirection = new Runnable() {
        @Override
        public void run() {
            //
            while (true) {
                if (opMode.gamepad1.right_bumper) {
                    resetDriveSwitch();
                    while (opMode.gamepad1.right_bumper);
                    break;
                }
            }
        }

    };

    private void resetDriveSwitch() {
        opMode.sleep(500);
        new Thread(switchDriveDirection).start();
    }
}
