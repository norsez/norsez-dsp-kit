package com.norsez.dsp.block.filter.effects;

/**
 * <p>Title: PanoramaLK</p>
 * <p>Description: The non-interpolated table lookup version of the Panorama class. (It saves 1/2 times processing power
 * compared to the Panarama class when used as an LFO, but the resulution is coarser.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */
public class PanoramaLK extends Panorama {
    double[] table;

    public PanoramaLK() {

        table = new double[com.norsez.dsp.block.DSPSystem.getSmallTableSize()];


    }

    private void _fillTable(double curv, double symm) {
        double ph;

        for (int i = 0; i < table.length; i++) {
            ph = (double) i / (table.length - 1);
            if (ph <= symm) {
                table[i] = 0.5 * (Math.pow(curv, ph / symm) - 1.0) /
                        (curv - 1.0);
            } else {
                double exp = (ph - symm) / (1.0 - symm);
                table[i] = 0.5 *
                        (1.0 -
                        (((Math.pow(curv, exp) - 1.0) /
                        (curv - 1.0))));
            }
            //System.out.println (ph + "," +curv + " , " + symm + "," + table[i]);
        }


    }

    protected double _getResult(double phase) {

        int p = (int) (phase * (table.length - 1));
        return table[p];
        /*
        try{
          return table[p];
        }catch(ArrayIndexOutOfBoundsException ae){
          System.err.println ( p + " " + phase + " " + table.length);

        }
        finally {
          return 0;
        }*/
        //return com.norsez.dsp.block.Interpolation.linear(table,phase);
    }

    public void setSymmetry(double s) {
        super.setSymmetry(s);
        this._fillTable(this.curvature, this.symmetry);
    }

    public void setCurvature(double c) {
        super.setCurvature(c);
        this._fillTable(this.curvature, this.symmetry);
    }

    /**
     * for debugging the curve shape
     *
     * @param filename String
     */
    public void savePoints(String filename) {
        com.norsez.dsp.block.DSPSystem.saveToFile(filename, table);
    }
}
