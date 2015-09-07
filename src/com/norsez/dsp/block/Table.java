package com.norsez.dsp.block;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author unascribed
 * @version 1.0
 */

public class Table {
    private static final int LENGTH = DSPSystem.getSmallTableSize();
    private static final int HI_LENGTH = DSPSystem.getBigTableSize();
    private static final int MASK = LENGTH - 1;
    private static final double K_ENV_CURVE = 10.0;

    public final static double[] T_IMPLUSE = getImpulseTable(LENGTH);
    public final static double[] T_SINE = getSineTable(LENGTH);
    public final static double[] T_SAW = getSawUpTable(LENGTH);
    public final static double[] T_TRIANGLE = getTriangleTable(LENGTH);
    public final static double[] T_SQUARE = getSquareTable(LENGTH);
    public final static double[] T_LOG_UP = getLogTable(LENGTH, K_ENV_CURVE, true);
    public final static double[] T_LOG_DOWN = getLogTable(LENGTH, K_ENV_CURVE, false);
    public final static double[] T_EXP_DOWN = getExponentialTable(LENGTH, K_ENV_CURVE, false);
    public final static double[] T_EXP_UP = getExponentialTable(LENGTH, K_ENV_CURVE, true);

    /**
     * Map 0..1 to appriate Envelope time.
     */
    public final static double[] T_ENV_TIME = getEnvelopeTime(LENGTH, 5.0);


    public final static double[] T_ENV_UP = getEnvUp(DSPSystem.ENV_CURVE, LENGTH);

    public final static double[] T_ENV_DOWN_LOG = getEnvDownLog(DSPSystem.
            ENV_CURVE, LENGTH);
    public final static double[] T_ENV_DOWN_EXP = getEnvDownExp(DSPSystem.
            ENV_CURVE, LENGTH);

    /**
     * This maps raw notes, i.e. midi note number / 127.0 , to note frequency cps.
     */
    public final static double[] T_NOTE2CPS = getNote2CpsTable(LENGTH);

    /**
     * This is the table to map [0,1] to appropriate cutoff cps for filters
     */
    public final static double[] T_CUTOFF = getCutoff(LENGTH);
    public final static double[][] T_BL_SAW = getAntiAliasedSawTable(HI_LENGTH,
            DSPSystem.MAX_PARTIALS);
    public final static double[][] T_BL_SQUARE = getAntiAliasedSquareTable(HI_LENGTH, DSPSystem.MAX_PARTIALS);
    public final static double[][] T_BL_TRIANGLE = getAntiAliasedTriangleTable(HI_LENGTH, DSPSystem.MAX_PARTIALS);

    public final static double[] T_FREQUENCY = getFrequencyTable(DSPSystem.
            NYQUIST_CPS, LENGTH);
    public final static double[] T_LFO_RATE = getFrequencyTable(DSPSystem.
            MAX_LFO_RATE / DSPSystem.getSamplingRate(), LENGTH);
    public final static String[] T_NOTE_NAMES = getNoteNames();

    /**
     * Use this table to set drive for the antialiased oscillator
     * to eliminate final aliases. Interpolation.linear( Table.T_DRIVE , rawnote ) where 0.0 < rawnote < 0.1
     * will give good drive value for AntiAliasedWavetable.setDrive ( double ).
     */
    public final static double[] T_DRIVE = getDriveTable(LENGTH);

    public final static double[] T_SQRT_2 = getSqrtTwo(LENGTH);

    private Table() {
    }

    /**
     *
     */
    private static double[] getEnvelopeTime(int length, double curve) {
        double[] table = new double[length];
        /*
        final double curve_denom = Math.exp(curve) - 1.0;
        final double envtime_denom = Math.exp(K_ENV_CURVE) -1.0;
        int mask = length -1;

        for (int i = 0; i < table.length; i++) {
          //scale value

          table [i] = 1.0- (( Math.exp(curve * (1.0-((double)i/mask))) - 1.0 ) / curve_denom);

          //put in env time
          table [i] = (( Math.exp(K_ENV_CURVE * (1.0-table[i])) - 1.0 ) / envtime_denom ) ;
        }
        */
        final double taper = 0.00001;
        for (int i = 0; i < table.length; i++) {

            //taper the values
            //table [i] = 1.0 - ( (Math.pow(taper,-(double)i/(length-1)))/(taper-1) );
            table[i] = ((Math.pow(taper, (double) i / (length - 1))) - 1) / (taper - 1);

            //calculate delta time for each tapered value
            table[i] = (-0.01 + 0.0000005) * table[i] + 0.01;

        }

        return table;
    }

