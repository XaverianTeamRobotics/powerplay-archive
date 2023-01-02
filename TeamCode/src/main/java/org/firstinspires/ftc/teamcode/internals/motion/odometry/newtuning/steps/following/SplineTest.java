package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.following;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.drivers.AutonomousDriver;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.State;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;

public class SplineTest extends Feature implements Conditional {

    private MenuManager menuManager = null;
    private AutonomousDriver driver = null;

    private enum Step {
        ALIGN,
        TEST,
        RECON,
        NEXT
    }

    private Step step = Step.ALIGN;

    @Override
    public boolean when() {
        return State.spline == Affair.PRESENT;
    }

    @Override
    public void loop() {

    }

}
