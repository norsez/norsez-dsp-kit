package com.norsez.dsp.block.filter.effects;

import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Interpolation;
import com.norsez.dsp.block.Note;
import com.norsez.dsp.block.filter.Delay;
import com.norsez.dsp.block.filter.Filter;
import com.norsez.dsp.block.filter.SimpleFilter;
import com.norsez.dsp.block.filter.SmoothingFilter;
import com.norsez.dsp.block.sequencer.ClockSource;

/**
 * <p>Title: DelayStereo</p>
 * <p>Description: (Send FX) Stereo tempo synced delay effect with band select filter a la Emagic Stereo Delay.
 * <p/>
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class DelayTempoStereo
        extends Effect {

    private final int NUM_CHANNELS = 2;
    private final double MAX_DELAY_SECS = 5.0;
    private Delay delay[];
    private SmoothingFilter lp[];
    private SimpleFilter hp[];
    private ClockSource.Resolution note[];
    private double bpm[];
    private double pan[];
    private double feedback[];
    private double crossfeed[];
    private double level[];

    //deviation off the tempo delay time
    private double swing[];
    private final double MAX_SWING_SECS = 0.5;

    public DelayTempoStereo() {

        delay = new Delay[NUM_CHANNELS];
        lp = new SmoothingFilter[NUM_CHANNELS];
        hp = new SimpleFilter[NUM_CHANNELS];
        note = new ClockSource.Resolution[NUM_CHANNELS];
        bpm = new double[NUM_CHANNELS];
        pan = new double[NUM_CHANNELS];
        feedback = new double[NUM_CHANNELS];
        crossfeed = new double[NUM_CHANNELS];
        swing = new double[NUM_CHANNELS];
        level = new double[NUM_CHANNELS];

        for (int i = 0; i < NUM_CHANNELS; i++) {
            delay[i] = new Delay((int) (MAX_DELAY_SECS * DSPSystem.getSamplingRate()));
            delay[i].setDelay(0.25);
            lp[i] = new SmoothingFilter();
            lp[i].setCutoff(0.4353);
            lp[i].setMode(Filter.Mode.LP);
            hp[i] = new SimpleFilter();
            hp[i].setMode(Filter.Mode.HP);
            hp[i].setCutoff(.4353);
            hp[i].setResonance(0);
            note[i] = ClockSource.Resolution.ONE_HALF;
            swing[i] = 0.5;
            level[i] = 0.75;
        }

        pan[0] = 0;
        pan[1] = 1;

    }

    /**
     * @param ch channel index. 0 = left. 1 = right.
     */
    public void setDelayByTempo(int ch, double bpm, Note note) {
        double sec = Note.getSecondsPerNote(bpm, note)
                +
                Interpolation.mapToRangeLinear(0, -1, 1, 1, swing[ch]) * MAX_SWING_SECS;
        //System.out.println ( sec );
        if (sec > MAX_DELAY_SECS * DSPSystem.getSamplingRate()) {
            delay[ch].setDelay(0.99);
        } else {
            delay[ch].setDelay(sec / MAX_DELAY_SECS);
        }
    }

    public void setLevel(int ch, double l) {
        level[ch] = l;
    }

    public void setLpCutoff(int ch, double cps) {
        //if ( cps < hp [ch].getCutoff()) return;
        lp[ch].setCutoff(cps);
    }

    public void setHpCutoff(int ch, double cps) {
        //if ( cps > lp [ch].getCutoff()) return;
        hp[ch].setCutoff(cps);
    }

    public void setFeedback(int ch, double f) {
        feedback[ch] = f;
    }

    public void setCrossfeed(int ch, double c) {
        crossfeed[ch] = c;
    }

    public void setPan(int ch, double p) {
        pan[ch] = p;
    }

    /**
     * Set deviation off the tempo. 0 = -0.5 sec, 0.5 = 0 sec, 1 = 0.5 sec
     */
    public void setSwing(int ch, double s) {
        swing[ch] = s;
    }

    private double lastDelayL, lastDelayR;

    public void tick(double l, double r) {

        if (outputLevel == 0) {
            lastValueL = lastValueR = 0;
            return;
        }

        lastDelayL = delay[0].tick(l * inputLevel + lastDelayL * feedback[0] +
                lastDelayR * crossfeed[1]);
        lastDelayR = delay[1].tick(r * inputLevel + lastDelayR * feedback[1] +
                lastDelayL * crossfeed[0]);

        for (int i = 0; i < NUM_CHANNELS; i++) {
            lp[i].tick(delay[i].getLastValue());
            hp[i].tick(lp[i].getLastValue());
        }

        lastValueL = hp[0].getLastValue() * (1.0 - pan[0]) +
                hp[1].getLastValue() * (1.0 - pan[1]);
        lastValueR = hp[0].getLastValue() * (pan[0]) +
                hp[1].getLastValue() * (pan[1]);
        //lastValueL = lp[0].getLastValue()  * (1.0 - pan[0]) + lp[1].getLastValue() * (1.0-pan[1]);
        //lastValueR = lp[0].getLastValue()  * (pan[0]) + lp[1].getLastValue() * (pan[1]);

        lastValueL *= outputLevel;
        lastValueR *= outputLevel;

    }

    /**
     * The insert effect version of this effect. The output will already be dry and wet which
     * are inversely proportionate to each other and the proportion is adjusted by using
     * setOutputLevel(double) method.
     *
     * @param l left channel input
     * @param r right channel input
     */
    public void tickInsert(double l, double r) {

        if (outputLevel == 0) {
            lastValueL = l;
            lastValueR = r;
            return;
        }

        lastDelayL = delay[0].tick(l * inputLevel + lastDelayL * feedback[0] +
                lastDelayR * crossfeed[1]);
        lastDelayR = delay[1].tick(r * inputLevel + lastDelayR * feedback[1] +
                lastDelayL * crossfeed[0]);

        for (int i = 0; i < NUM_CHANNELS; i++) {
            lp[i].tick(delay[i].getLastValue());
            hp[i].tick(lp[i].getLastValue());
        }

        lastValueL = hp[0].getLastValue() * (1.0 - pan[0]) +
                hp[1].getLastValue() * (1.0 - pan[1]);
        lastValueR = hp[0].getLastValue() * (pan[0]) +
                hp[1].getLastValue() * (pan[1]);
        //lastValueL = lp[0].getLastValue()  * (1.0 - pan[0]) + lp[1].getLastValue() * (1.0-pan[1]);
        //lastValueR = lp[0].getLastValue()  * (pan[0]) + lp[1].getLastValue() * (pan[1]);

        lastValueL *= outputLevel;
        lastValueR *= outputLevel;

        lastValueL += l * this.getDryMixFactor();
        lastValueR += r * this.getDryMixFactor();

    }


}