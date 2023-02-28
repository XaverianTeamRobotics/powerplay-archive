package org.firstinspires.ftc.teamcode.features;

import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.time.Clock;

/**
 * <blockquote>
 *     No. We are not naming them the I and J cameras. No one will understand.<br>
 *     <cite>&ndash; Braden</cite>
 * </blockquote>
 */
public class JCam extends Feature implements Buildable {

    private static boolean down = false;

    @Override
    public void build() {
        Devices.servo2.setPosition(100);
        Clock.make("jcam");
    }

    @Override
    public void loop() {
        if(down) {
            Devices.servo2.setPosition(0);
        }else{
            Devices.servo2.setPosition(100);
        }
    }

    public static void toggle() {
        down = !down;
        Clock.get("jcam").reset();
    }

    public static boolean complete() {
        return Clock.get("jcam").elapsed(2);
    }

    public static boolean down() {
        return down;
    }

}
