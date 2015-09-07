package com.norsez.dsp.block.oscillator.multimode;

import com.norsez.dsp.block.DSPBlock;
import com.norsez.dsp.block.Interpolation;

/**
 * <p>Title: MultiModeOsc</p>
 * <p>Description: Abstract class for a multi mode oscillator block. One should <b>not</b> use com.norsez.dsp.block.oscillator
 * classes in a real synth. Instead incorporate many modes and write it as a child class of
 * MultiModeOsc.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public abstract class MultiModeOsc
        extends DSPBlock {

    protected OscMode oscMode;

    /**
     * This one should be filled in the constructor of the child class.
     */
    protected OscMode[] allModes;

    public void setOscMode(OscMode o) {
        oscMode = o;
    }

    public OscMode getOscMode() {
        return oscMode;
    }

    /**
     * Should somehow return allModes.
     */
    public abstract OscMode[] getAllModes();

    public abstract double tick();

    public abstract void setCps(double cps);

    public static final double ONE_RAW_NOTE = 1 / 128.0;
    public static final int SEMI_RANGE = 24; //this is in midi note
    public static final double FINE_RANGE = 0.995 * ONE_RAW_NOTE; // 0.995 midi note tune. this is in raw note.

    /**
     * @return raw note increment for semi tuning, i.e. it maps [0,1] to [-SEMI_RANGE,+SEMI_RANGE] in raw note which
     *         is suitable to add to midi raw note or fine tune raw note before passing the sum of everything into NOTE2CPS to get
     *         the frequency cps.
     */
    public static double getSemiRawTune(double s) {
        int semi = (int) Interpolation.mapToRangeLinear(0, -SEMI_RANGE, 1.0,
                SEMI_RANGE, s);
        return ONE_RAW_NOTE * semi;
    }

    /**
     * @return raw note increment for fine tuning.
     * @see getSemiRawTune()
     */
    public static double getFineRawTune(double f) {
        return Interpolation.mapToRangeLinear(0, -FINE_RANGE, 1.0, FINE_RANGE, f);
    }

}