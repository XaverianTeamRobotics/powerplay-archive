package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.features.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.features.PoseResetter;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DSLogging;

public class SmallbotProductionOdo extends OperationMode implements TeleOperation {

    DcMotorEx motor;

    @Override
    public void construct() {
        registerFeature(new MecanumDrivetrain(true, false));
//        registerFeature(new FourMotorArm());
//        registerFeature(new Hand());
        registerFeature(new PoseResetter());
        // debug
        motor = HardwareGetter.getHardwareMap().get(DcMotorEx.class, Devices.motor5.getName());
    }

    @Override
    public void run() {
        DSLogging.log(motor.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION).toString());
    }

}
