package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.constants;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.misc.AsyncQuestionExecutor;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.State;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;

public class EncoderGearRatioTuner extends Feature implements Conditional {

    @Override
    public boolean when() {
        return State.encoderGearRatioTuning == Affair.PRESENT;
    }

    @Override
    public void loop() {
        AsyncQuestionExecutor.ask(new Menu.MenuBuilder().setDescription("Set the ENCODER_GEAR_RATIO to your dead wheel gear ratio, then select Ok.").addItem("Ok").build(), Devices.controller1, a -> {
            State.encoderGearRatioTuning = Affair.PAST;
            State.encoderTrackWidthTuning = Affair.PRESENT;
        });
    }

}
