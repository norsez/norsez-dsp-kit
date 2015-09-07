package com.norsez.dsp.block;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>Title: DSPSystem </p>
 * <p>Description: Defines many static constants and functions that are shared among
 * classes in com.norsez.dsp package.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 *
 * @author unascribed
 * @version 1.0
 */

public class DSPSystem {

    private DSPSystem() {
        java.util.Locale.setDefault(new java.util.Locale("En", "US"));
    };

    private static double SAMPLING_RATE = 44100;

    /**
     * @return System overall sampling rate.
     */
    public static double getSamplingRate() {
        return SAMPLING_RATE;
    };
    public static void setSamplingRate(double v) {
        SAMPLING_RATE = v;
    }

    private static int BUFFER_SIZE = 4096;
    private static int PUT_SIZE = 512;

    public static int getBufferSize() {
        return BUFFER_SIZE;
    }

    public static int getPutSize() {
        return PUT_SIZE;
    }

    public static void setBufferSize(int s) {
        BUFFER_SIZE = s;
    }

    public static void setPutSize(int s) {
        PUT_SIZE = s;
    }

    private final static int BIG_TABLE_LENGTH = 1024;
    /**
     * TABLE_FACTOR is used to scale the size of big table to small table. Default factor is 0.5.
     */
    private static double TABLE_FACTOR = 0.5;

    /**
     * @return defined size of large tables such as those used for waveform generation.
     */
    public static int getBigTableSize() {
        return BIG_TABLE_LENGTH;
    }

    /**
     * @return defined size of small tables such those used by Interpolation and
     *         approximation of math functions.
     */
    public static int getSmallTableSize() {
        return (int) (BIG_TABLE_LENGTH * TABLE_FACTOR);
    }

    /**
     * Set the small/large ratio of table size.
     *
     * @param f factor
     */
    public static void setTableFactor(double f) {
        TABLE_FACTOR = f;
    };

    /**
     * @see com.norsez.dsp.block.Oversampler for the class that encapusulates the oversampling
     *      process.
     * @deprecated Try not to use this. Oversampling rate of each block should be
     *             freely definable by the class users.
     */
    private static int OVER_SAMPLING = 32;

    public static int getOverSamplingFactor() {
        return OVER_SAMPLING;
    };
    public static void setOverSamplingFactor(int times) {
        OVER_SAMPLING = times;
    };

    /**
     * Maximum partials for the bandlimited waveform tables used by com.norsez.dsp.block.oscillator.*
     */
    public static final int MAX_PARTIALS = 500;

    /**
     * Maximum LFO rate. This is used by com.norsez.dsp.block.oscillator.multimode.LFO for determining
     * the LFO speed from input [0..1]
     */
    public static final double MAX_LFO_RATE = 20.0;
    /**
     * Midi note number of note A4
     */
    public static final int A4_MIDI = 69;

    /**
     * Frequency of note A4
     */
    public static final double A4_FREQUENCY = 440.0;

    /**
     * Decided normalized nyquist frequency for the system.
     */
    public static final double NYQUIST_CPS = 0.45351473922;

    /**
     * Curve constant used in the capacitor charging formular for analog-styled envelope shape.
     */
    public static final double ENV_CURVE = 0.3;

    /**
     * Precalculated value of 1/127.0
     */
    public static final double ONE_OVER_127 = 0.007874015748031496062992125984252;

    /**
     * Precalculated value of 1.0 / samplingrate
     */
    public static final double ONE_OVER_SAMPLINGRATE = 1.0 / getSamplingRate();

    /**
     * System maximum tempo
     */
    public static final double MAX_BPM = 255.0;
    /**
     * System minimum tempo
     */
    public static final double MIN_BPM = 5.0;
    /**
     * The decibel cutoff point for audio blocks, e.g. Envelope. If the output is
     * lower than this point, the output can be considered insignificant by
     * the programmer. For example, if the last output of an ADSR envelope is
     * lower this point, then the envelop is considered inactive and can be reused
     * for other tasks.
     */

    public static final double STEALABLE_POINT = 1E-4; // -276 dB

    /**
     * The output PrintStream.
     */

    //public static final PrintWriter DEBUG_OUT = getDebugOutput();

