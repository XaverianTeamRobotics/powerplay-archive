package org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.steps.constants;

import org.firstinspires.ftc.teamcode.internals.features.Conditional;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.misc.Affair;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.newtuning.state.State;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;

public class Directions extends Feature implements Conditional {

    @Override
    public boolean when() {
        return State.constantDirections == Affair.PRESENT;
    }

    @Override
    public void loop() {
        Questions.ask(new Menu.MenuBuilder().setDescription("Ok! We're going to start by defining initial values you can find or calculate without doing any physical tuning. Then we'll get into physically tuning the robot. When you're ready, select Ok.").addItem("Ok").build(), Devices.controller1);
        State.constantDirections = Affair.PAST;
        State.motorConfigSetup = Affair.PRESENT;
    }

}
