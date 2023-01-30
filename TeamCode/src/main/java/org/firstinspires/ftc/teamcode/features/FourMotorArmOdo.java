package org.firstinspires.ftc.teamcode.features;

import com.acmerobotics.dashboard.config.Config;
import org.firstinspires.ftc.teamcode.internals.features.Buildable;
import org.firstinspires.ftc.teamcode.internals.features.Feature;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;
import org.firstinspires.ftc.teamcode.internals.motion.pid.constrained.ProfiledPIDController;
import org.firstinspires.ftc.teamcode.internals.motion.pid.constrained.TrapezoidProfile;
import org.firstinspires.ftc.teamcode.internals.telemetry.logging.DashboardLogging;

@Config
public class FourMotorArmOdo extends Feature implements Buildable {

    public static double a____p = 0.01, b____i = 0, c____d = 0, d____v = 0, e____a = 0;
    public static int f____pos = Position.GROUND;
    public static boolean g____run = false;

    private double lp = a____p, li = b____i, ld = c____d, lv = d____v, la = e____a;
    private TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(d____v, e____a);
    private ProfiledPIDController pid = new ProfiledPIDController(a____p, b____i, c____d, constraints);
    private static class Position { // TODO: you get the idea
        public static int HIGH = 1500;
        public static int MEDIUM = 1000;
        public static int LOW = 0;
        public static int GROUND = 0;
    }

    @Override
    public void build() {
        Devices.encoder5.reset();
        Devices.encoder6.reset();
        Devices.motor5.setPower(0);
        Devices.motor6.setPower(0);
        pid.setTolerance(20, 1);
        DashboardLogging.logData("sp", f____pos);
        DashboardLogging.logData("encoder2", -Devices.encoder5.getPosition());
        DashboardLogging.logData("encoder1", Devices.encoder6.getPosition());
        DashboardLogging.logData("output2", 0);
        DashboardLogging.logData("output1", 0);
        DashboardLogging.update();
    }

    @Override
    public void loop() {
        if(Devices.controller1.getA() || Devices.controller2.getA()) {
            g____run = false;
        }
        double output2 = 0.0;
        double output1 = 0.0;
        updatePID();
        if(g____run && !pid.atSetpoint()) {
            output2 = pid.calculate(-Devices.encoder5.getPosition(), f____pos);
            output1 = pid.calculate(Devices.encoder6.getPosition(), f____pos);
        }
        DashboardLogging.logData("encoder2", -Devices.encoder5.getPosition());
        DashboardLogging.logData("encoder1", Devices.encoder6.getPosition());
        DashboardLogging.logData("output2", output2);
        DashboardLogging.logData("output1", output1);
        DashboardLogging.update();
        Devices.motor4.setPower(-(output2 * 50));
        Devices.motor5.setPower(output2 * 50);
        Devices.motor6.setPower(-(output1 * 50));
        Devices.motor7.setPower(output1 * 50);
    }

    private void updatePID() {
        if(a____p != lp) {
            pid.setP(a____p);
            lp = a____p;
        }
        if(b____i != li) {
            pid.setI(b____i);
            li = b____i;
        }
        if(c____d != ld) {
            pid.setD(c____d);
            ld = c____d;
        }
        if(lv != d____v) {
            constraints.maxVelocity = d____v;
            lv = d____v;
        }
        if(la != e____a) {
            constraints.maxAcceleration = e____a;
            la = e____a;
        }
    }

}
