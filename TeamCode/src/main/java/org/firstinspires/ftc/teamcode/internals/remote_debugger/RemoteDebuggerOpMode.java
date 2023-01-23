package org.firstinspires.ftc.teamcode.internals.remote_debugger;

import org.firstinspires.ftc.teamcode.features.Debugger;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class RemoteDebuggerOpMode extends OperationMode implements TeleOperation {
    @Override
    public void construct() {
        registerFeature(new Debugger());
    }

    @Override
    public void run() {

    }
}
