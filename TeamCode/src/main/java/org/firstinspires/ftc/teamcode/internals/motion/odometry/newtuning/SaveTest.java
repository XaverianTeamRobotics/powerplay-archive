package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning;

import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.SettingLoader;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.utils.SettingLoaderFailureException;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;

public class SaveTest extends OperationMode implements TeleOperation {

    private boolean saved = false;

    @Override
    public void construct() {
        System.out.println("running");
    }

    @Override
    public void run() {
        if(!saved) {
            saved = true;
            try {
                SettingLoader.save();
            } catch(SettingLoaderFailureException e) {
                System.out.println("Saving settings failed! " + e.getMessage());
                e.printStackTrace();
                System.out.println(e.toString());
            }
        }
    }
}
