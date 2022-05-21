package org.firstinspires.ftc.teamcode.main.scripts.jlooping.requests.imgproc;

import com.michaell.looping.ScriptParameters;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.main.utils.autonomous.image.ObjectDetector;

import java.util.List;

public class ObjectDetectionRequest extends ScriptParameters.Request {
    public ObjectDetector detector;

    public ObjectDetectionRequest(ObjectDetector detector) {
        this.detector = detector;
    }

    @Override
    public Object issueRequest(Object o) {
        return detector.getUpdatedRecognitions();
    }

    @Override
    public Class getOutputType() {
        return List.class;
    }

    @Override
    public Class getInputType() {
        return null;
    }
}
