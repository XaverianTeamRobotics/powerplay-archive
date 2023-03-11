package net.xbhs.robotics.HNS;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.ScriptTemplate;

public abstract class NavigationSystem extends ScriptTemplate {
    public NavigationSystem(String name, boolean needsInit) {
        super(name, needsInit);
    }

    @Override
    public void init(ScriptParameters parameters) {
        try {
            start();
        } catch (NavigationSystemException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(ScriptParameters scriptParameters) {
        try {
            update();
        } catch (NavigationSystemException e) {
            throw new RuntimeException(e);
        }
    }

    private Localizer localizer = new Localizer();

    public static class NavigationSystemException extends Exception {
        public NavigationSystemException(String message) {
            super(message);
        }
    }

    public void throwException(String message) throws NavigationSystemException {
        throw new NavigationSystemException(message);
    }


    public abstract void update() throws NavigationSystemException;

    public abstract void reset() throws NavigationSystemException;

    public abstract void stop() throws NavigationSystemException;

    public abstract void start() throws NavigationSystemException;

    public Localizer getLocalizer() {
        return localizer;
    }

    public void setLocalizer(Localizer localizer) {
        this.localizer = localizer;
    }
}
