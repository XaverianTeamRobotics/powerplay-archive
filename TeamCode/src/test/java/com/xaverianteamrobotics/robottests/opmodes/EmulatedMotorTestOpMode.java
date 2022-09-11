package com.xaverianteamrobotics.robottests.opmodes;

import com.xaverianteamrobotics.robottests.EmulatedOpMode;

import org.firstinspires.ftc.teamcode.hardware.Devices;
import org.firstinspires.ftc.teamcode.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.hardware.physical.MotorOperation;
import org.firstinspires.ftc.teamcode.hardware.physical.StandardMotorParameters;

public class EmulatedMotorTestOpMode extends EmulatedOpMode {
    @Override
    public void construct() {
        setTimeUntillAbort(5);
    }

    @Override
    public void run() {
        Devices.getMotor0().setPower(1.0);
        sleep(1000);
    }
}
