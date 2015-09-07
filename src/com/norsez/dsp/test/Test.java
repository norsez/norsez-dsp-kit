package com.norsez.dsp.test;
import java.util.*;
import java.text.*;
import javax.swing.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import com.norsez.dsp.block.*;
import com.norsez.dsp.block.oscillator.*;
import com.norsez.dsp.block.sequencer.*;
import com.norsez.dsp.block.filter.effects.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Test extends JFrame{

  public Test() {


  }

  public void test11 (){
    PanoramaLK p = new PanoramaLK ();
    final String path = "d:/my documents";



    p.setCurvature(1);
    p.setSymmetry(0.8);
    p.savePoints(path + "/3.csv");

  }

  public void test9 (){

    System.out.println ( Integer.toHexString( new javax.sound.midi.ShortMessage ().getData2() ));

  }


  public void test8 (){
    StepSequencer.StepSequencerInfo info = new StepSequencer.StepSequencerInfo ();
    info.num_steps = 16;
    info.num_tracks = 1;
    info.samplingRate = 16*40;
    StepSequencer seq = new StepSequencer (info);
    seq.setSwing(0.3);
    for(int i=0; i < 1600*10; i++){
      seq .tick();
      if (seq.getLastValue()  > 0 ){
        System.out.println ( i + " " +seq.getCurrentStep() + " " + seq.getCurrentEvent(0));
      }

    }
  }


  public void test1 (){
    DateFormat df = new SimpleDateFormat("yyyy MMMMM",new Locale("Pt","PT"));
   Date d = new Date();

   GregorianCalendar gc = (GregorianCalendar)GregorianCalendar.getInstance();
   gc.setTime(d);
   System.out.println (df.format(d));

  }

  abstract class Unit {};

  class blankunit  extends Unit {

  }

  interface Updater {
    public void update ();
  }

  class Osc extends Unit {
    double cps = 440.0/44100.0;
    double phase = 0;
    double value;
    int mask = (int)(com.norsez.dsp.block.Table.T_SINE.length-1);
    double tick (){
      phase += cps;
      if ( phase > 1) phase = phase - 1;
      try{
        value = com.norsez.dsp.block.Table.T_SINE[ (int) (phase * mask)];
      } catch ( Exception e ){
        System.err.println( phase + " " + mask);
      }

      return value;
    }
  }

  class MtxMod{
    final int slots;
    double values [];
    Unit units [];
    public MtxMod (int num_slots){
      slots = num_slots;
      values = new double [ slots ];
      units = new Unit [ slots ];
    }
  }



  public void test3 (){


    final int NUM = 3;
    long [] lapses = new long [NUM];
    final long UPDATE_RATE = (long)(0.025 * 44100.0);
    double output;
    MtxMod mmd = new MtxMod ( 6 );
    Unit blank = null;
    mmd.units[0] = blank;

    Osc o1 = new Osc ();
    Osc o2 = new Osc ();

    mmd.units[1] = o1;
    mmd.values[1] = -1.0;
    mmd.units[2] = o2;
    mmd.values[1] = 0.5;
    mmd.units[3] = o1;
    mmd.units[4] = o2;


    for (int i = 0; i < NUM; i++) {
      long start = new java.util.Date().getTime();
      System.out.println ("Doing .. " + i);
      long samples = 44100 * 60 * 10;
      int update_cycle = 0;
      while (samples-- > 0) {

        //slow update
        if (update_cycle == UPDATE_RATE){

          for (int k=0; k < mmd.slots; k++){
            if ( mmd.units[k] !=null ){

            }
          }

          update_cycle = 0;
        }

        update_cycle++;

        //fast update
        output = o1.tick() + o2.tick();
      }

      lapses [i] = new java.util.Date ().getTime() - start ;
    }

    //find mean
    long sum = 0;
    for(int i=0; i < NUM; i++)
      sum += lapses [i];
    double mean = (double)sum / NUM;

    System.out.println (mean);

  }




  public void test2(){
    com.norsez.dsp.synth.swing.Dragger d1 = new com.norsez.dsp.synth.swing.Dragger (null,0,0,127,0);
    d1.setVisible(true);
    JOptionPane.showMessageDialog(new JScrollPane ( d1 ), "");
  }

  public static void main(String [] args){
    new Test ().test11();
  }

  public void test10 (){

    final int times = 30;
    ProcessingTimeTest.test(new Pan2 (), times);
    ProcessingTimeTest.test(new Pan (), times);
  }

  class Pan implements ProcessingTimeTest.ToTest {
  Panorama pan;
  public Pan (){
    pan = new Panorama ();
  }

  public void run (){
    for(int i=0; i< 44100;i++)
    pan.tick();
  }
}

class Pan2 implements ProcessingTimeTest.ToTest {
  PanoramaLK pan;
  public Pan2 (){
    pan = new PanoramaLK ();
  }

  public void run (){
    for(int i=0; i< 44100;i++)
    pan.tick();
  }
}



  public void test5(){
    final int times = 3;


     ProcessingTimeTest.test(new NotBoxed (), times);
     ProcessingTimeTest.test(new Boxed (), times);



    /**
      * ProcessingTimeTest.test(new WithIf (), times, DUR_SAMPLES);
     ProcessingTimeTest.test(new WithoutIf (), times, DUR_SAMPLES);

      */


}

   public void test6 (){
     final int times = 5;
     //ProcessingTimeTest.test(new OutObj3 (), 35,DUR_SAMPLES);

     ProcessingTimeTest.test(new OutObj3 (), times,DUR_SAMPLES);



    ProcessingTimeTest.test(new OutObj2 (), times,DUR_SAMPLES);

    ProcessingTimeTest.test(new OutObj (), times,DUR_SAMPLES);
    ProcessingTimeTest.test(new InObj (), times,DUR_SAMPLES);


   }

   class WithIf implements ProcessingTimeTest.ToTest {
     double d = 0.01;
     public void run (){
       if ( d < 0.000000000000001){
         d = 0.01;
       }
       else
       {
           d = d * 0.00015;
       }
     }
   }

   class WithoutIf implements ProcessingTimeTest.ToTest {
     double d = 0.01;
     public void run (){

           d = d * 0.00015;

     }
   }



  final static int DUR_SAMPLES = 44100 ;

  class Boxed implements ProcessingTimeTest.ToTest {
    public void run (){
      Double d = new Double (0.0006);
      for (int i=0; i < DUR_SAMPLES; i++){
        d = new Double ((d.doubleValue() * 0.0036) + d.doubleValue()*0.1);
      }

    }


  }

public void test7 (){
  final int times = 45;
  //ProcessingTimeTest.test(new OscTest2 (), DUR_SAMPLES);
  ProcessingTimeTest.test(new Osc7a (), times,DUR_SAMPLES);

  ProcessingTimeTest.test(new Osc7b (), times,DUR_SAMPLES);
  ProcessingTimeTest.test(new Osc7 (), times,DUR_SAMPLES);
}


class Osc7 implements ProcessingTimeTest.ToTest {
  Wavetable [] osc;
  public Osc7 (){
    osc = new Wavetable [7];
    for(int i=0; i< osc.length;i++){
      osc [i] = new Wavetable ();
      osc [i].setWavetable(Table.T_SINE);
    }
  }

  public void run (){
    for(int i=0; i< osc.length;i++){
      osc[i].tick();
    }

  }
}



class Osc7a implements ProcessingTimeTest.ToTest {
  double [] phase;
  double [] cps;
  double [] lastValue;
  int mask = Table.T_SINE.length - 1;
  public Osc7a (){
    phase = new double [7];
    cps = new double [7];
    lastValue = new double [7];
  }
  int  phase_1,phase_0;
  double phase_;
  public void run (){
    for(int i=0; i< phase.length;i++){
      while (phase[i] > 1) {
       phase[i] -= 1.0;

     }
     phase_ = phase[i] * mask;
     phase_1 = (int) Math.ceil(phase_);
     phase_0 = (int) (phase_);

     lastValue[i] = (Table.T_SINE[phase_1] - Table.T_SINE[phase_0]) * (phase_ - phase_0) +
         Table.T_SINE[phase_0];

     phase[i] += cps[i];



    }

  }
}


class Osc7b implements ProcessingTimeTest.ToTest {
  double [] phase;
  double [] cps;
  double [] lastValue;
  int mask = Table.T_SINE.length - 1;
  Func f;
  public Osc7b (){
    phase = new double [7];
    cps = new double [7];
    lastValue = new double [7];
    f = new Func ();
  }
  int  phase_1,phase_0;
  double phase_;
  public void run (){
    f.process(cps,phase,lastValue);
  }

  class Func {
    public void process ( double [] cps, double [] phase, double lastValue []){
      for(int i=0; i< phase.length;i++){
      while (phase[i] > 1) {
       phase[i] -= 1.0;

     }
     phase_ = phase[i] * mask;
     phase_1 = (int) Math.ceil(phase_);
     phase_0 = (int) (phase_);

     lastValue[i] = (Table.T_SINE[phase_1] - Table.T_SINE[phase_0]) * (phase_ - phase_0) +
         Table.T_SINE[phase_0];

     phase[i] += cps[i];



    }


    }
  }
}


class NotBoxed implements ProcessingTimeTest.ToTest {
    public void run (){
      double d = 0.0006;
      for (int i=0; i < DUR_SAMPLES; i++){
        d = (d * 0.0036) + d*0.1;
      }

    }



  }

  class InObj implements ProcessingTimeTest.ToTest {
    double [] values = {440./44100.0,0.0,0.0};
      final int cps = 0;
      final int phase = 1;
      final int lastvalue = 2;
      final int mask = com.norsez.dsp.block.Table.T_SINE.length - 1;

    private void update (int cps, int last, int phase){
      values [ phase ] += values [cps ];
      if ( values [ phase ] > 1 ) values [ phase ] = values [ phase ] - 1.0;
      values[last] = com.norsez.dsp.block.Table.T_SINE[(int)(values[phase] * mask)];
    }

    public void run (){
       update ( cps, lastvalue, phase );
    }
  }

  class OutObj implements ProcessingTimeTest.ToTest {
    Osc o1 = new Osc ();
    public void run (){
      o1.tick();
    }
  }



  class OutObj2 implements ProcessingTimeTest.ToTest {

    class Osc {
      double phase;
      double cps=440/44100.0;
      double last;
    }

    final int mask = com.norsez.dsp.block.Table.T_SINE.length - 1;
    private void update ( Osc o1 ){
      o1. phase += o1.cps ;
      if ( o1. phase  > 1 ) o1. phase  = o1. phase - 1.0;
      o1.last = com.norsez.dsp.block.Table.T_SINE[(int)(o1.phase * mask)];

    }

    Osc o1 = new Osc ();
    public void run (){
      update ( o1 );
    }
  }
  class OutObj3 implements ProcessingTimeTest.ToTest {

     class Osc {
       double phase;
       double cps=440/44100.0;
       double last;
     }



     final int mask = com.norsez.dsp.block.Table.T_SINE.length - 1;
     private void update ( Osc o1 , Object d){
       o1. phase += o1.cps ;
       if ( o1. phase  > 1 ) o1. phase  = o1. phase - 1.0;
       o1.last = ((double [])d)[(int)(o1.phase * mask)];

     }

     Osc o1 = new Osc ();
     public void run (){
       update ( o1 , com.norsez.dsp.block.Table.T_SINE);
     }
   }

   class OscTest implements ProcessingTimeTest.ToTest {
     Wavetable osc;
     public OscTest (){
       osc = new Wavetable ();
       osc.cps = 440.0 / 44100.0;
       osc.setWavetable(Table.T_SINE);
     }

     public void run (){
       osc.tick();
     }
   }


   class OscTestFunc implements ProcessingTimeTest.ToTest {
     Wavetable osc;
     public OscTestFunc (){
       osc = new Wavetable ();
       osc.cps = 440.0 / 44100.0;
       osc.setWavetable(Table.T_SINE);
     }

     public void run (){
       osc.tick();
     }
   }





   class SamplerTest implements ProcessingTimeTest.ToTest {
     PcmOsc osc;
     double [] inputs;
     public SamplerTest (){
       osc = new PcmOsc ();
       osc.loadWavFileInMono("C:\\Documents and Settings\\norsez\\Desktop\\pad.wav");
       inputs = new double [128];
     }

     public void run (){
       osc.tickProcess(inputs);
     }
   }

   class SamplerTest2 implements ProcessingTimeTest.ToTest {
    PcmOsc osc;
    double [] inputs;
    public SamplerTest2 (){
      osc = new PcmOsc ();
      osc.loadWavFileInMono("C:\\Documents and Settings\\norsez\\Desktop\\pad.wav");
      inputs = new double [128];
    }

    public void run (){
      osc.tickLinIntProcess(inputs);
    }
  }


}
