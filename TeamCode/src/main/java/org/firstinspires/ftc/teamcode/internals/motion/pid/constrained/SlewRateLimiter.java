// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.firstinspires.ftc.teamcode.internals.motion.pid.constrained;

import com.qualcomm.robotcore.util.Range;

/**
 * A class that limits the rate of change of an input value. Useful for implementing voltage,
 * setpoint, and/or output ramps. A slew-rate limit is most appropriate when the quantity being
 * controlled is a velocity or a voltage; when controlling a position, consider using a {@link TrapezoidProfile} instead.
 */
public class SlewRateLimiter {
    private double m_positiveRateLimit;
    private double m_negativeRateLimit;
    private double m_prevVal;
    private double m_prevTime;

    /**
     * Creates a new SlewRateLimiter with the given positive and negative rate limits and initial
     * value.
     *
     * @param positiveRateLimit The rate-of-change limit in the positive direction, in units per
     *     second. This is expected to be positive.
     * @param negativeRateLimit The rate-of-change limit in the negative direction, in units per
     *     second. This is expected to be negative.
     * @param initialValue The initial value of the input.
     */
    public SlewRateLimiter(double positiveRateLimit, double negativeRateLimit, double initialValue) {
        m_positiveRateLimit = positiveRateLimit;
        m_negativeRateLimit = negativeRateLimit;
        m_prevVal = initialValue;
        m_prevTime = System.nanoTime() * 1e-9;
    }

    /**
     * Creates a new SlewRateLimiter with the given positive rate limit and negative rate limit of
     * -rateLimit and initial value.
     *
     * @param rateLimit The rate-of-change limit, in units per second.
     * @param initalValue The initial value of the input.
     * @deprecated Use SlewRateLimiter(double positiveRateLimit, double negativeRateLimit, double
     *     initalValue) instead.
     */
    public SlewRateLimiter(double rateLimit, double initalValue) {
        this(rateLimit, -rateLimit, initalValue);
    }

    /**
     * Creates a new SlewRateLimiter with the given positive rate limit and negative rate limit of
     * -rateLimit.
     *
     * @param rateLimit The rate-of-change limit, in units per second.
     */
    public SlewRateLimiter(double rateLimit) {
        this(rateLimit, -rateLimit, 0);
    }

    /**
     * Filters the input to limit its slew rate.
     *
     * @param input The input value whose slew rate is to be limited.
     * @return The filtered value, which will not change faster than the slew rate.
     */
    public double calculate(double input) {
        double currentTime = System.nanoTime() * 1e-9;
        double elapsedTime = currentTime - m_prevTime;
        m_prevVal +=
            Range.clip(
                input - m_prevVal,
                m_negativeRateLimit * elapsedTime,
                m_positiveRateLimit * elapsedTime);
        m_prevTime = currentTime;
        return m_prevVal;
    }

    /**
     * Resets the slew rate limiter to the specified value; ignores the rate limit when doing so.
     *
     * @param value The value to reset to.
     */
    public void reset(double value) {
        m_prevVal = value;
        m_prevTime = System.nanoTime() * 1e-9;
    }

    public void setPositiveRateLimit(double rate) {
        m_positiveRateLimit = rate;
    }

    public void setNegativeRateLimit(double rate) {
        m_negativeRateLimit = rate;
    }

    public void setRateLimit(double rate) {
        m_positiveRateLimit = rate;
        m_negativeRateLimit = -rate;
    }

    public double getPositiveRateLimit(double rate) {
        return m_positiveRateLimit;
    }

    public double getNegativeRateLimit(double rate) {
        return m_negativeRateLimit;
    }

}