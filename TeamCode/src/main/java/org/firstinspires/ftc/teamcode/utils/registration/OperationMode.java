package org.firstinspires.ftc.teamcode.utils.registration;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.ScriptRunner;
import com.michaell.looping.ScriptTemplate;
import com.michaell.looping.builtin.ConvertToScript;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.utils.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.utils.hardware.Logging;

import java.util.HashMap;
import java.util.List;

/**
 * An {@link OperationMode} represents a program the robot can run.
 * <br>
 * <br>
 * To make an {@link OperationMode}, make a class extending extending this one, implement the necessary methods, and inplement {@link AutonomousOperation} or {@link TeleOperation} to determine the operation type. If both are implemented, {@link AutonomousOperation} will take priority.
 * <br>
 * <br>
 * To disable an {@link OperationMode}, annotate it with {@link Disabled}.
 * @see OperationModeRegistrar
 */
public abstract class OperationMode extends LinearOpMode {

    private double timestamp = 0;
    private HashMap<String, Double> otherTimestamps = new HashMap<>();

    public ScriptParameters environment;

    @Override
    public void runOpMode() throws InterruptedException {
        Logging.setTelemetry(telemetry);
        HardwareGetter.setEmulated(false);
        HardwareGetter.setHardwareMap(hardwareMap);
        ScriptTemplate jloopingScript;
        ScriptRunner runner;
        try {
            jloopingScript = new ConvertToScript(this.getClass().getName(), this,
                "absolutelyNothing", "run");
            runner = new ScriptRunner();
            runner.addScript(jloopingScript);
            this.environment = runner.scriptParametersGlobal;
            HardwareGetter.setJloopingRunner(runner);
            HardwareGetter.makeGamepadRequest("gamepad1", gamepad1);
            HardwareGetter.makeGamepadRequest("gamepad2", gamepad2);
            HardwareGetter.initAllDevices();

            // Set the Caching mode to auto. This allows for faster access of all sensors
            // The cache gets cleared whenever a call to a sensor is repeated
            // This does NOT effect writing data to a device
            List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

            for (LynxModule hub : allHubs) {
                hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
            }
        } catch (NoSuchMethodException | ScriptRunner.DuplicateScriptException e) {
            throw new RuntimeException(e);
        }
        // set environment variables
        // tell user-defined code of the opmode to construct itself
        construct();
        // wait until the opmode is executed
        waitForStart();
        resetRuntime();
        // run the opmode using jlooping
        while (opModeIsActive()) {
            runner.runOneScript();
            this.environment = runner.scriptParametersGlobal;
        }

        // tell the app to stop this opmode
        requestOpModeStop();
    }

    /**
     * Updates the timestamp stored in the {@link OperationMode} to the current time.
     */
    public void updateDefaultTimestamp() {
        timestamp = time;
    }

    /**
     * Updates the timestamp stored in the {@link OperationMode} to a specific time.
     * @param time The time to set
     */
    public void updateDefaultTimestamp(double time) {
        timestamp = time;
    }

    /**
     * Gets the most recent timestamp set by {@link #updateDefaultTimestamp()} or {@link #updateDefaultTimestamp(double)}, or 0 if the timestamp has never been set.
     * @return The most recent timestamp
     */
    public double getDefaultTimestamp() {
        return timestamp;
    }

    /**
     * Updates a named timestamp to the current time. If a timestamp does not exist with this name, the timestamp is created.
     * @param name The name of the timestamp
     */
    public void updateNamedTimestamp(String name) {
        otherTimestamps.put(name, time);
    }

    /**
     * Updates a named timestamp to a specific time. If a timestamp does not exist with this name, the timestamp is created.
     * @param name The name of the timestamp
     * @param time The time to set
     */
    public void updateNamedTimestamp(String name, double time) {
        otherTimestamps.put(name, time);
    }

    /**
     * Gets the most recent timestamp with a certain name, or 0 if the timestamp has never been set.
     * @param name The name of the timestamp
     * @return The most recent timestamp with that name
     */
    public Double getNamedTimestamp(String name) {
        Double timestamp = otherTimestamps.get(name);
        if(timestamp == null) {
            timestamp = 0D;
        }
        return timestamp;
    }

    /**
     * The method to be called at the start of the {@link OperationMode}'s operation, after the INIT button is pressed but before the PLAY button is pressed. This will run once. You do not need to call {@link #waitForStart()}, the {@link OperationMode} will do this automatically after this method is finished.
     */
    public abstract void construct();

    /**
     * The method to be called during the {@link OperationMode}'s operation, after the PLAY button is pressed but before the STOP button is pressed. This will run constantly until the {@link OperationMode} ends.
     */
    public abstract void run();

    public void absolutelyNothing() {
        // do absolutely nothing
    }
}