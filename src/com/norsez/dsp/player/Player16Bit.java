package com.norsez.dsp.player;

import javax.sound.sampled.*;

import com.norsez.dsp.block.*;
import com.norsez.dsp.synth.*;

/**
 * <p>Title: </p>
 * <p>Description: This audio real time player assumes 16 bit data stereo</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Player16Bit
    extends Thread {
  private AudioFormat audioFormat;
  private DataLine.Info info;
  private SourceDataLine line;
  private boolean isActive;
  private double samplingRate;

  private int BUFFER_SIZE = 512;
  private int samplesFromSynth;

  private double output[][];
  private double maxAmp;
  private byte _data[];
  private int sampleSizeInBytes;

  private SynthModel synth;

  public void setBufferSize(int s) {
    BUFFER_SIZE = s;
  }



  public Player16Bit(double samplingRate, SynthModel synth) {

    this.synth = synth;

    audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                  (float) samplingRate, 16, 2, 4,
                                  (float) samplingRate, false);

    maxAmp = Math.pow(2, audioFormat.getSampleSizeInBits() - 1) - 4;

    line = null;
    info = new DataLine.Info(
        SourceDataLine.class,
        audioFormat);

    try {
      line = (SourceDataLine) AudioSystem.getLine(info);
      line.open(audioFormat);
    }
    catch (LineUnavailableException e) {
      e.printStackTrace();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    line.start();

    _data = new byte[BUFFER_SIZE];
    samplesFromSynth = BUFFER_SIZE / (2 * audioFormat.getSampleSizeInBits() / 8);
  }

  public void setActive(boolean b) {
    isActive = b;


  }

  public boolean isActive() {
    return isActive;
  }

  public void run() {
    double temp;
    int baseAddr;
    while (isActive()) {

      if (!synth.isInUse()) {
        continue;
      }

      for (int i = 0; i < samplesFromSynth; i++) {

        synth.tick();

        baseAddr = i * 4;

        temp = synth.getLastValueR();
        //temp = clip ( temp , -1.0, 1.0);
        //if (temp > 1 || temp < -1) temp = temp * 1.0/temp;

        //16 bit little endian
        _data[baseAddr] = (byte) ( (int) Math.round(maxAmp * temp) & 0xFF);
        _data[baseAddr + 1] = (byte) ( ( (int) Math.round(maxAmp * temp)) >>> 8 & 0xFF);
        temp = synth.getLastValueL();
        _data[baseAddr + 2] = (byte) ( (int) Math.round(maxAmp * temp) & 0xFF);
        _data[baseAddr +
            3] = (byte) ( ( (int) Math.round(maxAmp * temp)) >>> 8 & 0xFF);
      }

      line.write(_data, 0, _data.length);
    }
    line.drain();

  }

  private double clip(double x, double a, double b) {
    double x1 = Math.abs(x - a);
    double x2 = Math.abs(x - b);
    x = x1 + (a + b);
    x -= x2;
    x *= 0.5;
    return x;
  }

  public static void main(String[] args) {
    Player16Bit p = new Player16Bit(DSPSystem.getSamplingRate(), new Test());
    p.start();
    p.setActive(true);
  }

  public static class Test
      extends SynthModel {

    com.norsez.dsp.block.oscillator.AntiAliasedWavetable osc;

    public Test() {
      osc = new com.norsez.dsp.block.oscillator.AntiAliasedWavetable();
      osc.setWavetable(Table.T_BL_SQUARE);
      osc.setCps(2200 / 44100.0);

    }

    public boolean isInUse() {
      return true;
    }

    public void tick(double l, double r) {}

    public void tick() {

    }

    double last;
    public double getLastValueL() {
      last = osc.tick() * 0.1;
      return last;
    }

    public double getLastValueR() {
      return last;
    }

    public void tick(double[] L, double[] r) {

    }

  }

}
