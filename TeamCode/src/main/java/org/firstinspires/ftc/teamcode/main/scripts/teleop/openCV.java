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

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        // With live preview
        //OpenCvCamera camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        WebcamName webCam = hardwareMap.get(WebcamName.class, Resources.Misc.Webcam);
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webCam, cameraMonitorViewId);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                telemetry.addData("Camera", "True");
                telemetry.update();
                camera.startStreaming(stream_W, stream_H, OpenCvCameraRotation.UPRIGHT);
                camera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
                camera.setPipeline(new awesomePipeline());
            }
            @Override
            public void onError(int errorCode) {
                //This will be called if the camera could not be opened
                telemetry.addData("Camera", "False");
                telemetry.update();
            }
        });

        while (!isStopRequested()) {
            //loop until stop
        }
    }
    class awesomePipeline extends OpenCvPipeline
    {
        private Mat original = new Mat();
        private Mat output = new Mat();
        final Scalar blue = new Scalar(255, 0, 0);

        @Override
        public Mat processFrame(Mat input)
        {
            original = input;
            Imgproc.cvtColor(input, output,Imgproc.COLOR_RGB2HSV);
            drawLinesToPoint(original, new Point(200, 100));
            return original;
        }

        public void drawLinesToPoint(Mat matInput, Point pointInput) {
            Point point1_1 = new Point(0, pointInput.y); //horizontal line border point
            Point point1_2 = new Point(pointInput.x, pointInput.y); //horizontal line destination point
            Point point2_1 = new Point(pointInput.x, 0); //vertical line border point
            Point point2_2 = new Point(pointInput.x, pointInput.y); //vertical line destination point
            Imgproc.line(matInput, point1_1, point1_2, blue, 2);
            Imgproc.line(matInput, point2_1, point2_2, blue, 2);
        }
    }
}
