package net.xbhs.robotics.HNS;

import com.michaell.looping.ScriptParameters;
import com.michaell.looping.ScriptRunner;
import com.michaell.looping.ScriptTemplate;

public abstract class NavigationSystem extends ScriptTemplate {
    public NavigationSystem(String name) {
        super(name, false);
    }

    @Override
    public void run(ScriptParameters scriptParameters) {
        try {
            update();
        } catch (NavigationSystemException e) {
            throw new RuntimeException(e);
        }
    }

    protected Localizer localizer = new Localizer();

    public static class NavigationSystemException extends Exception {
        public NavigationSystemException(String message) {
            super(message);
        }

        public NavigationSystemException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public void throwException(String message) throws NavigationSystemException {
        throw new NavigationSystemException(message);
    }


    public abstract void update() throws NavigationSystemException;

    public void reset() throws NavigationSystemException {
        this.localizer = new Localizer();
    }

    public abstract void start() throws NavigationSystemException;

    public abstract void correct(Localizer localizer) throws NavigationSystemException;

    public Localizer getLocalizer() {
        return localizer;
    }

    public void setLocalizer(Localizer localizer) {
        this.localizer = localizer;
    }

    public void register(ScriptRunner runner) throws ScriptRunner.DuplicateScriptException, NavigationSystemException {
        start();
        runner.addScript(this);
    }
}
