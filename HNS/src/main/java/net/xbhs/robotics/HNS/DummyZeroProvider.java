package net.xbhs.robotics.HNS;

import net.xbhs.robotics.HNS.roles.HNSRole_ZeroProvider;

@HNSCompatible
@HNSRole_ZeroProvider
public class DummyZeroProvider extends NavigationSystem{
    public DummyZeroProvider() {
        super("Dummy Zero Provider");
    }

    @Override
    public void update() throws NavigationSystemException {
        localizer = new Localizer(1,1,1,1,1,1,1,1,1);
    }

    @Override
    public void start() throws NavigationSystemException {

    }

    @Override
    public void correct(Localizer localizer) throws NavigationSystemException {

    }
}
