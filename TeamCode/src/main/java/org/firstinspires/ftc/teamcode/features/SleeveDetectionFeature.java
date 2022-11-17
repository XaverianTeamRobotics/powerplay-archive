package org.firstinspires.ftc.teamcode.features;

import com.acmerobotics.dashboard.FtcDashboard;
import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.image.SleeveColorDetection;
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging;
import org.firstinspires.ftc.teamcode.internals.telemetry.MenuItem;
import org.firstinspires.ftc.teamcode.internals.telemetry.MenuItemType;
import org.firstinspires.ftc.teamcode.internals.telemetry.TelemetryMenu;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.Objects;

import static org.firstinspires.ftc.teamcode.internals.image.ImageProcessingConstants.GRAY_MAX;
import static org.firstinspires.ftc.teamcode.internals.image.ImageProcessingConstants.GRAY_MIN;

public class SleeveDetectionFeature extends Feature implements Buildable {

    private SleeveColorDetection detector;
    private TelemetryMenu menu;
    public int spot = 1;

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

        menu = new TelemetryMenu();
        MenuItem grayscalePreset = new MenuItem("Grayscale Preset", MenuItemType.INT, true);
        grayscalePreset.setStepSize(1);
        grayscalePreset.setMax(3);
        grayscalePreset.setMin(1);
        grayscalePreset.setValue(2);
        menu.addMenuItem(grayscalePreset);
        menu.runInBackground();
    }

    @Override
    public void loop() {
        int detectionID = detector.getDetection();
        spot = detectionID;
        int grayscalePreset = (int) menu.getMenuItem("Grayscale Preset").getValue();

        switch (grayscalePreset) {
            case 1:
                GRAY_MAX = 75;
                GRAY_MIN = 20;
                break;
            case 2:
                GRAY_MAX = 100;
                GRAY_MIN = 45;
                break;
            case 3:
                GRAY_MAX = 150;
                GRAY_MIN = 95;
                break;
        }

        menu.setAnnotation("To change the grayscale presets, set them to the approriate value \n 1 - Low Brightness \n 2 - Medium Brightness \n 3 - High Brightness" +
            "\n\nCurrent gray range: " + GRAY_MIN + " - " + GRAY_MAX + "\n\nCurrent detection ID: " + detectionID);
    }

}
