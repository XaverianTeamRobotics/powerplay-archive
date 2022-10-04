package org.firstinspires.ftc.teamcode.internals.image;

import org.firstinspires.ftc.teamcode.internals.hardware.Logging;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import static org.opencv.core.Core.inRange;
import static org.opencv.core.Core.sumElems;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class SleeveColorDetection extends OpenCvPipeline {
    Mat processedMat = new Mat();

    @Override
    public Mat processFrame(Mat input) {
        cvtColor(input, processedMat, Imgproc.COLOR_RGB2HSV);

        Scalar redLowHSV = new Scalar(0, 53, 73);
        Scalar redHighHSV = new Scalar(20, 255, 255);

        inRange(processedMat, redLowHSV, redHighHSV, processedMat);

        double sum = sumElems(processedMat).val[0];
        double area = processedMat.rows() * processedMat.cols();

        double averageValue = sum / area / 255;

        // Log all values used in calculation for verification purposes
        Logging.logData("Red - value", averageValue * 100 + "%");
        Logging.logData("Red - redLowHSV", redLowHSV);
        Logging.logData("Red - redHighHSV", redHighHSV);

        Logging.logData("Red - sum", sum);
        Logging.logData("Red - area", area);
        Logging.updateLog();

        return processedMat;
    }
}
