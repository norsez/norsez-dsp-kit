package com.norsez.dsp.block.sequencer;

import com.norsez.dsp.block.DSPSystem;

/**
 * <p>Title: ArpSwingPoly</p>
 * <p>Description: This is the polyphonic input version of ArpSwing. It is used for input from
 * midi keyboard or Kb2Midi so that it will work like a real Arpeggiator where
 * you hold down the keyboard, and the arp knows to circle through those keys.
 * Use the tick (double, double [] ) for this class.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class ArpSwingPoly extends ArpSwing {

    protected double[] lastInputGates;

    public ArpSwingPoly() {
        super(128);
        setActiveSteps(1.0);

        lastInputGates = new double[128];

    }

    public double tick(double sampleNumber, double[] inputGates) {

        int noteCount = 0;
        for (int i = 0; i < 128; i++) {

            if (lastInputGates[i] != inputGates[i]) {
                noteCount++;
                setNoteAt(noteCount, DSPSystem.ONE_OVER_127 * i);
            }
            setActiveSteps(noteCount * DSPSystem.ONE_OVER_127);
            lastInputGates[i] = inputGates[i];

        }

        lastValue = super.tick(sampleNumber);

        return lastValue;
    }

    /**
     * @param g input gate value
     * @return output gate value.
     * @deprecated Use tick (double []) instead.
     */
    public double tick(double g) {
        return super.tick(g);
    }

    public static void main(String[] args) {
        //ArpSwingPoly arpSwingPoly1 = new ArpSwingPoly();
    }

}