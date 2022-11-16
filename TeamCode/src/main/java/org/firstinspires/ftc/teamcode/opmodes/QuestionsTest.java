package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;
import org.firstinspires.ftc.teamcode.internals.registration.TeleOperation;
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging;
import org.firstinspires.ftc.teamcode.internals.telemetry.Questions;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;

public class QuestionsTest extends OperationMode implements TeleOperation {

    private Item item;

    @Override
    public void construct() {
        item = Questions.ask(new Menu.MenuBuilder().setDescription("This is a test menu!").addItem(new Item("hi")).addItem(new Item("bye")).addItem(new Item("woah")).build(), Devices.controller1);
    }

    @Override
    public void run() {
        Logging.logText(item.toString());
        Logging.updateLog();
    }

}
