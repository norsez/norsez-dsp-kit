package com.norsez.dsp.block.filter.effects;

import com.norsez.dsp.block.DSPBlock;
import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Oversampler;

/**
 * <p>Title: RingModulator </p>
 * <p>Description: Encapsulates the process of oversampled RingModulation.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class RingModulator
        extends DSPBlock {

    private RingMod rmod;

    public RingModulator(int oversamplingTimes) {
        rmod = new RingMod(oversamplingTimes);

    }

    public double tick() {
        DSPSystem.unsupported("use tick ( double, double ) instead.");
        return 0;
    }

    public double tick(double a, double b) {

        rmod.setSignals(a, b);

        lastValue = rmod.tick(0);
        return lastValue;
    }

    private static class RingMod
            extends Oversampler {

        public RingMod(int t) {
            super(t);
        }

        private double a, b;

        /**
         * Must call this to set input signals for ring modulator.
         * Can't input in getOneSample because getOneSample only takes 1 input. :-(
         */
        public void setSignals(double a, double b) {
            this.a = a;
            this.b = b;
        }

        protected double getOneSample(double in) {
            return a * b;
        }
    }

}