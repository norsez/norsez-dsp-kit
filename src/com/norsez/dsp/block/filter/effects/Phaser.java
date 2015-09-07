package com.norsez.dsp.block.filter.effects;

import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Interpolation;
import com.norsez.dsp.block.Note;
import com.norsez.dsp.block.Table;
import com.norsez.dsp.block.filter.AllPassFilter;
import com.norsez.dsp.block.oscillator.Wavetable;

/**
 * <p>Title: Phaser</p>
 * <p>Description: Stereo Phaser. (Send FX) Variable poles. Sweeping rate can be synced to tempo.
 * Sweeping phase can be reset by gate.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Phaser
        extends Effect {

    private double frequency, width, rate, feedback;

    private AllPassFilter ap[];
    private Wavetable waves[];

    //number of allpass filters ( 2 x numPoles)
    private int numLines;

    //number of poles in active.
    private int activePoles;

    //maximum poles available.
    private int numPoles;

    //number of all filters  in active.
    private int activeLines;

    private double crossfeed;
    //phase difference between the two channels' lfos
    private double split, _split;

    private double outputScale;

    /**
     * set phase difference of the two channels. 0 is 0, 1 is Pi
     */
    public void setSplit(double s) {
        split = s;
        _split = split;

        double phase = waves[0].phase;
        phase += _split * 0.5;
        while (phase > 1) {
            phase -= 1.0;

        }
        for (int i = 0; i < numPoles; i++) {
            waves[2 * i + 1].phase = phase;
        }
    }

    public double getSplit() {
        return split;
    }

    private final double MAX_LFO_CPS = 10.0 / DSPSystem.getSamplingRate();

    public Phaser(int maxPoles) {
        outputScale = 2.0 / Math.sqrt((double) maxPoles);
        if (maxPoles < 01) {
            maxPoles = 1;

        }
        activePoles = numPoles = maxPoles;

        activeLines = numLines = maxPoles * 2;

        ap = new AllPassFilter[numLines];
        for (int i = 0; i < numLines; i++) {
            ap[i] = new AllPassFilter();
            ap[i].setName("ap" + i);
        }

        waves = new Wavetable[numLines];

        for (int i = 0; i < numLines; i++) {
            waves[i] = new Wavetable();
            waves[i].setWavetable(Table.T_BL_TRIANGLE[5]);
            waves[i].cps = 2.0 / DSPSystem.getSamplingRate();
        }

        setSplit(0.5);
        setActivePoles(1);
    }

    /**
     * set how many poles to be active per tick. The minumum pole is always 1.
     */
    public void setActivePoles(double i) {
        activePoles = (int) (i * numLines) + 1;
        if (activePoles > numPoles) {
            activePoles = numPoles;

        }
        activeLines = activePoles * 2;
        //outputScale = 1.0/activePoles;

    }

    /**
     * set phasing sweeping rate. Linear [0,1] value.
     */
    public void setRate(double r) {
        rate = r;
        for (int i = 0; i < numLines; i++) {
            waves[i].cps = r * MAX_LFO_CPS;
        }
    }

    /**
     * set phasing sweeping rate by tempo.
     */
    public void setRateByTempo(double bpm, Note note) {
        for (int i = 0; i < numLines; i++) {
            waves[i].cps = (1.0 / Note.getSecondsPerNote(bpm, note)) /
                    DSPSystem.getSamplingRate();
        }
    }

    public void setFeedback(double feedback) {
        this.feedback = feedback;
    }

    private double lastGate;

    public void setPhasebyGate(double gate) {

        if (lastGate == 0 && gate > 0) {
            for (int i = 0; i < numLines; i++) {
                waves[i].phase = i / (double) (numLines - 1.0);
            }
        }

        lastGate = gate;
    }

    /**
     * set the amount of level of one channel feeded into the other at the output stage.
     *
     * @deprecated Does seem to blow up the filters. So don't use it.
     */
    public void setCrossFeed(double f) {
        crossfeed = f;
    }

    /**
     * set base sweep frequency. The input must be linearly 0..1.
     */
    public void setFrequency(double linear) {
        frequency = linear;
    }

    /**
     * set sweep width.  (preferably exponentially scaled.
     */
    public void setWidth(double w) {
        this.width = w;
    }

    protected final int UPDATE_MARK = 1;

    public void tick(double l, double r) {

        if (outputLevel == 0) {
            lastValueR = lastValueL = 0;

            return;
        }

        if (update_count >= UPDATE_MARK) {

            for (int i = 0; i < numLines; i++) {
                waves[i].tick(UPDATE_MARK);
                ap[i].setDelay(Interpolation.linear(Table.T_FREQUENCY,
                        frequency +
                        waves[i].getLastValue() * width));
//        temp = Interpolation.linear ( Table.T_FREQUENCY,
//            frequency + waves[i].getLastValue() * width);
            }

            update_count = 0;
        }

        update_count++;


        ap[0].tick((l * inputLevel + feedback * lastValueL) * outputScale);
        ap[1].tick((r * inputLevel + feedback * lastValueR) * outputScale);

        for (int i = 1; i < activePoles; i++) {
            ap[2 * i].tick(ap[2 * (i - 1)].getLastValue());
            ap[2 * i + 1].tick(ap[2 * (i - 1) + 1].getLastValue());
        }

        lastValueL = ap[(activePoles - 1) * 2].getLastValue();
        lastValueR = ap[(activePoles - 1) * 2 + 1].getLastValue();

        //lastValueL += crossfeed * lastValueR;
        //lastValueR += crossfeed * lastValueL;

        //scaler
        //temp =  Interpolation.linear(Table.T_SQRT_2, .99 - crossfeed * feedback);
        lastValueL *= outputLevel;
        lastValueR *= outputLevel;
    }

    public void setOutputLevel(double o) {
        super.setOutputLevel(o);
        for (int i = 0; i < numLines; i++) {
            ap[i].reset();
        }
        lastValueL = lastValueR = 0;
    }
}