package com.norsez.dsp.block.filter;

/**
 * <p>Title: AllPassFilter</p>
 * <p>Description: Good for building phaser. </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class AllPassFilter
        extends Filter {

    private double _zm1, _a1, delay;

    public void setDelay(double delay) { //sample delay time
        if (delay < 0.000453515) {
            delay = 0.000453515;
        }
        if (delay > 0.4535) {
            delay = 0.4535;
        }
        this.delay = delay;
        _a1 = (1. - delay) / (1. + delay);
    }

    public void setCutoff(double f) {
        setDelay(f);
        frequency = delay;
    }

    public void reset() {
        lastValue = _zm1 = 0;

    }

    public double tick(double inSamp) {

        lastValue = inSamp * -_a1 + _zm1;
        _zm1 = lastValue * _a1 + inSamp;
        /*
             if (lastValue < -1000000 ){
          //throw new java.lang.IllegalStateException
          System.out.println
          ( "\n"+
            this.name
            +  " filter blown up\n"
            + "a1 " + this._a1
            + "\nzm1 " + this._zm1
            + "\nin " + inSamp
            + "\nlastvalue "+ lastValue
            );
             }
         */

        return lastValue;
    }

    public double getDelay() {
        return delay;
    }

    public Mode[] getSupportedModes() {
        Mode m[] = {
            Mode.OFF, Mode.AP};
        return m;
    }

    private String name;

    public void setName(String name) {
        this.name = name;
    }

}