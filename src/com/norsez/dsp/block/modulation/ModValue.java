package com.norsez.dsp.block.modulation;

/**
 * <p>Title: ModValue</p>
 * <p>Description: Since some values can be used as mod source and they are not
 * an DSPBlock ( e.g. velocity, midi controller), this class serves
 * as an ModSource that keeps these values.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class ModValue implements ModSource {
    private double value;

    public ModValue(double init) {
        this.value = init;
    }

    public void tick(double newValue) {
        value = newValue;
    }

    public double getValue() {
        return value;
    }

}