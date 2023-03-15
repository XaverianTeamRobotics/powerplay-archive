package net.xbhs.robotics.HNS;

import net.xbhs.robotics.HNS.roles.HNSRole_PrimaryNavigator;
import net.xbhs.robotics.HNS.roles.HNSRole_ZeroProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * This annotation is used to mark classes that are compatible with a Hybrid Navigation System.
 * <p>
 * Note that this must be accompanied by either {@link HNSRole_ZeroProvider} or {@link HNSRole_PrimaryNavigator}
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface HNSCompatible {}
