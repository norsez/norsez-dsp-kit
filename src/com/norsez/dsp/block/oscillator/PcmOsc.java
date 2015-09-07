package com.norsez.dsp.block.oscillator;

import com.norsez.dsp.block.DSPSystem;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class PcmOsc extends com.norsez.dsp.block.DSPBlock {

    private double[] pcmData;
    private int channels;
    private int bits;

    private double cps;
    private double based_cps;
    boolean looping;
    double phase;
    int mask;
    int loop_start_point;
    int loop_end_point;
    int end_minus_start;
    final double ONE_TWELFTH = 1.0 / 12;

    /**
     * @param v double must be [0..1] which is mapped from midi note [0..127]. The based key is assumed to be A4.
     */
    public void setCps(double v) {
        cps = based_cps * Math.pow(2, (v * 127 - DSPSystem.A4_MIDI) * ONE_TWELFTH);
    }

    public PcmOsc() {
        cps = 1.0 / DSPSystem.getSamplingRate();
    }

    public void resetPhase() {
        phase = loop_start_point;
    }

    public void setLoopPoints(int start, int end) {
        if (start > end) start = end;

        if (start < 0) start = 0;
        if (end > mask) end = mask;

        loop_start_point = start;
        loop_end_point = end;
        end_minus_start = end - start;
    }


    /**
     * Batch ticking with no interpolation.
     *
     * @param input double[]
     */
    public void tickProcess(double input[]) {

        for (int i = 0; i < input.length; i++) {

            input[i] = pcmData[(int) (end_minus_start * phase) + loop_start_point];

            phase += cps;

            if (phase >= 1.0) {
                if (!looping) {
                    input[i] = 0;

                }
                while (phase > 1)
                    phase = phase - 1.0;

            }
        }

        lastValue = input[input.length - 1];
    }


    int x_c, x_f;
    double y_c, y_f, y_0, x_0;
    int len;

    /**
     * Batch ticking with linear interpolation
     *
     * @param input double[]
     */
    public void tickLinIntProcess(double input[]) {

        for (int i = 0; i < input.length; i++) {

            x_0 = (end_minus_start * phase) + loop_start_point;
            x_f = (int) x_0;
            x_c = (int) (x_f + 1) % len;
            y_f = pcmData[x_f];
            y_c = pcmData[x_c];

            input[i] = ((y_c - y_f) / (x_c - x_f)) * (x_0 - x_f) + y_f;

            phase += cps;

            if (phase >= 1.0) {
                if (!looping) {
                    input[i] = 0;

                }
                while (phase > 1)
                    phase = phase - 1.0;

            }
        }


    }


    public double tick() {


        if (phase >= 1.0) {
            if (!looping)
                return (lastValue = 0);
            else
                while (phase > 1)
                    phase = phase - 1.0;
        }

        phase += cps;


        lastValue = pcmData[(int) (end_minus_start * phase) + loop_start_point];


        return lastValue;
    }

    public void loadWavFileInMono(String filename) {
        try {

            AudioFileFormat format = AudioSystem.getAudioFileFormat(new File(filename));


            BufferedInputStream ins = new BufferedInputStream(new FileInputStream(filename));

            byte buf[];

            channels = format.getFormat().getChannels();
            bits = format.getFormat().getSampleSizeInBits();

            int bytesToRead;


            if (bits == 16)
                bytesToRead = 2;
            else {
                throw new java.lang.UnsupportedOperationException("PcmOsc only supports 16 bit wave files.");
            }

            buf = new byte[bytesToRead];


            int header_length = format.getByteLength() - (format.getFrameLength() * format.getFormat().getFrameSize());
            int table_length = format.getFrameLength();

            pcmData = new double[table_length];
            double volume_factor = 1.0 / Math.sqrt(channels);
            double temp;
            int bytesRead;
            //skip header
            ins.skip(header_length);
            //start reading.
            for (int i = 0; i < table_length; i++) {
                temp = 0;
                for (int j = 0; j < channels; j++) {
                    bytesRead = ins.read(buf);
                    if (bytesRead == 0) throw new java.io.EOFException();
                    temp += (((buf[0] & 0xFF) | (buf[1] << 8)) / 32768.0)
                            * volume_factor
                            ;

                }
                pcmData[i] += temp;
            }

            ins.close();

            //reset loop property
            mask = table_length - 1;
            len = table_length;
            looping = true;
            setLoopPoints(0, mask);
            based_cps = 1.0 / mask;
            resetCps();

            //DSPSystem.saveToFile("C:\\Documents and Settings\\norsez\\Desktop\\debug.txt",pcmData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets cps at the rate that the sample was recorded, i.e. set it at key note.
     */
    public void resetCps() {
        cps = based_cps;
    }

}
