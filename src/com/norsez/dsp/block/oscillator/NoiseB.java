package com.norsez.dsp.block.oscillator;

/**
 * <p>Title: NoiseB</p>
 * <p>Description: Some anonymous code for Gaussian White Noise ripped from music-com.norsez.dsp. It is not optimized.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */
public class NoiseB extends Noise {
    public NoiseB() {
    }

    boolean pass;
    double y2;
    double x1, x2, w, y1;


    public double tick(int m, double s) {

        if (pass) {
            y1 = y2;
        } else {
            do {
                x1 = 2.0f * Math.random() - 1.0;
                x2 = 2.0f * Math.random() - 1.0;
                w = x1 * x1 + x2 * x2;
            } while (w >= 1.0);

            w = Math.sqrt(-2.0 * Math.log(w) / w);
            y1 = x1 * w;
            y2 = x2 * w;
        }
        pass = !pass;

        return ((y1 * s + (float) m));
    }

}
