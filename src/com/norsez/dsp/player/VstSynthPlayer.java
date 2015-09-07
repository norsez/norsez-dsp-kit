package com.norsez.dsp.player;
import javax.sound.sampled.*;

import com.norsez.dsp.block.*;
import com.norsez.dsp.synth.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class VstSynthPlayer {

  private AudioFormat audioFormat;
  private DataLine.Info info;
  private SourceDataLine line;

  private int BUFFER_SIZE = 128;

  private double _buffer_out [][];

  private double output[][];
  private double maxAmp;
  private byte _data[];
  private int sampleSizeInBytes;

  private VstSynthModel synth;

  public long timeStamp;

  public void setBufferSize(int s) {
    BUFFER_SIZE = s;
  }

  public VstSynthPlayer(double samplingRate, VstSynthModel synth) {

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


    //samplesFromSynth = BUFFER_SIZE / (2 * audioFormat.getSampleSizeInBits() / 8);
    _data = new byte [4 * BUFFER_SIZE];

  }


  private boolean paused;
  private volatile boolean noStopRequested;
  private Thread playThread;
  public void startPlayThread (){

    paused = true;
    noStopRequested = true;
    Runnable r = new Runnable() {
       public void run() {
         runWork();
       }
     };
     playThread = new Thread(r);
     playThread.start();

  }



  public void stopPlayThread() {
    noStopRequested = false;
    playThread.interrupt();

  }

  private void runWork() { // note that this is private
    int baseAddr;
    double temp_double_sample;
    int temp_int_sample;

    try {
      while (noStopRequested) {
        waitWhilePaused();
        _buffer_out = new double[audioFormat.getChannels()][BUFFER_SIZE];
        synth.processReplacing(null, _buffer_out, BUFFER_SIZE);

        for (int i = 0; i < BUFFER_SIZE; i++) {

          baseAddr = i * 4;
          //16 bit little endian

          temp_int_sample = (int) Math.round(maxAmp * _buffer_out[0][i]);
          _data[baseAddr] = (byte) (temp_int_sample & 0xFF);
          _data[baseAddr + 1] = (byte) ( (temp_int_sample >>> 8) & 0xFF);

          temp_int_sample = (int) Math.round(maxAmp * _buffer_out[1][i]);
          _data[baseAddr + 2] = (byte) (temp_int_sample & 0xFF);
          _data[baseAddr +
              3] = (byte) ( (temp_int_sample >>> 8) & 0xFF);
        }

        line.write(_data, 0, _data.length);
        playThread.yield();
      }

    }
    catch (InterruptedException x) {
      // reassert interrupt
      Thread.currentThread().interrupt();
      //System.out.println("interrupt and return from run");
    }
  }

  private Object pauseLock = new Object ();


  public void setPaused(boolean newPauseState) {
    synchronized ( pauseLock ) {
      if ( paused != newPauseState ) {
        paused = newPauseState;
        pauseLock.notifyAll();
      }
    }
  }

  public void waitWhilePaused() throws InterruptedException {
    synchronized ( pauseLock ) {
      while ( paused ) {
        pauseLock.wait();
      }
    }
  }


}
