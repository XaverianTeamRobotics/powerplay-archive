package org.firstinspires.ftc.teamcode.internals.motion.odometry.utils;

import android.os.Environment;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.internals.motion.odometry.OdometrySettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class SettingLoader {

    public static final String PATH = String.format("%s" + File.pathSeparator + "FIRST" + File.pathSeparator + "data" + File.pathSeparator + "settings.odometry", Environment.getExternalStorageDirectory().getAbsolutePath());

    /**
     * Write to the odometry settings file.
     * @return true if the file was successfully written to, false if something went wrong
     */
    private static boolean write() {
        try {
            File file = new File(PATH);
            file.createNewFile();
            FileWriter writer = new FileWriter(file, false);
            writer.write(makeString());
            writer.close();
            return true;
        } catch(IOException exception) {
            return false;
        }
    }

    /**
     * Entires are delimited by a semicolon
     */
    private static String makeString() {
        StringBuilder builder = new StringBuilder();
        builder.append(";");
        for(Field field : OdometrySettings.class.getFields()) {
            try {
                String str = makeEntry(field);
                builder.append(str).append(";");
            } catch(IllegalAccessException exception) {
                System.out.println("Failed to save field with exception '" + exception.getMessage() + "', moving on without saving this field.");
            }
        }
        if(!builder.toString().endsWith(";")) {
            builder.append(";");
        }
        return builder.toString();
    }

    /**
     * Entry syntax:
     * <blockquote>
     *     Field name
     *     <br>
     *     Field type
     *     <br>
     *     Field value 1
     *     <br>
     *     Field value 2
     *     <br>
     *     Field value n...
     *     <br>
     * </blockquote>
     * @throws IllegalAccessException whenever something goes wrong
     */
    private static String makeEntry(Field field) throws IllegalAccessException {
        StringBuilder str = new StringBuilder();
        str.append(System.lineSeparator()).append(field.getName()).append(System.lineSeparator());
        Class<?> type = field.getType();
        if(type == double.class) {
            try {
                str.append("Double").append(System.lineSeparator()).append(field.getDouble(null)).append(System.lineSeparator());
            } catch(NullPointerException e) {
                throw new IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.");
            }
        }else if(type == MotorConfig.class) {
            MotorConfig config = (MotorConfig) field.get(null);
            if(config == null) throw new IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.");
            DcMotorSimple.Direction direction = config.DIRECTION;
            str.append("MotorConfig").append(System.lineSeparator()).append(config.NAME).append(System.lineSeparator());
            if(direction == DcMotorSimple.Direction.FORWARD) {
                str.append("Forward");
            }else{
                str.append("Reverse");
            }
            str.append(System.lineSeparator());
        }else if(type == EncoderConfig.class) {
            EncoderConfig config = (EncoderConfig) field.get(null);
            if(config == null) throw new IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.");
            Encoder.Direction direction = config.DIRECTION;
            str.append("EncoderConfig").append(System.lineSeparator()).append(config.NAME).append(System.lineSeparator());
            if(direction == Encoder.Direction.FORWARD) {
                str.append("Forward");
            }else{
                str.append("Reverse");
            }
            str.append(System.lineSeparator());
        }else if(type == PIDCoefficients.class) {
            PIDCoefficients coefficients = (PIDCoefficients) field.get(null);
            if(coefficients == null) throw new IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed; field value was null.");
            str.append("PIDCoefficents").append(System.lineSeparator()).append(coefficients.kP).append(System.lineSeparator()).append(coefficients.kI).append(System.lineSeparator()).append(coefficients.kD).append(System.lineSeparator());
        }else{
            throw new IllegalAccessException("Entry creation of " + field + " with a type of " + type + " failed.");
        }
        return str.toString();
    }

    /**
     * Read from the odometry settings file.
     * @return the settings as a string, null if something went wrong
     */
    private static String read() {
        try {
            StringBuilder builder = new StringBuilder();
            File file = new File("filename.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            scanner.close();
            return builder.toString();
        } catch(FileNotFoundException exception) {
            return null;
        }
    }

    /**
     * Generate a map of the currently stored settings.
     */
    private static HashMap<String, Value> parseSettings() {
        String file = read();
        if(file != null) {
            HashMap<String, Value> parsed = new HashMap<>();
            String[] items = file.split(";");
            for(String item : items) {
                item = item.trim();
                String[] pieces = file.split(System.lineSeparator());
                Value value;
                ArrayList<String> trimmedPieces = new ArrayList<>();
                for(String piece : pieces) {
                    trimmedPieces.add(piece.trim());
                }
                if(trimmedPieces.size() < 2) {
                    continue;
                }
                switch(trimmedPieces.get(1)) {
                    case "Double":
                        value = new Value(Double.parseDouble(trimmedPieces.get(2)), Double.class);
                        break;
                    case "MotorConfig":
                        if(trimmedPieces.get(3).equals("Forward")) {
                            value = new Value(new MotorConfig(trimmedPieces.get(2), DcMotorSimple.Direction.FORWARD), MotorConfig.class);
                        }else{
                            value = new Value(new MotorConfig(trimmedPieces.get(2), DcMotorSimple.Direction.REVERSE), MotorConfig.class);
                        }
                        break;
                    case "EncoderConfig":
                        if(trimmedPieces.get(3).equals("Forward")) {
                            value = new Value(new EncoderConfig(trimmedPieces.get(2), Encoder.Direction.FORWARD), EncoderConfig.class);
                        }else{
                            value = new Value(new EncoderConfig(trimmedPieces.get(2), Encoder.Direction.REVERSE), EncoderConfig.class);
                        }
                        break;
                    case "PIDCoefficents":
                        value = new Value(new PIDCoefficients(Double.parseDouble(trimmedPieces.get(2)), Double.parseDouble(trimmedPieces.get(3)), Double.parseDouble(trimmedPieces.get(4))), PIDCoefficients.class);
                        break;
                    default:
                        continue;
                }
                parsed.put(trimmedPieces.get(0), value);
            }
            return parsed;
        }else{
            throw new SettingLoaderFailureException("Settings file not found!");
        }
    }

    /**
     * Update {@link OdometrySettings} with the settings stored in the odometry settings file.
     */
    private static void setSettings() {
        HashMap<String, Value> settings = parseSettings();
        for(Object keyO : settings.keySet().toArray()) {
            try {
                String key = (String) keyO;
                OdometrySettings.class.getField(key).set(null, Objects.requireNonNull(settings.get(key)).obj);
            } catch(NoSuchFieldException | IllegalAccessException | NullPointerException | ExceptionInInitializerError | IllegalArgumentException e) {
                String key = (String) keyO;
                System.out.println(key + " not found in Odometry settings, so I'm skipping it.");
            } catch(SecurityException e) {
                throw new SettingLoaderFailureException("Odometry settings class failed security check.");
            } catch(ClassCastException e) {
                System.out.println("Key was not a string, so I'm skipping it.");
            }
        }
    }

    private static class Value {

        public final Object obj;
        public final Class<?> clazz;

        public Value(Object object, Class<?> clazz) {
            obj = object;
            this.clazz = clazz;
        }

    }

    /**
     * Save the current odometry settings.
     */
    public static void save() {
        try {
            write();
        } catch(Exception e) {
            throw new SettingLoaderFailureException("Saving settings failed.", e);
        }
    }

    /**
     * Load the odometry settings from the most recent save.
     */
    public static void load() {
        try {
            setSettings();
        } catch(Exception e) {
            throw new SettingLoaderFailureException("Loading settings failed.", e);
        }
    }

}
