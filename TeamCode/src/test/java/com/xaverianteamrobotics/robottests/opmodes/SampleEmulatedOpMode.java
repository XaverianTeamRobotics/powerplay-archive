package com.xaverianteamrobotics.robottests.opmodes;

import com.xaverianteamrobotics.robottests.EmulatedOpMode;
import org.firstinspires.ftc.teamcode.hardware.emulated.EmulatedGamepadRequest;

public class SampleEmulatedOpMode extends EmulatedOpMode {
    @Override
    public void construct() {
        System.out.println("EmulatedOpMode Constructed");
    }

    @Override
    public void run() {
        System.out.println("EmulatedOpMode ran");
        requestOpModeStop();
    }
}
