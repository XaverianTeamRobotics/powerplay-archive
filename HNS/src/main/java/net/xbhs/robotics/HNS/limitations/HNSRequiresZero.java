package net.xbhs.robotics.HNS.limitations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is used to mark classes that require a ZeroProvider for error correction in a Hybrid Navigation System.
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface HNSRequiresZero {}
