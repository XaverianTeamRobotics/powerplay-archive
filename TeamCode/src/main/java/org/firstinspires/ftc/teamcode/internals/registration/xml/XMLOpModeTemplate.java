package org.firstinspires.ftc.teamcode.internals.registration.xml;

import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

public class XMLOpModeTemplate extends OperationMode {
    public XMLRoboscriptParser roboscriptParser;

    public XMLOpModeTemplate(XMLRoboscriptParser roboscriptParser) {
        super();
        this.roboscriptParser = roboscriptParser;
    }

    @Override
    public void construct() {
        // TODO: Make it do things....
        telemetry.addLine("XML OpMode active");
        telemetry.update();
    }

    @Override
    public void run() {

    }
}
