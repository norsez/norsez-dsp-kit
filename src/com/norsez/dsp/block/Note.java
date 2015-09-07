package com.norsez.dsp.block;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Note {
    private final String name;
    private final double factor;
    private static int nextOrdinal = 0;
    private final int ordinal = nextOrdinal++;
    private static java.util.Vector all;

    private Note(double factor) {
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

    public static Note[] getAllNotes() {
        Note[] m = new Note[all.size()];

        for (int i = 0, n = all.size(); i < n; i++) {
            m[i] = (Note) all.elementAt(i);
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

    public static final Note FOUR = new Note(0.25);
    public static final Note THIRD = new Note(1.0 / 3);
    public static final Note TWO = new Note(0.5);
    public static final Note ONE = new Note(1);
    public static final Note ONE_HALF = new Note(2);
    public static final Note ONE_THIRD = new Note(3);
    public static final Note ONE_FOURTH = new Note(4);
    public static final Note ONE_SIXTHTH = new Note(6);
    public static final Note ONE_EIGHTH = new Note(8);
    public static final Note ONE_EIGHTH_T = new Note(12);
    public static final Note ONE_SIXTEENTH = new Note(16);
    public static final Note ONE_SIXTEENTH_T = new Note(24);
    public static final Note ONE_THIRTY_SECOND = new Note(32);
    public static final Note ONE_THIRTY_SECOND_T = new Note(48);
    public static final Note ONE_SIXTY_FORTH = new Note(64);

    /**
     * @param beatPerMinute the referenced tempo.
     * @param res           the note to calculate.
     * @return the length of a note in seconds with respect to the referenced tempo.
     */
    public static double getSecondsPerNote(double beatPerMinute, Note n) {
        return 8 * 60.0 / (n.getFactor() * beatPerMinute);
    }

    /**
     * @param bpm  tempo in beat per minute
     * @param note the note to calculate sample length for
     * @return number of samples that is the length of this note at this bpm
     */
    public static double getSamplesPerNote(double bpm, Note note) {

        return DSPSystem.getSamplingRate() * 4.0 / (bpm * note.getFactor() / (60.0));
    }

}
