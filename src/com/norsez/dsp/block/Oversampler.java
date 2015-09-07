package com.norsez.dsp.block;

import com.norsez.dsp.block.filter.Filter;
import com.norsez.dsp.block.filter.SmoothingFilter;

/**
 * <p>Title: Oversampler </p>
 * <p>Description: Encapsulates the process of oversampling. A process that need oversampling should extends from
 * this this class. Override the getOneSample ( double ). Then call tick ( double ) to generate
 * one oversampled sample of the desired signal generation. </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public abstract class Oversampler {

    private SmoothingFilter lp;

    protected final double OS_RATE;
    protected final int TIMES;

    protected double[] buffer;

    public Oversampler(int oversamplingTimes) {
        OS_RATE = oversamplingTimes * DSPSystem.getSamplingRate();

        TIMES = oversamplingTimes;
        buffer = new double[TIMES];

        lp = new SmoothingFilter();
        lp.setMode(Filter.Mode.LP);
        lp.setCutoff(DSPSystem.NYQUIST_CPS * DSPSystem.getSamplingRate() / OS_RATE);

    }

    /**
     * Override this method with the process the way you would do in non-oversampling situation.
     * The tick (double) method calls getOneSample internally. Therefore, the output of tick(double)
     * is the oversampled version of this process.
     */
    protected abstract double getOneSample(double in);

    private double temp;

    final public double tick(double in) {
        buffer[0] = temp;

        //stuffing zeros
        for (int i = 1; i < TIMES; i++) {
            buffer[i] = 0;
        }

        //process
        for (int i = 0; i < TIMES; i++) {
            buffer[i] = getOneSample(in);
        }

        //filter
        for (int i = 0; i < TIMES; i++) {
            buffer[i] = lp.tick(buffer[i]);
        }

        return buffer[0];
    }
}