package com.norsez.dsp.block.filter.effects;

/**
 * <p>Title: ArcTanClipper </p>
 * <p>Description: An arctan table lookup (with linear interpolation) waveshaper. It is useful for clipping signals. </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */
public class ArcTanClipper extends com.norsez.dsp.block.DSPBlock {

    private double[] table;
    private double maxInput;


    /**
     * @param maxInput   double The assuming absolute maximum input (0 ..infinity) .
     *                   Any absolute value of input greater than this value will be clipped to clipCeil
     * @param clipCeil   double The absolute value of the maximum output ( 0.0..1.0)
     * @param table_size int the size of the look up table. The greater the value of the maxInput,
     *                   the greater the table size should be.
     */
    public ArcTanClipper(double maxInput, double clipCeil, int table_size) {

        this.maxInput = maxInput;
        mask = table_size - 1;
        this.maskfactor = mask / maxInput;

        table = new double[table_size];

        double max = 0;
        //build arctan lookup.
        for (int i = 0; i < table_size; i++) {
            table[i] = Math.atan(maxInput * i / mask);
            if (table[i] > max) max = table[i];
        }

        double factor = max / clipCeil;
        //scale the table to the ceiling amp.
        for (int i = 0; i < table_size; i++) {
            table[i] = table[i] / factor;
        }
    }

    double sign;
    final int mask;
    final double maskfactor;
    int x_1, x_0;
    double x;

    public double tick(double in) {
        if (in < 0) sign = -1; else sign = 1;
        in = Math.abs(in);
        if (in > maxInput) in = maxInput;

        x = in * maskfactor;
        if (x == 0) return 0;

        x_0 = (int) x;
        x_1 = x_0 + 1;

        if (x_1 > mask) return sign * table[mask];

        lastValue = sign * ((table[x_1] - table[x_0]) * (x) + table[x_0]);

        return lastValue;
    }


    public void tickProcess(double[] input) {
        for (int i = 0; i < input.length; i++) {
            if (input[i] < 0) sign = -1; else sign = 1;
            input[i] = Math.abs(input[i]);
            if (input[i] > maxInput) input[i] = maxInput;

            x = input[i] * maskfactor;
            if (x == 0) {
                input[i] = 0;
                continue;
            }

            x_0 = (int) x;
            x_1 = x_0 + 1;

            if (x_1 > mask) {
                input[i] = sign * table[mask];
                continue;
            }

            lastValue = sign * ((table[x_1] - table[x_0]) * (x) + table[x_0]);

            input[i] = lastValue;

        }
    }

    public double tick() {
        throw new java.lang.UnsupportedOperationException("Use tick (double) or tickProcess(double[]).");
    }
    /*
    public static void main ( String [] args ){
      ArcTanClipper ac = new ArcTanClipper (6,0.90,1024);
      double t [] = new double [ 256 ];
      for (int i=0; i < 256; i++){
        t [ i ] = ac.tick(Math.sin( 2 * Math.PI * i / 255.0) * 6.0);
      }

      com.norsez.dsp.block.DSPSystem.saveToFile("d:/my documents/test.csv",t);

    }
    */
}
