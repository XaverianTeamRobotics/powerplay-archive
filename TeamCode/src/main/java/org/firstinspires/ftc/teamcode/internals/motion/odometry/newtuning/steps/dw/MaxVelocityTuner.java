package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.dw;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.state.State;

public class MaxVelocityTuner extends Feature implements Conditional {

    @Override
    public boolean when() {
        return State.maxVelocityTuner == Affair.PRESENT;
    }

    @Override
    public void loop() {
        // TODO: this needs to be for both max vel AND max ang vel tuning. so yeah. do that. cool
    }

}
