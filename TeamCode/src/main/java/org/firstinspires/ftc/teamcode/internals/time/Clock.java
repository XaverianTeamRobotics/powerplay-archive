package org.firstinspires.ftc.teamcode.internals.time;

import java.util.HashMap;

/**
 * Manages {@link Timer}s.
 */
public class Clock {

    private static final HashMap<String, Timer> timers = new HashMap<>();

    /**
     * Makes a new timer. If a timer with this name already exists, it will be overwritten.
     */
    public static void make(String name) {
        timers.put(name, new Timer(name));
    }

    /**
     * Gets the timer with the specified name, or null if no timer with that name exists.
     */
    public static Timer get(String name) {
        return timers.get(name);
    }

    /**
     * Sleeps the current thread synchronously. Avoid this unless your goal is specifically to sleep synchronously. If you can use a timer to do it asynchronously, please do.
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
