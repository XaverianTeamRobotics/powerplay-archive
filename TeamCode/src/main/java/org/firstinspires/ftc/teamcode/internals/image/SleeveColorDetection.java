package org.firstinspires.ftc.teamcode.internals.image;

import org.firstinspires.ftc.teamcode.internals.telemetry.SafeLogging;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import static org.firstinspires.ftc.teamcode.internals.image.ImageProcessingConstants.*;
import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class SleeveColorDetection extends OpenCvPipeline {
    private volatile int detection = 0;
    private volatile boolean debugEnabled = false;

    @Override
    public void init(Mat mat) {}

    @Override
    public Mat processFrame(Mat input) {
        //Logging.logData("Input Height", input.height());
        //Logging.logData("Input Width", input.width());

        Mat processedMat, grayScale;

        double cropRatio = CROP_RATIO;

        // Crop part of the image to focus the center of the image
        Rect roi = new Rect(    new Point(input.width()*cropRatio, input.height()*cropRatio),
                                new Point(input.width()*(1-cropRatio), input.height()*(1-cropRatio)));

        processedMat = input.submat(roi).clone();

        // Blur both the image to reduce noise
        Size blurSize = new Size(GAUSSIAN_BLUR_SIZE, GAUSSIAN_BLUR_SIZE);
        GaussianBlur(processedMat, processedMat, blurSize, 0);

        // Convert to grayscale
        grayScale = processedMat.clone();
        cvtColor(grayScale, grayScale, Imgproc.COLOR_RGB2GRAY);

        cvtColor(processedMat, processedMat, Imgproc.COLOR_RGB2HSV);

        // Define the colors that we want to detect
        Scalar redLowHSV = new Scalar(RED_H_MIN, RED_S_MIN, RED_V_MIN);
        Scalar redHighHSV = new Scalar(RED_H_MAX, RED_S_MAX, RED_V_MAX);

        Scalar blueLowHSV = new Scalar(BLUE_H_MIN, BLUE_S_MIN, BLUE_V_MIN);
        Scalar blueHighHSV = new Scalar(BLUE_H_MAX, BLUE_S_MAX, BLUE_V_MAX);

        Scalar greenLowHSV = new Scalar(GREEN_H_MIN, GREEN_S_MIN, GREEN_V_MIN);
        Scalar greenHighHSV = new Scalar(GREEN_H_MAX, GREEN_S_MAX, GREEN_V_MAX);

        // Remove the background from the gray image and just get the cone to use as a mask and then convert back to hsv
        inRange(
            grayScale,
            new Scalar(GRAY_MIN),
            new Scalar(GRAY_MAX),
            grayScale
        );
        // Set the GRAY_MIN and GRAY_MAX values to 0 and 255, respectively, to disable the feature

        // Use the grayscale image with the background removed to get rid of the background in the color image
        cvtColor(grayScale, grayScale, COLOR_GRAY2RGB);
        cvtColor(processedMat, processedMat, COLOR_HSV2RGB);

        bitwise_and(processedMat, grayScale, processedMat);

        cvtColor(processedMat, processedMat, COLOR_RGB2HSV);

        // Find the average colors of the original image
        double redAverage = processForColor(processedMat, redLowHSV, redHighHSV, "red");
        double blueAverage = processForColor(processedMat, blueLowHSV, blueHighHSV, "blue");
        double greenAverage = processForColor(processedMat, greenLowHSV, greenHighHSV, "green");

        double largestVal = Math.max(redAverage, Math.max(blueAverage, greenAverage));
        if (largestVal == redAverage) {
            detection = 1;
        } else if (largestVal == greenAverage) {
            detection = 2;
        } else if (largestVal == blueAverage) {
            detection = 3;
        } else {
            detection = 0;
        }

        if (debugEnabled) {
            switch (detection) {
                case 1:
                    SafeLogging.logData("Detected Color", "Red");
                    break;
                case 2:
                    SafeLogging.logData("Detected Color", "Green");
                    break;
                case 3:
                    SafeLogging.logData("Detected Color", "Blue");
                    break;
                default:
                    SafeLogging.logData("Detected Color", "None");
                    break;
            }
            SafeLogging.update();
        }

        grayScale.release();

        cvtColor(processedMat, processedMat, COLOR_HSV2RGB); // Convert back to RGB for display
        input = processedMat.clone();
        processedMat.release();
        return input; // Useful for previewing the final output
    }

    /**
     * Processes the image for a specific color
     * @param input The image to process. In HSV Color Space
     * @param lowHSV The lower bound of the color to detect
     * @param highHSV The upper bound of the color to detect
     * @param name The name to print on the telemetry
     * @return The average value of the color in the image
     */
    public double processForColor(Mat input, Scalar lowHSV, Scalar highHSV, String name) {
        Mat mat = new Mat();
        inRange(input, lowHSV, highHSV, mat);

        double sum = sumElems(mat).val[0];
        double area = mat.rows() * mat.cols();

        double averageValue = sum / area / 255;

        // Log all values used in calculation for verification purposes
        if (debugEnabled) {
            SafeLogging.logData(name + " - Average", averageValue * 100 + "%");
            SafeLogging.update();
        }

        mat.release();

        return averageValue;
    }

    public int getDetection() {
        return detection;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }
}
