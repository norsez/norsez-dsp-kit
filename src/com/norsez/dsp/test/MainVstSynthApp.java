package com.norsez.dsp.test;
import java.util.*;
import com.norsez.dsp.block.*;
import com.norsez.dsp.synth.*;
import com.norsez.dsp.synth.swing.*;
import com.norsez.dsp.player.*;
import javax.swing.*;
import java.awt.event.*;
public class MainVstSynthApp extends JFrame {

  java.awt.Component thisProgram;
  VstSynthModel synth;
  public MainVstSynthApp() {

    Locale.setDefault(new Locale("En","US"));

    this.thisProgram = this;
    //com.norsez.dsp.block.DSPSystem.setTableFactor(.25);
    synth = new VstRainStorm();
    //VstSynthModel synth = new VstTestTone();
    com.norsez.dsp.synth.swing.VstSynthGUI gui = new com.norsez.dsp.synth.swing.VstSynthGUI(synth);

    VstSynthPlayer player = new VstSynthPlayer(DSPSystem.getSamplingRate(),
                                               synth);

    player.startPlayThread();
    player.setPaused(false);
    System.out.println (Runtime.getRuntime().maxMemory() + "/" + Runtime.getRuntime().totalMemory());

    this.getContentPane().add ( gui );
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocation(250,250);
    this.setSize(800,225);
    this.setVisible(true);


    this.addWindowListener( new WindowAdapter (){
      public void windowClosing (java.awt.event.WindowEvent e){
        if ( synth instanceof VstRainStorm )
        ((VstRainStorm)synth).saveCurrentProgram("rainstorm");
      }
    });

    //javax.swing.JOptionPane.showMessageDialog(null, gui);
    //player.stopPlayThread();
    //System.exit(0);
  }


  public static void main(String[] args) {

    new MainVstSynthApp ();

  }
}

class VstRainStorm extends com.norsez.dsp.synth.VstSynthModel {

  //parameters
  public static final int P_MAIN_VOL = new Counter(0).getInt();
  public static final int P_EXCITEMENT = new Counter().getInt();
  public static final int P_DELAY_AMT= new Counter ().getInt();
  public static final int P_DELAY_CUTOFF= new Counter ().getInt();
  public static final int P_DELAY_L_TIME= new Counter ().getInt();
  public static final int P_DELAY_L_FB= new Counter ().getInt();
  public static final int P_DELAY_R_TIME= new Counter ().getInt();
  public static final int P_DELAY_R_FB= new Counter ().getInt();

  public static final int P_RAIN_VOL = new Counter ().getInt();
  public static final int P_RAIN_VOL_LO = new Counter ().getInt();
  public static final int P_RAIN_COLOR = new Counter ().getInt();
  public static final int P_RAIN_COLOR_LO = new Counter ().getInt();
  public static final int P_RAIN_DENSITY = new Counter ().getInt();
  public static final int P_RAIN_SH = new Counter ().getInt();
  public static final int P_RAIN_AMP = new Counter ().getInt();
  public static final int P_RAIN_PAN = new Counter ().getInt();
  public static final int P_RAIN_PAN_CURVE = new Counter ().getInt();
  public static final int P_RAIN_PAN_SYMMETRY = new Counter ().getInt();
  public static final int P_RAIN_PAN_STEREO_PHASE = new Counter ().getInt();

  public static final int P_THUNDER_VOL = new Counter ().getInt();
  public static final int P_THUNDER_INTERVAL = new Counter ().getInt();
  public static final int P_THUNDER_NUM = new Counter ().getInt();
  public static final int P_THUNDER_CUTOFF = new Counter ().getInt();
  public static final int P_THUNDER_Q = new Counter ().getInt();
  public static final int P_THUNDER_LO_EQ_CUTOFF = new Counter ().getInt();
  public static final int P_THUNDER_LO_EQ_Q = new Counter ().getInt();
  public static final int P_THUNDER_LO_EQ_BOOST = new Counter ().getInt();

  public static final int P_THUNDER_SH = new Counter ().getInt();
  public static final int P_THUNDER_AMP_LO = new Counter ().getInt();
  public static final int P_THUNDER_AMP_HI = new Counter ().getInt();
  public static final int P_THUNDER_ATTACK_MIN = new Counter ().getInt();
  public static final int P_THUNDER_ATTACK_MAX = new Counter ().getInt();
  public static final int P_THUNDER_HOLD_MIN = new Counter ().getInt();
  public static final int P_THUNDER_HOLD_MAX = new Counter ().getInt();
  public static final int P_THUNDER_RELEASE_MIN = new Counter ().getInt();
  public static final int P_THUNDER_RELEASE_MAX = new Counter ().getInt();

