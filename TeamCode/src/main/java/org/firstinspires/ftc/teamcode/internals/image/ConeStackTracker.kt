package org.firstinspires.ftc.teamcode.internals.image

import com.acmerobotics.dashboard.FtcDashboard
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.Companion.hardwareMap
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging.Companion.logData
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging.Companion.updateLog
import org.opencv.core.*
import org.opencv.features2d.SimpleBlobDetector
import org.opencv.features2d.SimpleBlobDetector_Params
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvPipeline
import kotlin.math.atan2
import kotlin.math.tan

class ConeStackTracker(val isBlueTeam: Boolean, val enableDisplayOfAngles: Boolean): OpenCvPipeline() {
    var detectedCones: MatOfKeyPoint = MatOfKeyPoint()
    override fun processFrame(input: Mat): Mat {

        //Logging.logData("Input Height", input.height());
        //Logging.logData("Input Width", input.width());

        // Blur the image to reduce noise
        val blurSize = Size(ImageProcessingConstants.GAUSSIAN_BLUR_SIZE, ImageProcessingConstants.GAUSSIAN_BLUR_SIZE)
        Imgproc.GaussianBlur(input, input, blurSize, 0.0)

        // Convert to HSV
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HSV)

        // Generate a mask of whatever the team's color is
        val mask1 = input.clone()

        val blueLowerBound = Scalar(ImageProcessingConstants.BLUE_H_MIN, ImageProcessingConstants.BLUE_S_MIN, ImageProcessingConstants.BLUE_V_MIN)
        val blueUpperBound = Scalar(ImageProcessingConstants.BLUE_H_MAX, ImageProcessingConstants.BLUE_S_MAX, ImageProcessingConstants.BLUE_V_MAX)

        val redLowerBound = Scalar(ImageProcessingConstants.RED_H_MIN, ImageProcessingConstants.RED_S_MIN, ImageProcessingConstants.RED_V_MIN)
        val redUpperBound = Scalar(ImageProcessingConstants.RED_H_MAX, ImageProcessingConstants.RED_S_MAX, ImageProcessingConstants.RED_V_MAX)

        if (isBlueTeam) {
            Core.inRange(mask1, blueLowerBound, blueUpperBound, mask1)
        } else {
            Core.inRange(mask1, redLowerBound, redUpperBound, mask1)
        }

        // Run a blob detector on the mask to find all the blobs
        val params = SimpleBlobDetector_Params()
        params._filterByColor = false
        val detector = SimpleBlobDetector.create(params)
        val keypoints = MatOfKeyPoint()
        detector.detect(mask1, keypoints)
        mask1.release()
        detectedCones = keypoints

        // Draw the keypoints on the image as boxes with a number in them
        val color = Scalar(255.0, 255.0, 255.0)
        for (i in keypoints.toArray().indices) {
            val keypoint = keypoints.toArray()[i]
            val rect = Rect(keypoint.pt.x.toInt() - 10, keypoint.pt.y.toInt() - 10, 20, 20)
            Imgproc.rectangle(input, rect, color, 2)
            var text = "#$i"
            if (enableDisplayOfAngles) {
                val angle = getAngleToTarget(keypoint)
                text += " ${angle.toInt()}°"
            }
            Imgproc.putText(input, text, keypoint.pt, Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, color, 2)
        }

        // Clean up and return the output
        Imgproc.cvtColor(input, input, Imgproc.COLOR_HSV2RGB)
        return input
    }

    fun startStreaming(): ConeStackTracker {
        val cameraMonitorViewId = hardwareMap!!.appContext?.resources?.getIdentifier(
            "cameraMonitorViewId",
            "id",
            hardwareMap!!.appContext.packageName
        )
        val camera: OpenCvCamera = OpenCvCameraFactory.getInstance().createWebcam(Devices.camera0,
            cameraMonitorViewId!!
        )

        camera.setPipeline(this)
        FtcDashboard.getInstance().startCameraStream(camera, 0.0)

        camera.openCameraDeviceAsync(object : AsyncCameraOpenListener {
            override fun onOpened() {
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int) {
                /*
                 * This will be called if the camera could not be opened
                 */
                logData("Camera error", errorCode)
                updateLog()
            }
        })

        return this
    }

    fun getAngleToTarget(keyPoint: KeyPoint): Double {
        val center = keyPoint.pt
        return Math.toDegrees(atan2(center.x - 160, 160 / tan(Math.toRadians(60.0))))
    }

    fun returnBiggestCone(): KeyPoint? {
        var biggestCone: KeyPoint? = null
        for (cone in detectedCones.toArray()) {
            if (biggestCone == null || cone.size > biggestCone.size) {
                biggestCone = cone
            }
        }
        return biggestCone
    }
}