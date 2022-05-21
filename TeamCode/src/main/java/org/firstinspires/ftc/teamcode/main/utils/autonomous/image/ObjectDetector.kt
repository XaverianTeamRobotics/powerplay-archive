package org.firstinspires.ftc.teamcode.main.utils.autonomous.image

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix
import org.firstinspires.ftc.robotcore.external.matrices.VectorF
import org.firstinspires.ftc.robotcore.external.navigation.*
import org.firstinspires.ftc.robotcore.external.tfod.Recognition
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector
import org.firstinspires.ftc.teamcode.main.utils.resources.Resources

class ObjectDetector(
    private var vuforiaKey: String = Resources.Misc.VuforiaKey,
    private var tfodModel: String = "FreightFrenzy_BC.tflite",
    private var labels: Array<String> = arrayOf<String>("Ball", "Cube"),
    private var hardwareMap: HardwareMap,
    cameraName: String = Resources.Misc.Webcam
) {

    private val OBJECT_TO_IDENT = "Duck"
    private var CAMERA_NAME: String? = cameraName
    private val detections: ArrayList<Detection>? = null
    private var vuforia: VuforiaLocalizer? = null
    private var tfod: TFObjectDetector? = null
    @JvmField
    var confidence = 0.5f
    private var initialObjectIdent = false
    private var initialObjectIdentSTRICT = false

    private var vuforiaTrackables: VuforiaTrackables? = null

    private val mmPerInch = 25.4f
    private val mmTargetHeight: Float = 6 * mmPerInch
        // the height of the center of the target image above the floor

    private val halfField: Float = 72 * mmPerInch
    private val halfTile: Float = 12 * mmPerInch
    private val oneAndHalfTile: Float = 36 * mmPerInch

    private var allTrackables: ArrayList<VuforiaTrackable>? = null
    private var possibleStartingPositions: ArrayList<InitialPositions?> = ArrayList()

    fun init() {
        initVuforia()
        initTFOD()
    }

    fun setZoom(magnification: Double, aspectRatio: Double) {
        tfod!!.setZoom(magnification, aspectRatio)
    }

    fun activate() {
        tfod!!.activate()
        vuforiaTrackables!!.activate()
    }

    fun getUpdatedRecognitions(): List<Recognition>? {
        return tfod!!.updatedRecognitions
    }

    private fun initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        val parameters: org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters =
            org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters()
        parameters.vuforiaLicenseKey = vuforiaKey
        parameters.cameraName = hardwareMap.get<WebcamName>(WebcamName::class.java, CAMERA_NAME)
        parameters.useExtendedTracking = false
        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters)
        vuforiaTrackables = vuforia!!.loadTrackablesFromAsset("FreightFrenzy")
        allTrackables = ArrayList<VuforiaTrackable>(vuforiaTrackables!!)
        identifyTarget(0, "Blue Storage", -halfField, oneAndHalfTile, mmTargetHeight, 90f, 0f, 90f)
        identifyTarget(1, "Blue Alliance Wall", halfTile, halfField, mmTargetHeight, 90f, 0f, 0f)
        identifyTarget(2, "Red Storage", -halfField, -oneAndHalfTile, mmTargetHeight, 90f, 0f, 90f)
        identifyTarget(3, "Red Alliance Wall", halfTile, -halfField, mmTargetHeight, 90f, 0f, 180f)
        val CAMERA_FORWARD_DISPLACEMENT = 6.0f * mmPerInch
        val CAMERA_VERTICAL_DISPLACEMENT = 6.0f * mmPerInch
        val CAMERA_LEFT_DISPLACEMENT = -6.0f * mmPerInch
        val cameraLocationOnRobot: OpenGLMatrix = OpenGLMatrix
            .translation(
                CAMERA_FORWARD_DISPLACEMENT,
                CAMERA_LEFT_DISPLACEMENT,
                CAMERA_VERTICAL_DISPLACEMENT
            )
            .multiplied(
                Orientation.getRotationMatrix(
                    AxesReference.EXTRINSIC,
                    AxesOrder.XZY,
                    AngleUnit.DEGREES,
                    90f,
                    90f,
                    0f
                )
            )
        for (trackable in allTrackables!!) {
            (trackable.listener as VuforiaTrackableDefaultListener).setCameraLocationOnRobot(
                parameters.cameraName!!,
                cameraLocationOnRobot
            )
        }
    }

    private fun initTFOD() {
        val tfodMonitorViewId: Int = hardwareMap.appContext.resources.getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.packageName
        )
        val tfodParameters: TFObjectDetector.Parameters =
            TFObjectDetector.Parameters(
                tfodMonitorViewId
            )
        tfodParameters.minResultConfidence = confidence
        tfodParameters.isModelTensorFlow2 = true
        tfodParameters.inputSize = 320
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia)
        tfod!!.loadModelFromAsset(tfodModel, *labels)
        tfod!!.setZoom(1.5, 16.0 / 9.0)
        possibleStartingPositions.add(InitialPositions.POS1)
        possibleStartingPositions.add(InitialPositions.POS2)
        possibleStartingPositions.add(InitialPositions.POS3)
    }

    data class Detection(
        var x: Float,
        var y: Float,
        var imageHeight: Float,
        var imageWidth: Float,
        var friendlyName: String,
        var angle: Float
    )

    data class VuforiaLocationInfo(
        var location: OpenGLMatrix?,
        var translation: VectorF?,
        var trackableName: String?)

    fun getVisibleVuforiaTarget(): VuforiaLocationInfo? {
        val toReturn = VuforiaLocationInfo(null, null, null)
        for (trackable in allTrackables!!) {
            if ((trackable.listener as VuforiaTrackableDefaultListener).isVisible) {
                toReturn.trackableName = trackable.name

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                val robotLocationTransform: OpenGLMatrix =
                    (trackable.listener as VuforiaTrackableDefaultListener).updatedRobotLocation
                toReturn.location = robotLocationTransform
                toReturn.translation = robotLocationTransform.translation
                break
            }
        }
        return toReturn
    }

    fun identifyStartingPos(): Int {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            val updatedRecognitions: List<Recognition>? = getUpdatedRecognitions()
            if (updatedRecognitions != null) {
                for ((i, recognition) in updatedRecognitions.withIndex()) {
                    if (!initialObjectIdent || !initialObjectIdentSTRICT) {
                        if (InitialPositions.POS1.evalPos(recognition.left.toInt())) {
                            if (recognition.label == OBJECT_TO_IDENT) {
                                possibleStartingPositions = ArrayList<InitialPositions?>()
                                possibleStartingPositions.add(InitialPositions.POS1)
                                initialObjectIdentSTRICT = true
                            } else possibleStartingPositions.remove(InitialPositions.POS1)
                        } else if (InitialPositions.POS2.evalPos(recognition.left.toInt())) {
                            if (recognition.label == OBJECT_TO_IDENT) {
                                possibleStartingPositions = ArrayList<InitialPositions?>()
                                possibleStartingPositions.add(InitialPositions.POS2)
                                initialObjectIdentSTRICT = true
                            } else possibleStartingPositions.remove(InitialPositions.POS2)
                        } else if (InitialPositions.POS3.evalPos(recognition.left.toInt())) {
                            if (recognition.label == OBJECT_TO_IDENT) {
                                possibleStartingPositions = ArrayList()
                                possibleStartingPositions.add(InitialPositions.POS3)
                                initialObjectIdentSTRICT = true
                            } else possibleStartingPositions.remove(InitialPositions.POS3)
                        }
                    }
                }
                initialObjectIdent = possibleStartingPositions.size == 1
            }
        }
        var t = 0
        if (initialObjectIdent || initialObjectIdentSTRICT) {
            when (possibleStartingPositions[0]) {
                InitialPositions.POS1 -> t = 1
                InitialPositions.POS2 -> t = 2
                InitialPositions.POS3 -> t = 3
            }
        }
        return t
    }

    @Suppress("SameParameterValue")
    private fun identifyTarget(
        targetIndex: Int,
        targetName: String?,
        dx: Float,
        dy: Float,
        dz: Float,
        rx: Float,
        ry: Float,
        rz: Float
    ) {
        val aTarget: VuforiaTrackable = vuforiaTrackables!![targetIndex]
        aTarget.name = targetName
        aTarget.location = OpenGLMatrix.translation(dx, dy, dz)
            .multiplied(
                Orientation.getRotationMatrix(
                    AxesReference.EXTRINSIC,
                    AxesOrder.XYZ,
                    AngleUnit.DEGREES,
                    rx,
                    ry,
                    rz
                )
            )
    }
}
