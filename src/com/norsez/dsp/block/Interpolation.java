package com.norsez.dsp.block;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Interpolation {

    private Interpolation() {
    }

    /**
     * This maps value in the interval [0,1] to another interval of values determined by table.
     * The input is clipped to [0,1]
     *
     * @param table the table containing curve to interpolate the value to.
     * @param value must be between 0..1
     */
    public static double linear(double[] table, double value) {

        //clip the input because we will usually use this for dspblock's parameter input
        if (value > 1.0) {
            value = 1.0;
        }
        if (value < 0) {
            value = 0;

        }
        value = value * (table.length - 1);

        int x1 = (int) Math.ceil(value);
        int x0 = (int) value;

        return (table[x1] - table[x0]) * (value - x0) + table[x0];
    }

    /**
     * This maps value in the interval [-1,1] to another interval of values determined by table.
     * The input is wrapped around in [-1,1]
     *
     * @param table the table containing curve to interpolate the value to.
     * @param value must be between 0..1
     */
    public static double linearBipolar(double[] table, double value) {

        //wrap around the input because we normally use this method with audio input

        if (value > 1.0) {
            value = 1;
            //while (value> 1) value -=1;
        }
        if (value < -1) {
            value = -1;
            //while ( value < -1) value += 1;

        }
        value = 0.5 * (table.length - 1) * (value + 1);

        int x1 = (int) Math.ceil(value);
        int x0 = (int) value;

        return (table[x1] - table[x0]) * (value - x0) + table[x0];
    }

    /**
     * This maps value in the interval [0,1] to another intervalof values determined by table which is scaled to the interval [lo,hi].
     *
     * @param table the table containing curve to interpolate the value to. <br><b>Table must be normalized for this to work</b>.
     * @param value must be between lo..hi
     * @param lo    must be the minimum of the original interval.
     * @param hi    must be the maximum ofthe original interval.
     */
    public static double linear(double[] table, double value, double lo,
                                double hi) {

        if (lo > hi) {
            throw new java.lang.IllegalArgumentException("Interpolation.linear:  lo > hi Error\n");
        }

        return (hi - lo) * linear(table, value) + lo;
    }

    public static void main(String[] arg) {

        double[] buf = new double[256];
        double temp;
        for (int i = 0; i < buf.length; i++) {
            temp = ((2 / 255.0) * i) - 1;
            System.out.println(temp);
            buf[i] = linearBipolar(Table.T_SINE, temp);
        }

        DSPSystem.saveToFile("output.csv", buf);

    }

    /**
     * @return the nearest value in the table (no interpolation).
     */
    public static double none(double table[], double v) {
        if (v > 1) {
            return table[table.length - 1];
        } else if (v < 0) {
            return table[0];
        } else {
            return table[(int) ((table.length - 1) * v)];
        }
    }

    /**
     * @return the result of mapping x in the range of [x1,x2] to [y1,y2].
     */
    public static double mapToRangeLinear(double x1, double y1, double x2,
                                          double y2, double x) {
        double slope = (y2 - y1) / (x2 - x1);
        return slope * (x - x1) + y1;
    }
}
