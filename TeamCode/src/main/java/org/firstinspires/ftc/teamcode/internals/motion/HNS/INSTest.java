package org.firstinspires.ftc.teamcode.internals.motion.HNS;

import com.michaell.looping.ScriptRunner;
import net.xbhs.robotics.HNS.NavigationSystem;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.Logging;

import static org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter.getJloopingRunner;

public class INSTest extends OperationMode implements TeleOperation {
    public InertialNavigationSystem navigationSystem;
    public double lastTimeStamp = 0;
    @Override
    public void construct() {
        navigationSystem = new InertialNavigationSystem(InertialNavigationSystem.ControlHubOrientation.UPRIGHT, 0.02);
        try {
            navigationSystem.register(getJloopingRunner());
        } catch (NavigationSystem.NavigationSystemException | ScriptRunner.DuplicateScriptException e) {
            throw new RuntimeException(e);
        }
        lastTimeStamp = System.currentTimeMillis();
    }

    @Override
    public void run() {
        // Log the localizer data
        Logging.log("X", navigationSystem.getLocalizer().x);
        Logging.log("Y", navigationSystem.getLocalizer().y);
        Logging.log("AZ", navigationSystem.getLocalizer().azimuth);
        Logging.log("\n");
        Logging.log("vX", navigationSystem.getLocalizer().vX);
        Logging.log("vY", navigationSystem.getLocalizer().vY);
        Logging.log("vAZ", navigationSystem.getLocalizer().vAzimuth);
        Logging.log("\n");
        Logging.log("aX", navigationSystem.getLocalizer().aX);
        Logging.log("aY", navigationSystem.getLocalizer().aY);
        Logging.log("aAZ", navigationSystem.getLocalizer().aAzimuth);

        // Log the IMU Acceleration data
        Logging.log("IMU aX", Devices.getImu().getAcceleration().getX());
        Logging.log("IMU aY", Devices.getImu().getAcceleration().getY());
        Logging.log("IMU aZ", Devices.getImu().getAcceleration().getZ());

        // Log the IMU Gyro data
        Logging.log("IMU Azimuth", Devices.getImu().getOrientation().getX());
        Logging.log("IMU Attitude", Devices.getImu().getOrientation().getY());
        Logging.log("IMU Roll", Devices.getImu().getOrientation().getZ());

        // Log deltaT
        double timeStamp = System.currentTimeMillis();
        Logging.log("deltaT", timeStamp - lastTimeStamp);
        lastTimeStamp = timeStamp;

        Logging.update();
    }
}
