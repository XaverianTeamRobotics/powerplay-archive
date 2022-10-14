package org.firstinspires.ftc.teamcode.internals.image;

import org.firstinspires.ftc.teamcode.internals.hardware.Logging;
import org.opencv.core.*;
import org.opencv.features2d.SimpleBlobDetector;
import org.opencv.features2d.SimpleBlobDetector_Params;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import static org.firstinspires.ftc.teamcode.internals.image.ImageProcessingConstants.*;
import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class SleeveColorDetection extends OpenCvPipeline {
    private volatile int detection = 0;

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

        MatOfKeyPoint grayscaleBlobs = autoCalibrateBackgroundFilter(grayScale);

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

        grayScale.release();

        cvtColor(processedMat, processedMat, COLOR_HSV2RGB); // Convert back to RGB for display
        for (KeyPoint blob : grayscaleBlobs.toList()) {
            // Generate a circle given the points and size of the blob
            circle(processedMat, new Point(blob.pt.x, blob.pt.y), (int) blob.size/2, new Scalar(0, 255, 0), 3);
        }
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
        Logging.logData(name+" - Average", averageValue * 100 + "%");
        Logging.updateLog();

        mat.release();

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
     * @return The blobs found in the image after attepmting background filtering
     */
    public MatOfKeyPoint autoCalibrateBackgroundFilter(Mat input) {
        int iterations = 0;
        while (true) {
            iterations++;
            Mat mat = input.clone();
            // Remove the background from the gray image
            inRange(
                mat,
                new Scalar(GRAY_MIN),
                new Scalar(GRAY_MAX),
                mat
            );

            // Run blob detection on the binary mask
            // The goal is not to make the mask just the cone, but to adjust the boundaries so that the area AROUND the cone is removed
            // If we set the mask to be just the cone, there is the odd chance that opencv will detect something else as the cone
            // This helps if we choose to use zoom later
            // Again, proper calibration of GRAYSCALE_BLOB_MIN_AREA and GRAYSCALE_BLOB_MAX_AREA is required
            MatOfKeyPoint keypoints = new MatOfKeyPoint();
            SimpleBlobDetector_Params grayscaleBlobParameters = new SimpleBlobDetector_Params();
            grayscaleBlobParameters.set_minArea((float) GRAYSCALE_BLOB_MIN_AREA);
            grayscaleBlobParameters.set_maxArea((float) GRAYSCALE_BLOB_MAX_AREA);

            // Binary mask (our input) is either 0 or 255, so we need to set the threshold to 254 to get the blob detection to work for bright colors
            grayscaleBlobParameters.set_minThreshold(254);
            grayscaleBlobParameters.set_maxThreshold(255);

            // We only care about the area and color, so we can tell opencv to ignore the rest of the parameters
            grayscaleBlobParameters.set_filterByArea(true);
            grayscaleBlobParameters.set_filterByCircularity(false);
            grayscaleBlobParameters.set_filterByConvexity(false);
            grayscaleBlobParameters.set_filterByInertia(false);
            grayscaleBlobParameters.set_filterByColor(true);

            SimpleBlobDetector detector = SimpleBlobDetector.create(grayscaleBlobParameters);
            detector.detect(mat, keypoints);

            // If there is only one blob, we have found the correct values
            // For the GRAY_MIN and GRAY_MAX values, adjust them if there are too many or too few blobs
            KeyPoint[] keypointsArray = keypoints.toArray();
            if (keypointsArray != null) {
                if (keypointsArray.length == 1) {
                    return keypoints;
                } else if (keypointsArray.length > 1) {
                    // Decide whether or not we should increase the GRAY_MIN or decrease the GRAY_MAX
                    // If the GRAY_MIN is below 20, then we should do nothing.
                    if (GRAY_MIN >= 25) {
                        GRAY_MIN += 5;
                        GRAY_MAX -= 5;
                    }
                } else {
                    // Do the opposite of the above, but GRAY_MAX shouldnt go above 235
                    if (GRAY_MAX <= 230) {
                        GRAY_MIN -= 5;
                        GRAY_MAX += 5;
                    }
                }
            }
            // Force GRAY_MIN and GRAY_MAX to be within the range of 0-255, and the difference between GRAY_MIN and GRAY_MAX to be at least 50
            GRAY_MIN = Math.max(0, Math.min(205, GRAY_MIN));
            GRAY_MAX = Math.max(50, Math.min(255, GRAY_MAX));
            GRAY_MAX = Math.max(GRAY_MIN + 50, GRAY_MAX);
            GRAY_MIN = Math.min(GRAY_MAX - 50, GRAY_MIN);

            mat.release();
            if (iterations >= MAX_BACKGROUND_FILTER_ADJUSTMENT_ITERATIONS) return keypoints;
        }
    }

    public int getDetection() {
        return detection;
    }
}
