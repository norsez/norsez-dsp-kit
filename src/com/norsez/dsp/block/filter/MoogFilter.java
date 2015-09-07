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

public class MoogFilter
        extends Filter {

    double f, p, q; //filter coefficients
    double b0, b1, b2, b3, b4; //filter buffers (beware denormals!)
    double t1, t2; //temporary buffers

    public MoogFilter() {

        mode = Mode.LP;
        setCutoff(0.5);
        setResonance(0.01);

    }

    /**
     * @param c is the cutoff frequency. Must be scale by a table so that it is between  0 and 0.5 and with the right curve.
     */
    public void setCutoff(double c) {

        super.setCutoff(c);

        if (mode != Mode.OFF && mode != null) {
            updateConstants();
        }
    }

    private void clear() {
        f = p = q = b0 = b1 = b2 = b3 = b4 = t1 = t2 = 0;
    }

    public void setResonance(double r) {

        super.setResonance(r);

        if (mode != Mode.OFF && mode != null) {
            updateConstants();
        }
    }

    private void updateConstants() {
        q = 1.0 - frequency;
        p = frequency + 0.8 * frequency * q;
        f = p + p - 1.0;
        q = resonance * (1.0 + 0.5 * q * (1.0 - q + 5.6 * q * q));
    };

    public double tick(double in) {

        if (mode == Mode.BP) {

            in -= (q * b4); //feedback
            t1 = b1;
            b1 = (in + b0) * p - b1 * f;
            t2 = b2;
            b2 = (b1 + t1) * p - b2 * f;
            t1 = b3;
            b3 = (b2 + t2) * p - b3 * f;
            b4 = (b3 + t1) * p - b4 * f;
            b4 = b4 - b4 * b4 * b4 * 0.166667; //clipping
            b0 = in;

            lastValue = (3.0 * (b3 - b4));

        } else if (mode == Mode.LP) {

            in -= (q * b4);
            t1 = b1;
            b1 = (in + b0) * p - b1 * f;
            t2 = b2;
            b2 = (b1 + t1) * p - b2 * f;
            t1 = b3;
            b3 = (b2 + t2) * p - b3 * f;
            b4 = (b3 + t1) * p - b4 * f;

            b4 = b4 - b4 * b4 * b4 * 0.166667;
            b0 = in;

            lastValue = b4;
        } else if (mode == Mode.HP) {

            in -= (q * b4); //feedback
            t1 = b1;
            b1 = (in + b0) * p - b1 * f;
            t2 = b2;
            b2 = (b1 + t1) * p - b2 * f;
            t1 = b3;
            b3 = (b2 + t2) * p - b3 * f;
            b4 = (b3 + t1) * p - b4 * f;
            b4 = b4 - b4 * b4 * b4 * 0.166667; //clipping
            b0 = in;
            // Highpass output:  in - b4;
            lastValue = (in - b4);

        } else {
            lastValue = in;

        }
        if (lastValue > 100) {
            throw new java.lang.IllegalStateException(" Moog filter blew"
                    + "\n in = " + in
                    + "\n q = " + q);
        }

        return lastValue;
    }

    public Mode[] getSupportedModes() {
        Mode[] m = {
            Mode.OFF, Mode.LP, Mode.HP, Mode.BP};
        return m;
    }

}