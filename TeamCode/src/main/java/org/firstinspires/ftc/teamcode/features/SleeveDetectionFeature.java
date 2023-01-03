package org.firstinspires.ftc.teamcode.features;

import com.acmerobotics.dashboard.FtcDashboard;
import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.image.SleeveColorDetection;
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.Objects;

public class SleeveDetectionFeature extends Feature implements Buildable {

    private SleeveColorDetection detector;

    private int spot = 1;

    @Override
    public void build() {
        int cameraMonitorViewId = Objects.requireNonNull(HardwareGetter.getHardwareMap()).appContext.getResources().getIdentifier("cameraMonitorViewId", "id", HardwareGetter.getHardwareMap().appContext.getPackageName());
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(Devices.camera0, cameraMonitorViewId);

        detector = new SleeveColorDetection();

        detector.setDebugEnabled(false);

        camera.setPipeline(detector);
        FtcDashboard.getInstance().startCameraStream(camera, 0);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened() { camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT); }
            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */

                Logging.logData("Camera error", errorCode);
                Logging.updateLog();
            }
        });
    }

    @Override
    public void loop() {
        spot = detector.getDetection();
    }

    public int getSpot() {
        return spot;
    }

    public void setDebugEnabled(boolean enabled) {
        detector.setDebugEnabled(enabled);
    }

}
