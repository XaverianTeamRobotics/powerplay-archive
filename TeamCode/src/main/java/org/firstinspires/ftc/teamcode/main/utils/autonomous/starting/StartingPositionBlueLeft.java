package org.firstinspires.ftc.teamcode.main.utils.autonomous.starting;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.main.utils.autonomous.EncoderTimeoutManager;
import org.firstinspires.ftc.teamcode.main.utils.autonomous.location.pipeline.PositionSystem;
import org.firstinspires.ftc.teamcode.main.utils.geometry.Angle;
import org.firstinspires.ftc.teamcode.main.utils.interactions.groups.StandardTankVehicleDrivetrain;
import org.firstinspires.ftc.teamcode.main.utils.io.InputSpace;
import org.firstinspires.ftc.teamcode.main.utils.resources.Resources;

@Autonomous(name = "Starting Position Blue Left")
public class StartingPositionBlueLeft extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        new StartingPositionManager(this, true, false, 3);
    }

}
