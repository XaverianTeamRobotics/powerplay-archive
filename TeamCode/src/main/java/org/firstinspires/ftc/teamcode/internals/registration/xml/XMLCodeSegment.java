package org.firstinspires.ftc.teamcode.internals.registration.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class XMLCodeSegment {
    public Node node;
    public SegmentType segmentType;

    public XMLCodeSegment(Node node) {
        this.node = node;
        segmentType = SegmentType.fromTagName(node.getNodeName());
    }

    public enum SegmentType {
        INIT_CONTAINER,
        LOOP_CONTAINER,
        CODE_BLOCK;

        public static SegmentType fromTagName(String tagName) {
            switch (tagName) {
                case "init":
                    return INIT_CONTAINER;
                case "loop":
                    return LOOP_CONTAINER;
                case "code":
                    return CODE_BLOCK;
                default:
                    throw new RuntimeException("Unknown tag name: " + tagName);
            }
        }
    }

    public ArrayList<XMLCodeSegment> getContainedCode() throws WrongSegmentTypeException {
        if (segmentType == SegmentType.CODE_BLOCK) {
            throw new WrongSegmentTypeException("Cannot get contained code from a code segment");
        }
        NodeList list = node.getChildNodes();
        ArrayList<XMLCodeSegment> segments = new ArrayList<>();
        for (int i = 0; i < list.getLength(); i++) {
            segments.add(new XMLCodeSegment(list.item(i)));
        }

        return segments;
    }

    public class WrongSegmentTypeException extends RuntimeException {
        public WrongSegmentTypeException(String message) {
            super(message);
        }
    }
}
