package org.firstinspires.ftc.teamcode.main.utils.interactions.items;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * A StandardBlinkinDriver represents a REV Robotics Blinkin LED Driver. It can be used to control LEDs.
 */
public class StandardBlinkinDriver {

    /**
     * The possible patterns a Blinkin driver can be set to. Find more information on page 15 of <a href="https://www.revrobotics.com/content/docs/REV-11-1105-UM.pdf">the Blinkin manual</a>.
     */
    public static class Pattern {

        /**
         * Fixed-palette patterns a Blinkin driver can be set to. These are listed as fixed palette patterns on the Blinkin manual. They use their own colors rather than the colors physically set into the driver by turning it's real-life dials, but are different than {@link Color}s because they have complex patterns.
         */
        public enum FixedPalette {
            GAY_RAINBOW,
            PARTY_RAINBOW,
            OCEAN_RAINBOW,
            LAVA_RAINBOW,
            FOREST_RAINBOW,
            GLITTER_RAINBOW,
            CONFETTI,
            RED_SHOT,
            BLUE_SHOT,
            WHITE_SHOT,
            RAINBOW_SINE,
            PARTY_SINE,
            OCEAN_SINE,
            LAVA_SINE,
            FOREST_SINE,
            RAINBOW_BPM,
            PARTY_BPM,
            OCEAN_BPM,
            LAVA_BPM,
            FOREST_BPM,
            FIRE_MEDIUM,
            FIRE_LARGE,
            RAINBOW_TWINKLES,
            PARTY_TWINKLES,
            OCEAN_TWINKLES,
            LAVA_TWINKLES,
            FOREST_TWINKLES,
            RAINBOW_WAVES,
            PARTY_WAVES,
            OCEAN_WAVES,
            LAVA_WAVES,
            FOREST_WAVES,
            RED_LARSON_SCANNER,
            GRAY_LARSON_SCANNER,
            RED_LIGHT_CHASE,
            BLUE_LIGHT_CHASE,
            GRAY_LIGHT_CHASE,
            RED_HEARTBEAT,
            BLUE_HEARTBEAT,
            WHITE_HEARTBEAT,
            GRAY_HEARTBEAT,
            RED_BREATH,
            BLUE_BREATH,
            GRAY_BREATH,
            RED_STROBE,
            BLUE_STROBE,
            GOLD_STROBE,
            WHITE_STROBE
        }

        /**
         * Primary color patterns a Blinkin driver can be set to. These are listed as color 1 patterns on the Blinkin manual. They use the color defined by the rotation of the physical color 1 dial on the driver.
         */
        public enum PrimaryColor {
            END_TO_END_BLEND_TO_BLACK,
            LARSON_SCANNER,
            LIGHT_CHASE,
            HEARTBEAT_SLOW,
            HEARTBEAT_MEDIUM,
            HEARTBEAT_FAST,
            BREATH_SLOW,
            BREATH_FAST,
            SHOT,
            STROBE
        }

        /**
         * Secondary color patterns a Blinkin driver can be set to. These are listed as color 2 patterns on the Blinkin manual. They use the color defined by the rotation of the physical color 2 dial on the driver.
         */
        public enum SecondaryColor {
            END_TO_END_BLEND_TO_BLACK,
            LARSON_SCANNER,
            LIGHT_CHASE,
            HEARTBEAT_SLOW,
            HEARTBEAT_MEDIUM,
            HEARTBEAT_FAST,
            BREATH_SLOW,
            BREATH_FAST,
            SHOT,
            STROBE
        }

