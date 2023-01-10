package org.firstinspires.ftc.teamcode.internals.motion.odometry.utils;

import org.firstinspires.ftc.teamcode.internals.motion.odometry.OdometrySettings;

/**
 * Exception thrown when saving or loading {@link OdometrySettings} from a file fails for whatever reason.
 */
public class SettingLoaderFailureException extends RuntimeException {

    public SettingLoaderFailureException() {
        super();
    }

    public SettingLoaderFailureException(String message) {
        super(message);
    }

    public SettingLoaderFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
