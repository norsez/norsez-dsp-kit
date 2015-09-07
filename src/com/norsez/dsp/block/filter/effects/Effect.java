package com.norsez.dsp.block.filter.effects;

import com.norsez.dsp.block.DSPBlockStereo;
import com.norsez.dsp.block.DSPSystem;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public abstract class Effect
        extends DSPBlockStereo {

    protected double volumeScale;
    protected double inputLevel;
    protected double outputLevel;
    protected double temp;
    protected final int UPDATE_MARK = 10;
    protected int update_count, half;

    //pre-calculated value for the dry signal volume
    protected double dryMixFactor;

    protected final double MAX_DB = 1.0000;

    public void setInputLevel(double v) {
        inputLevel = v;
    }

    public void setOutputLevel(double v) {
        outputLevel = v * MAX_DB;
        //dryMixFactor = 1- Interpolation.linear( Table.T_SQRT_2, outputLevel );
        dryMixFactor = 1 - outputLevel;
        //dryMixFactor = (1- outputLevel) * (1-outputLevel);
    }

    public double getDryMixFactor() {
        return this.dryMixFactor;
    }

    public double getOutputdB() {
        return 20.0 * Math.log(outputLevel);
    }

    public Effect() {
        setInputLevel(1);
        setOutputLevel(0.6);
    }

    /**
     * @deprecated Effects need input to process output. This method does not make sense.
     */
    public void tick() {

        DSPSystem.unsupported(this + "use tick (double,double) instead");

    }

    public void tick(double[] l, double[] r) {
        DSPSystem.unsupported(this + "use tick (double,double) instead");
    }

    public abstract void tick(double l, double r);

    static public class Channel {
        private final String name;

        private Channel(String name) {
            this.name = name;
        }

        public static final Channel MONO = new Channel("Mono");
        public static final Channel STEREO = new Channel("Stereo");

        public String toString() {
            return name;
        }

        public int getNumOfChannels() {
            if (this == MONO) {
                return 1;
            } else {
                return 2;
            }
        }
    }
}