        /**
         * Multicolor patterns a Blinkin driver can be set to. These are listed as color 1 and 2 patterns on the Blinkin manual. They use the color defined by the rotation of the physical color 1 and color 2 dials on the driver.
         */
        public enum MultiColor {
            SPARKLE_PRIMARY_COLOR_OVER_SECONDARY_COLOR,
            SPARKLE_SECONDARY_COLOR_OVER_PRIMARY_COLOR,
            GRADIENT,
            BPM,
            END_TO_END_BLEND_FROM_PRIMARY_COLOR_TO_SECONDARY_COLOR,
            END_TO_END_BLEND,
            END_TO_END_STEP_FROM_PRIMARY_COLOR_TO_SECONDARY_COLOR,
            TWINKLE,
            WAVE,
            SINE
        }

        /**
         * Specific colors a Blinkin driver can display. They take no input from the physical color dials, rather are hardcoded into the Blinkin driver.
         */
        public enum Color {
            HOT_PINK,
            DARK_RED,
            RED,
            RED_ORANGE,
            ORANGE,
            GOLD,
            YELLOW,
            LAWN_GREEN,
            LIME,
            DARK_GREEN,
            GREEN,
            BLUE_GREEN,
            AQUA,
            SKY_BLUE,
            DARK_BLUE,
            BLUE,
            BLUE_VIOLET,
            VIOLET,
            WHITE,
            GRAY,
            DARK_GRAY,
            BLACK
        }

    }

    private final Servo SERVO;

    /**
     * Creates a new StandardBlinkinDriver.
     * @param hardware The hardware map of the Blinkin driver
     * @param name The name of the Blinkin driver
     */
    public StandardBlinkinDriver(HardwareMap hardware, String name) {
        SERVO = hardware.get(Servo.class, name);
    }

    /**
     * Sets the fixed pattern based on its {@link Pattern.FixedPalette}.
     * @param pattern The fixed light pattern to use
     */
    public void setFixedPattern(Pattern.FixedPalette pattern) {
        double val = 0;
        switch(pattern) {
            case GAY_RAINBOW:
                val = -0.99;
                break;
            case PARTY_RAINBOW:
                val = -0.97;
                break;
            case OCEAN_RAINBOW:
                val = -0.95;
                break;
            case LAVA_RAINBOW:
                val = -0.93;
                break;
            case FOREST_RAINBOW:
                val = -0.91;
                break;
            case GLITTER_RAINBOW:
                val = -0.89;
                break;
            case CONFETTI:
                val = -0.87;
            case RED_SHOT:
                val = -0.85;
                break;
            case BLUE_SHOT:
                val = -0.83;
                break;
            case WHITE_SHOT:
                val = -0.81;
                break;
            case RAINBOW_SINE:
                val = -0.79;
                break;
            case PARTY_SINE:
                val = -0.77;
                break;
            case OCEAN_SINE:
                val = -0.75;
                break;
            case LAVA_SINE:
                val = -0.73;
                break;
            case FOREST_SINE:
                val = -0.71;
                break;
            case RAINBOW_BPM:
                val = -0.69;
                break;
            case PARTY_BPM:
                val = -0.67;
            case OCEAN_BPM:
                val = -0.65;
                break;
            case LAVA_BPM:
                val = -0.63;
                break;
            case FOREST_BPM:
                val = -0.61;
                break;
            case FIRE_MEDIUM:
                val = -0.59;
                break;
            case FIRE_LARGE:
                val = -0.57;
                break;
            case RAINBOW_TWINKLES:
                val = -0.55;
                break;
            case PARTY_TWINKLES:
                val = -0.53;
                break;
            case OCEAN_TWINKLES:
                val = -0.51;
                break;
            case LAVA_TWINKLES:
                val = -0.49;
                break;
            case FOREST_TWINKLES:
                val = -0.47;
                break;
            case RAINBOW_WAVES:
                val = -0.45;
                break;
            case PARTY_WAVES:
                val = -0.43;
                break;
            case OCEAN_WAVES:
                val = -0.41;
                break;
            case LAVA_WAVES:
                val = -0.39;
                break;
            case FOREST_WAVES:
                val = -0.37;
                break;
            case RED_LARSON_SCANNER:
                val = -0.35;
                break;
            case GRAY_LARSON_SCANNER:
                val = -0.33;
                break;
            case RED_LIGHT_CHASE:
                val = -0.31;
                break;
            case BLUE_LIGHT_CHASE:
                val = -0.29;
                break;
            case GRAY_LIGHT_CHASE:
                val = -0.27;
                break;
            case RED_HEARTBEAT:
                val = -0.25;
                break;
            case BLUE_HEARTBEAT:
                val = -0.23;
                break;
            case WHITE_HEARTBEAT:
                val = -0.21;
                break;
            case GRAY_HEARTBEAT:
                val = -0.19;
                break;
            case RED_BREATH:
                val = -0.17;
                break;
            case BLUE_BREATH:
                val = -0.15;
                break;
            case GRAY_BREATH:
                val = -0.13;
                break;
            case RED_STROBE:
                val = -0.11;
                break;
            case BLUE_STROBE:
                val = -0.09;
                break;
            case GOLD_STROBE:
                val = -0.07;
                break;
            case WHITE_STROBE:
                val = -0.05;
                break;
            default:
                val = 2;
        }
        if(val != 2) {
            SERVO.setPosition(val);
        }
    }

