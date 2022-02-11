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
                camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
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

        @Override
        public Mat processFrame(Mat input)
        {
            original = input;
            Imgproc.cvtColor(input, output,Imgproc.COLOR_RGB2HSV);
            Point point1 = new Point(0, 100);
            Point point2 = new Point(200, 100);
            final Scalar blue = new Scalar(255, 0, 0);
            Imgproc.line(original, point1, point2, blue, 2,2,0);
            return original;
        }
    }
}
