package com.norsez.dsp.block.filter.effects;

/**
 * <p>Title: Panorama</p>
 * <p>Description: A Panorama utility based on the Emagic's Tremolo FX. It can be used as
 * just a static panorama calculator or a tremolo LFO like in Emagic Logic.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */
public class Panorama extends com.norsez.dsp.block.DSPBlockStereo {
    double curvature, symmetry, phase, cps, stereoPhase;
    final static double MIN_CURVATURE = Math.pow(10, 0.1);
    final static double MAX_CURVATURE = Math.pow(10, 10);

    public Panorama() {

        this.curvature = 0.5;
        this.symmetry = 0.5;
    }

    /**
     * The shape of the curve
     *
     * @param v double 0 = triangle, 0.5 = exponential tri, 1.0 = square.
     */
    public void setCurvature(double v) {
        /*
        curvature = com.norsez.dsp.block.Interpolation.mapToRangeLinear(0,0.1,1,10,
            com.norsez.dsp.block.Table.getTableLookupValue(v,com.norsez.dsp.block.Table.T_EXP_UP));
         */
        curvature = Math.pow(10.0, com.norsez.dsp.block.Interpolation.mapToRangeLinear(0, 0.1, 1, 10, v));
        //System.out.println ( curvature );
    }

    /**
     * Sets the stereo phase of the right channel
     *
     * @param v double
     */
    public void setStereoPhase(double v) {
        this.stereoPhase = v;
    }

    /**
     * Left-Right Symmetry.
     *
     * @param v double
     */
    public void setSymmetry(double v) {
        this.symmetry = v;
    }

    /**
     * Set CPS in case of using it as tremolo LFO.
     *
     * @param cps double
     */
    public void setCps(double cps) {
        this.cps = cps;

    }

    /**
     * Set LFO phse in case of using it as tremolo LFO.
     *
     * @param p double
     */
    public void setPhase(double p) {
        this.phase = p;
    }

    /**
     * Core algorithm.
     *
     * @param phase double 0.0..1.0
     * @return double the result of left channel
     */
    protected double _getResult(double phase) {
        if (phase <= symmetry) {
            return 0.5 * (Math.pow(curvature, phase / symmetry) - 1.0) / (curvature - 1.0);
        } else {
            return 0.5 * (1.0 - (((Math.pow(curvature, (phase - symmetry) / (1.0 - symmetry)) - 1) / (curvature - 1.0))));
        }
    }

    public double getLeftChannelAtPhase(double phase) {
        return _getResult(phase);
    }

    public double getRightChannelAtPhase(double phase) {
        double temp = phase + this.stereoPhase;
        while (temp > 1.0)
            temp = temp - 1.0;
        return _getResult(temp);
    }


    /**
     * This is the tick method to be called in case of using it as an LFO tremolo.
     * This method actually calls getLeftChannelAtPhase and getRightChannelAtPhase.
     * Call getLastValueL() and getLastValueR() for results.
     */
    public void tick() {
        this.lastValueL = this.getLeftChannelAtPhase(phase);
        this.lastValueR = this.getRightChannelAtPhase(phase);

        phase += cps;

        while (phase > 1)
            phase = 1.0 - phase;
    }


    /**
     * Call this when you wants to tick at a slower control rate
     *
     * @param steps int number of samples to skip ticking.
     */
    public void tick(int steps) {
        while (steps-- > 0) {
            phase += cps;

            while (phase > 1)
                phase = 1.0 - phase;

        }
        this.lastValueL = this.getLeftChannelAtPhase(phase);
        this.lastValueR = this.getRightChannelAtPhase(phase);
    }
}
