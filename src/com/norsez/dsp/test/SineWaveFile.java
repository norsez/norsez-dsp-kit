package com.norsez.dsp.test;
import javax.sound.sampled.*;
import com.norsez.dsp.block.oscillator.*;
import com.norsez.dsp.block.*;
import com.norsez.dsp.player.*;
import com.norsez.dsp.synth.*;

public class SineWaveFile {
  public SineWaveFile() {
  }


  final static double[] FREQS = {
      20, 60, 120, 320, 250, 500, 900, 1100, 3000, 6000, 8000, 10000, 12000,
      14000, 17000, 20000};
  final static String filename = "d:/my documents/test.wav";
  final static double WAVE_DUR = 1.5;
  final static double WAVE_INT = 2.0;
  final static double SAMPLING_RATE = 44100;

  final static double MAX_AMP = 0.5;

  public static void main(String[] args) {
    try {
      Wavetable wt = new Wavetable();
      wt.setWavetable(com.norsez.dsp.block.Table.T_SINE);
      MyJob job = new MyJob ();
      Player16Bit player = new Player16Bit (SAMPLING_RATE, job);

      final double wave_dur = WAVE_DUR * SAMPLING_RATE;
      final double wave_int = WAVE_INT * SAMPLING_RATE;

      player.setActive(true);
      player.setBufferSize(1024);
      player.run();

      for (int i = 0; i < FREQS.length; i++) {
        wt.cps = FREQS[i] / SAMPLING_RATE;
        wt.phase = 0;



      }

    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}

class MyJob extends com.norsez.dsp.synth.SynthModel {
  boolean job_done ;
  Wavetable wt;
  public MyJob (){
    job_done = false;
  }

  public void setOsc (Wavetable wt ){
    this.wt = wt;
  }

  public void tick() {
   this.lastValueL = this.lastValueR =  wt.tick();
  }



  public boolean isInUse() {
    return job_done;
  }
}
