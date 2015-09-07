package com.norsez.dsp.block;

/**
 * <p>Title: DSPBlockStereo</p>
 * <p>Description: Basic abstract class for my class that has two outputs in com.norsez.dsp
 * package.<br> The convention is to override <b>public void
 * tick ()</b> with desire process and store the output in <b>lastValueL</b>
 * and <b>lastValueR</b>. Then called getLastValueL () and getLastValueR(). </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public abstract class DSPBlockStereo {
    protected double lastValueR, lastValueL;

    /**
     * updates lastValueL and lastValueR
     */

    public abstract void tick();

    public double getLastValueL() {
        return this.lastValueL;
    }

    public double getLastValueR() {
        return this.lastValueR;
    }

    //public abstract void tick (double [] buffL, double [] buffR);
    //public abstract void tick (double inL, double inR);
}