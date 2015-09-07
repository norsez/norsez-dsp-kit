package com.norsez.dsp.block.envelope;

import com.norsez.dsp.block.DSPBlock;
import com.norsez.dsp.block.Interpolation;
import com.norsez.dsp.block.Table;

/**
 * <p>Title: Envelope </p>
 * <p>Description: Envelope uses tick (double gate).
 * It uses polling technique to check key on/off from the gate value that's coming in tick(double gate).
 * When gate changes from 0 to >0, it's key on. When gate changes from >0 to 0, it's key off.
 * <p/>
 * </p>
 * <p>Because we use polling to detect key on off, default operation of envelope will be like mono legato.
 * That is, you must retrigger () the envelope explicitly to restart the envelope. Or else envelope will
 * only restart when current gate > 0 and lastgate = 0;
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public abstract class Envelope
        extends DSPBlock {

    /**
     * gate value from last tick
     */
    protected double lastGate;

    /**
     * maintains last value of the last stage. useful for interpolation of the value of the decay stage where the max and mix value are in (0,1) as opposed to just 0 and 1.
     */
    protected double lastValueOfLastStage;

    /**
     * This maps linear 0..1 to envelope time with respect to the sampling rate.
     */
    protected double convertNormalizeToTime(double value) {
        return value;
    }

    /**
     * this is just a buffer used in calculating envelope output.
     */
    protected double temp;

    /**
     * this is where the envelope is in its current stage.
     */
    protected double point;

    /**
     * this is the current stage of the envelope.
     */
    protected Stage stage;

    protected double stealablePoint;

    public Stage getStage() {
        return stage;
    }

    /**
     * this is the current key or gate stage.
     */
    //protected Key key;

    //public Key getKey () { return key;}

    //public abstract void setKey (Envelope.Key k);

    public final double tick() {
        throw new java.lang.UnsupportedOperationException("tick () does not work in Envelope, use tick(double) instead.");
    }

    public abstract double tick(double gate);

    public abstract void retrigger();

    public static class Key {
        private final String name;
        private static int nextOrdinal = 0;
        private final int ordinal = nextOrdinal++;

        private Key(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }

        public static final Key KEY_ON = new Key("Key On");
        public static final Key KEY_OFF = new Key("Key Off");
    }

    public static class Stage {
        private final String name;
        private static int nextOrdinal = 0;
        private final int ordinal = nextOrdinal++;

        private Stage(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }

        public static final Stage SUSTAIN = new Stage("Sustain");
        public static final Stage ATTACK = new Stage("Attack");
        public static final Stage DECAY = new Stage("Decay");
        public static final Stage RELEASE = new Stage("Release");
        public static final Stage HOLD_S = new Stage("Hold at Sustain");

    }

    /**
     * maps [0..1.00] to appropriate value for envelope time
     */
    public static double getEnvelopetime(double x) {

        return Interpolation.linear(Table.T_ENV_TIME, x);
    }

    public static double envTimetoSeconds(double envtime, double samplingrate) {
        return (1.0 / envtime) / samplingrate;
    }

    /**
     * if the output is lower than the steal point, the envelope is considered ended.
     */
    public void setstealablePoint(double s) {

        stealablePoint = s;
    }

    public boolean isStealable() {
        return lastValue <= stealablePoint;
    }




    /**
     *
     joe wright at nyrsound
     f(x) = y1 + (y2-y1) * ( (1-e^(I(x) * a)) / (1-e^a) )

     Where I(x) = (x - x1) / (x2 - x1)

     a = 0 : straight line
     a < 0 : exponential curve
     a > 0 : inverse exponential




     Dan Timis
     for (long i = 0; i < rampUpLength; i++) {
     float rampValue = (float) i / (float) rampUpLength;
     *out++ = *in++ * rampValue * rampValue;
     }

     */


}
