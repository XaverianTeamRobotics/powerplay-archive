package org.firstinspires.ftc.teamcode.opmodes;

import android.annotation.SuppressLint;
import com.acmerobotics.dashboard.FtcDashboard;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.internals.image.SleeveColorDetection;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging;
import org.firstinspires.ftc.teamcode.internals.telemetry.MenuItem;
import org.firstinspires.ftc.teamcode.internals.telemetry.MenuItemType;
import org.firstinspires.ftc.teamcode.internals.telemetry.TelemetryMenu;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.Objects;

import static org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.getHardwareMap;
import static org.firstinspires.ftc.teamcode.internals.image.ImageProcessingConstants.GRAY_MAX;
import static org.firstinspires.ftc.teamcode.internals.image.ImageProcessingConstants.GRAY_MIN;

public class OpenCvSleeveDetectionTest extends OperationMode implements TeleOperation {

    public SleeveColorDetection detector;
    public TelemetryMenu menu;

    @SuppressLint("DiscouragedApi")
    @Override
    public void construct() {
        WebcamName webcamName = Objects.requireNonNull(getHardwareMap()).get(WebcamName.class, "camera0");
        int cameraMonitorViewId = getHardwareMap().appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

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
                sleep(2000);
                stop();
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
    public void run() {
        int detectionID = detector.getDetection();
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
