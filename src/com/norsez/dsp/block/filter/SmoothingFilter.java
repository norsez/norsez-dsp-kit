package com.norsez.dsp.block.filter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class SmoothingFilter
        extends Filter {

    public SmoothingFilter() {
        setCutoff(com.norsez.dsp.block.DSPSystem.NYQUIST_CPS);
    }

    public double tick(double input) {
        lastValue = lastValue + frequency * (input - lastValue);
        return lastValue;
    }

    public Mode[] getSupportedModes() {
        Mode[] m = {
            Mode.LP};
        return m;
    }

}