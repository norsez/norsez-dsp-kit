package com.norsez.dsp.block.oscillator;

import com.norsez.dsp.block.Oversampler;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class SyncableOversampledOsc
        extends SyncableOscillator {

    private SOsc so;

    public SyncableOversampledOsc(int oversamplingTimes) {
        so = new SOsc(oversamplingTimes, this);
    }

    public double tick(double in) {
        lastValue = so.tick(0);
        return lastValue;
    }

    private class SOsc
            extends Oversampler {
        SyncableOscillator osc;

        public SOsc(int s, SyncableOscillator osc) {
            super(s);
            this.osc = osc;

        }

        protected double getOneSample(double in) {
            return osc.tick();
        }

    }

}