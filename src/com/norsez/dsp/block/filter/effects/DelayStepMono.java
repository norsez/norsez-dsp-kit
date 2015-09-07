package com.norsez.dsp.block.filter.effects;

import com.norsez.dsp.block.DSPBlockStereo;
import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Note;
import com.norsez.dsp.block.filter.Delay;
import com.norsez.dsp.block.filter.SmoothingFilter;
import com.norsez.dsp.block.oscillator.multimode.LFO;
import com.norsez.dsp.block.sequencer.ClockSource;

/**
 * <p>Title: DelayStepMono</p>
 * <p>Description: Mono->Stereo  Delay that sets delay time by tempo. This is a "send FX".
 * Stereo input will be combined into mono.
 * The output can ben panned by a fixed position or a tempo based lfo. </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class DelayStepMono
        extends DSPBlockStereo {

    private double pan, feedback, outputLevel;
    private double pantemp;
    private ClockSource.Resolution tempo;
    private Delay delay;
    private SmoothingFilter lp;
    private final int UPDATE_MARK = 1000;
    private int update_count;
    private LFO lfo;
    private final double BUF_LEN_IN_SECS = 5.0;
    private final int BUF_LEN = (int) (DSPSystem.getSamplingRate() *
            BUF_LEN_IN_SECS);
    private boolean useLFO;

    public DelayStepMono() {

        delay = new Delay(BUF_LEN);

        lfo = new LFO();
        lfo.setShape(LFO.Shape.SINE);

        lp = new SmoothingFilter();
        lp.setCutoff(25.0 / DSPSystem.getSamplingRate());

    }

    public void setTempo(double bmp, ClockSource.Resolution note) {
        double sec = ClockSource.getSecondsPerNote(bmp, note);
        //System.out.println ( sec );
        if (sec > BUF_LEN_IN_SECS) {
            delay.setDelay(0.99);
        } else {
            delay.setDelay(sec / BUF_LEN_IN_SECS);
        }

    }

    public void setCutoff(double c) {
        lp.setCutoff(c);
    }

    public void tick() {

        DSPSystem.unsupported(this + "use tick () instead");

    }

    public void tick(double[] l, double[] r) {
        DSPSystem.unsupported(this + "use tick () instead");
    }

    public void setFeedback(double v) {
        feedback = v;
    }

    public void setPan(double p) {
        pan = p;
    }

    public void setOutputLevel(double o) {
        outputLevel = o;
    }

    public void setUseLfo(boolean b) {
        useLFO = b;
    }

    public void setUseLfo(double b) {
        if (b == 0) {
            useLFO = false;
        } else {
            useLFO = true;

        }
    }

    public boolean useLfo() {
        return useLFO;
    }

    public void setLfoShape(LFO.Shape s) {
        lfo.setShape(s);
    }

    public void setLfoTempo(double bpm, Note res) {
        lfo.setRateByTempo(bpm, res);
    }

    public void tick(double l, double r) {

        if (update_count >= UPDATE_MARK) {
            lfo.tick(UPDATE_MARK);
            update_count = 0;
        }
        update_count++;

        if (outputLevel > 0) {

            delay.tick(l + r + delay.getLastValue() * feedback);

            //lp.tick(delay.getLastValue());
            if (!useLfo()) {
                lastValueL = delay.getLastValue() * (1.0 - pan) * outputLevel;
                lastValueR = delay.getLastValue() * (pan) * outputLevel;
            } else {
                pantemp = lfo.getLastValue();
                lastValueL = delay.getLastValue() * outputLevel * (1 - pantemp);
                lastValueR = delay.getLastValue() * outputLevel * pantemp;
            }
        } else {
            lastValueL = lastValueR = 0;

        }
    }
}