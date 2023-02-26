package org.firstinspires.ftc.teamcode.internals.features;

import org.firstinspires.ftc.teamcode.internals.registration.OperationMode;

/**
 * Represents a buildable feature.<br>
 * When this is implemented in a feature, one of two things will happen:
 * <ol>
 *  <li>
 *      If the feature was registered outside of {@link OperationMode#construct()}, the feature will run {@link Buildable#build()} on its first execution instead of {@link Feature#loop()}. It will then run {@link Feature#loop()} every execution thereafter.
 *  </li>
 *  <li>
 *      If the feature was registered inside {@link OperationMode#construct()}, the feature will run {@link Buildable#build()} directly after {@link OperationMode#construct()} runs. It will run {@link Feature#loop()} on every "real" execution.
 *  </li>
 * </ol>
 */
public interface Buildable {
    /**
     * The method to run when a feature is being built.
     */
    void build();
}
