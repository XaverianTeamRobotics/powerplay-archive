package org.firstinspires.ftc.teamcode.internals.image;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
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

@Config
public class PoleLocalizer extends OpenCvPipeline {

    private final boolean indexed;
    private final int index;
    private long time = 0;

    public PoleLocalizer(int index) {
        indexed = true;
        this.index = index;
    }

    public PoleLocalizer() {
        indexed = false;
        index = 0;
    }

    public static int minArea = 30, maxArea = 70;
    public static double minC = 0, maxC = 1, alpha = 5.25, beta = -560, lasagna = 1;

    private double poleDistanceX, poleDistanceY, poleDistance;
    private boolean data = false;
    private int width = 0, height = 0; // Is set after first run of #processFrame(Mat), call that before accessing these!

    @Override
    public Mat processFrame(Mat input) {

        width = input.width();
        height = input.height();

        // contrast & brightness
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2GRAY);
        input.convertTo(input, -1, alpha, beta);

        SimpleBlobDetector_Params blobDetectorParams = new SimpleBlobDetector_Params();
        blobDetectorParams.set_filterByCircularity(true);
        blobDetectorParams.set_filterByArea(true);
        blobDetectorParams.set_minArea(minArea*minArea);
        blobDetectorParams.set_maxArea(maxArea*maxArea);
        blobDetectorParams.set_maxCircularity((float) maxC);
        blobDetectorParams.set_minCircularity((float) minC);
        blobDetectorParams.set_filterByColor(true);
        SimpleBlobDetector detector = SimpleBlobDetector.create(blobDetectorParams);
        MatOfKeyPoint detections = new MatOfKeyPoint();
        detector.detect(input, detections);

        // draw found detections for debugging purposes
        for (KeyPoint kpt :
                detections.toArray()) {
            Imgproc.rectangle(
                    input,
                    new Rect(new Point(kpt.pt.x - 2, kpt.pt.y - 2), new Point(kpt.pt.x + 2, kpt.pt.y + 2)),
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
            double[] dists = findDistance(closestKPT, center);
            poleDistanceX = dists[0];
            poleDistanceY = dists[1];
            poleDistance = closestKPT_dist;
            data = true;
        }else{
//            poleDistNotFound();
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

    /**
     * @return An array containing the X, Y, width, and height (in that order) of the most recent frame processed.
     */
    public double[] getData() {
        if(data) {
            return new double[] { poleDistanceX, poleDistanceY, width, height };
        }else{
            return null;
        }
//        if(data) {
//            return new double[] { poleDistanceX, poleDistanceY, width, height, };
//        }else{
//            return null;
//        }
    }

    /**
     * Forcefully invalidate the cache. By default, the cache is invalidated every time new data is received or 1 second passes with no new data (in which case the cached data becomes filler). This method forces the cache to be invalidated and new data to be retrived, if possible.
     */
    public void invalidate() {
        data = false;
    }

    public void startStreaming() {
        int cameraMonitorViewId = Objects.requireNonNull(HardwareGetter.getHardwareMap()).appContext.getResources().getIdentifier("cameraMonitorViewId", "id", HardwareGetter.getHardwareMap().appContext.getPackageName());

        int[] viewportContainerIds = null;
        if(indexed) {
            viewportContainerIds = MultipleCameraManager.get(cameraMonitorViewId);
        }

        OpenCvCamera camera;

        if(indexed) {
            camera = OpenCvCameraFactory.getInstance().createWebcam(Devices.camera1, viewportContainerIds[index]);
        }else{
            camera = OpenCvCameraFactory.getInstance().createWebcam(Devices.camera1, cameraMonitorViewId);
        }

        camera.setPipeline(this);
        FtcDashboard.getInstance().startCameraStream(camera, 0);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened() {
                camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */

                AdvancedLogging.logData("Camera error", errorCode);
                System.out.println("Camera error: " + errorCode);
                AdvancedLogging.update();
            }
        });
    }

    private double[] findDistance(KeyPoint kpt, Point center) {
        return new double[] { (kpt.pt.x - center.x) / kpt.size * lasagna, (kpt.pt.y - center.y) / kpt.size * lasagna };
    }

    private void poleDistNotFound() {
        if(poleDistanceX != 4 || poleDistanceY != 2) {
            time = System.currentTimeMillis() + 1000;
        }
        if(System.currentTimeMillis() > time) {
            poleDistanceX = 4;
            poleDistanceY = 2;
            data = true;
        }
    }

}