  public static final int NUM_PARAMS = new Counter().getInt();


  //groups
  public final static String[] GROUP_NAMES = {"Global",
      "Rain","Thunder"};
  public final static int G_GLOBAL = new Counter(0).getInt();
  public final static int G_RAIN = new Counter().getInt();
  public final static int G_THUNDER = new Counter().getInt();
  public final static int NUM_GROUPS = new Counter().getInt();

  //parameter infos
  final static int I_NAME = new Counter(0).getInt();
  final static int I_INIT_CTRL = new Counter().getInt();
  final static int I_GROUP = new Counter().getInt();
  final static int I_CTRL_MAX = new Counter().getInt();
  final static int I_CTRL_MIN = new Counter().getInt();

  //the order here must match the order of P_XXX constants
  final static String[][] PARAM_INFOS = {
      {"Volumne","0.91","0","0.0","1.0"},
{"Excitement","0.78","0","0.0","1.0"},
{"Delay Amount","0.5","0","0.0","1.0"},
{"Delay Cutoff","0.76","0","0.0","1.0"},
{"Delay L Time","0.727","0","0.0","1.0"},
{"Delay L Feedback","0.732","0","0.0","1.0"},
{"Delay R Time","0.697","0","0.0","1.0"},
{"Delay R Feedback","0.62","0","0.0","1.0"},
{"Rain Fg Volume","0.54","1","0.0","1.0"},
{"Rain Bg Volume","0.82","1","0.0","1.0"},
{"Rain Fg Color","0.71","1","0.0","1.0"},
{"Rain Bg Color","0.66","1","0.0","1.0"},
{"Rain Density","0.01","1","0.0","1.0"},
{"Rain S&H Amount","0.38","1","0.0","1.0"},
{"Rain LFO->Amp Amount","0.58","1","0.0","1.0"},
{"Rain LFO->Pan Amount","0.89","1","0.0","1.0"},
{"Rain Pan Curvature","0.5","1","0.0","1.0"},
{"Rain Pan Symmetry","0.5","1","0.0","1.0"},
{"Rain Pan Stereo Phase","0.58","1","0.0","1.0"},
{"Th Volume","0.79","2","0.0","1.0"},
{"Th Interval","0.26","2","0.0","1.0"},
{"Th # of Booms","0.43","2","0.0","1.0"},
{"Th Cutoff","0.54","2","0.0","1.0"},
{"Th Q","0.23","2","0.0","1.0"},
{"Th Low Hz","0.78","2","0.0","1.0"},
{"Th Low Q","0.81","2","0.0","1.0"},
{"Th Low Boost","0.8","2","0.0","1.0"},
{"Th SH","0.0","2","0.0","1.0"},
{"Th Amp Lo","0.59","2","0.0","1.0"},
{"Th Amp Hi","0.91","2","0.0","1.0"},
{"Th Attack Lo","0.11","2","0.0","1.0"},
{"Th Attack Hi","0.2","2","0.0","1.0"},
{"Th Hold Lo","0.56","2","0.0","1.0"},
{"Th Hold Hi","0.65","2","0.0","1.0"},
{"Th Release Lo","0.71","2","0.0","1.0"},
{"Th Release Hi","0.75","2","0.0","1.0"}




  };

  final static int UPDATE_RATE = (int)(DSPSystem.getSamplingRate() * 0.01) ;
  final static int THUNDER_POLYPHONY = 6;
  final static int MAX_NUM_THUNDER = 15;
  final static long MAX_THUNDER_INTERVAL = 5 * 44100 *60;
  final static double MAX_BOOST = 20 * Math.log(6); //times

  int current_voice_thunder ;
  int update_count;

  com.norsez.dsp.block.oscillator.Noise noise;
  com.norsez.dsp.block.filter.SimpleFilter flt_rain, flt_thunder [], loeq_thunder[];
  com.norsez.dsp.block.filter.SimpleFilter flt_rain_lo;

  com.norsez.dsp.block.oscillator.multimode.LFO sh_rain,amp_rain,sh_thunder [];
  com.norsez.dsp.block.filter.effects.Panorama pan_rain, pan_thunder[];
  com.norsez.dsp.block.filter.effects.DelayFbStr delay;
  ThunderEnvelope [] env_thunder;
  Vector thunder_events;

