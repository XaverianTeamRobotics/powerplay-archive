package com.xaverianteamrobotics.robottests.opmodes;

import com.michaell.looping.ScriptParameters;
import com.xaverianteamrobotics.robottests.EmulatedOpMode;

import org.firstinspires.ftc.teamcode.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.hardware.physical.StandardMotorRequest;

public class EmulatedMotorTestOpMode extends EmulatedOpMode {
    @Override
    public void construct() {
        HardwareGetter.getStandardMotorRequest("motor1");
        setTimeUntillAbort(5);
    }

    @Override
    public void run() {
        try {
            environment.issueRequest(new StandardMotorRequest.StandardMotorParameters(1.0), environment.getRequest("motor1"));
            sleep(1000);
        } catch (ScriptParameters.InvalidParametersException | ScriptParameters.RequestNotFoundException e) {
            e.printStackTrace();
        }
    }
}
