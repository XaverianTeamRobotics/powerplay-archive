package org.firstinspires.ftc.teamcode.opmodes;

import android.annotation.SuppressLint;
import com.acmerobotics.dashboard.FtcDashboard;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.internals.hardware.Logging;
import org.firstinspires.ftc.teamcode.internals.image.ImageProcessingConstants;
import org.firstinspires.ftc.teamcode.internals.image.SleeveColorDetection;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.Objects;

import static org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.getHardwareMap;

public class OpenCvSleeveDetectionTest extends OperationMode implements TeleOperation {

    public SleeveColorDetection detector;
    @SuppressLint("DiscouragedApi")
    @Override
    public void construct() {
        WebcamName webcamName = Objects.requireNonNull(getHardwareMap()).get(WebcamName.class, "camera0");
        int cameraMonitorViewId = getHardwareMap().appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        detector = new SleeveColorDetection();

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
    }

    @Override
    public void run() {
        Logging.logData("Sleeve Color ID", detector.getDetection());

        // Code here helps determine the effectiveness of the brightness adjustment feature
        Logging.logData("GRAY_MIN", ImageProcessingConstants.GRAY_MIN);
        Logging.logData("GRAY_MAX", ImageProcessingConstants.GRAY_MAX);
        Logging.updateLog();
    }
}
