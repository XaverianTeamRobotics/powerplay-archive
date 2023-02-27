package org.firstinspires.ftc.teamcode.internals.image;

import com.acmerobotics.dashboard.FtcDashboard;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.AdvancedLogging;
import org.opencv.core.*;
import org.opencv.features2d.SimpleBlobDetector;
import org.opencv.features2d.SimpleBlobDetector_Params;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Objects;

import static org.firstinspires.ftc.teamcode.internals.image.ImageProcessingConstants.*;
import static org.opencv.core.Core.inRange;

public class PoleNavigator extends OpenCvPipeline {
    private double poleDistanceX, poleDistanceY, poleDistance;
    private int width = 0, height = 0; // Is set after first run of #processFrame(Mat), call that before accessing these!
    @Override
    public Mat processFrame(Mat input) {

        width = input.width();
        height = input.height();

        // Blur the image to reduce noise
        Size blurSize = new Size(55.0,55.0);
        Imgproc.GaussianBlur(input, input, blurSize, 0.0);

        // Get the area withing BLACK_MIN and BLACK_MAX
        Mat filter = new Mat();
        inRange(
                input,
                new Scalar(BLACK_R_MIN, BLACK_G_MIN, BLACK_B_MIN),
                new Scalar(BLACK_R_MAX, BLACK_G_MAX, BLACK_B_MAX),
                filter);

        // Before the mask can be applied, we need to INVERT the image so that the black pole becomes white
        Core.invert(input, input);

        Core.bitwise_and(input, filter, input);
        filter.release();

        // Detect white blobs
        SimpleBlobDetector_Params blobDetectorParams = new SimpleBlobDetector_Params();
        blobDetectorParams.set_filterByColor(true);
        blobDetectorParams.set_filterByArea(true);
        blobDetectorParams.set_minArea(25*25);
        blobDetectorParams.set_maxArea(75*75);
        SimpleBlobDetector detector = SimpleBlobDetector.create(blobDetectorParams);
        MatOfKeyPoint detections = new MatOfKeyPoint();
        detector.detect(input, detections);

        // TODO: For now, draw blobs. Might remove when done developing
        for (KeyPoint kpt :
                detections.toArray()) {
            Imgproc.rectangle(
                    input,
                    new Rect(kpt.pt, new Size(kpt.size, kpt.size)),
                    new Scalar(255, 255, 255));
        }

        // Find the keypoint closest to the center of the image
        KeyPoint closestKPT = null;
        double closestKPT_dist = 1000000000; // Really big number
        Point center = new Point(input.width()/2.0, input.height()/2.0);
        for (KeyPoint kpt :
                detections.toArray()) {
            double dist = Math.sqrt(Math.pow(kpt.pt.x - center.x, 2) + Math.pow(kpt.pt.y - center.y, 2));
            if (dist < closestKPT_dist) {
                closestKPT = kpt;
                closestKPT_dist = dist;
            }
        }

        // Put the distance from the center in a global static variable
        if (closestKPT != null) {
            poleDistanceX = closestKPT.pt.x - center.x;
            poleDistanceY = closestKPT.pt.y - center.y;
            poleDistance = closestKPT_dist;
        }

        return input;
    }

    /**
     * @return The distance from the pole to the center of the camera in the X direction. No specific units whatsoever
     * Positive value means the pole is to the RIGHT of the camera, negative is LEFT
     */
    public double getPoleDistanceX() { return poleDistanceX; }

    /**
     * @return The distance from the pole to the center of the camera in the Y direction. No specific units whatsoever
     * Positive value means the pole is to the SOUTH of the camera, negative is NORTH
     */
    public double getPoleDistanceY() { return poleDistanceY; }

    /**
     * @return Get the overall distance from the center of the camera to the pole. Always positive.
     */
    public double getPoleDistance() { return poleDistance; }

    /**
     * @return An array containing the width and height (in that order) of the most recent {@link Mat} passed to {@link #processFrame(Mat)}.
     */
    public int[] getSize() { return new int[] { width, height }; }

    public void startStreaming() {
        int cameraMonitorViewId = Objects.requireNonNull(HardwareGetter.getHardwareMap()).appContext.getResources().getIdentifier("cameraMonitorViewId", "id", HardwareGetter.getHardwareMap().appContext.getPackageName());
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(Devices.camera, cameraMonitorViewId); // TODO: Implement second camera

        camera.setPipeline(this);
        FtcDashboard.getInstance().startCameraStream(camera, 0);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened() {
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */

                AdvancedLogging.logData("Camera error", errorCode);
                AdvancedLogging.update();
            }
        });
    }
}
