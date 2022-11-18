package org.firstinspires.ftc.teamcode.internals.registration.xml;

import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

import java.util.ArrayList;

public class XMLOpModeTemplate extends OperationMode {
    public XMLRoboscriptParser roboscriptParser;
    public XMLCodeSegment codeBlock;
    public XmlEvironment environment = new XmlEvironment();
    public ArrayList<XMLCodeLine> xmlCodeLines;

    public XMLOpModeTemplate(XMLRoboscriptParser roboscriptParser) {
        System.out.println("[XML] - Constructing XMLOpModeTemplate");
        this.roboscriptParser = roboscriptParser;
        this.codeBlock = new XMLCodeSegment(roboscriptParser.getCodeBlock());
        this.xmlCodeLines = codeBlock.getContainedCode();
    }

    @Override
    public void construct() {
        for (XMLCodeLine n: codeBlock.getContainedCode()) {
            n.runAction(environment);
            xmlCodeLines.remove(n);
        }
    }

    @Override
    public void run() {

    }
}
