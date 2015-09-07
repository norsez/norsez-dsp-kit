package com.norsez.dsp.block.filter;

import com.norsez.dsp.block.DSPBlock;

/**
 * <p>Title: Delay</p>
 * <p>Description: Delay line with out any kind of interpolation. Good for simple delay effect.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Delay
        extends DSPBlock {
    private final static int LENGTH = 44100 * 2;
    private double[] buffer;
    private int length;
    private int inPoint, outPoint;
    private double delayTime;

    public Delay() {

        buffer = new double[LENGTH];
    }

    public Delay(int buflength) {
        buffer = new double[buflength];
        length = buflength;
        clear();

        lastValue = 0;

        setDelay(1);

        inPoint = 0;
    }

    public void clear() {
        int i;
        for (i = 0; i < length; i++) {
            buffer[i] = 0;
        }
    }

    public double getLastValue() {
        return lastValue;
    }

    public double getDelayTime() {
        return delayTime;
    }

    public int getLength() {
        return length;
    }

    public void setDelay(double d) {
        if (d > 1) {
            d = 1;
        }
        if (d < 0) {
            d = 0;
        }
        delayTime = d;
        int lag = (int) (d * (length >> 1));
        outPoint = inPoint - lag;

        while (outPoint < 0) {
            outPoint += length;
        }
    }

    public double tick() {
        throw new java.lang.UnsupportedOperationException("tick () doesn't work in Delay, use tick (double) instead.");
    }

    public double tick(double in) {
        buffer[inPoint++] = in;

        // Check for end condition
        if (inPoint == length) {
            inPoint -= length;
            // Read out next value
        }
        lastValue = buffer[outPoint++];

        if (outPoint >= length) {
            outPoint -= length;

        }

        return lastValue;
    }
}