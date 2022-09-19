package com.xaverianteamrobotics.robottests.opmodes;

import com.xaverianteamrobotics.robottests.EmulatedOperationMode;

import org.firstinspires.ftc.teamcode.hardware.Devices;

public class EmulatedMotorTestOpMode extends EmulatedOperationMode {
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