  com.norsez.dsp.block.filter.effects.ArcTanClipper output_clipper;

  boolean thunder_done_in_this_interval;
  long current_thunder_interval;


  public void saveCurrentProgram (String filename ){
    java.util.Properties prop = new java.util.Properties ();
    java.io.PrintWriter out;
    try{

      for(int i=0; i  < this.NUM_PARAMS;i++)
        prop.setProperty(""+i,""+controller_values[i]);

      prop.store(new java.io.FileOutputStream (filename),filename);

      out = new java.io.PrintWriter (new java.io.FileWriter (filename +".txt"));
      for(int i=0; i  < this.NUM_PARAMS;i++){
        out.println ("{\""+this.getParameterName(i)+"\","
                     +"\"" + controller_values[i] + "\","
                     +"\"" + this.getParameterGroup(i) + "\","
                     +"\"" + getControllerMax(i) + "\","
                     +"\"" + getControllerMin(i)
                     + "\"},"
                     );
      }
      out.close();

    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public double getControllerMax ( int index ){
    return Double.parseDouble(PARAM_INFOS[index][I_CTRL_MAX]);
  }

  public double getControllerMin (int index ){
    return Double.parseDouble(PARAM_INFOS[index][I_CTRL_MIN]);
  }

  public int getNumGroups (){
      return NUM_GROUPS;
    }

    public int getParameterGroup (int index ) {
      return Integer.parseInt( PARAM_INFOS[index][I_GROUP] );
    }

    public String getGroupName (int index ){
      return GROUP_NAMES [index];
    }

    public int getNumParameters() {
      return NUM_PARAMS;
    }

    public void initProgram() {
      for (int i = 0; i < this.NUM_PARAMS; i++)
        setParameter(i, Double.valueOf(PARAM_INFOS[i][I_INIT_CTRL]).doubleValue());
    }

    public VstRainStorm() {
      controller_values = new double[NUM_PARAMS];
      synth_values = new double[NUM_PARAMS];
      update_count = this.UPDATE_RATE;
      ///debug
      current_thunder_interval = this.MAX_THUNDER_INTERVAL + 1;

    //initialize circuits
      output_clipper = new com.norsez.dsp.block.filter.effects.ArcTanClipper (3,0.985,1024);

      noise = new com.norsez.dsp.block.oscillator.Noise ();
      flt_rain = new com.norsez.dsp.block.filter.SimpleFilter();
      flt_rain.setMode(com.norsez.dsp.block.filter.Filter.Mode.BP);

      //stereo lo boost eq for thunder
      loeq_thunder = new com.norsez.dsp.block.filter.SimpleFilter[2];
      for(int i=0; i < loeq_thunder.length; i++){
        loeq_thunder [i]= new com.norsez.dsp.block.filter.SimpleFilter();
        loeq_thunder [i].setMode(com.norsez.dsp.block.filter.Filter.Mode.BP);
        loeq_thunder [i].setResonance(0.2);
      }

      flt_rain_lo = new com.norsez.dsp.block.filter.SimpleFilter();
      flt_rain_lo.setMode(com.norsez.dsp.block.filter.Filter.Mode.LP);
      flt_rain_lo.setResonance(0.5);
      sh_rain = new com.norsez.dsp.block.oscillator.multimode.LFO ();
      sh_rain.setShape(com.norsez.dsp.block.oscillator.multimode.LFO.Shape.SAMPLE_AND_HOLD);
      amp_rain = new com.norsez.dsp.block.oscillator.multimode.LFO ();
      amp_rain.setShape(com.norsez.dsp.block.oscillator.multimode.LFO.Shape.SINE);
      pan_rain = new com.norsez.dsp.block.filter.effects.Panorama ();

      pan_thunder = new com.norsez.dsp.block.filter.effects.Panorama [THUNDER_POLYPHONY];
      flt_thunder = new com.norsez.dsp.block.filter.SimpleFilter [THUNDER_POLYPHONY];
      env_thunder = new ThunderEnvelope[THUNDER_POLYPHONY];
      sh_thunder = new com.norsez.dsp.block.oscillator.multimode.LFO [THUNDER_POLYPHONY];

      thunder_events = new Vector ();

      for(int i=0; i  < THUNDER_POLYPHONY; i++){
        flt_thunder [i] = new com.norsez.dsp.block.filter.SimpleFilter();
        flt_thunder[i].setMode(com.norsez.dsp.block.filter.Filter.Mode.LP);

        env_thunder [i] = new ThunderEnvelope ();
        env_thunder [i].setReset ( false) ;
        sh_thunder[i] = new com.norsez.dsp.block.oscillator.multimode.LFO ();
        sh_thunder[i].setShape(com.norsez.dsp.block.oscillator.multimode.LFO.Shape.SAMPLE_AND_HOLD);
        pan_thunder[i] = new com.norsez.dsp.block.filter.effects.Panorama ();

      }

      delay = new com.norsez.dsp.block.filter.effects.DelayFbStr ();



      initProgram();
    }

    static final int MAX_LO_FREQ = 160;
    static final int MIN_LO_FREQ = 25;

    public void setParameter(int index, double value) {

      controller_values[index] = value;
      synth_values[index] = value;

      //for bipolar
      if (index == P_RAIN_PAN){
        synth_values[index] = Interpolation.mapToRangeLinear(0,-1,1,1,synth_values[index]);
      }
      else
      if ( index == this.P_THUNDER_LO_EQ_CUTOFF){
        synth_values [index ] = Interpolation.mapToRangeLinear(0,MIN_LO_FREQ,1,MAX_LO_FREQ,value)/DSPSystem.getSamplingRate();
        for(int i=0; i < loeq_thunder.length; i++){

          loeq_thunder[i].setCutoff(synth_values [index ]);
        }
      }
      else
      if (index == this.P_THUNDER_LO_EQ_BOOST){
        synth_values [ index] = this.MAX_BOOST * value;
      }
      else
      if (index == this.P_THUNDER_LO_EQ_Q ){
        for(int i=0; i < loeq_thunder.length; i++){

          loeq_thunder[i].setResonance(synth_values [index ]);
        }

      }
      else
      if ( index == P_MAIN_VOL ){
        synth_values[index] = Table.T_ENV_UP[ (int) (value *
            (Table.T_ENV_UP.length - 1))];
      }
      else
      if (index == this.P_THUNDER_VOL){
        synth_values  [this.P_THUNDER_VOL] *=  1;
      }
      else
      if (index == P_EXCITEMENT)
      {
        sh_rain.setRate( (1+Table.getTableLookupValue(value,Table.T_EXP_UP) * 200.0) / DSPSystem.getSamplingRate());
        amp_rain.setRate( Table.getTableLookupValue(value,Table.T_EXP_UP) * 0.675 / DSPSystem.getSamplingRate());
        pan_rain.setCps( Table.getTableLookupValue(value,Table.T_EXP_UP) * 0.3945 / DSPSystem.getSamplingRate());
        for (int i=0; i < this.THUNDER_POLYPHONY; i++){
          sh_thunder[i].setRate( (1+Table.getTableLookupValue(value,Table.T_EXP_UP) * 200.0) / DSPSystem.getSamplingRate());
        }
      }
      else
      if (index == P_RAIN_DENSITY) {

        flt_rain.setResonance(value);
      }
      else
      if (index == P_RAIN_SH ){
        synth_values [ P_RAIN_SH ] = Table.getTableLookupValue(value,Table.T_EXP_UP) * 0.22;
      }
      else
      if (index == P_DELAY_CUTOFF){
        double v = Table.getLinIntp(value,Table.T_NOTE2CPS);
        delay.setCutoff(v,0);
        delay.setCutoff(v,1);
      }
      else
      if ( index == P_THUNDER_Q){
        for(int i=0; i<this.THUNDER_POLYPHONY;i++)
        flt_thunder[i].setResonance(value);
      }

      else
      if (index == P_RAIN_AMP ) synth_values[P_RAIN_AMP] *= 0.1;
      else
      if (index == P_RAIN_COLOR_LO ) synth_values[P_RAIN_COLOR_LO] *= 0.77;
      else
      if (index == P_DELAY_L_TIME ) delay.setDelayTime( value ,0);
      else
      if (index == P_DELAY_R_TIME ) delay.setDelayTime( value ,1);
      else
      if (index == P_DELAY_L_FB ) delay.setFeedbackAmount( value  ,0);
      else
      if (index == P_DELAY_R_FB ) delay.setFeedbackAmount( value  ,1);
      else
      if (index == P_RAIN_PAN_CURVE ) pan_rain.setCurvature(value);
      else
      if (index == P_RAIN_PAN_SYMMETRY ) pan_rain.setSymmetry(value);
      else
      if (index == P_RAIN_PAN_STEREO_PHASE ) pan_rain.setStereoPhase(value);
      else
      if (index == this.P_THUNDER_INTERVAL ) {
        synth_values [ index ] = this.MAX_THUNDER_INTERVAL * value;
        //must trigger the event calculator.
        this.current_thunder_interval = (long) synth_values [ index ] +1;
      }
      else
      if (index == this.P_THUNDER_NUM ) synth_values [ index ] = Math.max(this.MAX_NUM_THUNDER* value,1);






    }

    public double getParameter(int index) {
      return controller_values[index];
    }

    public String getParameterName(int index) {
      return PARAM_INFOS[index][I_NAME];
    }

    public String getParameterDisplay(int index) {
      String result;
      if (index == P_MAIN_VOL)
        result = this.parameter2dB(synth_values[index]);
      else
      if ( index == this.P_EXCITEMENT ){
        result =  this.parameterRound( amp_rain.getFrequecyDisplay() ) + " Hz" ;
      }
      else
      if ( index == this.P_THUNDER_NUM ){
        result = ""+(int)( this.MAX_NUM_THUNDER * controller_values [ index ] ) + " x";
      }
      else
      if (index == this.P_THUNDER_INTERVAL){
        result = this.parameterRound(this.MAX_THUNDER_INTERVAL * controller_values [ index ] / (44000.0)) + " sec";
      }
      else
      if (index == this.P_THUNDER_ATTACK_MAX
           || index == this.P_THUNDER_HOLD_MAX
           || index == this.P_THUNDER_RELEASE_MAX
           || index == this.P_THUNDER_ATTACK_MIN
           || index == this.P_THUNDER_HOLD_MIN
           || index == this.P_THUNDER_RELEASE_MIN



          ){
        result = this.parameterRound(
      com.norsez.dsp.block.envelope.Envelope.envTimetoSeconds(
      com.norsez.dsp.block.envelope.Envelope.getEnvelopetime(synth_values[index]),com.norsez.dsp.block.DSPSystem.getSamplingRate())) + " sec";
      }
      else
      if ( index == this.P_RAIN_COLOR
          || index == this.P_RAIN_COLOR_LO
          || index == this.P_THUNDER_CUTOFF
          || index == this.P_DELAY_CUTOFF
          ){
        return this.parameter2Hz( Table.getLinIntp( controller_values [index] , Table.T_NOTE2CPS ) );
      }
      else
      if (index == this.P_THUNDER_LO_EQ_CUTOFF

          ){
        return this.parameterRound( synth_values [index] * 44100.0 ) + " Hz";
      }
      else
      if (index == this.P_THUNDER_LO_EQ_BOOST){
        return this.parameter2dB(synth_values[index]);
      }
      else
      result = controller_values[index] + "";

      return result + "(" + this.parameterRound( controller_values[index] ) + ")";
    }

    public String getParameterLabel(int index) {
      return "";
    }

    public void process(double[][] inputs, double[][] outputs, int sampleFrames) {
      processReplacing(inputs, outputs, sampleFrames);
    }

    /**
     *
     * <p>Title: ThunderEvent</p>
     * <p>Description: An describer of a Thunder hit. </p>
     */
    class ThunderEvent implements Comparable{
      public double attack,hold,release,velocity;
      public long deltaTime; // in number of samples
      public int compareTo(Object o ){
        return new Long ( deltaTime ).compareTo(new Long (((ThunderEvent)o).deltaTime));
      }
    }

    /**
     *
     * <p>Title:ThunderEnvelope </p>
     * <p>Description: Adapted EnvelopeAHR that scales its lastValue to the velocity.</p>
     */
    class ThunderEnvelope extends com.norsez.dsp.block.envelope.EnvelopeAHR {
      public double velocity;

      public double tick(double gate){
        return velocity * super.tick(gate);
      }

      public double getLastValue (){
        return this.lastValue * velocity;
      }
    }

    public void processReplacing(double[][] inputs, double[][] outputs,
                                 int sampleFrames) {

      if (this.isBypassed)return;

      double temp1,rainPan=0;
      double temp, tempL,tempR, thunderL, thunderR;

      for (int i = 0; i < sampleFrames; i++) {

        update_count++;

        if (update_count >= UPDATE_RATE) {
          sh_rain.tick(UPDATE_RATE);
          amp_rain.tick(UPDATE_RATE);

          pan_rain.tick(UPDATE_RATE);

          for(int j=0; j < this.THUNDER_POLYPHONY;j++){
            sh_thunder[j].tick(UPDATE_RATE);
            pan_thunder[j].tick(UPDATE_RATE);
          }


          this.current_thunder_interval += UPDATE_RATE;
          //After every thunder interval, new thunder events
          //get created here...

          if ( current_thunder_interval >= (long)(synth_values[this.P_THUNDER_INTERVAL])){

            thunder_events.clear();

            for (int k=0, n = (int) synth_values [this.P_THUNDER_NUM]; k<n;k++){
              ThunderEvent e = new ThunderEvent();
              e.attack = com.norsez.dsp.block.envelope.Envelope.getEnvelopetime(com.norsez.dsp.block.Interpolation.mapToRangeLinear(0,synth_values[P_THUNDER_ATTACK_MIN],1,synth_values[P_THUNDER_ATTACK_MAX],Math.random()));
              e.hold = com.norsez.dsp.block.envelope.Envelope.getEnvelopetime(com.norsez.dsp.block.Interpolation.mapToRangeLinear(0,synth_values[P_THUNDER_HOLD_MIN],1,synth_values[P_THUNDER_HOLD_MAX],Math.random()));
              e.release = com.norsez.dsp.block.envelope.Envelope.getEnvelopetime(com.norsez.dsp.block.Interpolation.mapToRangeLinear(0,synth_values[P_THUNDER_RELEASE_MIN],1,synth_values[P_THUNDER_RELEASE_MAX],Math.random()));
              e.velocity = com.norsez.dsp.block.Interpolation.mapToRangeLinear(0,synth_values[P_THUNDER_AMP_LO],1,synth_values[P_THUNDER_AMP_HI],Math.random());
              e.deltaTime = (long) (synth_values[this.P_THUNDER_INTERVAL] * Math.random());

              thunder_events.add( e);
            }

            Collections.sort(thunder_events);


            this.current_thunder_interval = 0;

          }

          //triggering env_thunder's date when it's time.
          //notice that the thunder event's deltatime only has for note on. note off is necessary because it can be specified by the hold part in the AHR envelope.
          if ( thunder_events.size() > 0 ){
            ThunderEvent e = (ThunderEvent) thunder_events.firstElement();
            if (  current_thunder_interval >=e.deltaTime) {
              thunder_events.remove(0);
              int voice = this.current_voice_thunder % this.THUNDER_POLYPHONY;
              env_thunder[voice].retrigger();
              env_thunder[voice].attackTime = e.attack;
              env_thunder[voice].holdTime = e.hold;
              env_thunder[voice].releaseTime = e.release;
              env_thunder[voice].velocity = e.velocity;
              pan_thunder[voice].setPhase(Math.random());
              //System.out.println (voice + " " +env_thunder[voice]);
              /*
              flt_thunder[voice].setCutoff(com.norsez.dsp.block.Table.getTableLookupValue(e.
                  velocity * 0.675, Table.T_NOTE2CPS));
               */

              voice++;
            }

          }

          update_count = 0;
        }


        /**
         * Start modulator processing here
         */

        for(int j=0; j < this.THUNDER_POLYPHONY;j++){
          env_thunder[j].tick(1);
        }




        /**start audio processing here
         *
        **/
        noise.tick();


        //processing rain
        temp = sh_rain.getValue() * synth_values[P_RAIN_SH];
        flt_rain.setCutoff(Table.getTableLookupValue( synth_values[P_RAIN_COLOR]
                                             + temp
                                             , Table.T_NOTE2CPS));
        flt_rain_lo.setCutoff(Table.getTableLookupValue( synth_values[P_RAIN_COLOR_LO]
                                                - temp
                                                , Table.T_NOTE2CPS));

        flt_rain.tick(noise.getLastValue());
        flt_rain_lo.tick(noise.getLastValue());

        //rain lo is in momo
        tempL = flt_rain_lo.getLastValue() * synth_values [P_RAIN_VOL_LO]
            * ( 1+ (amp_rain.getLastValue() * synth_values[P_RAIN_AMP]))
            ;
        tempR = tempL;

      //mix with rain hi which is in stereo

      temp = (1 + (amp_rain.getLastValue() * synth_values[P_RAIN_AMP]))
          * flt_rain.getLastValue() * synth_values[P_RAIN_VOL]
          ;

      tempL += temp * (1-pan_rain.getLastValueL()*synth_values[P_RAIN_PAN]);

      tempR += temp * (1-pan_rain.getLastValueR()*synth_values[P_RAIN_PAN]);




      //processing thunder.
      thunderL=thunderR=0;
      for(int j=0; j < this.THUNDER_POLYPHONY; j++){

        flt_thunder[j].setCutoff(Table.getTableLookupValue(synth_values[P_THUNDER_CUTOFF]+(sh_thunder[j].getLastValue()
             *synth_values [P_THUNDER_SH])

             ,Table.T_NOTE2CPS));

        //flt_thunder[j].setCutoff(com.norsez.dsp.block.Table.getTableLookupValue(env_thunder[j].
        //          velocity * 0.675 + sh_thunder[j].getLastValue(), Table.T_NOTE2CPS));
        flt_thunder[j].tick(noise.getLastValue());
        temp = synth_values [this.P_THUNDER_VOL] * env_thunder [j].getLastValue()
            * flt_thunder [j].getLastValue()
            // * noise.getLastValue()
            ;
        thunderL += pan_thunder [j].getLastValueL() *temp;
        thunderR += pan_thunder [j].getLastValueR() * temp;
      }




      //pass all thunders to lo boost eq
      thunderL = loeq_thunder[0].tick(thunderL) * synth_values[this.P_THUNDER_LO_EQ_BOOST]
          + thunderL
          ;
      thunderR = loeq_thunder[1].tick(thunderR) * synth_values[this.P_THUNDER_LO_EQ_BOOST]
          + thunderR
          ;

      tempL += thunderL;
      tempR += thunderR;

        //main vol
        outputs[0][i] = tempL* synth_values[P_MAIN_VOL] ;
        //copy to the other channel
        outputs[1][i] = tempR *synth_values[P_MAIN_VOL] ;




      }

      double [][] delay_signal = new double [outputs.length][outputs[0].length];
      delay.tickProcess(outputs,delay_signal);

      //mix with send effects
      for(int j=0; j < 2; j++){
        for (int i = 0; i < sampleFrames; i++) {

          outputs[j][i] += synth_values[P_DELAY_AMT] * delay_signal[j][i] ;

        }
      }
      /*
      //clipping output
      output_clipper.tickProcess(outputs[0]);
      output_clipper.tickProcess(outputs[1]);
      */
    }

}

class VstTestTone
    extends com.norsez.dsp.synth.VstSynthModel implements com.norsez.dsp.midi.MidiDSPPort.MidiDSPPortListener {

  public static final int P_MAIN_VOL = new Counter(0).getInt();
  public final static int P_WAVE_SHAPE = new Counter().getInt();
  public final static int P_FREQ = new Counter().getInt();
  public final static int P_CUTFF = new Counter().getInt();
  public final static int P_Q = new Counter().getInt();

  public static final int NUM_PARAMS = new Counter().getInt();


  public final static String [] GROUP_NAMES = {"Oscillator"};
  public final static int G_OSC = new Counter(0).getInt();
  public final static int NUM_GROUPS = new Counter().getInt();

  final static int I_NAME = new Counter(0).getInt();
  final static int I_INIT_CTRL = new Counter().getInt();
  final static int I_GROUP = new Counter().getInt();

  final static String[][] PARAM_INFOS = {
      {"Main Volumne", "0.25",""+G_OSC}
      , {"Wave Shape", "0",""+G_OSC}
      , {"Frequency",""+(69/127.0),""+G_OSC}
      , {"cutoff",""+(69/127.0),""+G_OSC}
      , {"Q",""+(69/127.0),""+G_OSC}
  };

  //circuit components
  com.norsez.dsp.block.oscillator.Wavetable sine;
  com.norsez.dsp.block.oscillator.PcmOsc pcm;
  com.norsez.dsp.midi.MidiDSPPort midiPort;
  com.norsez.dsp.block.sequencer.SampleClock clock;
  com.norsez.dsp.block.filter.SimpleFilter f;
  com.norsez.dsp.block.oscillator.Noise noise;
  Vector events;

  public void onMidiReceived ( com.norsez.dsp.midi.MidiDSPPort.MidiDSPPortEvent e){
    doMidiIn ( e );
  }

  private synchronized void doMidiIn (com.norsez.dsp.midi.MidiDSPPort.MidiDSPPortEvent e){
    if (e.isNoteOn()){
      pcm.setCps(e.getEvent().getData1() / 127.0);
    }

  }

  public int getNumGroups (){
    return NUM_GROUPS;
  }

  public int getParameterGroup (int index ) {
    return Integer.parseInt( PARAM_INFOS[index][I_GROUP] );
  }

  public String getGroupName (int index ){
    return GROUP_NAMES [index];
  }

  public int getNumParameters() {
    return NUM_PARAMS;
  }

  public void initProgram() {
    for (int i = 0; i < this.NUM_PARAMS; i++)
      setParameter(i, Double.valueOf(PARAM_INFOS[i][I_INIT_CTRL]).doubleValue());
  }

  public VstTestTone() {
    controller_values = new double[NUM_PARAMS];
    synth_values = new double[NUM_PARAMS];

    f = new com.norsez.dsp.block.filter.SimpleFilter ();
    f.setResonance(.1);
    f.setMode(com.norsez.dsp.block.filter.Filter.Mode.BP);

    noise = new com.norsez.dsp.block.oscillator.Noise ();


    sine = new com.norsez.dsp.block.oscillator.Wavetable();
    sine.setWavetable(Table.T_SINE);
    sine.cps = (110.0 / DSPSystem.getSamplingRate());

    pcm = new com.norsez.dsp.block.oscillator.PcmOsc ();
    pcm.loadWavFileInMono("C:\\Documents and Settings\\norsez\\Desktop\\pad.wav");

    clock = new com.norsez.dsp.block.sequencer.SampleClock ();
    events = new Vector ();

    javax.sound.midi.MidiDevice.Info info = javax.sound.midi.MidiSystem.getMidiDeviceInfo()[27];
    try{
      javax.sound.midi.MidiDevice midiIn = javax.sound.midi.MidiSystem.
          getMidiDevice(info);
      midiPort = new com.norsez.dsp.midi.MidiDSPPort( midiIn );
      midiPort.addPortListener(this);
      midiIn.open();
    }catch(Exception  e){
      e.printStackTrace();
    }


    initProgram();
  }

  public void setParameter(int index, double value) {

    controller_values[index] = value;
    //System.out.print ( value + " ");
    switch (index) {
      case 0:
        synth_values[index] = Table.T_ENV_UP[ (int) (value *
            (Table.T_ENV_UP.length - 1))];
        break;
      case 1:

        //sine.setOscMode(sine.getAllModes()[(int)(value*(sine.getAllModes().length-1))]);
        //System.out.println (sine.getAllModes()[(int)(value*(sine.getAllModes().length-1))].toString());
        break;
    }

    if (index == P_FREQ){
      synth_values[index] = 0.5 * (Math.pow(1000,value) - 1)/999.0 ;
      sine.cps = synth_values[index];
      pcm.setCps(controller_values [index] );
    }
    else
    if (index == this.P_CUTFF ){
      f.setCutoff(  Table.getLinIntp(value,Table.T_NOTE2CPS) );
    }
    else
    if (index == this.P_Q ){
      f.setResonance(  value );
    }


  }

  public double getParameter(int index) {
    return controller_values[index];
  }

  public String getParameterName(int index) {
    return PARAM_INFOS[index][I_NAME];
  }

  public String getParameterDisplay(int index) {

    if (index == P_MAIN_VOL)
      return this.parameter2dB(synth_values[index]);
    else
    if ( index == P_FREQ){
      return synth_values[index] * 44100.0 + "";
    }

    return controller_values[index] + "";
  }

  public String getParameterLabel(int index) {
    return "";
  }

  public void process(double[][] inputs, double[][] outputs, int sampleFrames) {
    processReplacing(inputs, outputs, sampleFrames);
  }

  public void processReplacing(double[][] inputs, double[][] outputs,
                               int sampleFrames) {
    double temp;
    if (this.isBypassed)return;

    //sine.processLin(null,outputs[0] );
    //pcm.tickLinIntProcess(outputs[0]);

    for (int i = 0; i < sampleFrames; i++) {

      temp = noise.tick();
       f.tick(temp);
       temp = f.getLastValue();
      if ( Double.isNaN(temp) ) System.out.println ( f.getCutoff()  + " " + f.getResonance());
      //main vol
      outputs[0][i] = temp * synth_values[P_MAIN_VOL] ;
      //copy to the other channel
      outputs[1][i] = outputs[0][i];




    }

  }

}
