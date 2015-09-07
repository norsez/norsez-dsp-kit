package com.norsez.dsp.block.oscillator;

import com.norsez.dsp.block.filter.effects.Chorus;

/**
 * <p>Title: SuperSaw</p>
 * <p>Description: Does the Roland JP80x0's SuperSaw Oscillator.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class SuperSaw extends AntiAliasedWavetable {
    private Chorus chorus;
    private double detune, mix;

    public SuperSaw() {
        this.setWavetable(com.norsez.dsp.block.Table.T_BL_SAW);

        chorus = new Chorus(6);
        chorus.setActiveLines(1);
        chorus.setDepth(0.7);
        chorus.setModulation(0);
        chorus.setRate(.17);
        chorus.setOutputLevel(1);
    }

    public double tick() {
        chorus.tick(super.tick(), 0);
        lastValue = 0.375 * mix * (chorus.getLastValueL() + chorus.getLastValueR())
                + super.getLastValue() * (1 - mix + .2);
        return lastValue;
    }

    public void setDetune(double v) {
        if (v < .02) v = 0.02;
        detune = v;
        chorus.setModulation(com.norsez.dsp.block.Interpolation.linear(com.norsez.dsp.block.Table.T_EXP_UP, v * 0.44));
    }

    public void setMix(double m) {
        mix = m;
        //chorus.setOutputLevel(m);
    }

}