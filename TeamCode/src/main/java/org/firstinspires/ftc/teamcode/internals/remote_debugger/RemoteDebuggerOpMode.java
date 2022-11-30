package org.firstinspires.ftc.teamcode.internals.remote_debugger;

import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

import static org.firstinspires.ftc.teamcode.internals.hardware.Devices.initializeControlHubMotors;

public class RemoteDebuggerOpMode extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new WebServerFeature());
        initializeControlHubMotors();
    }

    @Override
    public void run() {

    }
}
