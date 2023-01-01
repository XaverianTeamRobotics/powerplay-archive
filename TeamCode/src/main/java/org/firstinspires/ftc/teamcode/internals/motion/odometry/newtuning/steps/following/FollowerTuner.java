package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.following;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDriver;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.State;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;

public class FollowerTuner extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDriver driver = null;

    private enum Step {
        ALIGN_BF,
        BF,
        ALIGN_HEAD,
        HEAD,
        NEXT
    }

    private Step step = Step.ALIGN_BF;

    @Override
    public boolean when() {
        return State.followerTuning == Affair.PRESENT;
    }

    @Override
    public void loop() {
        switch(step) {

        }
    }

}
