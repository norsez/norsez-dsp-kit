package com.norsez.dsp.block.filter.effects;

import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.filter.SmoothingFilter;

/**
 * <p>Title: EQLowShelf</p>
 * <p>Description: (Insert FX) This effect works in mono and stereo mode. It shelves (adjustable) low frequency
 * up to +36 dB. In Mono mode, left and right input are added before processing.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class EQLowShelf
        extends Effect {

    private Channel chan;
    private SmoothingFilter filter[];
    private final int CHANNELS;
    private double gain;
    //cut off frequency for shelving
    private double cps;
    private final double MAX_GAIN = 6.05; //+36 dB

    public EQLowShelf(Effect.Channel numOfchannels) {
        chan = numOfchannels;
        CHANNELS = chan.getNumOfChannels();
        filter = new SmoothingFilter[chan.getNumOfChannels()];
        for (int i = 0; i < CHANNELS; i++) {
            filter[i] = new SmoothingFilter();
            filter[i].setCutoff(200.0 / DSPSystem.getSamplingRate());
        }
    }

    public void tick(double l, double r) {

        if (outputLevel == 0) {
            lastValueL = lastValueR = 0;
            return;
        }

        if (CHANNELS == 1) {

            lastValueL = lastValueR = l + r + filter[0].tick(l + r) * gain;

        } else {
            lastValueL = l + filter[0].tick(l) * gain;
            lastValueR = r + filter[1].tick(r) * gain;
        }

    }

    public void setFrequency(double cps) {
        this.cps = cps;
        for (int i = 0; i < CHANNELS; i++) {
            filter[i].setCutoff(cps);
        }
    }

    public void setGain(double gain) {
        this.gain = gain * MAX_GAIN;

    }

    public double getGaindB() {
        return 20.0 * Math.log(gain);
    }

}