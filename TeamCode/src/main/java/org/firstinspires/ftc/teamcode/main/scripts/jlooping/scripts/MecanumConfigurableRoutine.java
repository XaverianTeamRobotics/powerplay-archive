package org.firstinspires.ftc.teamcode.main.scripts.jlooping.scripts;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.builtin.ConditionalScriptTemplate;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class MecanumConfigurableRoutine extends ConditionalScriptTemplate {
    private DcMotor FrontRight;
    private DcMotor BackRight;
    private DcMotor FrontLeft;
    private DcMotor BackLeft;
    private int driveDirection = 0;
    private LinearOpMode opMode;

    float vertical;
    float horizontal;
    float pivot;

    public MecanumConfigurableRoutine(String name, LinearOpMode opMode) {
        super(name, true);
        this.opMode = opMode;
    }

    @Override
    public void init(ScriptParameters parameters) {
        FrontRight = opMode.hardwareMap.get(DcMotor.class, "Front Right");
        BackRight = opMode.hardwareMap.get(DcMotor.class, "Back Right");
        FrontLeft = opMode.hardwareMap.get(DcMotor.class, "Front Left");
        BackLeft = opMode.hardwareMap.get(DcMotor.class, "Back Left");

        // Put initialization blocks here.
        FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        BackRight.setDirection(DcMotorSimple.Direction.REVERSE);

        ScriptParameters.GlobalVariable<Boolean> mecanumConfigRoutineAvailable = new ScriptParameters.GlobalVariable<>("mecanumConfigRoutineAvailable");
        mecanumConfigRoutineAvailable.setValue(true);
        parameters.addGlobalVariable(mecanumConfigRoutineAvailable);

        ScriptParameters.GlobalVariable<Integer> mecanumConfigVerticalAxis = new ScriptParameters.GlobalVariable<>("mecanumConfigVerticalAxis");
        mecanumConfigVerticalAxis.setValue(0);
        parameters.addGlobalVariable(mecanumConfigVerticalAxis);

        ScriptParameters.GlobalVariable<Float> mecanumConfigHorizontalAxis = new ScriptParameters.GlobalVariable<>("mecanumConfigHorizontalAxis");
        mecanumConfigHorizontalAxis.setValue(0.0f);
        parameters.addGlobalVariable(mecanumConfigHorizontalAxis);

        ScriptParameters.GlobalVariable<Float> mecanumConfigPivotAxis = new ScriptParameters.GlobalVariable<>("mecanumConfigPivotAxis");
        mecanumConfigPivotAxis.setValue(0.0f);
        parameters.addGlobalVariable(mecanumConfigPivotAxis);

        ScriptParameters.GlobalVariable<Float> mecanumConfigTimesRun = new ScriptParameters.GlobalVariable<>("mecanumConfigTimesRun");
        mecanumConfigTimesRun.setValue(0.0f);
        parameters.addGlobalVariable(mecanumConfigTimesRun);

        needsInit = false;
    }

    @Override
    public void toRun(ScriptParameters scriptParameters) {
        // Put loop blocks here.
        try {
            vertical = (float) scriptParameters.getGlobalVariable("mecanumConfigVerticalAxis").getValue();
            horizontal = (float) scriptParameters.getGlobalVariable("mecanumConfigHorizontalAxis").getValue();
            pivot = (float) scriptParameters.getGlobalVariable("mecanumConfigPivotAxis").getValue();
        } catch (ScriptParameters.VariableNotFoundException e) {
            e.printStackTrace();
        }
        FrontRight.setPower(-pivot + (vertical - horizontal));
        BackRight.setPower(-pivot + vertical + horizontal);
        FrontLeft.setPower(pivot + vertical + horizontal);
        BackLeft.setPower(pivot + (vertical - horizontal));
        opMode.telemetry.addData("Vert", vertical);
        opMode.telemetry.addData("Horiz", horizontal);
        opMode.telemetry.addData("Pivot", pivot);
        opMode.telemetry.addData("Drive Direction", driveDirection);
        opMode.telemetry.update();
    }

    @Override
    public boolean shouldRun(ScriptParameters scriptParameters) {
        return opMode.opModeIsActive() && opMode.isStarted();
    }
}