    /**
     * Sets the variable {@link Pattern.PrimaryColor} pattern using the primary color set by physically turning the dials on the Blinkin driver in real life.
     * @param pattern The pattern to use
     */
    public void setPrimaryPattern(Pattern.PrimaryColor pattern) {
        double val = 0;
        switch(pattern) {
            case END_TO_END_BLEND_TO_BLACK:
                val = -0.03;
                break;
            case LARSON_SCANNER:
                val = -0.01;
                break;
            case LIGHT_CHASE:
                val = 0.01;
                break;
            case HEARTBEAT_SLOW:
                val = 0.03;
                break;
            case HEARTBEAT_MEDIUM:
                val = 0.05;
                break;
            case HEARTBEAT_FAST:
                val = 0.07;
                break;
            case BREATH_SLOW:
                val = 0.09;
                break;
            case BREATH_FAST:
                val = 0.11;
                break;
            case SHOT:
                val = 0.13;
                break;
            case STROBE:
                val = 0.15;
                break;
            default:
                val = 2;
        }
        if(val != 2) {
            SERVO.setPosition(val);
        }
    }

    /**
     * Sets the variable {@link Pattern.SecondaryColor} pattern using the secondary color set by physically turning the dials on the Blinkin driver in real life.
     * @param pattern The pattern to use
     */
    public void setSecondaryPattern(Pattern.SecondaryColor pattern) {
        double val = 0;
        switch(pattern) {
            case END_TO_END_BLEND_TO_BLACK:
                val = 0.17;
                break;
            case LARSON_SCANNER:
                val = 0.19;
                break;
            case LIGHT_CHASE:
                val = 0.21;
                break;
            case HEARTBEAT_SLOW:
                val = 0.23;
                break;
            case HEARTBEAT_MEDIUM:
                val = 0.25;
                break;
            case HEARTBEAT_FAST:
                val = 0.27;
                break;
            case BREATH_SLOW:
                val = 0.29;
                break;
            case BREATH_FAST:
                val = 0.31;
                break;
            case SHOT:
                val = 0.33;
                break;
            case STROBE:
                val = 0.35;
                break;
            default:
                val = 2;
        }
        if(val != 2) {
            SERVO.setPosition(val);
        }
    }

