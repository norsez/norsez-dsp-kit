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

public class Player16Bit2 {

  private AudioFormat audioFormat;
  private DataLine.Info info;
  private SourceDataLine line;
  private boolean isActive;
  private double samplingRate;

  private int BUFFER_SIZE = 1024;
  private int samplesFromSynth;

  private double output[][];
  private double maxAmp;
  private byte _data[];
  private int sampleSizeInBytes;

  private SynthModel synth;

  public void setBufferSize(int s) {
    BUFFER_SIZE = s;
  }

  public Player16Bit2(double samplingRate, SynthModel synth) {

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
    double temp;
    try {
        while ( noStopRequested ) {
        waitWhilePaused();

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
        _data[baseAddr + 3] = (byte) ( ( (int) Math.round(maxAmp * temp)) >>> 8 & 0xFF);
      }

      line.write(_data, 0, _data.length);
    }


    } catch ( InterruptedException x ) {
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