package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.ff;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.State;

public class TrackWidthTuner extends Feature implements Conditional {

    @Override
    public boolean when() {
        return State.driveTrackWidthExperimentalTuning == Affair.PRESENT;
    }

    @Override
    public void loop() {

    }

}
