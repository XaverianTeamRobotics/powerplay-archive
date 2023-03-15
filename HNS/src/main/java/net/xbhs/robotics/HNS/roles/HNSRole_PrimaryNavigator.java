package net.xbhs.robotics.HNS.roles;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is used to mark classes that are the primary navigator for a Hybrid Navigation System.
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface HNSRole_PrimaryNavigator {}
