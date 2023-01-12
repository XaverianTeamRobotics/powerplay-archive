package org.firstinspires.ftc.teamcode.internals.remote_debugger;

import org.firstinspires.ftc.teamcode.features.WebServerFeature;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class RemoteDebuggerOpMode extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new WebServerFeature());
    }

    @Override
    public void run() {

    }
}
