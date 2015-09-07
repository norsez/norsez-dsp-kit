package com.norsez.dsp.block.oscillator;

import com.norsez.dsp.block.DSPBlock;
import com.norsez.dsp.block.DSPSystem;

/**
 * <p>Title: AntiAliasedWavetable </p>
 * <p>Description: Oscillator class that uses 2D wavetable to generate bandlimited signal.
 * The edges of the wave can be adjusted by the setDrive () method. </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class AntiAliasedWavetable
        extends DSPBlock {
    protected double cps, phase, phase_, partial_, temp1, temp0;
    protected double[][] wavetable;
    protected int curPartial, phase_0, phase_1, partial_0, partial_1;

    protected int mask;

    // defines nyquist frequency in cps.
    protected static final double NYQUIST_CPS = DSPSystem.NYQUIST_CPS;

    // defines the drive that scale the nyquist frequency to reduce wave edges.
    protected double drive;

    public AntiAliasedWavetable() {
        setDrive(1);
    }

    /**
     * Set the brightness (edges) of the wave.
     */
    public void setDrive(double drive) {
        this.drive = drive;
    }

    public double getCps() {
        return cps;
    }

    public void setCps(double cps) {
        this.cps = cps;
        partial_ = drive * NYQUIST_CPS / cps;

        if (partial_ >= wavetable.length) {
            partial_ = wavetable.length - 1;

        }
        partial_0 = (int) (partial_);
        partial_1 = (int) Math.ceil(partial_);

        curPartial = (int) (partial_);
    }

    public void setWavetable(double[][] w) {
        mask = (w[0].length - 1);
        wavetable = w;
    }

    public double getPhase() {
        return phase;
    }

    public double tick() {

        while (phase > 1) {
            phase -= 1.0;
        }
        phase_ = phase * mask;
        phase_1 = (int) Math.ceil(phase_);
        phase_0 = (int) (phase_);
        phase += cps;
        lastValue = (wavetable[partial_0][phase_1] - wavetable[partial_0][phase_0])
                * (phase_ - phase_0) + wavetable[partial_0][phase_0];
        return lastValue;
    }

    private double shifted_phase;
    private int shifted_phase_;

    public double tickPhaseSubtraction(double pdiff) {

        //do normal phase
        while (phase > 1) {
            phase -= 1.0;
        }

        if (pdiff > 1) pdiff = 1;
        if (pdiff < 0) pdiff = 0;

        phase_ = phase * mask;
        phase_1 = (int) Math.ceil(phase_);
        phase_0 = (int) (phase_);

        lastValue = (wavetable[partial_0][phase_1] - wavetable[partial_0][phase_0])
                * (phase_ - phase_0) + wavetable[partial_0][phase_0];

        //do shifted phase
        shifted_phase = phase + pdiff;
        while (shifted_phase > 1) {
            shifted_phase -= 1.0;
        }
        phase_ = shifted_phase * mask;
        phase_1 = (int) Math.ceil(phase_);
        phase_0 = (int) (phase_);

        //subtract each other
        lastValue -= (wavetable[partial_0][phase_1] - wavetable[partial_0][phase_0])
                * (phase_ - phase_0) + wavetable[partial_0][phase_0];

        phase += cps;
        return lastValue;
    }

    /*
       public void process ( double [] input, double [] output ){
        for (int i =0; i < output.length; i++){
          while (phase > 1) phase -= 1.0;
          phase_ = phase * mask;
          phase_1 = (int)Math.ceil(phase_);
          phase_0 = (int)(phase_);
         lastValue = (wavetable[partial_0][phase_1]-wavetable[partial_0] [phase_0])
     * (phase_ - phase_0) + wavetable[partial_0][phase_0];
          phase += cps;
          output [i] = lastValue;
        }
       }
       public void process ( double [] input, double [] output ){
      for (int i =0; i < output.length; i++){
        while (phase > 1) phase -= 1.0;
        phase_ = phase * mask;
        phase_1 = (int)Math.ceil(phase_);
        phase_0 = (int)(phase_);
        temp0 = (wavetable[partial_0][phase_1]-wavetable[partial_0] [phase_0]) * (phase_ - phase_0) + wavetable[partial_0][phase_0];
        temp1 = (wavetable[partial_1][phase_1]-wavetable[partial_1] [phase_0]) * (phase_ - phase_0) + wavetable[partial_1][phase_0];
        lastValue = (temp1-temp0) * (partial_ - partial_0) + temp0;
        phase += cps;
        output [i] = lastValue;
      }
       }
     */
}