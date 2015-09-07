package com.norsez.dsp.block.filter.effects;

import com.norsez.dsp.block.DSPBlock;
import com.norsez.dsp.block.MixRatio;
import com.norsez.dsp.block.filter.Delay;
import com.norsez.dsp.block.filter.SmoothingFilter;

/**
 * <p>Title: DelayFBMono</p>
 * <p>Description: Mono Delay with Feedback. Insert FX so it has its own dry/wet mixer.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class DelayFBMono
        extends DSPBlock {

    private Delay delay;
    private SmoothingFilter filter;
    private double feedback;
    private MixRatio outRatio;
    private double dryRatio, wetRatio;
    private double wetMix, dryMix;

    public DelayFBMono(int buffer_length) {
        delay = new Delay(buffer_length);
        setOutputRatio(MixRatio.M1_1);
        setWetMix(0.25);
        setDryMix(1);
        setFeedback(0.25);

    }

    public double tick(double in) {

        lastValue = delay.tick(in + feedback * delay.getLastValue());

        return lastValue * wetRatio * wetMix + in * dryRatio * dryMix;
    }

    /**
     * set Output Ratio
     */

    public void setOutputRatio(MixRatio r) {
        outRatio = r;
        wetRatio = outRatio.getOut();
        dryRatio = outRatio.getIn();

    }

    /**
     * set delay time.
     *
     * @param d 0..1
     */
    public void setDelayTime(double d) {
        delay.setDelay(d);
    }

    /**
     * set Feedback level
     */
    public void setFeedback(double f) {
        feedback = f;
    }

    /**
     * set Wet level
     */
    public void setWetMix(double w) {
        wetMix = w;
    }

    /**
     * set dry level
     */
    public void setDryMix(double d) {
        dryMix = d;
    }

    public double tick() {
        /**@todo Implement this com.norsez.dsp.block.filter.Filter abstract method*/
        throw new java.lang.UnsupportedOperationException("Method tick() not yet implemented.");
    }
}