package org.firstinspires.ftc.teamcode.internals.motion.path_creator

import android.os.Environment
import android.util.Xml
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import org.firstinspires.ftc.teamcode.internals.hardware.Devices
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDrivetrain
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.PodLocalizer
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * An OpMode that allows you to create a path for the robot to follow by pushing the robot and recording
 * the odometer values.
 *
 *
 * Exports data in XML format to the sdcard.
 */
class PushbotAutoPathCreator : OperationMode(), TeleOperation {
    private lateinit var localizer: PodLocalizer
    private val poses = ArrayList<Pose2d>()
    override fun construct() {
        // Initialize localizer
        localizer = PodLocalizer(hardwareMap)
        // Set the initial pose of the robot to start on the side of the field specified in the config
        localizer.poseEstimate =    if (PathCreatorConfig.startOnLeft)  Pose2d( 39.25, 61.50, Math.toRadians(-90.00))
                                    else                                Pose2d(-39.25, 61.50, Math.toRadians(-90.00))

        poses.add(localizer.poseEstimate)
    }

    override fun run() {
        // Update the robot's position
        localizer.update()
        // If the user presses the A button, save the current position to a list
        if (Devices.controller1.a) {
            poses.add(localizer.poseEstimate)
        }
        // If the user presses the B button, then export the list of positions to a file
        if (Devices.controller1.b) {
            // Get the time and date to use as the file name
            val time = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
            // Verify that the external storage is available
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                telemetry.addData("Error", "External storage not available")
                telemetry.update()
                return
            }
            // Create a new file with the name "path_<time>.xml" on the sdcard
            val file =
                File(Environment.getExternalStorageDirectory().path + "/" + PathCreatorConfig.saveDirectory + "/path_" + time + ".xml")
            // Generate the XML data from the list of poses and write it to the file
            val xml = Xml.newSerializer()
            xml.setOutput(file.outputStream(), "UTF-8")
            xml.startDocument("UTF-8", true)
            xml.startTag("", "poses")
            for (pose in poses) {
                xml.startTag("", "pose")
                xml.startTag("", "x")
                xml.text(pose.x.toString())
                xml.endTag("", "x")
                xml.startTag("", "y")
                xml.text(pose.y.toString())
                xml.endTag("", "y")
                xml.startTag("", "heading")
                xml.text(pose.heading.toString())
                xml.endTag("", "heading")
                xml.endTag("", "pose")
            }
            xml.endTag("", "poses")
            xml.endDocument()
            xml.flush()
            telemetry.addData("Saved", file.absolutePath)
            telemetry.update()
        }
    }
}