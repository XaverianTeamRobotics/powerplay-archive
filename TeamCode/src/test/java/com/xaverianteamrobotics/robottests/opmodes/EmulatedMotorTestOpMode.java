package com.xaverianteamrobotics.robottests.opmodes;

import com.xaverianteamrobotics.robottests.EmulatedOpMode;

import org.firstinspires.ftc.teamcode.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.hardware.physical.MotorOperation;
import org.firstinspires.ftc.teamcode.hardware.physical.StandardMotorParameters;

public class EmulatedMotorTestOpMode extends EmulatedOpMode {
    @Override
    public void construct() {
        HardwareGetter.makeMotorRequest("Motor1");
        setTimeUntillAbort(5);
    }

    @Override
    public void run() {
        HardwareGetter.setMotorValue("Motor1", new StandardMotorParameters(1.0,
            MotorOperation.POWER));
        sleep(1000);
    }
}
