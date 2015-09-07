package com.norsez.dsp.block.sequencer;

import com.norsez.dsp.block.DSPBlock;

/**
 * <p>Title: SampleClock</p>
 * <p>Description: Count the samples since the clock is generated.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class SampleClock
        extends DSPBlock {
    long time;

    public SampleClock() {
        time = 1;
    }

    public double tick() {

        if (time < 0) {
            lastValue = time = 0;
        } else {
            lastValue = time++;

        }
        return lastValue;
    }

    public static void main(String[] args) {

    }
}