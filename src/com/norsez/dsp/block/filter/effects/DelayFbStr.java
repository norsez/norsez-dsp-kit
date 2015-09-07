package com.norsez.dsp.block.filter.effects;

import com.norsez.dsp.block.DSPBlockStereo;
import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.filter.Delay;
import com.norsez.dsp.block.filter.Filter;
import com.norsez.dsp.block.filter.SmoothingFilter;

/**
 * <p>Title: DelayFbStr</p>
 * <p>Description: Basic Stereo Delay with feedback. Send FX (you have to mix with dry signal yourself).</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class DelayFbStr
        extends DSPBlockStereo {

    private Delay[] delays;
    private SmoothingFilter[] filters;

    private double[] feedbacks;
    public static final double BUF_LEN_IN_SECONDS = 3.0;
    private static final int BUFF_LEN = (int) (DSPSystem.getSamplingRate() *
            BUF_LEN_IN_SECONDS);

    /**
     * @param syncedTo The ClockSource that this delay is being synced to.
     */

    public DelayFbStr() {
        delays = new Delay[2];
        filters = new SmoothingFilter[2];
        feedbacks = new double[2];
        for (int i = 0; i < 2; i++) {
            delays[i] = new Delay(BUFF_LEN);
            filters[i] = new SmoothingFilter();
            filters[i].setMode(Filter.Mode.LP);
            filters[i].setCutoff(0.25);
            filters[i].setResonance(0.127);
        }


    }

    /**
     * Batch processing. The output contains wet mix result only.
     *
     * @param input  double[][]
     * @param output double[][]
     */
    public void tickProcess(double[][] input, double[][] output) {
        for (int c = 0; c < 2; c++) {
            double lastValue = 0;
            for (int i = 0; i < input[0].length; i++) {
                lastValue = delays[c].tick(input[c][i] + filters[c].tick(lastValue * feedbacks[c]));
                output[c][i] = lastValue;
            }
        }

    }

    /**
     * @param t       double 0..1.0
     * @param channel int channel=0,1
     */
    public void setDelayTime(double t, int channel) {
        delays[channel].setDelay(t);
    }

    public void setCutoff(double c, int channel) {
        filters[channel].setCutoff(c);
    }

    public void setFeedbackAmount(double a, int channel) {
        feedbacks[channel] = a;
    }

    public void tick(double inL, double inR) {

    }

    public void tick(double[] l, double[] r) {
        DSPSystem.unsupported(this + "use tick (double,double) instead.");
    }

    public void tick() {
        DSPSystem.unsupported(this + "use tick (double,double) instead.");
    }
}
