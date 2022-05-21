package org.firstinspires.ftc.teamcode.main.scripts.jlooping.requests;

import com.michaell.looping.ScriptParameters;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.main.utils.autonomous.sensors.NavigationSensorCollection;
import org.firstinspires.ftc.teamcode.main.utils.resources.Resources;

public class NavigationSensorRequest extends ScriptParameters.Request {
    public NavigationSensorCollection navSensors;

    public NavigationSensorRequest(NavigationSensorCollection navSensors) {
        this.navSensors = navSensors;
    }

    public NavigationSensorRequest(HardwareMap hardwareMap) {
        this.navSensors = Resources.Navigation.Sensors.getSensors(hardwareMap);
    }

    @Override
    public Object issueRequest(Object o) {
        return null;
    }

    @Override
    public Class getOutputType() {
        return Double.class;
    }

    @Override
    public Class getInputType() {
        return SensorIndex.class;
    }

    // TODO: Add Navigation Sensor Request
    public enum SensorIndex {
        DIST_NORTH,
        DIST_EAST,
        DIST_SOUTH,
        DIST_WEST,
        IMU_ROT_X,
        IMU_ROT_Y,
        IMU_ROT_Z,
        IMU_VEL_X,
        IMU_VEL_Y,
        IMU_VEL_Z,
        IMU_ACC_X,
        IMU_ACC_Y,
        IMU_ACC_Z,
        IMU_DIST_X,
        IMU_DIST_Y,
        IMU_DIST_Z,
    }
}
