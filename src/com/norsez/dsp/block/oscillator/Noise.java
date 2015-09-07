package com.norsez.dsp.block.oscillator;

import com.norsez.dsp.block.DSPBlock;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Noise
        extends DSPBlock {

    public Noise() {
    }

    public double tick() {
        lastValue = Math.random();
        if (Math.random() > 0.5)
            lastValue = -lastValue;
        return lastValue;
    }

    public void tickProcess(double[] input) {
        for (int i = 0; i < input.length; i++) {
            input[i] = Math.random();
            if (Math.random() > 0.5)
                input[i] = -input[i];
        }
        lastValue = input[input.length - 1];
    }
}
