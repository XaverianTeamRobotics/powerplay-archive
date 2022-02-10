package org.firstinspires.ftc.teamcode.main.utils.interactions.items;

import android.graphics.Color;
import android.graphics.ColorSpace;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class StandardColorSensor extends InteractionItem {

    ColorSensor SENSOR;

    public enum Color {
        BLACK,
        BLUE,
        CYAN,
        DARK_GRAY,
        GRAY,
        GREEN,
        LIGHT_GRAY,
        MAGENTA,
        ORANGE,
        PINK,
        RED,
        WHITE,
        YELLOW
    }

    /**
     * Creates a new StandardColorSensor, which can sense colors.
     * @param hardware The HardwareMap to get the physical servo from
     * @param name The servo's name in the configuration
     */
    public StandardColorSensor(HardwareMap hardware, String name) {
        SENSOR = hardware.get(ColorSensor.class, name);
    }

    /**
     * Gets the RGBA values of the sensor, formatted as [ Red, Green, Blue, Alpha (in thiscase, amount of photons) ].
     * @return the RGBA values of the sensor.
     */
    public int[] getRGBA() {
        return new int[] { SENSOR.red(), SENSOR.green(), SENSOR.blue(), SENSOR.alpha() };
    }

    /**
     * Gets the HSV values of the sensor, formatted as [ Hue, Saturation, Value ]
     * @return The HSV values of the sensor
     */
    public double[] getHSV() {
        float[] colors = new float[3];
        int[] vals = getRGBA();
        android.graphics.Color.RGBToHSV(vals[0], vals[1], vals[2], colors);
        double[] colorsd = new double[3];
        colorsd[0] = colors[0];
        colorsd[1] = colors[1];
        colorsd[2] = colors[2];
        return colorsd;
    }

    /**
     * Gets the ARGB value of the sensor. This is <strong>NOT</strong> the same as {@link #getRGBA()}. The ARGB value is a packed 32-bit integer (to be interpreted as unsigned) containing all 4 color channels. It can be unpacked via bitshifting and bitmasking.
     * @return The ARGB value of the sensor.
     */
    public int getARGB() {
        return SENSOR.argb();
    }

    /**
     * Turns the color sensor's LED on.
     */
    public void powerLED() {
        SENSOR.enableLed(true);
    }

    /**
     * Turns the color sensor's LED off.
     */
    public void unpowerLED() {
        SENSOR.enableLed(false);
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