    /**
     * Sets the variable {@link Pattern.MultiColor} pattern using the primary and secondary colors set by physically turning the dials on the Blinkin driver in real life.
     * @param pattern The pattern to use
     */
    public void setMultiColorPattern(Pattern.MultiColor pattern) {
        double val = 0;
        switch(pattern) {
            case SPARKLE_PRIMARY_COLOR_OVER_SECONDARY_COLOR:
                val = 0.37;
                break;
            case SPARKLE_SECONDARY_COLOR_OVER_PRIMARY_COLOR:
                val = 0.39;
                break;
            case GRADIENT:
                val = 0.41;
                break;
            case BPM:
                val = 0.43;
                break;
            case END_TO_END_BLEND_FROM_PRIMARY_COLOR_TO_SECONDARY_COLOR:
                val = 0.45;
                break;
            case END_TO_END_BLEND:
                val = 0.47;
                break;
            case END_TO_END_STEP_FROM_PRIMARY_COLOR_TO_SECONDARY_COLOR:
                val = 0.49;
                break;
            case TWINKLE:
                val = 0.51;
                break;
            case WAVE:
                val = 0.53;
                break;
            case SINE:
                val = 0.55;
                break;
            default:
                val = 2;
        }
        if(val != 2) {
            SERVO.setPosition(val);
        }
    }

    /**
     * Sets the {@link Pattern.Color} of the Blinkin driver's connected LEDs. The primary and secondary color dials have no control over this color
     * @param color The color of the LEDs
     */
    public void setColor(Pattern.Color color) {
        double val = 0;
        switch(color) {
            case HOT_PINK:
                val = 0.57;
                break;
            case DARK_RED:
                val = 0.59;
                break;
            case RED:
                val = 0.61;
                break;
            case RED_ORANGE:
                val = 0.63;
                break;
            case ORANGE:
                val = 0.65;
                break;
            case GOLD:
                val = 0.67;
                break;
            case YELLOW:
                val = 0.69;
                break;
            case LAWN_GREEN:
                val = 0.71;
                break;
            case LIME:
                val = 0.73;
                break;
            case DARK_GREEN:
                val = 0.75;
                break;
            case GREEN:
                val = 0.77;
                break;
            case BLUE_GREEN:
                val = 0.79;
                break;
            case AQUA:
                val = 0.81;
                break;
            case SKY_BLUE:
                val = 0.83;
                break;
            case DARK_BLUE:
                val = 0.85;
                break;
            case BLUE:
                val = 0.87;
                break;
            case BLUE_VIOLET:
                val = 0.89;
                break;
            case VIOLET:
                val = 0.91;
                break;
            case WHITE:
                val = 0.93;
                break;
            case GRAY:
                val = 0.95;
                break;
            case DARK_GRAY:
                val = 0.97;
                break;
            case BLACK:
                val = 0.99;
            default:
                val = 2;
        }
        if(val != 2) {
            SERVO.setPosition(val);
        }
    }

    /**
     * Sets the pattern based on its numerical ID, assigned by REV Robotics. They can be found on page 15 of <a href="https://www.revrobotics.com/content/docs/REV-11-1105-UM.pdf">the Blinkin manual</a>.
     * @param id The numerical ID of the pattern
     * @throws IllegalArgumentException The exception thrown when the ID is not a valid Blinkin pattern ID
     */
    public void setPatternById(double id) throws IllegalArgumentException {
        int idi = (int) (id * 100);
        if(idi >= -100 && idi <= 100 && idi % 2 != 0) {
            SERVO.setPosition(id);
        }else{
            throw new IllegalArgumentException(id + " is not a valid Blinkin LED driver ID!");
        }
    }

    // 0.2525-0.7475

    /**
     * Gets the pattern's numerical ID, a value assigned by REV Robotics. They can be found on page 15 of <a href="https://www.revrobotics.com/content/docs/REV-11-1105-UM.pdf">the Blinkin manual</a>.
     * @return The pattern's numerical ID
     */
    public double getPatternById() {
        return SERVO.getPosition();
    }

    /**
     * Returns the internal {@link Servo} of the Blinkin driver. Yes it is a servo, Blinkin drivers are technically servos.
     * @return The Blinkin driver
     */
    public Servo getInternalSensor() {
        return SERVO;
    }

}
