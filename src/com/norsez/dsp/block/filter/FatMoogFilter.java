package com.norsez.dsp.block.filter;

import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.synth.Parameter;
import com.norsez.dsp.synth.ParameterDisplay;
import com.norsez.dsp.synth.ParameterManager;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class FatMoogFilter extends MoogFilter {

    private double fatness;
    private final static double LOW_EQ_CPS = 660.0 / DSPSystem.getSamplingRate();
    private SmoothingFilter lp;
    private double outGain;
    private final static double MAX_GAIN = 3.0;

    public FatMoogFilter() {

        lp = new SmoothingFilter();
        lp.setCutoff(LOW_EQ_CPS);
        setOutGain(1 / 3.);
    }

    public Mode[] getSupportedModes() {

        return super.getSupportedModes();
    }

    public double tick(double in) {
        if (mode != Mode.OFF) {

            lastValue = super.tick(in);

            return (lastValue + fatness * lp.tick(in)) * outGain;
        } else
            return in;
    }

    public void setFat(double f) {
        fatness = f;
    }

    public void setOutGain(double g) {
        outGain = (g * MAX_GAIN);
    }

    public static final ParameterDisplay GAIN_dB = new ParameterDisplay() {
        public String getDisplay(double v) {
            return df.format(20.0 * Math.log(v * MAX_GAIN));
        }
    };

    public void addToParameteManager(ParameterManager pm, String groupName, String prefix) {
        pm.addParameter(new Parameter(prefix + "Mode", this.getSupportedModes(), groupName));
        pm.addParameter(new Parameter(prefix + "Cutoff", 1, groupName, ParameterDisplay.CUTOFF));
        pm.addParameter(new Parameter(prefix + "Resonance", 0, groupName));
        pm.addParameter(new Parameter(prefix + "Fatness", .4, groupName));


    }
}