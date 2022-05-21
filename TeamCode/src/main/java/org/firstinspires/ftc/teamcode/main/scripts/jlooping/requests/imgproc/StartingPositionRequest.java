package org.firstinspires.ftc.teamcode.main.scripts.jlooping.requests.imgproc;

import com.michaell.looping.ScriptParameters;

import org.firstinspires.ftc.teamcode.main.utils.autonomous.image.ObjectDetector;

import java.util.List;

public class StartingPositionRequest extends ScriptParameters.Request {
    public ObjectDetector detector;

    public StartingPositionRequest(ObjectDetector detector) {
        this.detector = detector;
    }

    @Override
    public Object issueRequest(Object o) {
        return detector.identifyStartingPos();
    }

    @Override
    public Class getOutputType() {
        return Integer.class;
    }

    @Override
    public Class getInputType() {
        return null;
    }
}
