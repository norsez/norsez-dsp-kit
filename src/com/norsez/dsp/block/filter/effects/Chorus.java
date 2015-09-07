package com.norsez.dsp.block.filter.effects;

import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Interpolation;
import com.norsez.dsp.block.Table;
import com.norsez.dsp.block.filter.Delay;
import com.norsez.dsp.block.oscillator.Wavetable;

/**
 * <p>Title: Chorus. </p>
 * <p>Description: (Send FX) Construct by choosing the number of the the delay line to use (preferrable even numbers)
 * Then half of the lines are used for the left channel and the other half are used for the right channel.
 * Initialize with 12 delay lines for a hexa chorus effect.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Chorus
        extends Effect {

    private Delay delays[];
    private Wavetable waves[];
    private int numLines;
    private final double BUF_LEN_IN_SECS = 0.0825;
    private final int BUF_LEN = (int) (BUF_LEN_IN_SECS *
            DSPSystem.getSamplingRate());
    private final double MAX_LFO_CPS = 5.0 / DSPSystem.getSamplingRate();
    private double depth, rate, modulation, feedback, panSpread, _panSpread;

    //this is how many delay lines are active at one tick
    private int activeLines;

    public Chorus(int lines) {
        if (lines < 1) {
            lines = 1;
        }
        numLines = lines;
        volumeScale = 1.0 / Math.sqrt(numLines * 0.5);
        //volumeScale = 1;

        waves = new Wavetable[numLines];
        double[][] tables = {
            Table.T_SINE, Table.T_BL_TRIANGLE[10], Table.T_BL_TRIANGLE[5]};
        for (int i = 0; i < numLines; i++) {
            waves[i] = new Wavetable();

            waves[i].setWavetable(tables[i % tables.length]);

            waves[i].phase = i / (double) (numLines - 1.0);
            waves[i].cps = 2.0 / DSPSystem.getSamplingRate();
        }

        delays = new Delay[numLines];
        for (int i = 0; i < numLines; i++) {
            delays[i] = new Delay(BUF_LEN);
            delays[i].setDelay(.5);
        }

        setDepth(0.5);
        setModulation(0.5);
        setRate(0.5);
        setActiveLines(1);
    }

    /**
     * Set how many chorus channels are active. Minimum lines is always 1.
     */
    public void setActiveLines(double i) {
        activeLines = (int) (i * numLines) + 1;
        if (activeLines > numLines) {
            activeLines = numLines;

        }
    }

    /**
     * set rate of LFO modulating the delay time of the delay lines.
     */
    public void setRate(double r) {
        rate = r;
        for (int i = 0; i < numLines; i++) {
            waves[i].cps = r * MAX_LFO_CPS;
        }
    }

    double tempL, tempR;

    public void tick(double l, double r) {

        if (outputLevel == 0) {
            lastValueR = lastValueL = 0;
            return;
        }

        if (update_count >= UPDATE_MARK) {
            update_count = 0;
            for (int i = 0; i < numLines; i++) {

                temp = waves[i].tick(UPDATE_MARK) * modulation + depth;
                delays[i].setDelay(temp);

            }
        } else {
            update_count++;

        }

        tempL = l * inputLevel;
        tempR = r * inputLevel;
        temp = 1.0 - panSpread;

        lastValueL = lastValueR = 0;

        for (int i = 0; i < activeLines; i++) {
            if (i % 2 == 0) {
                /*
                         if (tempL+ feedback * lastValueL > 100){
                       throw new java.lang.IllegalStateException (
                  "" + tempL + "\n"+
                  feedback + "\n"+
                  lastValueL + "\n" +
                  temp
                  );
                         }*/

                delays[i].tick(tempL + feedback * lastValueL);
                lastValueL += delays[i].getLastValue() * (temp);
                lastValueR += delays[i].getLastValue() * (temp);
            } else {
                delays[i].tick(tempR + feedback * lastValueR);
                lastValueL += delays[i].getLastValue() * (panSpread);
                lastValueR += delays[i].getLastValue() * (panSpread);

            }

        }

        temp = volumeScale * outputLevel;

        lastValueL *= temp;
        lastValueR *= temp;
        /*
             if (delays [0].getLastValue() > 100){
          throw new java.lang.IllegalStateException (
              "" + tempL + "\n"+
              feedback + "\n"+
              lastValueL + "\n" +
              temp
              );
             }
         */
    }

    /**
     * set feedback amount to create flanger.
     */
    public void setFeedback(double f) {
        feedback = f;
    }

    /**
     * set the modulation amount of the lfo to modulate the delay time.
     */
    public void setModulation(double m) {
        modulation = m;
    }

    /**
     * Set the delay time of the delay lines.
     */
    public void setDepth(double r) {
        depth = r;

    }

    /**
     * set pan spread of the effect.
     */
    public void setPanSpread(double p) {
        //value in the [ 0,1 ]range
        _panSpread = p;
        //value used in real calculation
        panSpread = Interpolation.linear(Table.T_EXP_UP, p, 0, 0.5);
    }

    /**
     * get pan spread value
     */
    public double getPanSpread() {
        return _panSpread;
    }

    public static void main(String[] args) {
        Chorus c = new Chorus(2);
        c.setOutputLevel(1);
        c.setRate(0.5);
        c.setModulation(.5);
        c.setDepth(0.5);
        for (int i = 0; i < 30000; i++) {
            c.tick(i / 300.0, 2 * i / 300.0);
        }
    }
}