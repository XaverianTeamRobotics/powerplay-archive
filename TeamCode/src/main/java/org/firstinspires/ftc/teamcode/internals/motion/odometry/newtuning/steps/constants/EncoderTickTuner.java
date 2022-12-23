package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.constants;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.state.State;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;

public class EncoderTickTuner extends Feature implements Conditional {

    @Override
    public boolean when() {
        return State.encoderTickTuning == Affair.PRESENT;
    }

    @Override
    public void loop() {
        Questions.ask(new Menu.MenuBuilder().setDescription("Set the ENCODER_TICKS_PER_REV to your dead wheel encoders' resolutions, then select Ok.").addItem("Ok").build(), Devices.controller1);
        State.encoderTickTuning = Affair.PAST;
        State.encoderWheelRadiusTuning = Affair.PRESENT;
    }

}
