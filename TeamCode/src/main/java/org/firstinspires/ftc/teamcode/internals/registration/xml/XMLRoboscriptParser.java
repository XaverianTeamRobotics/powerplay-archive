package org.firstinspires.ftc.teamcode.internals.registration.xml;

import android.content.res.AssetManager;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.xml.parsers.*;

public class XMLRoboscriptParser {

    public String filePath;
    private static AssetManager am;
    public Document xmlDocument;
    public Element rootElement;

    public static AssetManager getAm() {
        return am;
    }

    public static void setAm(AssetManager am) {
        XMLRoboscriptParser.am = am;
    }

    public static ArrayList<String> getRoboscriptXMLFiles() {
        AssetManager am = FtcRobotControllerActivity.getAppContext().getAssets();

        ArrayList<String> fileNames;

        try {
            fileNames = (ArrayList<String>) Arrays.asList(am.list("roboscript"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String f:
             fileNames) {
            if (!f.contains(".xml")) fileNames.remove(f);
        }

        return fileNames;
    }

    public XMLRoboscriptParser(String filePath) {
        this.filePath = filePath;
        if (!getRoboscriptXMLFiles().contains(filePath)) {
            throw new RuntimeException(filePath + " not found");
        }
        am = Objects.requireNonNull(HardwareGetter.getHardwareMap()).appContext.getAssets();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder builder = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        File file = new File("test.xml");
        Document doc;
        try {
            doc = builder.parse(file);
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        xmlDocument = doc;
        rootElement = xmlDocument.getDocumentElement();
    }

    public boolean isTeleOp() {
        if (rootElement.getAttribute("isTeleOp") == "1") {
            return true;
        } else if (rootElement.getAttribute("isTeleOp") == "0") {
            return false;
        } else {
            throw new RuntimeException("Invalid XML Data");
        }
    }

    public String getGroup() {
        return rootElement.getElementsByTagName("group").item(0).getTextContent();
    }

    public String getName() {
        return rootElement.getElementsByTagName("name").item(0).getTextContent();
    }

    public Node getInit() {
        return rootElement.getElementsByTagName("init").item(0);
    }

    public Node getLoop() {
        return rootElement.getElementsByTagName("loop").item(0);
    }
}
