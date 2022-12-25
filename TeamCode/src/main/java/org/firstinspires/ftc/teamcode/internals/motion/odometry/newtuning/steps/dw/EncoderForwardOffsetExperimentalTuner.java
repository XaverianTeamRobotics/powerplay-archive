package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.dw;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.state.State;

public class EncoderForwardOffsetExperimentalTuner extends Feature implements Conditional {

    @Override
    public boolean when() {
        return State.encoderForwardOffsetExperimentalTuner == Affair.PRESENT;
    }

    @Override
    public void loop() {

    }

}
