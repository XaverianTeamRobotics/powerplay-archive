package org.firstinspires.ftc.teamcode.internals.time;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * Manages {@link Timer}s.
 */
public class Clock {

    private static final Timer internalTimer = new Timer("");

    private static final HashMap<String, Timer> timers = new HashMap<>();

    /**
     * Makes a new timer. If a timer with this name already exists, it will be overwritten.
     */
    public static Timer make(String name) {
        Timer timer = new Timer(name);
        timers.put(name, timer);
        return timer;
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

    /**
     * Blocks the current thread until the given supplier returns true. Avoid this unless your goal is specifically to block synchronously. If you can use a timer to do it asynchronously, please do.
     */
    public static void block(Supplier<Boolean> until) {
        while(!until.get());
    }

}
