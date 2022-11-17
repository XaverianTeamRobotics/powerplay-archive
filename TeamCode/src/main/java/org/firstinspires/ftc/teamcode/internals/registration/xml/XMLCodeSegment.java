package org.firstinspires.ftc.teamcode.internals.registration.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class XMLCodeSegment {
    public Node node;

    public XMLCodeSegment(Node node) {
        this.node = node;
    }

    public ArrayList<XMLCodeLine> getContainedCode() {
        NodeList list = node.getChildNodes();
        ArrayList<XMLCodeLine> segments = new ArrayList<>();
        for (int i = 0; i < list.getLength(); i++) {
            segments.add(new XMLCodeLine(list.item(i)));
        }

        return segments;
    }
}
