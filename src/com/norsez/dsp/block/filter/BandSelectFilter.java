package com.norsez.dsp.block.filter;

import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Interpolation;

/**
 * <p>Title: Band Select Filter <b>does not work. Don't use it.<b></p>
 * <p>Description: Select band by passing lp thru hp.
 * It is the kind of filter used in Emagic Logic Stereo Delay.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class BandSelectFilter
        extends Filter {

    public double low, high, notch, band, f, q, scale;
    private static double[] TUNE_TABLE = makeTuneTable();

    public BandSelectFilter() {

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

    public double tick(double input) {
        low = low + f * band;
        high = input - low - band;
        band = f * high + band;
        notch = high + low;

        if (mode == Mode.LP) {
            return low;
        } else if (mode == Mode.HP) {
            return high;
        } else if (mode == Mode.BP) {
            return band;
        } else if (mode == Mode.NH) {
            return notch;
        } else {
            return input;
        }
    }

    public void setMode(Mode m) {
        super.setMode(m);
        if (m == Mode.OFF) {
            reset();
        }
    }

    public Mode[] getSupportedModes() {
        Mode[] m = {
            Mode.NH, Mode.OFF};
        return m;
    }

}