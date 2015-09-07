package com.norsez.dsp.block.oscillator;

import com.norsez.dsp.block.DSPBlock;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Wavetable
        extends DSPBlock {
    public double cps, phase, phase_;
    private double[] wavetable;

    private int mask, phase_1, phase_0;

    public Wavetable() {

    }

    /*
       public void process ( double [] input, double [] output ){
      mask = (wavetable.length-1);
      for (int i =0; i < output.length; i++){
        while (phase > 1) phase -= 1.0;
        lastValue = wavetable [(int)(phase * mask)];
        phase += cps;
        output [i] = lastValue;
      }
       }
     */

    public void setWavetable(double[] w) {
        mask = (w.length - 1);
        wavetable = w;
    }

    //process with linear interpolation
    public void processLin(double[] input, double[] output) {

        for (int i = 0; i < output.length; i++) {
            while (phase > 1) {
                phase -= 1.0;

            }
            phase_ = phase * mask;
            phase_1 = (int) Math.ceil(phase_);
            phase_0 = (int) (phase_);

            lastValue = (wavetable[phase_1] - wavetable[phase_0]) * (phase_ - phase_0) +
                    wavetable[phase_0];

            phase += cps;
            output[i] = lastValue;
        }
    }

    public double tick() {
        while (phase > 1) {
            phase -= 1.0;

        }
        phase_ = phase * mask;
        phase_1 = (int) Math.ceil(phase_);
        phase_0 = (int) (phase_);

        lastValue = (wavetable[phase_1] - wavetable[phase_0]) * (phase_ - phase_0) +
                wavetable[phase_0];

        phase += cps;

        return lastValue;
    }

    public double tickProcess() {
        while (phase > 1) {
            phase -= 1.0;

        }
        phase_ = phase * mask;
        phase_1 = (int) Math.ceil(phase_);
        phase_0 = (int) (phase_);

        lastValue = (wavetable[phase_1] - wavetable[phase_0]) * (phase_ - phase_0) +
                wavetable[phase_0];

        phase += cps;

        return lastValue;
    }

    /**
     * Ticking without interpolation. Too noisy for small audio tables
     *
     * @return double
     */
    public double tickRaw() {
        while (phase > 1) {
            phase -= 1.0;

        }

        lastValue = wavetable[(int) (phase * mask)];

        phase += cps;

        return lastValue;
    }


    public double tick(int step_times) {

        while (phase > 1) {
            phase -= 1.0;

        }
        phase_ = phase * mask;
        phase_1 = (int) Math.ceil(phase_);
        phase_0 = (int) (phase_);

        lastValue = (wavetable[phase_1] - wavetable[phase_0]) * (phase_ - phase_0) +
                wavetable[phase_0];

        phase += cps * step_times;

        ///System.out.println ( lastValue );
        return lastValue;
    }

}
