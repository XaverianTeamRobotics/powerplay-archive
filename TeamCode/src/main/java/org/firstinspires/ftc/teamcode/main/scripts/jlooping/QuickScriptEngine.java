package org.firstinspires.ftc.teamcode.main.scripts.jlooping;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.ScriptRunner;
import com.michaell.looping.ScriptTemplate;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.main.scripts.jlooping.scripts.MecanumConfigurableRoutine;
import org.firstinspires.ftc.teamcode.main.scripts.jlooping.scripts.OpModeStopper;

public class QuickScriptEngine {
    private ScriptRunner runner;
    private LinearOpMode opMode;

    // Constructor
    public QuickScriptEngine(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    // Constructor
    public QuickScriptEngine() {
        this.opMode = null;
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

    public void addMecanumConfigurableRoutine() throws ScriptRunner.DuplicateScriptException, NullPointerException {
        if (this.runner != null) {
            this.runner.addScript(new MecanumConfigurableRoutine("mecanumConfigurableRoutine", this.opMode));
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
}
