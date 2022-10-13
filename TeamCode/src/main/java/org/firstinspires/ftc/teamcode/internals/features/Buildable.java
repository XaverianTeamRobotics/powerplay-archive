package org.firstinspires.ftc.teamcode.internals.features;

/**
 * Represents a buildable feature. When this is implemented in a feature, the feature will run this method on its first execution instead of its loop method. It will then run its loop method every execution thereafter.
 */
public interface Buildable {
    /**
     * The method to run when a feature is being built.
     */
    void build();
}