    private static double[] getEnvUp(double curve, int length) {
        double[] table = new double[length];
        for (int i = 0; i < table.length; i++) {
            table[i] = Math.exp((1.0 - (double) i / (length - 1)) / -curve);
        }
        return table;
    }

    private static double[] getEnvDownExp(double curve, int length) {
        double[] table = new double[length];
        for (int i = 0; i < table.length; i++) {
            table[i] = Math.exp(((double) i / (length - 1.0)) / -curve);
        }
        return table;
    }

    private static double[] getEnvDownLog(double curve, int length) {
        double[] table = new double[length];
        for (int i = 0; i < table.length; i++) {
            table[i] = 1.0 - Math.exp((1.0 - (double) i / (length - 1)) / -curve);
        }
        return table;
    }

    private static double[] getDriveTable(int len) {
        double table[] = new double[len];
        double temp;
        double curve = 100.0;

        for (int i = 0; i < len; i++) {
            temp = i / (len - 1.0);
            table[i] = 1.0 - (0.08 * (Math.pow(curve, temp) - 1.0) / (curve - 1.0));
        }
        return table;

    }

    private static double[] getSqrtTwo(int LENGTH) {
        double[] result = new double[LENGTH];
        double len = LENGTH - 1.0;
        for (int i = 0; i < LENGTH; i++) {
            result[i] = Math.sqrt(i / (len));
        }
        return result;
    }

