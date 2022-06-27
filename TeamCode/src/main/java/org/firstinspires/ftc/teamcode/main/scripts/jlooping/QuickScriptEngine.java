package org.firstinspires.ftc.teamcode.main.scripts.jlooping;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.ScriptRunner;
import com.michaell.looping.ScriptTemplate;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.main.scripts.jlooping.scripts.MecanumConfigurableRoutine;
import org.firstinspires.ftc.teamcode.main.scripts.jlooping.scripts.OpModeStopper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class QuickScriptEngine {
    private ScriptRunner runner;
    private LinearOpMode opMode;
    private ArrayList<MecanumDriveCommand> commandQueue;

    // Constructor
    public QuickScriptEngine(@NotNull LinearOpMode opMode) {
        this.opMode = opMode;
        generateBlankRunner();
    }

    /**
     * Generates a new ScriptRunner and overrides an existing one if it exists.
     */
    public void generateBlankRunner() {
        this.runner = new ScriptRunner();
    }

    /**
     * Adds a script to the runner.
     * @param script The script to add
     * @throws ScriptRunner.DuplicateScriptException Thrown if the script already exists in the runner.
     * @throws NullPointerException Thrown if the runner is null.
     */
    public void addScriptToRunner(ScriptTemplate script) throws ScriptRunner.DuplicateScriptException, NullPointerException {
        if (this.runner != null) {
            this.runner.addScript(script);
        } else {
            throw new NullPointerException("Runner is null");
        }
    }

    /**
     * Adds an OpModeStopper to the runner.
     * @throws ScriptRunner.DuplicateScriptException Thrown if the script already exists in the runner.
     * @throws NullPointerException Thrown if the runner is null.
     */
    public void addOpModeStopper() throws ScriptRunner.DuplicateScriptException, NullPointerException {
        if (this.runner != null) {
            this.runner.addScript(new OpModeStopper("opModeStopper", this.opMode));
        } else {
            throw new NullPointerException("Runner is null");
        }
    }

    /**
     * Adds a MecanumConfigurableRoutine to the runner, as well as an OpModeStopper.
     * @throws ScriptRunner.DuplicateScriptException Thrown if the script already exists in the runner.
     * @throws NullPointerException Thrown if the runner is null.
     */
    public void addMecanumConfigurableRoutine() throws ScriptRunner.DuplicateScriptException, NullPointerException {
        if (this.runner != null) {
            this.runner.addScript(new MecanumConfigurableRoutine("mecanumConfigurableRoutine", this.opMode));
            this.runner.addScript(new OpModeStopper("opModeStopper", this.opMode));
        } else {
            throw new NullPointerException("Runner is null");
        }
    }

    /**
     * Runs the runner.
     * @throws NullPointerException Thrown if the runner is null.
     */
    public void run() throws NullPointerException {
        if (this.runner != null) {
            this.runner.runConstantly();
        } else {
            throw new NullPointerException("Runner is null");
        }
    }

    /**
     * Runs all scripts once in the runner.
     * @throws NullPointerException Thrown if the runner is null.
     */
    public void runOneTime() throws NullPointerException {
        if (this.runner != null) {
            this.runner.runOneScript();
        } else {
            throw new NullPointerException("Runner is null");
        }
    }

    /**
     * Changed the drive values of the Mecanum Configurable Routine.
     * @param vertical The vertical drive value.
     * @param horizontal The horizontal drive value.
     * @param pivot The pivot drive value.
     * @throws NullPointerException Thrown if the runner is null.
     */
    public void changeMecanumDriveValues(float vertical, float horizontal, float pivot) throws NullPointerException {
        if (this.runner != null) {
            try {
                this.runner.getScriptByName("mecanumConfigurableRoutine");
            } catch (Exception ignored) {
                return;
            }

            try {
                this.runner.scriptParametersGlobal.getGlobalVariable("mecanumConfigVerticalAxis").setValue((Float) vertical);
                this.runner.scriptParametersGlobal.getGlobalVariable("mecanumConfigHorizontalAxis").setValue((Float) horizontal);
                this.runner.scriptParametersGlobal.getGlobalVariable("mecanumConfigPivotAxis").setValue((Float) pivot);
            } catch (Exception ignored) {}
        } else {
            throw new NullPointerException("Runner is null");
        }
    }

    /**
     * Changes the drive values of the Mecanum Configurable Routine.
     * @param command The values of vertical, horizontal, and pivot.
     * @throws NullPointerException Thrown if the runner is null.
     */
    public void changeMecanumDriveValues(MecanumDriveCommand command) throws NullPointerException {
        changeMecanumDriveValues(command.getVertical(), command.getHorizontal(), command.getPivot());
    }

    /**
     * Runs the mecanum drive once
     * @throws NullPointerException Thrown if the runner is null.
     */
    public void runMecanumDriveOnce() throws NullPointerException {
        if (this.runner != null) {
            try {
                this.runner.getScriptByName("mecanumConfigurableRoutine");
            } catch (Exception ignored) {
                return;
            }

            try {
                int before = 0;
                int after = 0;

                do {
                    before = (int) this.runner.scriptParametersGlobal.getGlobalVariable("mecanumConfigTimesRun").getValue();
                    this.runOneTime();
                    after = (int) this.runner.scriptParametersGlobal.getGlobalVariable("mecanumConfigTimesRun").getValue();
                } while (before == after);
            } catch (ScriptParameters.VariableNotFoundException e) {
                while (true) {
                    this.runOneTime();
                    for (ScriptParameters.GlobalVariable variable:
                         this.runner.scriptParametersGlobal.globalVariables) {
                        if (variable.name.equals("mecanumConfigRoutineAvailable")) {
                            if ((boolean) variable.getValue()) {
                                runMecanumDriveOnce();
                                return;
                            }
                        }
                    }
                }
            }
        } else {
            throw new NullPointerException("Runner is null");
        }
    }

    /**
     * Set the ScriptRunner to be used.
     * @param runner The runner to set
     */
    public void setRunner(ScriptRunner runner) {
        this.runner = runner;
    }

    private LinearOpMode getOpMode() {
        return this.opMode;
    }

    /**
     * CAN BE NULL
     * @return Why do I need to make docs for this?
     */
    public ScriptRunner getRunner() {
        return this.runner;
    }

    public void queueCommand(MecanumDriveCommand command) {
        this.commandQueue.add(command);
    }

    /**
     * Runs the command queue.
     * @throws NullPointerException Thrown if the runner is null.
     */
    public void runQueue() throws NullPointerException {
        while (this.commandQueue.size() > 0) {
            MecanumDriveCommand command = this.commandQueue.get(0);
            this.changeMecanumDriveValues(command);
            this.runMecanumDriveOnce();
            this.commandQueue.remove(0);
            this.opMode.sleep(command.getTime());
            if (this.opMode.isStopRequested()) {
                return;
            }
        }
    }

    public static class MecanumDriveCommand {
        public float vertical;
        public float horizontal;
        public float pivot;
        public long time;

        public MecanumDriveCommand(float vertical, float horizontal, float pivot, long time) throws IllegalArgumentException {
            if (vertical < -1 || vertical > 1 || horizontal < -1 || horizontal > 1 || pivot < -1 || pivot > 1) {
                throw new IllegalArgumentException("Invalid drive values");
            }
            this.vertical = vertical;
            this.horizontal = horizontal;
            this.pivot = pivot;
            this.time = time;
        }

        public float getHorizontal() {
            return this.horizontal;
        }

        public float getVertical() {
            return this.vertical;
        }

        public float getPivot() {
            return this.pivot;
        }

        public long getTime() {
            return this.time;
        }
    }
}
