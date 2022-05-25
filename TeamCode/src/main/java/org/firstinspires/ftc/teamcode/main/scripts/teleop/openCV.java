package org.firstinspires.ftc.teamcode.main.scripts.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.main.utils.resources.Resources;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.*;

@TeleOp(name = "ColinCam", group = "ColinCode")
public class openCV extends LinearOpMode {

    int stream_W = 640;
    int stream_H = 480;

    @Override
    public void runOpMode() throws InterruptedException {
        // Init camera view

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        // Setup some variables
        // With live preview
        //OpenCvCamera camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        WebcamName webCam = hardwareMap.get(WebcamName.class, Resources.Misc.Webcam);
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webCam, cameraMonitorViewId);

        // Setup a camera listener
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                telemetry.addData("Camera", "True");
                telemetry.update();
                camera.startStreaming(stream_W, stream_H, OpenCvCameraRotation.UPRIGHT);
                camera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
                camera.setPipeline(new AwesomePipeline());
            }
            @Override
            public void onError(int errorCode) {
                //This will be called if the camera could not be opened
                telemetry.addData("Camera", "False"); //show the user that the camera probably won't work
                telemetry.update();
            }
        });

        while (!isStopRequested()) {
            //loop until stop
        }
    }
    static class AwesomePipeline extends OpenCvPipeline
    {
        //private List<MatOfPoint> cnts;
        private Mat original = new Mat();
        private Mat output = new Mat();
        //private Mat mask = new Mat();
        final Scalar blue = new Scalar(255, 0, 0);
        //private Point selectedPoint;

        @Override
        public Mat processFrame(Mat input)
        {
            original = input;
            Imgproc.cvtColor(input, output,Imgproc.COLOR_RGB2HSV);
            // Create two points and a color
            Point point1 = new Point(0, 100);
            Point point2 = new Point(200, 100);
            final Scalar blue = new Scalar(255, 0, 0);

            // Create a line and return the image
            Imgproc.line(original, point1, point2, blue, 2,2,0);
            return original;
        }
    }
}
