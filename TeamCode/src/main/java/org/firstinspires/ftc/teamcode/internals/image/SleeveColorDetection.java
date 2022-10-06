package org.firstinspires.ftc.teamcode.internals.image;

import org.firstinspires.ftc.teamcode.internals.hardware.Logging;
import org.opencv.core.*;
import org.opencv.features2d.SimpleBlobDetector;
import org.opencv.features2d.SimpleBlobDetector_Params;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.lang.reflect.Array;

import static org.opencv.core.Core.*;
import static org.opencv.features2d.Features2d.drawKeypoints;
import static org.opencv.imgproc.Imgproc.*;

public class SleeveColorDetection extends OpenCvPipeline {
    private volatile int detection = 0;

    @Override
    public void init(Mat mat) {}

    @Override
    public Mat processFrame(Mat input) {
        Logging.logData("Input Height", input.height());
        Logging.logData("Input Width", input.width());

        double cropRatio = ImageProcessingConstants.CROP_RATIO;

        // Crop part of the image to focus the center of the image
        Rect roi = new Rect(    new Point(input.width()*cropRatio, input.height()*cropRatio),
                                new Point(input.width()*(1-cropRatio), input.height()*(1-cropRatio)));

        Mat processedMat = input.submat(roi);

        // Blur both the image to reduce noise
        Size blurSize = new Size(ImageProcessingConstants.GAUSSIAN_BLUR_SIZE, ImageProcessingConstants.GAUSSIAN_BLUR_SIZE);
        GaussianBlur(processedMat, processedMat, blurSize, 0);

        // Convert to grayscale
        Mat grayScale = processedMat.clone();
        cvtColor(grayScale, grayScale, Imgproc.COLOR_RGB2GRAY);

        cvtColor(processedMat, processedMat, Imgproc.COLOR_RGB2HSV);

        // Define the colors that we want to detect
        Scalar redLowHSV = new Scalar(ImageProcessingConstants.RED_H_MIN, ImageProcessingConstants.RED_S_MIN, ImageProcessingConstants.RED_V_MIN);
        Scalar redHighHSV = new Scalar(ImageProcessingConstants.RED_H_MAX, ImageProcessingConstants.RED_S_MAX, ImageProcessingConstants.RED_V_MAX);

        Scalar blueLowHSV = new Scalar(ImageProcessingConstants.BLUE_H_MIN, ImageProcessingConstants.BLUE_S_MIN, ImageProcessingConstants.BLUE_V_MIN);
        Scalar blueHighHSV = new Scalar(ImageProcessingConstants.BLUE_H_MAX, ImageProcessingConstants.BLUE_S_MAX, ImageProcessingConstants.BLUE_V_MAX);

        Scalar greenLowHSV = new Scalar(ImageProcessingConstants.GREEN_H_MIN, ImageProcessingConstants.GREEN_S_MIN, ImageProcessingConstants.GREEN_V_MIN);
        Scalar greenHighHSV = new Scalar(ImageProcessingConstants.GREEN_H_MAX, ImageProcessingConstants.GREEN_S_MAX, ImageProcessingConstants.GREEN_V_MAX);

        autoCalibrateBackgroundFilter(grayScale);

        // Remove the background from the gray image and just get the cone to use as a mask and then convert back to hsv
        inRange(
            grayScale,
            new Scalar(ImageProcessingConstants.GRAY_MIN),
            new Scalar(ImageProcessingConstants.GRAY_MAX),
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
            Logging.logData("Sleeve Color", "Red");
            detection = 1;
        } else if (largestVal == greenAverage) {
            Logging.logData("Sleeve Color", "Green");
            detection = 2;
        } else if (largestVal == blueAverage) {
            Logging.logData("Sleeve Color", "Blue");
            detection = 3;
        } else {
            Logging.logData("Sleeve Color", "Unknown");
            detection = 0;
        }

        Logging.updateLog();

        if (ImageProcessingConstants.RETURN_GRAYSCALE) {
            processedMat.release();
            return grayScale; // Useful for tuning the background filtering
        } else {
            grayScale.release();
            cvtColor(processedMat, processedMat, COLOR_HSV2RGB); // Convert back to RGB for display
            return processedMat; // Useful for previewing the final output
        }
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
        Logging.logData(name+" - Average", averageValue * 100 + "%");
        Logging.updateLog();

        return averageValue;
    }

    /**
     * Attempts to automatically find the GRAY_MIN and GRAY_MAX values for the background filtering
     * Works by trying to change the values until only one blob is found
     * Requires proper values for GRAYSCALE_BLOB_MIN_AREA and GRAYSCALE_BLOB_MAX_AREA
     * <br>
     * <br>
     * This method will automatically change the constants in the ImageProcessingConstants class. No value will be returned
     * @param input The grayscale image to process
     */
    public void autoCalibrateBackgroundFilter(Mat input) {
        int iterations = 0;
        while (iterations <= ImageProcessingConstants.MAX_BACKGROUND_FILTER_ADJUSTMENT_ITERATIONS) {
            iterations++;
            Mat mat = input.clone();
            // Remove the background from the gray image
            inRange(
                mat,
                new Scalar(ImageProcessingConstants.GRAY_MIN),
                new Scalar(ImageProcessingConstants.GRAY_MAX),
                mat
            );
            MatOfKeyPoint keypoints = new MatOfKeyPoint();
            SimpleBlobDetector_Params grayscaleBlobParameters = new SimpleBlobDetector_Params();
            grayscaleBlobParameters.set_minArea((float) ImageProcessingConstants.GRAYSCALE_BLOB_MIN_AREA);
            grayscaleBlobParameters.set_maxArea((float) ImageProcessingConstants.GRAYSCALE_BLOB_MAX_AREA);
            SimpleBlobDetector detector = SimpleBlobDetector.create();
            detector.detect(mat, keypoints);

            KeyPoint[] keypointsArray = keypoints.toArray();
            if (keypointsArray != null) {
                if (keypointsArray.length == 1) {
                    return;
                } else if (keypointsArray.length > 1) {
                    ImageProcessingConstants.GRAY_MIN += 5;
                    ImageProcessingConstants.GRAY_MAX -= 5;
                } else {
                    ImageProcessingConstants.GRAY_MIN -= 5;
                    ImageProcessingConstants.GRAY_MAX += 5;
                }
                Logging.logData("GRAY_MIN", ImageProcessingConstants.GRAY_MIN);
                Logging.logData("GRAY_MAX", ImageProcessingConstants.GRAY_MAX);
                Logging.updateLog();
            }
            mat.release();
            if (iterations >= ImageProcessingConstants.MAX_BACKGROUND_FILTER_ADJUSTMENT_ITERATIONS) return;
        }
    }

    public int getDetection() {
        return detection;
    }
}
