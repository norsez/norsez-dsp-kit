package com.norsez.dsp.block.filter;

import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Interpolation;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class SimpleFilter
        extends Filter {
    public double low, high, notch, band, f, q, scale;
    private static double[] TUNE_TABLE = makeTuneTable();

    public SimpleFilter() {
        setResonance(0.395);
        setCutoff(0.75);
        reset();
    }

    public void reset() {
        low = high = notch = band = scale = lastValue = 0;
    }

    static double[] makeTuneTable() {
        double[] table = new double[DSPSystem.getSmallTableSize()];
        for (int i = 0; i < table.length; i++) {
            table[i] = 2.0 * Math.sin(Math.PI * i / (table.length - 1));
        }
        return table;
    }

    /**
     * Use this to set tune (f) of the filter. Range is 0..1.0 but should be
     * passed thru Table.T_NOTE2CPS to get the right curve.
     */
    public void setCutoff(double v) {
        super.setCutoff(v);
        //f = TUNE_TABLE [(int)( v * (TUNE_TABLE.length-1))];
        f = Interpolation.linear(TUNE_TABLE, v * 0.5);
    }

    private static final double RESONANCE_THRESHOLD = 0.395;

    public void setResonance(double v) {
        super.setResonance(v);

        if (v < RESONANCE_THRESHOLD) {
            v = RESONANCE_THRESHOLD;
            //v = Interpolation.mapToRangeLinear(0,1,RESONANCE_THRESHOLD,1.0,v);
        }
        scale = v;
        q = 1. - v;
    }

    public double tick(double input) {
        low = low + f * band;
        high = scale * input - low - q * band;
        band = f * high + band;
        notch = high + low;

        if (mode == Mode.LP) {
            lastValue = low;
        } else if (mode == Mode.HP) {
            lastValue = high;
        } else if (mode == Mode.BP) {
            lastValue = band;
        } else if (mode == Mode.NH) {
            lastValue = notch;
        } else {
            lastValue = input;

        }
        return lastValue;
    }

    public void tickProcess(double[] input, double[] output) {

        for (int i = 0; i < input.length; i++) {

            low = low + f * band;
            high = scale * input[i] - low - q * band;
            band = f * high + band;
            notch = high + low;

            if (mode == Mode.LP) {
                output[i] = low;
            } else if (mode == Mode.HP) {
                output[i] = high;
            } else if (mode == Mode.BP) {
                output[i] = band;
            } else if (mode == Mode.NH) {
                output[i] = notch;
            } else {
                output[i] = input[i];

            }

        }
        lastValue = output[output.length - 1];
    }

    public void tickProcess(double[] input, double[] output, double[] cutoffMod) {

        for (int i = 0; i < input.length; i++) {

            this.setCutoff(cutoffMod[i]);

            low = low + f * band;
            high = scale * input[i] - low - q * band;
            band = f * high + band;
            notch = high + low;

            if (mode == Mode.LP) {
                output[i] = low;
            } else if (mode == Mode.HP) {
                output[i] = high;
            } else if (mode == Mode.BP) {
                output[i] = band;
            } else if (mode == Mode.NH) {
                output[i] = notch;
            } else {
                output[i] = input[i];

            }

        }
        lastValue = output[output.length - 1];
    }


    public double getLastValue() {
        return lastValue;
    }

    public void setMode(Mode m) {
        super.setMode(m);
        if (m == Mode.OFF) {
            reset();
        }
    }

    public Mode[] getSupportedModes() {
        Mode[] m = {
            Mode.LP, Mode.HP, Mode.BP, Mode.NH, Mode.OFF};
        return m;
    }

    public static void main(String[] arg) {
        SimpleFilter f = new SimpleFilter();
        com.norsez.dsp.block.oscillator.Noise n = new com.norsez.dsp.block.oscillator.Noise();
        f.setMode(Mode.LP);
        f.setResonance(0.1);
        f.setCutoff(150 / 44100.0);
        for (int i = 0; i < 4000; i++) {
            System.out.println(n.tick() + ", " + f.tick(n.getLastValue()));
        }
    }
}
