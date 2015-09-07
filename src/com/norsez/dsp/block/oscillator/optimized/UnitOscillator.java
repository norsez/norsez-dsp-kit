package com.norsez.dsp.block.oscillator.optimized;

public class UnitOscillator extends com.norsez.dsp.block.Unit {
    double phase;
    double cps;

    public UnitOscillator() {
        cps = 440.0 / com.norsez.dsp.block.DSPSystem.getSamplingRate();
    }
}
