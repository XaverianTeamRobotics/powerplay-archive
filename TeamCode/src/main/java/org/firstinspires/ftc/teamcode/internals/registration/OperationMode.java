package org.firstinspires.ftc.teamcode.internals.registration;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.ScriptRunner;
import com.michaell.looping.ScriptTemplate;
import com.michaell.looping.builtin.ConvertToScript;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.HardwareGetter;
import org.firstinspires.ftc.teamcode.internals.telemetry.Logging;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

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
            HardwareGetter.initStdDevices();

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

    /**
     * Registers a {@link Feature}, appending it to the runner's script queue to be ran by jlooping.
     * @param feature The feature to register.
     * @throws ScriptRunner.DuplicateScriptException Thrown when a script with the same name already exists. Features' names come from their class names.
     */
    public static void registerFeature(@NotNull Feature feature) throws ScriptRunner.DuplicateScriptException {
        Objects.requireNonNull(HardwareGetter.getJloopingRunner()).addScript(feature);
    }

}