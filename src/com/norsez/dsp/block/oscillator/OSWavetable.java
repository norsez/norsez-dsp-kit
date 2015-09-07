package com.norsez.dsp.block.oscillator;

import com.norsez.dsp.block.DSPBlock;
import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.filter.SmoothingFilter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class OSWavetable
        extends DSPBlock {

    public double phase, phase_;
    private double[] wavetable;
    private double cps;
    private double drive;
    private SmoothingFilter lp;

    private int mask, phase_1, phase_0;

    //oversampling times
    private final int OS_TIMES;

    //nyquist frequency in cps with respect to oversampling frequency
    private final double NYQUIST_OS_CPS;

    private double[] buffer;

    public OSWavetable(int oversamplingTimes) {

        OS_TIMES = oversamplingTimes;
        NYQUIST_OS_CPS = 0.5 * DSPSystem.getSamplingRate() / (OS_TIMES);
        buffer = new double[OS_TIMES];

        lp = new SmoothingFilter();
        lp.setCutoff(NYQUIST_OS_CPS);

        setDrive(1);
    }

    /**
     * set CPS of oscillator
     */
    public void setCps(double cps) {

        //convert cps in sampling rate to cps in oversampling rate
        this.cps = cps;
    }

    public void setWavetable(double[] w) {
        mask = (w.length - 1);
        wavetable = w;
    }

    public void setDrive(double d) {
        drive = d;
        lp.setCutoff(drive * NYQUIST_OS_CPS);
    }

    public double tick() {

        //make sample
        buffer[0] = getSample();

        //stuffing zeros
        for (int i = 1; i < OS_TIMES; i++) {
            buffer[i] = 0;
        }

        //filter
        for (int i = 0; i < OS_TIMES; i++) {
            buffer[i] = lp.tick(buffer[i]);
        }

        //throw away the rest
        lastValue = buffer[0];

        return lastValue;
    }

    private double temp;

    public double getSample() {
        while (phase > 1) {
            phase -= 1.0;

        }
        phase_ = phase * mask;
        phase_1 = (int) Math.ceil(phase_);
        phase_0 = (int) (phase_);

        temp = (wavetable[phase_1] - wavetable[phase_0]) * (phase_ - phase_0) +
                wavetable[phase_0];

        phase += cps;

        return temp;
    }

}