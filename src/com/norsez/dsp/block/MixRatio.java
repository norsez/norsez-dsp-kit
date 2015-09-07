package com.norsez.dsp.block;

import java.util.Vector;

/**
 * <p>Title: MixRatio</p>
 * <p>Description: Class represents common mix ratio of effect dry/wet ratio. Use getIn to get dry ratio, use getOut to get wet ratio.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class MixRatio {

    private final String name;
    private final double in, out;
    private static Vector ratios;

    private MixRatio(String name, double in, double out) {
        if (ratios == null) {
            ratios = new Vector();
        }
        this.name = name;
        ratios.add(this);
        this.in = in;
        this.out = out;

    }

    /**
     *
     */
    public double getIn() {
        return in;
    };
    public double getOut() {
        return out;
    };

    public static MixRatio[] getAllRatios() {

        MixRatio[] m = new MixRatio[ratios.size()];
        for (int i = 0, n = ratios.size(); i < n; i++) {
            m[i] = (MixRatio) ratios.elementAt(i);
        }
        return m;
    }

    public String toString() {
        return name;
    }

    public final static MixRatio M1_1 = new MixRatio("1:1", 1, 1);
    public final static MixRatio M4_3 = new MixRatio("4:3", 4, 3);
    public final static MixRatio M3_4 = new MixRatio("3:4", 3, 4);
    public final static MixRatio M3_2 = new MixRatio("3:2", 3, 2);
    public final static MixRatio M2_3 = new MixRatio("2:3", 2, 3);
    public final static MixRatio M2_1 = new MixRatio("2:1", 2, 1);
    public final static MixRatio M1_2 = new MixRatio("1:2", 1, 2);
    public final static MixRatio M3_1 = new MixRatio("3:1", 3, 1);
    public final static MixRatio M1_3 = new MixRatio("1:3", 1, 3);
    public final static MixRatio M4_1 = new MixRatio("4:1", 4, 1);
    public final static MixRatio M1_4 = new MixRatio("1:4", 1, 4);
    public final static MixRatio M1_0 = new MixRatio("1:0", 1, 0);
    public final static MixRatio M0_1 = new MixRatio("0:1", 0, 1);

}