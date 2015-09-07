package com.norsez.dsp.block;

/**
 * <p>Title: DSPBlock </p>
 * <p>Description: Basic abstract class for many classes in the com.norsez.dsp package. </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public abstract class DSPBlock {

    protected double lastValue;

    public double getLastValue() {
        return lastValue;
    }

    public abstract double tick();

    /*
    public void tick(double[] buffer) {

      for (int i = 0; i < buffer.length; i++) {
        buffer[i] = tick();
      }
    }
        */

}