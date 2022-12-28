package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.constants;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.State;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;

public class GearRatioTuner extends Feature implements Conditional {

    @Override
    public boolean when() {
        return State.gearRatioTuning == Affair.PRESENT;
    }

    @Override
    public void loop() {
        Questions.ask(new Menu.MenuBuilder().setDescription("Set the GEAR_RATIO to your drive gear ratio, then select Ok.").addItem("Ok").build(), Devices.controller1);
        State.gearRatioTuning = Affair.PAST;
        State.trackWidthTuningEstimate = Affair.PRESENT;
    }

}
