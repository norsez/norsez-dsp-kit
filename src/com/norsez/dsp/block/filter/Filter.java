package com.norsez.dsp.block.filter;

import com.norsez.dsp.block.DSPBlock;

/**
 * <p>Title: Filter</p>
 * <p>Description: Abstract class for Filter classes.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public abstract class Filter
        extends DSPBlock {

    /**
     * <p>Title: Mode</p>
     * <p>Description: Filter.Mode for selecting filter mode.</p>
     * <p>Copyright: Copyright (c) 2003</p>
     * <p>Company: Norsez Orankijanan</p>
     *
     * @author Norsez Orankijanan
     * @version 1.0
     */
    public static class Mode {
        public final String name;

        private Mode(String n) {
            name = n;
        }

        private static int nextOrdinal = 0;
        private final int ordinal = nextOrdinal++;

        public String toString() {
            return name;
        }

        public static final Mode LP = new Mode("LP");
        public static final Mode HP = new Mode("HP");
        public static final Mode BP = new Mode("BP");
        public static final Mode OFF = new Mode("Off");
        public static final Mode NH = new Mode("NH");
        public static final Mode AP = new Mode("AP");

    }

    protected double frequency, resonance;
    protected Mode mode;

    /**
     * @param c cutoff frequency in [0,1].
     */
    public void setCutoff(double c) {
        if (c > 1) {
            c = 1;
        }
        if (c < 0) {
            c = 0;
        }
        frequency = c;
    }

    public double getResonance() {
        return resonance;
    }

    public void setResonance(double r) {
        if (r > 1) {
            r = 1;
        }
        if (r < 0) {
            r = 0;
        }
        resonance = r;
    }

    public double getCutoff() {
        return frequency;
    }

    public void setMode(Mode m) {
        mode = m;
    }

    public Mode getMode() {
        return mode;
    }

    public final double tick() {
        throw new java.lang.UnsupportedOperationException("tick() is not supported in Filter, use tick (double) instead.");
    }

    public abstract double tick(double in);

    public abstract Mode[] getSupportedModes();
}