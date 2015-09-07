package com.norsez.dsp.block.filter.effects;

import com.norsez.dsp.block.Interpolation;

/**
 * <p>Title:  WaveShaper </p>
 * <p>Description: (Insert FX) Stereo or Mono.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Waveshaper
        extends Effect {

    protected final int NUM_CHANNELS;
    protected double table[];
    protected double drive;
    protected final double MAX_dB = 6.05; //+36 dB

    public Waveshaper(Effect.Channel ch) {

        NUM_CHANNELS = ch.getNumOfChannels();
        setDrive(0);
    }

    /**
     * Table used are assumed to be bipolar.
     */
    public void setTable(double[] table) {
        this.table = table;
    }

    public void setDrive(double d) {
        drive = 1 + d * MAX_dB;
    }

    public void tick(double l, double r) {

        if (outputLevel == 0) {
            lastValueL = l;
            lastValueR = r;
            return;
        }

        if (NUM_CHANNELS == 1) {
            temp = (l + r) * inputLevel;
            lastValueL = Interpolation.linearBipolar(table, temp * drive);
            lastValueL = temp * lastValueL * drive * outputLevel +
                    temp * (1 - outputLevel);
            lastValueR = lastValueL;
        } else {
            temp = l * inputLevel;
            lastValueL = Interpolation.linearBipolar(table, temp * drive);
            lastValueL = temp * lastValueL * outputLevel * drive +
                    temp * (1 - outputLevel);

            temp = r * inputLevel;
            lastValueR = Interpolation.linearBipolar(table, temp * drive);
            lastValueR = temp * lastValueR * outputLevel * drive +
                    temp * (1 - outputLevel);

        }
    }
}