    private static java.io.PrintWriter getDebugOutput() {
        PrintWriter debugOutput = null;
        try {
            debugOutput = new PrintWriter(new FileWriter("debug.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return debugOutput;
    }

    /*
       public static double  SYSTEM_SAMPLING_RATE = 44100;
       public static double  SYSTEM_NYQUIST_RATE = SYSTEM_SAMPLING_RATE * 0.5;
         public static int  SYSTEM_CONTROL_RATE = (int)( SYSTEM_SAMPLING_RATE * 0.0001 );
       public static int SYSTEM_BUFFER_SIZE = 128;
       public final static double  TWO_PI = (double )(Math.PI * 2.0f);
       public final static double  PI = (double )Math.PI;
       public static int SYSTEM_HI_TABLE_LENGTH = 4096;
       public static int SYSTEM_LO_TABLE_LENGTH = 256;
       public static int OVER_SAMPLING = 36;
       public static double ONE_OVER_SAMPLING = 1.0/OVER_SAMPLING;
     */
    /*
       public static int getSystemBufferSize  (){
      return SYSTEM_BUFFER_SIZE;
       }
       public static void setSystemBufferSize (int i){
      SYSTEM_BUFFER_SIZE = i;
       }
       public static int getHighTableLength  (){
      return SYSTEM_HI_TABLE_LENGTH;
       }
       public static void setHighTableLength (int i){
      SYSTEM_HI_TABLE_LENGTH = i;
       }
       public static int getLowTableLength  (){
      return SYSTEM_LO_TABLE_LENGTH;
       }
       public static void setLowTableLength (int i){
      SYSTEM_LO_TABLE_LENGTH = i;
       }
       public static void setSystemSamplingRate (double  fs){
      SYSTEM_SAMPLING_RATE = fs;
      SYSTEM_NYQUIST_RATE = fs * 0.5f;
       }
       public static final double  getSystemSamplingRate (){
      return SYSTEM_SAMPLING_RATE;
       }
       public static void setSystemControlRate (double  fs){
      if (fs > getSystemSamplingRate ())
         throw new java.lang.IllegalArgumentException("Control rate > Sampling rate");
      SYSTEM_CONTROL_RATE = fs;
       }
       public static final double  getSystemControlRate (){
      return SYSTEM_CONTROL_RATE;
       }
       public static final double  getSystemNyquistRate (){
      return SYSTEM_NYQUIST_RATE;
       }
     */

    /**
     * dump  an array of doubles into a file.
     *
     * @param filename filename to save to
     * @param data     the data to save
     */
    public static final void saveToFile(String filename, double[] data) {
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < data.length; i++) {
                w.write("" + data[i] + "\n");
            }
            w.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts midi note into normalized frequency.
     *
     * @param note midi note number
     * @return normalized frequency of the note number.
     */
    public static double getMidiNoteToCPS(double note) {
        if (note >= 0 && note < 128) {
            return (double) (A4_FREQUENCY * Math.pow(2, (note - A4_MIDI) / 12.0)) /
                    getSamplingRate();
        }
        return 0;
    }

    /**
     * Converts milliseconds into number of samples.
     *
     * @param milliseconds the time to convert
     * @return number of samples in millseconds
     */
    public static int getSamplesPerMilliseconds(double milliseconds) {
        return (int) (milliseconds * getSamplingRate() * 0.001);
    }

    public static double getMillisecondsPerSamples(int samples) {
        return 1000 * samples / getSamplingRate();
    }

    public static void unsupported(String msg) {
        throw new java.lang.UnsupportedOperationException(msg);
    }

    public static String getDSPSystemInfo() {
        Runtime rt = Runtime.getRuntime();
        return "DSPSystem\n"
                + "Memory used " + (rt.totalMemory() - rt.freeMemory()) + " kb\n"
                + "Sampling Rate " + DSPSystem.getSamplingRate() + " Hz\n"
                + "Max partials " + DSPSystem.MAX_PARTIALS + "\n"
                + "Large Table Size " + DSPSystem.getBigTableSize() + "\n"
                + "Small Table Size " + DSPSystem.getSmallTableSize() + "\n"
                + "A4 Frequency " + DSPSystem.A4_FREQUENCY + " Hz\n"
                + "A4 MIDI NOTE NUMBER " + DSPSystem.A4_MIDI + "\n"
                + "Max LFO rate " + DSPSystem.MAX_LFO_RATE + " Hz\n"
                + "Decided Nyquist frequency " +
                DSPSystem.NYQUIST_CPS * DSPSystem.getSamplingRate() + " Hz\n"

                ;
    }

    /**
     * the normalized value of -6 dB.
     */
    public final static double dB_6 = Math.exp(-6 / 20.0);

    public static void main(String[] args) {
        System.out.println(DSPSystem.getDSPSystemInfo());
    }
}
