package org.firstinspires.ftc.teamcode.internals.motion.HNS;

import net.xbhs.robotics.HNS.HNSCompatible;
import net.xbhs.robotics.HNS.Localizer;
import net.xbhs.robotics.HNS.NavigationSystem;
import net.xbhs.robotics.HNS.roles.HNSRole_ZeroProvider;
import org.firstinspires.ftc.teamcode.internals.hardware.Devices;

@HNSCompatible
@HNSRole_ZeroProvider
public class TestZeroProvider extends NavigationSystem {
    public TestZeroProvider() {
        super("Test Zero Provider");
    }

    @Override
    public void update() throws NavigationSystemException {
        if (Devices.controller1.getA()) this.localizer = new Localizer();
        else this.localizer = new Localizer(1,1,1,1,1,1,1,1,1);
    }

    @Override
    public void start() throws NavigationSystemException {

    }

    @Override
    public void correct(Localizer localizer) throws NavigationSystemException {
        this.localizer = localizer;
    }
}
