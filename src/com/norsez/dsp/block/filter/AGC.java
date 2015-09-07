package com.norsez.dsp.block.filter;

/**
 * <p>Title: Auto Gain Control</p>
 * <p>Description: <b> Don't use this. It does not work.</b>
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class AGC
        extends Filter {

    public SmoothingFilter lp;
    public double minAmp, maxAmp;
    public double gainFactor;

    public AGC() {

        lp = new SmoothingFilter();

        gainFactor = 1;

    }

    public void setCutoff(double cps) {

        super.setCutoff(cps);
        lp.setCutoff(cps);

    }

    public void setMaxAmp(double max) {
        maxAmp = Math.abs(max);

    }

    public void setMinAmp(double min) {
        minAmp = Math.abs(min);
    }

    private double temp;

    public double tick(double in) {
        temp = Math.abs(in);

        if (getMode() == Mode.OFF) {
            lastValue = in;
        } else {
            if (temp >= minAmp && temp <= maxAmp) {
                lastValue = in;
            } else {
                if (temp < minAmp && temp != 0) {
                    gainFactor = minAmp / in;
                } else if (temp > maxAmp) {
                    gainFactor = maxAmp / in;
                }

                lastValue = lp.tick(gainFactor);
            }
        }

        return lastValue;
    }

    public Mode[] getSupportedModes() {
        Mode[] m = {
            Mode.OFF, Mode.LP};
        return m;

    }
}