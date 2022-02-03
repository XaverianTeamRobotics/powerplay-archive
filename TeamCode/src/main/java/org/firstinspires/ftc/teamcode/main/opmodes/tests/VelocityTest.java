package org.firstinspires.ftc.teamcode.main.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.main.utils.interactions.items.StandardIMU;

import java.util.concurrent.TimeUnit;

@Autonomous(name="Velocity Test", group="tests")
public class VelocityTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        StandardIMU imu = new StandardIMU(hardwareMap);
        while (!isStopRequested()) {
            StandardIMU.VelocityReturnData vel = imu.getVelocity();
            telemetry.addData("VELOCITY", "");
            telemetry.addData("  X", vel.getX());
            telemetry.addData("  Y", vel.getY());
            telemetry.addData("  Z", vel.getZ());

            StandardIMU.VelocityReturnData ang = imu.getAngularVelocity();
            telemetry.addData("ANGULAR VELOCITY", "");
            telemetry.addData("  X", ang.getX());
            telemetry.addData("  Y", ang.getY());
            telemetry.addData("  Z", ang.getZ());

            telemetry.update();
        }
    }
}
