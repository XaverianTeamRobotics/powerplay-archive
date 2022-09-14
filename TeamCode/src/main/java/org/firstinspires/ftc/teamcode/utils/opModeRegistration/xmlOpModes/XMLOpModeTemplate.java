package org.firstinspires.ftc.teamcode.utils.opModeRegistration.xmlOpModes;

import org.firstinspires.ftc.teamcode.utils.opModeRegistration.OperationMode;

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
