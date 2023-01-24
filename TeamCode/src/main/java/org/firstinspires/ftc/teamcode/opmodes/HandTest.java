package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.features.Hand;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class HandTest extends OperationMode implements TeleOperation {

    @Override
    public void construct() {
        registerFeature(new Hand());
    }

    @Override
    public void run() {

    }

}