    private static String[] getNoteNames() {
        String[] temp = new String[128];
        String names[] = {
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "B#"};

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < names.length; j++) {
                if (j + (12 * i) < 128) {
                    temp[j + (12 * i)] = names[j] + i;
                }
            }
        }

        return temp;
    }

    private static double[] getImpulseTable(int length) {
        double[] temp = new double[length];
        temp[0] = 1.0;
        return temp;
    }

    private static double[] getFrequencyTable(double maxNormalizedFreq,
                                              int length) {
        double[] temp = new double[length];

        for (int i = 0; i < length; i++) {
            temp[i] = maxNormalizedFreq *
                    Math.pow(2.0, ((i * 127.0 / (length - 1)) - 127) / 12.0);
        }
        return temp;
    }

    private static double[] getCutoff(int length) {
        double[] temp = new double[length];

        for (int i = 0; i < length; i++) {
            //temp[i] = 0.5 * Math.pow (2.0,(i - length - 1)/100.0);
            temp[i] = DSPSystem.NYQUIST_CPS *
                    (Math.pow(10.0, i / (length - 1.0)) - 1.0) / 9.0;
        }
        return temp;
    }

    private static double[] getNote2CpsTable(int length) {
        double[] temp = new double[length];
        double scale = 127.0 / (length - 1);
        for (int i = 0; i < length; i++) {
            temp[i] = DSPSystem.A4_FREQUENCY *
                    Math.pow(2.0, (scale * i - DSPSystem.A4_MIDI) / 12.0) /
                    DSPSystem.getSamplingRate();
        }
        return temp;
    }

    private static double[] getLogTable(int length, double factor, boolean up) {
        double[] temp = new double[length];
        //create log down
        for (int i = 0; i < length; i++) {
            temp[i] = 1.0 -
                    ((Math.pow(factor, i / (length - 1.0))) - 1) / (factor - 1);
        }

        //reverse the down table if up
        if (up == true) {
            double[] reverse = new double[length];
            for (int i = 0; i < length; i++) {
                reverse[i] = temp[length - 1 - i];
            }
            return reverse;
        }

        return temp;
    }

    private static double[] getExponentialTable(int length, double factor,
                                                boolean up) {
        double[] temp = new double[length];
        //create exp up
        for (int i = 0; i < length; i++) {
            temp[i] = ((Math.pow(factor, i / (length - 1.0))) - 1) / (factor - 1);
        }

        //reverse the up table if down
        if (up == false) {
            double[] reverse = new double[length];
            for (int i = 0; i < length; i++) {
                reverse[i] = temp[length - 1 - i];
            }
            return reverse;
        }

        return temp;
    }

    private static final double sineTable[] = getSineTable(DSPSystem.
            getBigTableSize());
    private static final int LEN = DSPSystem.getBigTableSize() - 1;

    /**
     * @param i ranges from 0..1.f, which is used as factor of 2Pi
     * @return sine approximation with no interpolation
     */
    private static double getSineApprox(double i) {
        return sineTable[(int) (i * LEN)];
    }

    /**
     * @return sine approximation with linear interpolation.
     */
    private static double getSineLinApprox(double i) {
        int x2 = (int) Math.ceil(i * LEN);

        return ((sineTable[x2] - sineTable[(int) (i * LEN)])) *
                (i * LEN - x2)
                + sineTable[x2];
    }

    private final static double[] NOTE_TABLE = createNoteTable();

    /**
     * @return a table that convert 0..1 to the cps values of midi notes 0..127
     */
    private static double[] createNoteTable() {
        double result[] = new double[DSPSystem.getBigTableSize()];
        double factor = result.length / 128.f;
        for (int i = 0; i < result.length; i++) {
            result[i] = DSPSystem.getMidiNoteToCPS(i / factor);
        }
        return result;
    }

    private static double[] getSineTable(int size) {
        double[] table = new double[size];

        for (int i = 0; i < size; i++) {
            table[i] = (double) (Math.sin(Math.PI * 2 * (double) i / size));
        }
        return table;
    }

    // unipolar saw up
    private static double[] getSawUpTable(int size) {
        double[] table = new double[size];

        for (int i = 0; i < size; i++) {
            table[i] = (double) i / (size - 1);
        }
        return table;
    }

    // unipolar saw down
    private static double[] getSawDownTable(int size) {
        double[] table = new double[size];

        for (int i = 0; i < size; i++) {
            table[i] = (double) -i / (size - 1);
        }
        return table;
    }

    // unipolar triangle
    private static double[] getTriangleTable(int size) {
        double[] table = new double[size];

        for (int i = 0; i < size; i++) {
            if (i < size * 0.5) {
                table[i] = 2.f * i / (size - 1);
            } else {
                table[i] = -2.f * ((double) i / (size - 1)) + 2.f;
            }
        }
        return table;
    }

    // unipolar square
    private static double[] getSquareTable(int size) {
        double[] table = new double[size];

        for (int i = 0; i < size; i++) {
            if (i < size * 0.5) {
                table[i] = 0;
            } else {
                table[i] = 1;
            }
        }
        return table;
    }

    /**
     * scales the overall amplitude of the table
     *
     * @param table the table to scale
     * @param scale the scale factor
     */
    public static void scaleTable(double[] table, double scale) {
        for (int i = 0; i < table.length; i++) {
            table[i] = table[i] * scale;
        }
    }

    /**
     * @param size          table size
     * @param numOfPartials the maximum partial of saw wave
     * @return a saw wavetable drawn with Fourier Series
     */
    private static double[] getFSSawTable(int size, int numOfPartials) {
        double[] sinetable = getSineTable(size);
        double[] result = sinetable;

        //write fundamental
        for (int i = 0; i < size; i++) {
            result[i] = sinetable[i];
        }
        int index;
        for (int p = 2; p <= numOfPartials; p++) {
            for (int i = 0; i < size; i++) {
                result[i] += (double) ((1.0 / p) * Math.sin(p * i * Math.PI * 2 / size));
                //result [i] += (double)((1.0/p) * sinetable[ (int)((p * i)% size)]);
            }
        }

        normalize(result);

        return result;
    }

    /**
     * @return numOfPartials x size matrix of fourier saw tables ranging in num of max partials.
     */

    private static double[][] getAntiAliasedSawTable(int size, int numOfPartials) {
        double[][] table = new double[numOfPartials][size];
        table[0] = Table.getSineTable(size);
        int idx = 0;
        double factor = 0;
        for (int i = 2; i <= numOfPartials; i++) {
            factor = 1.f / i;
            for (int j = 0; j < size; j++) {
                table[i - 1][j] = table[i - 2][j] +
                        factor * (Math.sin((double) j * i * Math.PI * 2 / (size - 1)));
            }
            //normalize ( table [ i - 1 ] );
            //scaleTable (table [ i - 1 ] , .7 );
        }
        return table;
    }

    /**
     * @return numOfPartials x size matrix of fourier saw tables ranging in num of max partials.
     */

    private static double[][] getAntiAliasedTriangleTable(int size,
                                                          int numOfPartials) {
        double[][] table = new double[numOfPartials][size];
        table[0] = Table.getSineTable(size);
        int idx = 0;
        double factor = 0;
        for (int i = 2; i <= numOfPartials; i++) {
            factor = 1.f / (i * i);
            for (int j = 0; j < size; j++) {
                if (i % 2 != 0) {
                    table[i - 1][j] = table[i - 2][j] +
                            (double) (factor * table[0][(i * j) % size]);
                } else {
                    table[i - 1][j] = table[i - 2][j];
                }
            }
            //normalize ( table [ i - 1]);
            //scaleTable (table [ 0] , 1.5 );
        }
        return table;
    }

    private static double[][] getAntiAliasedSquareTable(int size,
                                                        int numOfPartials) {
        double[][] table = new double[numOfPartials][size];
        table[0] = Table.getSineTable(size);
        int idx = 0;
        double factor = 0;
        for (int i = 2; i <= numOfPartials; i++) {
            factor = 1.f / i;

            for (int j = 0; j < size; j++) {
                if (i % 2 != 0) {
                    table[i - 1][j] = table[i - 2][j] +
                            (double) (factor * table[0][(i * j) % size]);
                } else {
                    table[i - 1][j] = table[i - 2][j];
                }
            }
            //normalize ( table [ i - 1]);
            //scaleTable (table [ 0] , 1.5 );
        }
        return table;
    }

    //linear interpolation between two points.
    public static double getLinIntp(double index, double[] data) {
        int ceil = (int) Math.ceil(index);
        int floor = (int) Math.floor(index);
        double result = -1;
        try {
            result = ((data[ceil] - data[floor]) / (ceil - floor)) * (index - ceil) +
                    data[ceil];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(ceil + " " + floor);
        }

        return result;
    }

    /**
     * Gets lookup table value without interpolation.
     *
     * @param value double 0..1.0
     * @param table double[] the table
     * @return double result
     */
    public static double getTableLookupValue(double value, double[] table) {
        if (value < 0) value = 0;
        if (value > 1) value = 1;
        return table[(int) ((table.length - 1) * value)];
    }

    public static void normalize(double data[]) {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < data.length; i++) {
            if (Math.abs(data[i]) > max) {
                max = Math.abs(data[i]);
            }
        }

        for (int i = 0; i < data.length; i++) {
            data[i] = (double) (data[i] / max);
        }

    }

    private static double[] getMidiToCPSTable() {
        double[] result = new double[DSPSystem.getSmallTableSize()];
        for (int i = 0; i < result.length; i++) {
            result[i] = DSPSystem.getMidiNoteToCPS(128.f * i / (result.length - 1));
        }
        return result;
    }

    /**
     * @param factor 0 = linear, >0 is exponential, <0 is logaritmic
     * @param up     y values go from 0..1 if true, and 1..0 otherwise.
     */

    private static double[] getCurve(double factor, boolean up) {
        double[] result = new double[DSPSystem.getSmallTableSize()];
        final int len = result.length - 1;

        if (factor == 0) {
            up = true;
            factor = 0.001f;
        }

        double tempk;
        if (up) {
            if (factor > 0) {
                //exponential up
                tempk = Math.exp(factor);
                for (int i = 0; i < result.length; i++) {
                    result[i] = (double) ((Math.exp(factor * i / len) - 1.f) / tempk);
                }
            } else
            //logarithm up
            {
                factor = -factor;
                for (int i = 0; i < result.length; i++) {
                    tempk = Math.exp(factor * i / len);
                    result[i] = (double) ((tempk - 1) / tempk);
                }

            }

        } else {
            if (factor > 0) {
                //exponential down
                for (int i = 0; i < result.length; i++) {
                    tempk = Math.exp(factor * i / len);
                    result[i] = -((double) ((tempk - 1) / tempk)) + 1;
                }
            } else {
                //log down
                factor = -factor;
                tempk = Math.exp(factor);
                for (int i = 0; i < result.length; i++) {
                    result[i] = -(double) ((Math.exp(factor * i / len) - 1.f) / tempk) +
                            1;
                }
            }
        }
        return result;
    }

    //used to convert 0..1 to -1..1
    private static double[] getBipolarTable() {
        double[] result = new double[DSPSystem.getSmallTableSize()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (((double) i / (DSPSystem.getSmallTableSize() - 1)) * 2f) -
                    1;
        }
        return result;
    }


    public static void main(String[] main) {
        DSPSystem.saveToFile("d:/my documents/test.txt", Table.T_ENV_TIME);
    }
}
