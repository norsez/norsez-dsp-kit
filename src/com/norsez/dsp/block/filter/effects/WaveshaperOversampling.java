package com.norsez.dsp.block.filter.effects;

import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Interpolation;
import com.norsez.dsp.block.filter.Filter;
import com.norsez.dsp.block.filter.SmoothingFilter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class WaveshaperOversampling
        extends Waveshaper {

    protected final double OS_RATE;
    protected final int TIMES;
    private double buffer[];
    private SmoothingFilter lp;

    public WaveshaperOversampling(Effect.Channel ch, int oversamplingMultiples) {
        super(ch);
        OS_RATE = oversamplingMultiples * DSPSystem.getSamplingRate();

        TIMES = oversamplingMultiples;
        buffer = new double[TIMES];

        lp = new SmoothingFilter();
        lp.setMode(Filter.Mode.LP);
        lp.setCutoff(0.43535 * DSPSystem.getSamplingRate() / OS_RATE);
    }

    public void tick(double l, double r) {

        if (outputLevel == 0) {
            lastValueL = l;
            lastValueR = r;
            return;
        }

        if (NUM_CHANNELS == 1) {

            lastValueL = osShape(l + r);
            lastValueR = lastValueL;
        } else {

            lastValueL = osShape(l);

            lastValueR = osShape(r);

        }
    }

    /**
     * does the real oversampled waveshaping.
     */
    private double osShape(double l) {
        temp = (l) * inputLevel;

        buffer[0] = temp;

        //stuffing zeros
        for (int i = 1; i < TIMES; i++) {
            buffer[i] = 0;
        }

        //process
        for (int i = 0; i < TIMES; i++) {
            buffer[i] = Interpolation.linearBipolar(table, buffer[i] * drive);
        }

        //filter
        for (int i = 0; i < TIMES; i++) {
            buffer[i] = lp.tick(buffer[i]);
        }

        return buffer[0] * temp * drive * outputLevel + temp * (1 - outputLevel);
    }

}