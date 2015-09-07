package com.norsez.dsp.block.oscillator;

import com.norsez.dsp.block.DSPBlock;
import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Table;

/**
 * <p>Title: DigiWave </p>
 * <p>Description: Digital wave version of the SyncableOscillator.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class DigiWave
        extends DSPBlock {

    private final static short PARTIALS[][] = {
        //these partials are created with hetro opcode in csound. they are short integers in the range of [0..32767]
        {
            13333, 104, 31, 274, 187, 1953, 12585, 1708, 139, 34, 154, 290, 137, 200,
            99, 208, 1, 140, 105, 161, 83, 161, 78, 96, 40, 146, 12, 82, 39, 85, 16,
            78}
        , {
            14961, 252, 10222, 5625, 329, 94, 240, 98, 70, 75, 57, 83, 50, 16, 28, 54,
            22, 35, 75, 30, 89, 2, 12, 57, 5, 29, 30, 8, 28, 31, 23, 27}
        , {
            9786, 89, 269, 9600, 6196, 436, 157, 45, 66, 293, 2526, 1136, 382, 168,
            144, 77, 138, 140, 124, 138, 50, 73, 104, 100, 82, 60, 87, 45, 84, 28, 63,
            91}
        , {
            25806, 6, 26, 45, 5, 100, 9, 503, 4292, 568, 126, 187, 13, 133, 53, 64,
            51, 83, 64, 90, 52, 72, 57, 64, 19, 51, 14, 60, 36, 30, 62, 17}
        , {
            544, 1761, 1087, 1339, 1327, 4953, 1186, 31, 935, 1550, 735, 1829, 1146,
            1669, 1273, 220, 676, 2820, 527, 1022, 801, 662, 632, 74, 239, 134, 394,
            296, 362, 1393, 932, 211}
        , {
            1359, 7492, 2577, 4836, 2506, 4837, 740, 25, 520, 696, 1054, 639, 1043,
            697, 934, 409, 437, 216, 230, 159, 145, 86, 124, 166, 142, 112, 98, 85,
            113, 66, 97, 118}
        , {
            14546, 224, 4781, 73, 2882, 75, 2413, 74, 1417, 59, 1144, 40, 1058, 83,
            644, 110, 493, 40, 371, 79, 91, 176, 169, 171, 164, 197, 189, 185, 107,
            181, 170, 351}
        , {
            43, 4255, 317, 4797, 471, 9635, 603, 3628, 809, 638, 3353, 380, 984, 482,
            258, 200, 247, 134, 151, 110, 186, 85, 125, 64, 155, 53, 154, 78, 97, 87,
            128, 46}
        , {
            1914, 10924, 3739, 7227, 3272, 1909, 169, 100, 148, 241, 363, 250, 377,
            236, 345, 135, 236, 105, 153, 55, 94, 48, 59, 106, 119, 62, 70, 43, 72,
            41, 65, 74}
        , {
            573, 2861, 1412, 1943, 1468, 6871, 1072, 96, 1073, 533, 1159, 2004, 1355,
            762, 1557, 267, 630, 3067, 1013, 350, 648, 533, 300, 183, 199, 21, 17,
            219, 30, 206, 223, 109}
        , {
            13853, 8962, 5025, 1252, 2085, 114, 414, 139, 121, 59, 61, 21, 52, 38, 70,
            15, 2, 67, 13, 50, 96, 22, 69, 5, 2, 12, 18, 42, 50, 10, 6, 7}
        , {
            16710, 10364, 2580, 1186, 607, 428, 81, 19, 76, 40, 35, 34, 63, 56, 33,
            42, 37, 4, 60, 19, 74, 19, 7, 26, 32, 3, 19, 37, 25, 21, 12, 24}
        , {
            6896, 9382, 8752, 3146, 1469, 655, 209, 278, 448, 236, 63, 29, 134, 82,
            74, 86, 123, 6, 33, 35, 109, 12, 29, 100, 53, 10, 31, 66, 65, 31, 68, 43}
        , {
            317, 26738, 509, 11, 225, 69, 47, 4, 20, 5, 89, 2, 463, 2440, 584, 1, 236,
            95, 165, 37, 98, 52, 118, 5, 73, 49, 84, 19, 29, 36, 137, 1}
        , {
            5600, 11807, 5678, 2653, 531, 1100, 1880, 499, 570, 232, 634, 305, 178,
            120, 14, 116, 61, 16, 96, 18, 9, 7, 10, 113, 41, 33, 29, 52, 61, 139, 94,
            61}
        , {
            4931, 2096, 249, 1334, 1086, 656, 1100, 5433, 9523, 2257, 15, 332, 436,
            560, 264, 301, 162, 167, 166, 211, 124, 181, 149, 167, 60, 162, 100, 141,
            70, 120, 96, 107}
        , {
            13762, 9154, 5244, 1011, 2135, 142, 379, 151, 9, 75, 13, 98, 2, 78, 19,
            52, 0, 53, 15, 55, 85, 33, 37, 8, 26, 2, 4, 43, 32, 21, 18, 0}
        , {
            3561, 2209, 1976, 993, 315, 677, 204, 1106, 2376, 2127, 1705, 288, 2338,
            6568, 2406, 393, 409, 272, 428, 207, 310, 144, 296, 120, 286, 166, 209,
            139, 128, 59, 242, 102}
        , {
            3107, 1063, 3474, 312, 992, 1643, 529, 50, 2812, 242, 169, 2860, 295,
            1657, 232, 1653, 702, 1030, 943, 929, 1294, 530, 247, 1443, 427, 730, 669,
            6, 1406, 145, 1039, 136}
        , {
            3657, 3931, 2526, 3911, 1899, 2079, 264, 3297, 36, 1679, 14, 1650, 2, 899,
            43, 1642, 377, 4, 114, 800, 290, 65, 170, 767, 437, 53, 86, 746, 526, 71,
            54, 668}
        , {
            7744, 4117, 2809, 2323, 2362, 1544, 1636, 1332, 1891, 680, 1196, 578, 708,
            489, 458, 382, 202, 308, 195, 243, 124, 156, 174, 121, 7, 313, 172, 195,
            169, 149, 107, 127}
        , {
            13250, 696, 2079, 3891, 435, 2829, 1107, 963, 1830, 1242, 83, 283, 623,
            834, 390, 414, 21, 161, 42, 221, 157, 163, 38, 1, 300, 99, 142, 113, 142,
            82, 75, 63}
        , {
            6656, 8079, 5796, 3939, 1683, 2221, 2602, 312, 160, 130, 154, 58, 63, 72,
            79, 110, 64, 55, 24, 23, 54, 34, 43, 22, 29, 38, 34, 46, 25, 77, 36, 36}
        , {
            1786, 3544, 3742, 4001, 281, 904, 1326, 753, 127, 497, 1469, 2696, 406,
            306, 238, 341, 1622, 383, 667, 275, 968, 156, 579, 336, 437, 1044, 743,
            942, 412, 416, 1063, 299}
        , {
            4210, 4358, 4494, 3481, 22, 33, 488, 4198, 275, 37, 486, 3423, 520, 17,
            425, 3321, 1042, 4, 492, 50, 313, 5, 238, 23, 231, 2, 207, 30, 177, 11,
            119, 18}
        , {
            4790, 6119, 4829, 5719, 1967, 797, 1534, 1527, 1101, 1474, 1066, 85, 248,
            29, 50, 12, 67, 166, 132, 112, 90, 78, 76, 106, 79, 99, 82, 32, 101, 40,
            101, 47}
        , {
            703, 219, 2045, 6711, 4451, 4513, 4277, 209, 103, 191, 14, 52, 285, 1366,
            3379, 1556, 100, 200, 101, 123, 121, 371, 235, 232, 131, 186, 175, 201,
            83, 139, 94, 185}
        , {
            13, 14827, 12523, 452, 145, 134, 10, 62, 30, 36, 70, 37, 45, 527, 2263,
            641, 29, 154, 56, 158, 28, 15, 36, 107, 55, 64, 32, 13, 39, 77, 17, 62}
        , {
            5293, 3367, 1488, 724, 127, 244, 327, 453, 559, 193, 691, 747, 1315, 1603,
            1740, 1677, 1423, 1115, 686, 209, 246, 590, 947, 1122, 1232, 1177, 1086,
            934, 693, 476, 237, 47}
        , {
            4065, 4065, 3039, 2889, 2440, 2348, 1657, 1613, 1597, 1097, 1151, 956,
            390, 425, 358, 333, 465, 356, 514, 288, 298, 291, 193, 76, 360, 360, 203,
            239, 174, 127, 122, 266}
        , {
            380, 420, 524, 885, 8571, 579, 399, 449, 850, 4126, 561, 382, 435, 855,
            2599, 535, 356, 410, 870, 1808, 454, 285, 384, 873, 1319, 386, 291, 347,
            878, 957, 330, 251}
        , {
            1340, 1404, 1411, 1405, 1393, 1378, 1360, 1339, 1315, 1289, 1262, 1233,
            1202, 1169, 1136, 1101, 1064, 1026, 988, 949, 908, 866, 824, 780, 736,
            691, 645, 600, 554, 508, 462, 417}
        , {
            404, 1330, 888, 585, 1264, 373, 342, 1538, 1444, 766, 810, 1202, 2045,
            590, 2543, 1284, 1836, 1312, 1012, 428, 702, 744, 1538, 2462, 1491, 1251,
            2734, 1197, 1311, 1238, 1475, 782}
        , {
            4887, 7117, 6046, 5671, 5066, 2941, 2896, 2897, 2712, 1983, 3780, 2569,
            651, 612, 2295, 611, 481, 2087, 573, 268, 182, 274, 145, 75, 59, 58, 55,
            21, 35, 45, 25, 25}

    };

    public static int getDigiWavesCount() {
        return PARTIALS.length;
    }

    public static int getDigiMorphWavesCount() {
        return PARTIALS.length * (PARTIALS.length - 1);
    }

    /**
     * This is the table for digiwaves orgainized by DIGIWAVES [table number][partial number][data of the table number].
     */
    public final static double DIGIWAVES[][][] = buildDigiWaves();

    private static double[][][] buildDigiWaves() {

        if (DIGIWAVES != null) {
            return DIGIWAVES;
        }

        double[][][] digiwaves = new double[PARTIALS.length][PARTIALS[0].length][
                DSPSystem.getSmallTableSize()];

        int i, j, k;
        double coeff;

        for (i = 0; i < PARTIALS.length; i++) {
            double factor = -1;
            for (j = 0; j < PARTIALS[0].length; j++) {
                if (PARTIALS[i][j] > factor) {
                    factor = PARTIALS[i][j];

                }
            }

            coeff = PARTIALS[i][0] / factor;

            for (j = 0; j < DSPSystem.getSmallTableSize(); j++) {
                digiwaves[i][0][j] = coeff *
                        Math.sin(6.283185307 * j / (DSPSystem.getSmallTableSize() - 1));
            }

            for (j = 1; j < PARTIALS[0].length; j++) {
                coeff = PARTIALS[i][j] / factor;

                for (k = 0; k < DSPSystem.getSmallTableSize(); k++) {
                    digiwaves[i][j][k] = digiwaves[i][j - 1][k] + coeff *
                            Math.sin((j + 1) * 6.283185307 * k /
                            (DSPSystem.getSmallTableSize() - 1));
                }
            }

        }

        for (i = 0; i < PARTIALS.length; i++) {
            for (j = 0; j < PARTIALS[0].length; j++) {
                Table.normalize(digiwaves[i][j]);
            }
        }

        return digiwaves;
    }


    /**
     * This is the table that pairs all combination of digiwave tables.
     * It is just used for fast reference of tables in tickMorph ()
     *
     * @see tickMorph()
     */
    private final static short MORPH_PAIRS[][] = getMorphPairTable();

    private static short[][] getMorphPairTable() {
        short[][] table = new short[PARTIALS.length * (PARTIALS.length - 1)][2];
        for (int i = 0; i < PARTIALS.length; i++) {

            int k = 0;
            int count = 0;
            while (count < PARTIALS.length - 1) {
                if (k == i) {
                    k++;
                } else {
                    table[i * (PARTIALS.length - 1) + count][0] = (short) i;
                    table[i * (PARTIALS.length - 1) + count][1] = (short) k;
                    k++;
                    count++;
                }

            }
        }

        return table;
    }

    private int morphPair;

    /**
     * Select the morph pairs.
     *
     * @see tickMorph ()
     */
    public void setMorphPair(double i) {
        morphPair = (int) ((MORPH_PAIRS.length - 1.0) * i);
    }

    private double morph;

    /**
     * Selects the morph factor between two wave tables.
     *
     * @see tickMorph()
     */
    public void setMorphAmount(double m) {
        morph = m;
    }

    /**
     * This generates PPG/Waldorf MicroWave styled wavemorphing output.
     * First call selectMorphPair (double) which is equivalent to selecting a wave table on PPG.
     * Then call setMorphAmount ( double ) which is equivalent to selecting wavestep on PPG.
     */

    public double tickMorph() {

        while (phase > 1) {
            phase -= 1.0;
        }
        phase_ = phase * mask;
        phase_1 = (int) Math.ceil(phase_);
        phase_0 = (int) (phase_);

        //wave 1
        temp0 = (DIGIWAVES[MORPH_PAIRS[morphPair][0]][partial_0][phase_1] -
                DIGIWAVES[MORPH_PAIRS[morphPair][0]][partial_0][phase_0])
                * (phase_ - phase_0) +
                DIGIWAVES[MORPH_PAIRS[morphPair][0]][partial_0][phase_0];

        //wave 2
        temp1 = (DIGIWAVES[MORPH_PAIRS[morphPair][1]][partial_0][phase_1] -
                DIGIWAVES[MORPH_PAIRS[morphPair][1]][partial_0][phase_0])
                * (phase_ - phase_0) +
                DIGIWAVES[MORPH_PAIRS[morphPair][1]][partial_0][phase_0];

        //calculate morph
        lastValue = (temp1 - temp0) * (morph) + temp0;

        phase += cps;
        return lastValue;
    }

    /**
     * Selects a digi wave table for tick ().
     *
     * @see double tick ()
     */
    public void setDigiWave(double i) {
        if (i > 1) i = 1;
        if (i < 0) i = 0;
        double tablenum = i * (PARTIALS.length - 1.0);
        curDigiTable = (int) (tablenum);

        smoothDigitable = tablenum - curDigiTable;
        table0 = curDigiTable;
        table1 = table0 + 1;
        if (table1 >= PARTIALS.length)
            table1 = 0;

    }

    protected double smoothDigitable;
    protected int curDigiTable;
    protected double cps, phase, phase_, partial_, temp1, temp0, temp;
    protected int curPartial, phase_0, phase_1, partial_0, partial_1,
    table0, table1;
    private int mask = (DSPSystem.getSmallTableSize() - 1);

    public void setCps(double cps) {
        this.cps = cps;
        partial_ = DSPSystem.NYQUIST_CPS / cps;

        if (partial_ >= PARTIALS[0].length) {
            partial_ = PARTIALS[0].length - 1;

        }
        partial_0 = (int) (partial_);
        partial_1 = (int) Math.ceil(partial_);

        curPartial = (int) (partial_);
    }

    /**
     * oscillates with a wavetable selected with setDigiwav (double)
     *
     * @see setDigiWave (double)
     */
    public double tick() {

        while (phase > 1) {
            phase -= 1.0;
        }
        phase_ = phase * mask;
        phase_1 = (int) Math.ceil(phase_);
        phase_0 = (int) (phase_);
        phase += cps;
        lastValue = (DIGIWAVES[curDigiTable][partial_0][phase_1] -
                DIGIWAVES[curDigiTable][partial_0][phase_0])
                * (phase_ - phase_0) + DIGIWAVES[curDigiTable][partial_0][phase_0];
        return lastValue;

    }

    /**
     * Like tick (), but it will interpolate between two adjacent waves
     * depending on the smoothDigitable wave value set by the setDigiWave (double)
     * method.
     *
     * @return audio output
     */
    public double tickSmooth() {
        while (phase > 1) {
            phase -= 1.0;
        }
        phase_ = phase * mask;
        phase_1 = (int) Math.ceil(phase_);
        phase_0 = (int) (phase_);



        //do table 1
        temp0 = (DIGIWAVES[table0][partial_0][phase_1] -
                DIGIWAVES[table0][partial_0][phase_0])
                * (phase_ - phase_0) + DIGIWAVES[table0][partial_0][phase_0];

        //do table 2
        temp1 = (DIGIWAVES[table1][partial_0][phase_1] -
                DIGIWAVES[table1][partial_0][phase_0])
                * (phase_ - phase_0) + DIGIWAVES[table1][partial_0][phase_0];


        //do the result in between
        lastValue = (temp1 - temp0) * smoothDigitable + temp0;

        phase += cps;
        return lastValue;

    }


    private double lastPhase;
    private double isSynced;

    public void setSync(double p) {
        if (p < lastPhase && isSynced()) {
            this.phase = 0;
        }
        lastPhase = p;
    }

    public void setIsSynced(double i) {
        isSynced = i;
    }

    public boolean isSynced() {
        if (isSynced == 0) {
            return false;
        } else {
            return true;
        }
    }

    public DigiWave() {
        setCps(0);
        setDigiWave(0);
    }

    public String toString() {
        return "DigiWave::"
                + " num of tables =" + this.PARTIALS.length
                + ", current table =" + this.curDigiTable
                + ", smoothDigiTable =" + this.smoothDigitable
                + ", table0 = " + table0
                + ", table1 = " + table1
                ;

    }
}