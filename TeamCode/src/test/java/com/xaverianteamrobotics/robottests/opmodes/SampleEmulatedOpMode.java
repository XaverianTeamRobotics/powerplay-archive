package com.xaverianteamrobotics.robottests.opmodes;

import com.xaverianteamrobotics.robottests.EmulatedOperationMode;

public class SampleEmulatedOpMode extends EmulatedOperationMode {
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
