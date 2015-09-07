package com.norsez.dsp.block.sequencer;

import com.norsez.dsp.block.DSPBlock;
import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Interpolation;

/**
 * <p>Title: ClockSource  </p>
 * <p>Description: <b>Outdated. Use Sample Clock instead<b><br>Sample Accurate Clock. <b>Must be ticked()  at sampling rate.</b> This class sends out clocked gate. It can be used as an arpeggiator clock.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class ClockSource
        extends DSPBlock {
    //beat per minute in 4/4 tempo of the clock
    private double bpm;
    //where the tick is on the impulse table
    private double phase;
    //cycle per second clock
    private double cps;
    //determines the gate length of the tick in the range of 0 to 1.0
    private double gate;

    //the impulse table
    private double table[];

    //the resolution of the ticks
    private Resolution resolution;

    //flags for the first tick of the Gate On signal. Arpeggiator needs this info to update its current step.
    private boolean significantTick;

    public ClockSource() {

        table = new double[DSPSystem.getBigTableSize()];

        setResolution(Resolution.ONE_SIXTEENTH);
        setBPM(130.0);
        setGate(0.75);

        //this makes significantTick true
        phase = 2.0;

    }

    public boolean isSignificantTick() {
        return this.significantTick;
    }

    /**
     * set clock gate length.
     */
    public void setGate(double gate) {
        if (gate <= 0) {
            gate = 0.01;
        }
        if (gate > 1) {
            gate = 1;
        }
        this.gate = gate;

        int open_period = (int) (table.length * gate);

        for (int i = 0; i < table.length; i++) {
            if (i <= open_period) {
                table[i] = 1;
            } else {
                table[i] = 0;

            }
        }

    }

    /**
     * @return clock gate length
     */

    public double getGate() {
        return gate;
    }

    public void setBPM(double t) {
        bpm = t;
        calculateCPS();
    }

    public double getBPM() {
        return this.bpm;
    }

    /**
     * calculates the cps for the impulse table running.
     */
    private void calculateCPS() {
        cps = 0.25 * ((bpm / (60.0)) / DSPSystem.getSamplingRate()) *
                getResolution().getFactor();
        //System.out.println (bpm + " " + cps * DSPSystem.getSamplingRate() + " " + getResolution().getFactor());
    }

    /**
     * @param beatPerMinute the referenced tempo.
     * @param res           the note to calculate.
     * @return the length of a note in seconds with respect to the referenced tempo.
     */
    public static double getSecondsPerNote(double beatPerMinute,
                                           ClockSource.Resolution res) {
        return 8 * 60.0 / (res.getFactor() * beatPerMinute);
    }

    public double tick() {

        if (phase > 1) {
            while (phase > 1) {
                phase = phase - 1;
            }

            this.significantTick = true;
        } else {
            this.significantTick = false;
        }

        lastValue = Interpolation.linear(table, phase);
        phase += cps;

        return lastValue;
    }

    public void setResolution(Resolution r) {
        this.resolution = r;
        calculateCPS();
    }

    /**
     * This sets time for the clock. Good for text based debugging only.
     */
    public void setRawCps(double i) {
        cps = i;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public String toString() {
        return "Clock Source: BPM = " + getBPM() + " Resolution " +
                resolution.toString();
    }

    public static class Resolution {
        private final String name;
        private final double factor;
        private static int nextOrdinal = 0;
        private final int ordinal = nextOrdinal++;
        private static java.util.Vector all;

        private Resolution(double factor) {
            this.factor = factor;

            java.text.DecimalFormat df = new java.text.DecimalFormat("0");

            if (factor < 1) {
                name = df.format(1.0 / factor) + "/1";
            } else {
                name = "1/" + df.format(factor);
            }

            if (all == null) {
                all = new java.util.Vector();
            }
            all.add(this);
        }

        public static Resolution[] getAllResolutions() {
            Resolution[] m = new Resolution[all.size()];

            for (int i = 0, n = all.size(); i < n; i++) {
                m[i] = (Resolution) all.elementAt(i);
            }
            return m;
        }

        /**
         * @return factor ready for multiplying with bpm/(sampling rate * seconds) to get the cps for the clock table.
         */

        public double getFactor() {
            return factor;
        }

        public String toString() {
            return name;
        }

        public static final Resolution FOUR = new Resolution(0.25);
        public static final Resolution THIRD = new Resolution(1.0 / 3);
        public static final Resolution TWO = new Resolution(0.5);
        public static final Resolution ONE = new Resolution(1);
        public static final Resolution ONE_HALF = new Resolution(2);
        public static final Resolution ONE_THIRD = new Resolution(3);
        public static final Resolution ONE_FOURTH = new Resolution(4);
        public static final Resolution ONE_SIXTHTH = new Resolution(6);
        public static final Resolution ONE_EIGHTH = new Resolution(8);
        public static final Resolution ONE_EIGHTH_T = new Resolution(12);
        public static final Resolution ONE_SIXTEENTH = new Resolution(16);
        public static final Resolution ONE_SIXTEENTH_T = new Resolution(24);
        public static final Resolution ONE_THIRTY_SECOND = new Resolution(32);
        public static final Resolution ONE_THIRTY_SECOND_T = new Resolution(48);
        public static final Resolution ONE_SIXTY_FORTH = new Resolution(64);

    }

    public static void main(String[] arg) {
        ClockSource c = new ClockSource();
        for (int i = 0; i < 5000; i++) {
            System.out.println(c.tick());
        }
    }
}