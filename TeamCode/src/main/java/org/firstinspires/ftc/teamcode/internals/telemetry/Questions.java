package org.firstinspires.ftc.teamcode.internals.telemetry;

import org.firstinspires.ftc.teamcode.internals.hardware.accessors.Gamepad;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Item;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.Menu;
import org.firstinspires.ftc.teamcode.internals.telemetry.graphics.MenuManager;

/**
 * The Questions framework allows a human driver to answer multiple-choice questions prompted by the robot via an interface on the DS.
 */
public class Questions {

    /**
     * Asks the driver a question, which is displayed on the telemetry window on the DS. The driver can choose an answer with the D-Pad and the A (or cross) button.
     * @param prompt The question itself.
     * @param input The gamepad which will be used to let the driver answer this question.
     * @return The answer of the question.
     */
    public static Item ask(Menu prompt, Gamepad input) {
        return new MenuManager(prompt, input).run();
    }

}
