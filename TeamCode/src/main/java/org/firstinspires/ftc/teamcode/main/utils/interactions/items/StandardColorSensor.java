package org.firstinspires.ftc.teamcode.main.utils.interactions.items;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class StandardColorSensor extends InteractionItem {

    ColorSensor SENSOR;

    /**
     * Creates a new StandardColorSensor, which can sense colors.
     * @param hardware The HardwareMap to get the physical servo from
     * @param name The servo's name in the configuration
     */
    public StandardColorSensor(HardwareMap hardware, String name) {
        SENSOR = hardware.get(ColorSensor.class, name);
    }

    /**
     * Gets the RGBA values of the sensor, formatted as [ Red, Green, Blue, Aplha ].
     * @return the RGBA values of the sensor.
     */
    public int[] getRGBA() {
        return new int[] { SENSOR.red(), SENSOR.green(), SENSOR.blue(), SENSOR.alpha() };
    }

    /**
     * Gets the ARGB value of the sensor. This is <strong>NOT</strong> the same as {@link #getRGBA()}. The ARGB value is a packed 32-bit integer (to be interpreted as unsigned) containing all 4 color channels. It can be unpacked via bitshifting and bitmasking.
     * @return The ARGB value of the sensor.
     */
    public int getARGB() {
        return SENSOR.argb();
    }

    /**
     * Updates the state of the LED.
     * @param state The state, on or off.
     */
    public void updateLEDState(boolean state) {
        SENSOR.enableLed(state);
    }

    public ColorSensor getInternalSensor() {
        return SENSOR;
    }

    @Override
    public void stop() {}

    @Override
    public boolean isInputDevice() {
        return false;
    }

    @Override
    public boolean isOutputDevice() {
        return false;
    }

}
