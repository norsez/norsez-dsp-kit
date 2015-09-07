package com.norsez.dsp.project.monolith;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import com.norsez.dsp.block.*;
import com.norsez.dsp.synth.*;
import com.norsez.dsp.synth.swing.*;
import com.norsez.dsp.test.*;
import com.norsez.dsp.block.envelope.*;
import com.norsez.dsp.block.filter.*;
import com.norsez.dsp.block.filter.effects.*;
import com.norsez.dsp.block.oscillator.multimode.*;
import com.norsez.dsp.block.sequencer.*;
import com.norsez.dsp.block.modulation.*;
import com.norsez.dsp.synth.Counter;
/**
 * <p>Title: Monolith</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Monolith extends SynthModel3 {
  static final int AMP = 0;
  static final int FILTER = 1;


  private static final int pVolume = new Counter(0).getInt();
  private static final int pPan = new Counter().getInt();
  private static final int pTranspose = new Counter().getInt();
  private static final int pTune = new Counter().getInt();
  private static final int pAmpAttack = new Counter().getInt();
  private static final int pAmpDecay = new Counter().getInt();
  private static final int pAmpSustain = new Counter().getInt();
  private static final int pAmpRelease = new Counter().getInt();
  private static final int pArpPlay = new Counter().getInt();
  private static final int pArpBpm = new Counter().getInt();
  private static final int pArpNote = new Counter().getInt();
  private static final int pArpGate = new Counter().getInt();
  private static final int pArpSwing = new Counter().getInt();
  private static final int pArpRange = new Counter().getInt();
  private static final int pArpTranspose = new Counter().getInt();
  private static final int pArpStep1 = new Counter().getInt();
  private static final int pArpStep2 = new Counter().getInt();
  private static final int pArpStep3 = new Counter().getInt();
  private static final int pArpStep4 = new Counter().getInt();
  private static final int pArpStep5 = new Counter().getInt();
  private static final int pArpStep6 = new Counter().getInt();
  private static final int pArpStep7 = new Counter().getInt();
  private static final int pArpStep8 = new Counter().getInt();
  private static final int pOsc1Level = new Counter().getInt();
  private static final int pOsc1Pan = new Counter().getInt();
  private static final int pOsc1Semi = new Counter().getInt();
  private static final int pOsc1Fine = new Counter().getInt();
  private static final int pOsc1Mode = new Counter().getInt();
  private static final int pOsc1Pw = new Counter().getInt();
  private static final int pOsc1SupersawMix = new Counter().getInt();
  private static final int pOsc1SupersawDetune = new Counter().getInt();
  private static final int pOsc1Digiwave = new Counter().getInt();
  private static final int pOsc2Level = new Counter().getInt();
  private static final int pOsc2Pan = new Counter().getInt();
  private static final int pOsc2Semi = new Counter().getInt();
  private static final int pOsc2Fine = new Counter().getInt();
  private static final int pOsc2Mode = new Counter().getInt();
  private static final int pOsc2Pw = new Counter().getInt();
  private static final int pOsc2SupersawMix = new Counter().getInt();
  private static final int pOsc2SupersawDetune = new Counter().getInt();
  private static final int pOsc2Digiwave = new Counter().getInt();
  private static final int pOsc3Level = new Counter().getInt();
  private static final int pOsc3Pan = new Counter().getInt();
  private static final int pOsc3Semi = new Counter().getInt();
  private static final int pOsc3Fine = new Counter().getInt();
  private static final int pOsc3Mode = new Counter().getInt();
  private static final int pOsc3Pw = new Counter().getInt();
  private static final int pOsc3SupersawMix = new Counter().getInt();
  private static final int pOsc3SupersawDetune = new Counter().getInt();
  private static final int pOsc3Digiwave = new Counter().getInt();

  private static final int pOsc2SyncedToOsc1 = new Counter().getInt();
  private static final int pOsc3SyncedToOsc2 = new Counter().getInt();

  private static final int pFilterMode = new Counter().getInt();
  private static final int pFilterCutoff = new Counter().getInt();
  private static final int pFilterResonance = new Counter().getInt();
  private static final int pFilterFatness = new Counter().getInt();
  private static final int pFilterKBT = new Counter().getInt();
  private static final int pFilterEnvMod = new Counter().getInt();
  private static final int pFilterAttack = new Counter().getInt();
  private static final int pFilterDecay = new Counter().getInt();
  private static final int pFilterSustain = new Counter().getInt();
  private static final int pFilterRelease = new Counter().getInt();

  private static final int pLfo1Mode = new Counter().getInt();
  private static final int pLfo1Rate = new Counter().getInt();
  private static final int pLfo1Tempo = new Counter().getInt();
  private static final int pLfo2Mode = new Counter().getInt();
  private static final int pLfo2Rate = new Counter().getInt();
  private static final int pLfo2Tempo = new Counter().getInt();
  private static final int pLfo3Mode = new Counter().getInt();
  private static final int pLfo3Rate = new Counter().getInt();
  private static final int pLfo3Tempo = new Counter().getInt();
  private static final int pLfo4Mode = new Counter().getInt();
  private static final int pLfo4Rate = new Counter().getInt();
  private static final int pLfo4Tempo = new Counter().getInt();

  private static final int pLfo1Pitch = new Counter().getInt();
  private static final int pLfo1Volume = new Counter().getInt();
  private static final int pLfo2Cutoff = new Counter().getInt();
  private static final int pLfo2Pan = new Counter().getInt();
  private static final int pLfo3Osc2Pitch = new Counter().getInt();
  private static final int pLfo3Pwm = new Counter().getInt();
  private static final int pLfo3SupersawMix = new Counter().getInt();
  private static final int pLfo4Osc3Pitch = new Counter().getInt();
  private static final int pLfo4Digiwav = new Counter().getInt();
  private static final int pLfo4SupersawDetune = new Counter().getInt();

  private static final int pEnv1Attack = new Counter().getInt();
  private static final int pEnv1Decay = new Counter().getInt();
  private static final int pEnv1Sustain = new Counter().getInt();
  private static final int pEnv1Release = new Counter().getInt();
  private static final int pEnv2Attack = new Counter().getInt();
  private static final int pEnv2Decay = new Counter().getInt();
  private static final int pEnv2Sustain = new Counter().getInt();
  private static final int pEnv2Release = new Counter().getInt();

  private static final int pEnv1Pitch = new Counter().getInt();
  private static final int pEnv1Resonance = new Counter().getInt();
  private static final int pEnv1Lfo1Rate = new Counter().getInt();
  private static final int pEnv2Digiwave = new Counter().getInt();
  private static final int pEnv2Pwm = new Counter().getInt();
  private static final int pEnv2Fatness = new Counter().getInt();

  private static final int pModSrc1 = new Counter().getInt();
  private static final int pModSrc2 = new Counter().getInt();
  private static final int pModSrc3 = new Counter().getInt();
  private static final int pModSrc4 = new Counter().getInt();
  private static final int pModSrc5 = new Counter().getInt();
  private static final int pModSrc6 = new Counter().getInt();
  private static final int pModSrc7 = new Counter().getInt();
  private static final int pModSrc8 = new Counter().getInt();
  private static final int pModDes1 = new Counter().getInt();
  private static final int pModDes2 = new Counter().getInt();
  private static final int pModDes3 = new Counter().getInt();
  private static final int pModDes4 = new Counter().getInt();
  private static final int pModDes5 = new Counter().getInt();
  private static final int pModDes6 = new Counter().getInt();
  private static final int pModDes7 = new Counter().getInt();
  private static final int pModDes8 = new Counter().getInt();
  private static final int pModAmt1 = new Counter().getInt();
  private static final int pModAmt2 = new Counter().getInt();
  private static final int pModAmt3 = new Counter().getInt();
  private static final int pModAmt4 = new Counter().getInt();
  private static final int pModAmt5 = new Counter().getInt();
  private static final int pModAmt6 = new Counter().getInt();
  private static final int pModAmt7 = new Counter().getInt();
  private static final int pModAmt8 = new Counter().getInt();

  private static final int pDistortionLevel = new Counter().getInt();
  private static final int pDistortionType = new Counter().getInt();
  private static final int pDistortionBoost = new Counter().getInt();

  private static final int pChorusLevel = new Counter().getInt();
  private static final int pChorusDepth = new Counter().getInt();
  private static final int pChorusMod = new Counter().getInt();
  private static final int pChorusFeedback = new Counter().getInt();
  private static final int pChorusSpeed = new Counter().getInt();
  private static final int pChorusSpread = new Counter().getInt();

  private static final int pPhaserLevel = new Counter().getInt();
  private static final int pPhaserSplit = new Counter().getInt();
  private static final int pPhaserWidth = new Counter().getInt();
  private static final int pPhaserFrequency = new Counter().getInt();
  private static final int pPhaserTempo = new Counter().getInt();
  private static final int pPhaserFeedback = new Counter().getInt();

  private static final int pDelayLevel = new Counter().getInt();
  private static final int pDelayLp = new Counter().getInt();
  private static final int pDelayHp = new Counter().getInt();
  private static final int pDelayLevelL = new Counter().getInt();
  private static final int pDelayLevelR = new Counter().getInt();
  private static final int pDelayNoteL = new Counter().getInt();
  private static final int pDelayNoteR = new Counter().getInt();
  private static final int pDelayFbL = new Counter().getInt();
  private static final int pDelayFbR = new Counter().getInt();
  private static final int pDelayXFbL = new Counter().getInt();
  private static final int pDelayXFbR = new Counter().getInt();



  private static final int pArpActiveSteps = new Counter().getInt();

  private static final int pPARAMCOUNT = new Counter().getInt();

  private double paramValues [] = new double [pPARAMCOUNT];
  /**
   *same as paramValues but only used as pre-calculated bipolar modulation, and
   * interpolated  value buffers
   */

  private double bipolarValues [] = new double [pPARAMCOUNT];

  public void setProgram ( double [] prog ){
    if (prog.length != paramValues.length){
      //throw new java.lang.NoSuchFieldError(
      //    "\nsetProgram Error: Wrong Version of Program File?\n");
      this.initProgram();
      return;
    }
    for (int i=0; i < prog.length; i++){

      setParameterValue ( i , prog [i]);
    }
  }

  public  double [] getProgram (){
    return paramValues;
  }

  private final static String [] GROUP_NAMES ={"Main","Arp","Osc 1","Osc 2","Osc 3",
  "Filter","Lfo","Env","Delay","Chorus","Phaser"};

  public  int getParameterGroupCount (){
    return GROUP_NAMES.length;
  }

  public String getParameterGroupName ( int index){

     if (index < this.pArpPlay) return "Main";
     else
     if (index >=this.pArpPlay && index < this.pOsc1Level) return "Arp";
     else
     if (index >=this.pOsc1Level && index < this.pOsc2Level) return "Osc 1";
     else
     if (index >=this.pOsc2Level && index < this.pOsc3Level) return "Osc 2";
     else
     if (index >=this.pOsc3Level && index < this.pFilterMode) return "Osc 3";
     else
     if (index >=this.pFilterMode && index < this.pLfo1Mode) return "Filter";
     else
     if (index >=this.pLfo1Mode && index < this.pEnv1Attack) return "Lfo";
     else
     if (index >=this.pEnv1Attack && index <= this.pEnv2Release) return "Env";
     else
     if (index >=this.pChorusLevel && index <= this.pChorusSpread) return "Chorus";
     else
     if (index >=this.pPhaserLevel && index <= this.pPhaserFeedback) return "Phaser";
     else
     if (index >=this.pDelayLevel && index <= this.pDelayXFbR) return "Delay";
     else


     return "";
  }

  final static OscMode omodes [] = {OscA.SHAPE_SAW,OscA.SHAPE_SUPERSAW,OscA.SHAPE_PULSE,OscA.SHAPE_TRIANGLE, OscA.SHAPE_SINE, OscA.SHAPE_DIGI};
  final static String [] MOD_SOURCES = {"Filter Env", "Amp Env", "Env 1", "Env 2", "Lfo 1", "Lfo 2", "Lfo 3", "Lfo 4",
                                        "KBT"};
  final static String [] MOD_DESTINATIONS ={"Volume", "Pan", "Pitch", "Amp Attack", "Amp Decay", "Amp Release",
  "Osc 1 Level", "Osc 2 Level", "Osc 3 Level", "Osc 1 Pan", "Osc 2 Pan", "Osc 3 Pan", "Osc 1 Pitch", "Osc 2 Pitch",
  "Osc 3 Pitch", "PWM","Supersaw Mix", "Supersaw Detune", "DigiWave", "Osc 1 PWM", "Osc 2 PWM", "Osc 3 PWM",
  "Osc 1 Supersaw Mix", "Osc 2 Supersaw Mix", "Osc 3 Supersaw Mix", "Osc 1 Supersaw Detune",  "Osc 2 Supersaw Detune",
  "Osc 3 Supersaw Detune", "Osc 1 DigiWave", "Osc 2 DigiWave", "Osc 3 DigiWave", "Cutoff", "Resonance", "Fatness",
  "Filter Env Mod","Filter Attack","Filter Decay", "Filter Release","Lfo1 Rate","Lfo2 Rate","Lfo3 Rate","Lfo4 Rate",
  "Lfo1->Pitch","Lfo1->Volume","Lfo2->Cutoff","Lfo2->Pan","Lfo3->Osc2Pitch","Lfo3->Pwm","Lfo3->SupersawMix","Lfo4->Osc3Pitch",
  "Lfo4->DigiWave","Lfo4->Supersaw Detune",
  "Env 1 Attack","Env 1 Decay", "Env 1 Release","Env 2 Attack","Env 2 Decay", "Env 2 Release",
  "Env1->Pitch","Env1->Q","Env1->Lfo1Rate","Env2->DigiWave","Env2->PWM","Env2->Filter Fatness"};

  class MyMod extends com.norsez.dsp.block.modulation.ModMatrixModel {
    public MyMod ( int i){
      super ( i );
    }
    public double getValueFromSource ( String src ){
      if (src.equalsIgnoreCase("Filter Env"))
        return env[FILTER].getLastValue();
      if (src.equalsIgnoreCase("Amp Env"))
        return env[AMP].getLastValue();
      if (src.equalsIgnoreCase("Env 1"))
        return env[2].getLastValue();
      if (src.equalsIgnoreCase("Env 2"))
        return env[3].getLastValue();
      if (src.equalsIgnoreCase("Lfo 1"))
        return lfo[0].getLastValue();
      if (src.equalsIgnoreCase("Lfo 2"))
        return lfo[1].getLastValue();
      if (src.equalsIgnoreCase("Lfo 3"))
        return lfo[2].getLastValue();
      if (src.equalsIgnoreCase("Lfo 4"))
        return lfo[3].getLastValue();
      if (src.equalsIgnoreCase("KBT"))
        return arp.getRawNote();



      return 0;
    }
  }


  public void setParameterValue ( int index , double value){
    paramValues[index] = value;

    //pre compute mod bipolar value
    if ((index >= pEnv1Pitch && index <= pEnv2Fatness )
        || (index >=pLfo1Pitch && index <= pLfo4SupersawDetune)
        || index == pFilterEnvMod){
      bipolarValues[index] = Interpolation.mapToRangeLinear(0,-1,1,1,value);
    }else
    if (index == pTranspose || index == pOsc1Semi || index == pOsc2Semi ||
        index == pOsc3Semi ){
      bipolarValues [index] = OscA.getSemiRawTune(value);
    }else
    if (index == pTune || index == pOsc1Fine || index == pOsc2Fine ||
        index == pOsc3Fine ){
      bipolarValues [index] = OscA.getFineRawTune(value);
    }
    else
    if(index == pFilterKBT){
      bipolarValues[index] = 0.5 * Interpolation.mapToRangeLinear(0,-1,1,1,value);
    }

    /////////////////////////////////////////////////

     //set value directly at the components.
    if (pAmpSustain == index)
      env[AMP].setSustain(value);
    else
    if (pFilterSustain == index)
      env[FILTER].setSustain(value);
    else
    if (pEnv1Sustain == index)
      env[2].setSustain(value);
    else
    if (pEnv2Sustain == index)
      env[3].setSustain(value);
    else
    if (index==pArpActiveSteps ){
      arp.setActiveSteps(value);
    }else
    if (pArpPlay == index)
      arp.setActive(value);
    else
    if (pArpGate == index )
      arp.setGateLength(value);
    else
    if (pArpBpm == index)
      arp.setTempo(value);
    else
    if (pArpNote == index) {
      int n = (int) ( (Note.getAllNotes().length - 1) * value);
      arp.setNote(Note.getAllNotes()[n]);
    }
    else
    if (pArpSwing == index)
      arp.setSwing(value);
    else
    if (pArpRange == index)
      arp.setRange(value);
    else
    if (pArpTranspose == index)
      arp.setTranspose(value);
    else
    if (pArpStep1 == index || (pArpStep2 == index) ||(pArpStep3 == index) ||  (pArpStep4 == index) || (pArpStep5 == index)
       || (pArpStep6 == index) || (pArpStep7 == index) || (pArpStep8 == index))
          arp.setNoteAt(index - pArpStep1, value);
    else

    if (pOsc1Mode == index) {
      int n = (int) ( (omodes.length - 1) * value);
      osc[0].setOscMode(omodes[n]);
      //DSPSystem.DEBUG_OUT.println(omodes[n]);
    }
    else
    if (pOsc2Mode == index) {
      int n = (int) ( (omodes.length - 1) * value);
      osc[1].setOscMode(omodes[n]);
    }
    else
    if (pOsc3Mode == index) {
      int n = (int) ( (omodes.length - 1) * value);
      osc[2].setOscMode(omodes[n]);
    }
    else
    if (this.pOsc2SyncedToOsc1 == index)
      osc[1].setIsSynced(value);
    else
    if (this.pOsc3SyncedToOsc2 == index)
      osc[2].setIsSynced(value);
    else
    if (pFilterMode == index) {
      int n = (int) ( (filter[0].getSupportedModes().length - 1) * value);
      filter[0].setMode(filter[0].getSupportedModes()[n]);
      filter[1].setMode(filter[0].getSupportedModes()[n]);
    }
    else
    if (pLfo1Mode == index || (pLfo2Mode == index) || (pLfo3Mode == index) ||  (pLfo4Mode == index) ){
            int n = (int) ( (LFO.Shape.ALL_SHAPES.length - 1) * value);
            if (index == pLfo1Mode)
              lfo[0].setShape(LFO.Shape.ALL_SHAPES[n]);
            if (index == pLfo2Mode)
              lfo[1].setShape(LFO.Shape.ALL_SHAPES[n]);
            if (index == pLfo3Mode)
              lfo[2].setShape(LFO.Shape.ALL_SHAPES[n]);
            if (index == pLfo4Mode)
              lfo[3].setShape(LFO.Shape.ALL_SHAPES[n]);
          }
    else
    if (pLfo1Tempo == index || pLfo2Tempo == index || pLfo3Tempo == index || pLfo4Tempo == index){

        Note n = Note.getAllNotes()[(int)((Note.getAllNotes().length - 1) * value)];
            if (index == pLfo1Tempo)
              lfo[0].setRateByTempo(arp.getTempo(),n);
            if (index == pLfo2Tempo)
              lfo[1].setRateByTempo(arp.getTempo(),n);
            if (index == pLfo3Tempo)
              lfo[2].setRateByTempo(arp.getTempo(),n);
            if (index == pLfo4Tempo)
              lfo[3].setRateByTempo(arp.getTempo(),n);

    }
    else
    if (index >= pModSrc1 && index <= pModSrc8){
      modMatrix.setSource(index - pModSrc1, MOD_SOURCES[(int)((MOD_SOURCES.length-1.0)*value)]);
    }
    else
    if (index >=pModDes1 && index <= pModDes8){
      modMatrix.setDestination(index - pModDes1, MOD_DESTINATIONS[(int)((MOD_DESTINATIONS.length-1.0)*value)]);
    }
    else
    if (index >= pModAmt1 && index <= pModAmt8){
      modMatrix.setAmount(index - pModAmt1,value);
    }
    else
    if ( index == pDelayLevel) delay.setOutputLevel(value);
    else
    if ( index == this.pDelayFbL) delay.setFeedback(0,value);
    else
    if ( index == this.pDelayFbR) delay.setFeedback(1,value);
    else
    if ( index == this.pDelayXFbL) delay.setCrossfeed(0,value);
    else
    if ( index == this.pDelayXFbR) delay.setCrossfeed(1,value);
    else
    if ( index == this.pDelayHp){
      double temp = Interpolation.linear(Table.T_CUTOFF,value);
      delay.setHpCutoff(0,temp);
      delay.setHpCutoff(1,temp);
    }
    else
    if ( index == this.pDelayLp){
      double temp = Interpolation.linear(Table.T_CUTOFF,value);
      delay.setLpCutoff(0,temp);
      delay.setLpCutoff(1,temp);
    }
    else
    if (index == this.pDelayNoteL){
      delay.setDelayByTempo(0,arp.getTempo(),Note.getAllNotes()[(int)((Note.getAllNotes().length - 1) * value)]);
    }
    else
    if (index == this.pDelayNoteR){
      delay.setDelayByTempo(1,arp.getTempo(),Note.getAllNotes()[(int)((Note.getAllNotes().length - 1) * value)]);
    }
    else
    if (index == this.pChorusDepth){
      chorus.setDepth( Interpolation.linear ( Table.T_EXP_UP, value));
    }
    else
    if (index == this.pChorusFeedback){
      chorus.setFeedback(Interpolation.linear ( Table.T_EXP_UP, value));
    }
    else
    if (index == this.pChorusLevel){
      chorus.setOutputLevel(value);
    }
    else
    if (index == this.pChorusMod){
      chorus.setModulation(Interpolation.linear ( Table.T_EXP_UP, value));
    }
    else
    if (index == this.pChorusSpeed){
      chorus.setRate(Interpolation.linear ( Table.T_EXP_UP, value));
    }
    else
    if (index == this.pChorusSpread){
      chorus.setPanSpread(value);
    }
    else
    if (index == this.pPhaserFeedback){
      phaser.setFeedback(value);
    }
    else
    if (index == this.pPhaserFrequency){
      phaser.setFrequency(Interpolation. linear ( Table. T_CUTOFF,value));
    }
    else
    if (index == this.pPhaserLevel){
      phaser.setOutputLevel(value);
    }
    else
    if (index == this.pPhaserSplit){
      phaser.setSplit(value);
    }
    else
    if (index == this.pPhaserWidth){
      phaser.setWidth(value);
    }
    if (index == this.pPhaserTempo){
      phaser.setRateByTempo(arp.getTempo(),Note.getAllNotes()[(int)((Note.getAllNotes().length - 1) * value)]);
    }

  }

  public int getParameterCount (){
    return pPARAMCOUNT;
  }

  static final String GN_MAIN [] = {"Volume","Pan","Transpose","Fine Tune","Amp Attack", "Amp Decay", "Amp Sustain", "Amp Release"};
  static final String GN_ARP [] = {"Play","BPM","Note","Gate","Swing","Range","Tranpose"};
  static final String GN_OSC [] = {"Level","Pan","Semi","Fine","Mode","Pulse Width","Supersaw Mix","Supersaw Detune","Digi Wave"};
  static final String GN_OSC_MOD [] = {"Osc2 Synced to Osc1","Osc3 Synced to Osc2"};
  static final String GN_FILTER [] = {"Type","Cutoff","Q","Fatness","KBT","Env Mod","Attack","Decay","Sustain","Release"};
  static final String GN_LFO [] = {"Mode","Rate","Tempo"};
  static final String GN_LFO_MOD [] = {"Lfo1->Pitch", "Lfo1->Volume", "Lfo2->Cutoff","Lfo2->Pan","Lfo3->Osc2Pitch","Lfo3->Pwm","Lfo3->SupersawMix",
                                     "Lfo4->Osc3Pitch","Lfo4->DigiWave","Lfo4->Supersaw Detune"};
  static final String GN_ENV [] = {"Attack","Decay","Sustain","Release"};
  static final String GN_ENV_MOD [] = {"Env1->Pitch","Env1->Q","Env1->Lfo1Rate","Env2->DigiWave","Env2->PWM","Env2->Filter Fatness"};
  static final String GN_DELAY [] = {"Level","LP","HP","Level L","Level R","Note L","Note R","Feedback L","Feedback R", "X Feed L",
                                     "X Feed R"};

  static final String GN_CHORUS [] = {"Level","Depth","Modulation","Feedback","Speed","Spread"};
  static final String GN_PHASER [] = {"Level","Split","Width","Frequency","Tempo","Feedback"};

  public String getParameterName ( int index){

    String s = getParameterGroupName ( index );

    if (index < this.pArpPlay)
      s += " " +  GN_MAIN [index];
    else
    if (index <= this.pArpTranspose)
      s += " " +  GN_ARP [index-this.pArpPlay];
    else
    if (index <= this.pArpStep8)
      s += " " +  "Step " + (index +1 - this.pArpStep1);
    else
    if (index >= this.pOsc1Level && index <= this.pOsc3Digiwave ){
      int idx = (index- this.pOsc1Level) % GN_OSC.length;
      s += " " +  GN_OSC[idx];
    }
    else
    if (index >= this.pOsc2SyncedToOsc1 && index <= this.pOsc3SyncedToOsc2){
      s += " " +  GN_OSC_MOD[index - this.pOsc2SyncedToOsc1];
    }
    else if (index == pArpActiveSteps )
      s += " Arp Active Steps";
    else
    if (index >=pFilterMode && index <= pFilterRelease){
      s += " " + GN_FILTER[index - this.pFilterMode];
    }
    else
    if (index >= this.pLfo1Mode && index <= this.pLfo4Tempo){
      int n = (int)((index - this.pLfo1Mode) /GN_LFO.length + 1);
      s += " Lfo " + n + " "+ GN_LFO[ (index - this.pLfo1Mode) % GN_LFO.length];
    }
    else
    if (index >= this.pLfo1Pitch && index <=this.pLfo4SupersawDetune)
      s += " " +  GN_LFO_MOD [ index -this.pLfo1Pitch];
    else
    if (index >= this.pEnv1Attack && index <= this.pEnv2Release){
      int idx = (index - pEnv1Attack) % 4;
      int n = (int)((index - this.pEnv1Attack) /GN_ENV.length + 1);
      s += " Env " + n + " " +  GN_ENV [idx];
    }
    else
    if (index >= pEnv1Pitch && index <= pEnv2Fatness){
      s += "" +  GN_ENV_MOD [ index - this.pEnv1Pitch];
    }
    else
    if (index >= this.pModSrc1 && index <= pModSrc8){
      s += "" +  "Mod Src " + (index - pModSrc1 + 1);
    }
    else
    if (index >= this.pModDes1 && index <= pModDes8){
      s += "" +  "Mod Des " + (index - pModDes1 + 1);
    }
    else
    if (index >= pModAmt1 && index <= pModAmt8){
      s += "" +  "Mod Amount " + (index - pModAmt1 + 1);
    }
    else
    if (index >= pChorusLevel && index <= pChorusSpread){
      s += GN_CHORUS[index - pChorusLevel];
    }
    else
    if (index >= pPhaserLevel && index <= pPhaserFeedback){
      s += GN_PHASER[index - pPhaserLevel];
    }
    else
    if (index >= this.pDelayLevel && index <= this.pDelayXFbR){
      s += GN_DELAY [index - this.pDelayLevel];
    }
    else
      s = "unimplemented";

    return s;
  }

  public double getParameterValue ( int index){
     return paramValues [ index ];
  }
  public String getParameterDisplay ( int index){

     if (index == pVolume || index == pAmpSustain || index == pOsc1Level ||
         index == pOsc2Level || index == pOsc3Level || index == pDelayLevel ||
         index == pDelayLevelL || index == pDelayLevelR || index == pChorusLevel ||
         index == pPhaserLevel
         )
       return ParameterDisplay.dB.getDisplay(paramValues[index]);

     if (index >= pModSrc1 && index <= pModSrc8 )
       return MOD_SOURCES[(int)(paramValues[index]* (MOD_SOURCES.length - 1.0))];

     if (index >= pModDes1 && index <= pModDes8 )
       return MOD_DESTINATIONS[(int)(paramValues[index]* (MOD_DESTINATIONS.length - 1.0))];

     if ((index >= pModAmt1 && index <= pModAmt8) || index == pFilterEnvMod
         || index == pOsc1Pw || index == pOsc2Pw || index == pOsc3Pw ||
         (index >= pLfo1Pitch && index <= pLfo4SupersawDetune) ||
         (index >= pEnv1Pitch && index <= pEnv2Fatness)
         || index == pFilterKBT
         )
       return ParameterDisplay.LINEAR_BIPOLAR.getDisplay(paramValues[index]);

     if (index == pArpNote || index == pDelayNoteL || index == pDelayNoteR || index == pPhaserTempo ||
         index == pLfo1Tempo || index == pLfo2Tempo || index == pLfo3Tempo || index == pLfo4Tempo){
       return Note.getAllNotes()[(int)(paramValues[index] * (Note.getAllNotes().length - 1))].toString();
     }

     if (index == pArpActiveSteps)
       return "" + arp.getNumActiveSteps();

     if (index == pArpRange) return ArpSwing.RANGE.getDisplay(paramValues[index]);
     if (index == pArpTranspose) return ParameterDisplay.SEMI.getDisplay(paramValues[index]);
     if (index == pArpBpm ) return ParameterDisplay.BPM.getDisplay(paramValues[index]);

     if (index >= pArpStep1 && index <= pArpStep8 )
       return Table.T_NOTE_NAMES [(int)(paramValues[index] *127)];

     if (index == pOsc1Mode || index == pOsc2Mode || index == pOsc3Mode){
       return this.omodes [(int)((omodes.length - 1.0) * paramValues[index])].toString();
     }

     if (index == pPan || index == pOsc1Pan || index == pOsc2Pan || index == pOsc3Pan)
       return ParameterDisplay.PAN.getDisplay(paramValues[index]);

     if (index == pLfo1Mode || index == pLfo2Mode  ||index == pLfo3Mode  ||index == pLfo4Mode )
       return LFO.Shape.ALL_SHAPES[(int)((LFO.Shape.ALL_SHAPES.length - 1) * paramValues[index])].toString();

     if (index == pLfo1Rate || index == pLfo2Rate  ||index == pLfo3Rate  ||index == pLfo4Rate )
       return ParameterDisplay.LFO_RATE.getDisplay(paramValues[index]);

     if (index == pTranspose || index == pOsc1Semi || index == pOsc2Semi || index == pOsc3Semi )
       return ParameterDisplay.SEMI.getDisplay(paramValues[index]);

     if (index == pTune || index == pOsc1Fine || index == pOsc2Fine || index == pOsc3Fine )
       return ParameterDisplay.FINE.getDisplay(paramValues[index]);

     if (index == pFilterAttack || index == pFilterDecay || index == pFilterRelease
         || index == pAmpAttack || index == pAmpDecay || index == pAmpRelease
         || index == pEnv1Attack || index == pEnv1Decay || index == pEnv1Release
         || index == pEnv2Attack || index == pEnv2Decay || index == pEnv2Release)
       return ParameterDisplay.ENV_TIME.getDisplay(paramValues[index]);

     if (index == pFilterCutoff || index == pDelayLp || index == pDelayHp  || index == pPhaserFrequency)
       return ParameterDisplay.CUTOFF.getDisplay(paramValues[index]);

     if (index == pFilterMode ){
       return this.filter[0].getSupportedModes()[(int)((filter[0].getSupportedModes().length-1) * paramValues[pFilterMode])].toString();
     }

     if (index == pArpPlay || index == this.pOsc2SyncedToOsc1 || index == this.pOsc3SyncedToOsc2 ){
       return ParameterDisplay.ON_OFF.getDisplay(paramValues[index]);
     }


     return ParameterDisplay.LINEAR.getDisplay( paramValues[index] );
  }
  public void initProgram (){
    for (int i=0; i < paramValues.length;i++){
      paramValues[i] = 0;
      if ((i>= pModAmt1 && i<= pModAmt8) || i== pFilterEnvMod
         || i== pOsc1Pw || i== pOsc2Pw || i== pOsc3Pw ||
         (i>= pLfo1Pitch && i<= pLfo4SupersawDetune) || i == pTune || i == pTranspose ||
         i == pArpTranspose ||
         (i>= pEnv1Pitch && i<= pEnv2Fatness ) || i == pFilterKBT)
       setParameterValue ( i ,.5);
    }

    setParameterValue(this.pVolume,DSPSystem.dB_6);
    setParameterValue(this.pPan,.5);
    setParameterValue(this.pOsc1Pan,.5);
    setParameterValue(this.pOsc2Pan,.5);
    setParameterValue(this.pOsc3Pan,.5);
    setParameterValue(this.pOsc1Semi,.5);
    setParameterValue(this.pOsc2Semi,.5);
    setParameterValue(this.pOsc3Semi,.5);
    setParameterValue(this.pOsc1Fine,.5);
    setParameterValue(this.pOsc2Fine,.5);
    setParameterValue(this.pOsc3Fine,.5);
    setParameterValue(this.pAmpSustain,1);
    setParameterValue(this.pArpGate,.7);
    setParameterValue(this.pArpBpm,.5);
    setParameterValue(this.pArpStep1,60.0/127.0);
    setParameterValue(this.pArpStep2,64.0/127.0);
    setParameterValue(this.pArpStep3,67.0/127.0);
    setParameterValue(this.pArpStep4,71.0/127.0);
    setParameterValue(this.pArpNote,.8);
    setParameterValue(this.pOsc1Level,DSPSystem.dB_6);

    setParameterValue(this.pFilterMode,.1);

    setParameterValue(this.pFilterCutoff,.90);
    setParameterValue(this.pFilterResonance,.1);
    setParameterValue(this.pFilterDecay,.375);


    for (int i=this.pModAmt1; i <= this.pModAmt8; i++){
      setParameterValue(i,.5);
    }

    setParameterValue ( this.pChorusDepth, .7);
    setParameterValue ( this.pChorusMod, .5);
    setParameterValue ( this.pChorusSpeed, .07);
    setParameterValue ( this.pPhaserFeedback, .75);
    setParameterValue ( this.pPhaserSplit, .57);
    setParameterValue ( this.pPhaserTempo, .37);
    setParameterValue ( this.pPhaserFrequency, .3975);

    setParameterValue(this.pDelayLp,1);
    setParameterValue(this.pDelayLevelL,.7);
    setParameterValue(this.pDelayLevelR,1);
    setParameterValue(this.pDelayNoteL,.5);
    setParameterValue(this.pDelayNoteR,.8);


  }

  public final boolean DEBUG = true;
  public EnvelopeADSR env[];
  public FatMoogFilter filter[];
  public OscA osc[];
  public LFO lfo[];
  public DelayTempoStereo delay;
  public Chorus chorus;
  public Phaser phaser;

  public SmoothingFilter portaFilter, panFilter;
  public SmoothingFilter panLp [];

  MyMod modMatrix;
  ModValue modKeyboard;

  SampleClock clock;
  ArpSwing arp;


  public Monolith() {



    clock = new SampleClock ();
    arp= new ArpSwing (8);

    env = new EnvelopeADSR [4];
    lfo = new LFO [4];
    for(int i=0; i < env.length;i++){
      env [i] = new EnvelopeADSR ();
      lfo [i] = new LFO ();
    }

    osc = new OscA [3];
    for(int i=0; i < osc.length;i++){
      osc[i] = new OscA ();
      osc[i].setPWM(0.5);

    }

    filter = new FatMoogFilter [2];
    for(int i=0; i < filter.length;i++){
      filter[i] = new FatMoogFilter ();
    }

    delay = new DelayTempoStereo ();
    chorus = new Chorus (4);
    phaser = new Phaser (6);

    modMatrix = new MyMod (8);


    initProgram ();
  }

  final static double ONE_OVER_THREE = 1.0/Math.sqrt(3.0);
  final int CONTROL_MARK = 400;
  final int SLOW_MARK = 4000;
  private int control_count, slow_count;
  private double temp,temp1,temp2,temp3,temp4,temp5,temp6;
  public void tick ( ){

    if (slow_count >= SLOW_MARK) {
      slow_count = 0;

    }
    slow_count++;

    if (control_count >= CONTROL_MARK) {
      control_count = 0;


        lfo[0].tick(CONTROL_MARK);
        lfo[1].tick(CONTROL_MARK);
        lfo[2].tick(CONTROL_MARK);
        lfo[3].tick(CONTROL_MARK);



        lfo[0].setRate(Interpolation.linear(Table.T_LFO_RATE, paramValues[pLfo1Rate]
                                            + modMatrix.getModValueForDestination("Lfo1 Rate")
                                            + env[2].getLastValue() * bipolarValues [pEnv1Lfo1Rate]
                                            ));
        lfo[1].setRate(Interpolation.linear(Table.T_LFO_RATE, paramValues[pLfo2Rate]
                                            + modMatrix.getModValueForDestination("Lfo2 Rate")
                                            ));
        lfo[2].setRate(Interpolation.linear(Table.T_LFO_RATE, paramValues[pLfo3Rate]
                                            + modMatrix.getModValueForDestination("Lfo3 Rate")
                                            ));
        lfo[3].setRate(Interpolation.linear(Table.T_LFO_RATE, paramValues[pLfo4Rate]
                                            + modMatrix.getModValueForDestination("Lfo4 Rate")
                                            ));


        env[AMP].setAttack(paramValues[pAmpAttack] + modMatrix.getModValueForDestination("Amp Attack"));
        env[AMP].setDecay(paramValues[pAmpDecay] + modMatrix.getModValueForDestination("Amp Decay"));
        env[AMP].setRelease(paramValues[pAmpRelease] + modMatrix.getModValueForDestination("Amp Release"));
        env[FILTER].setAttack(paramValues[pFilterAttack] + modMatrix.getModValueForDestination("Filter Attack"));
        env[FILTER].setDecay(paramValues[pFilterDecay] + modMatrix.getModValueForDestination("Filter Decay"));
        env[FILTER].setRelease(paramValues[pFilterRelease] + modMatrix.getModValueForDestination("Filter Release"));
        env[2].setAttack(paramValues[pEnv1Attack] + modMatrix.getModValueForDestination("Env 1 Attack"));
        env[2].setDecay(paramValues[pEnv1Decay] + modMatrix.getModValueForDestination("Env 1 Decay"));
        env[2].setRelease(paramValues[pEnv1Release] + modMatrix.getModValueForDestination("Env 1 Release"));
        env[3].setAttack(paramValues[pEnv2Attack] + modMatrix.getModValueForDestination("Env 2 Attack"));
        env[3].setDecay(paramValues[pEnv2Decay] + modMatrix.getModValueForDestination("Env 2 Decay"));
        env[3].setRelease(paramValues[pEnv2Release] + modMatrix.getModValueForDestination("Env 2 Release"));

        temp = lfo[2].getLastValue() * bipolarValues [pLfo3Pwm]
            + env[3].getLastValue() * bipolarValues[pEnv2Pwm];

        osc[0].setPWM( paramValues [pOsc1Pw] + modMatrix.getModValueForDestination("PWM")
                       + modMatrix.getModValueForDestination("Osc 1 PWM") + temp);
        osc[1].setPWM( paramValues [pOsc2Pw] + modMatrix.getModValueForDestination("PWM")
                       + modMatrix.getModValueForDestination("Osc 2 PWM") + temp);
        osc[2].setPWM( paramValues [pOsc3Pw] + modMatrix.getModValueForDestination("PWM")
                       + modMatrix.getModValueForDestination("Osc 3 PWM") + temp);

        temp =lfo[2].getLastValue() * bipolarValues [pLfo3SupersawMix];
        osc[0].setSuperSawMix( paramValues [pOsc1Pw] + modMatrix.getModValueForDestination("Supersaw Mix")
                       + modMatrix.getModValueForDestination("Osc 1 Supersaw Mix")
                       + temp);
        osc[1].setSuperSawMix( paramValues [pOsc2Pw] + modMatrix.getModValueForDestination("Supersaw Mix")
                       + modMatrix.getModValueForDestination("Osc 2 Supersaw Mix")
                       + temp);
        osc[2].setSuperSawMix( paramValues [pOsc3Pw] + modMatrix.getModValueForDestination("Supersaw Mix")
                       + modMatrix.getModValueForDestination("Osc 3 Supersaw Mix")
                       + temp);


         temp = lfo[3].getLastValue() * bipolarValues[pLfo4SupersawDetune];
        osc[0].setSuperSawDetune( paramValues [pOsc1Pw] + modMatrix.getModValueForDestination("Supersaw Detune")
                       + modMatrix.getModValueForDestination("Osc 1 Supersaw Detune")
                       + temp);
        osc[1].setSuperSawDetune( paramValues [pOsc2Pw] + modMatrix.getModValueForDestination("Supersaw Detune")
                       + modMatrix.getModValueForDestination("Osc 2 Supersaw Detune")
                       + temp);
        osc[2].setSuperSawDetune( paramValues [pOsc3Pw] + modMatrix.getModValueForDestination("Supersaw Detune")
                       + modMatrix.getModValueForDestination("Osc 3 Supersaw Detune")
                       + temp);

      temp = bipolarValues[pLfo4Digiwav] * lfo[3].getLastValue()
                                            +bipolarValues[pEnv2Digiwave] * env[3].getLastValue();
      osc[0].setDigiWave( paramValues [pOsc1Pw] + modMatrix.getModValueForDestination("DigiWave")
                     + modMatrix.getModValueForDestination("Osc 1 DigiWave")
                     +temp

                     );
      osc[1].setDigiWave( paramValues [pOsc2Pw] + modMatrix.getModValueForDestination("DigiWave")
                     + modMatrix.getModValueForDestination("Osc 2 DigiWave")
                     + temp
                     );
      osc[2].setDigiWave( paramValues [pOsc3Pw] + modMatrix.getModValueForDestination("DigiWave")
                     + modMatrix.getModValueForDestination("Osc 3 DigiWave")
                     + temp
                     );


      temp =  env[2].getLastValue() * bipolarValues[pEnv1Resonance]
                                            + env[3].getLastValue() * bipolarValues[pEnv2Fatness];
      for (int i=0; i < filter.length;i++){
        filter[i].setResonance( paramValues[pFilterResonance] + modMatrix.getModValueForDestination("Resonance") + temp);
        filter[i].setFat( paramValues[pFilterFatness] + modMatrix.getModValueForDestination("Fatness"));

      }


    }
    control_count++;

    clock.tick ();
    arp.tick (clock.getLastValue());

    //compute base pitch raw note
    temp = arp.getRawNote() + OscA.getSemiRawTune( paramValues [pTranspose] )
        + OscA.getFineRawTune(paramValues [pTune])
        + Interpolation.linear(Table.T_EXP_UP, (modMatrix.getModValueForDestination("Pitch")
                                                + lfo[0].getLastValue() * bipolarValues [pLfo1Pitch]
                                                +env[2].getLastValue() * bipolarValues [pEnv1Pitch]
                                                )* 0.0944881)

        ;


    temp1=temp2=temp3=temp4=temp5=temp6=0;

    env[AMP].tick(arp.getGateValue());
    env[FILTER].tick(arp.getGateValue());
    env[2].tick(arp.getGateValue());
    env[3].tick(arp.getGateValue());


    if (paramValues[pOsc1Level] != 0) {
      temp1 = temp + bipolarValues[pOsc1Semi]+
          bipolarValues[pOsc1Fine]
          + Interpolation.linear(Table.T_EXP_UP, modMatrix.getModValueForDestination("Osc 1 Pitch")
                                 //* 0.0944881
                                 )
          ;

      osc[0].setCps(Interpolation.linear(Table.T_NOTE2CPS, temp1));
      osc[0].tick();
      temp1 = osc[0].getLastValue() * (paramValues [ pOsc1Level ]
                                       +modMatrix.getModValueForDestination("Osc 1 Level")
                                       ) ;
    }

    if (paramValues[pOsc2Level] != 0) {
      temp2 = temp + bipolarValues[pOsc2Semi] +
          bipolarValues[pOsc2Fine]
          + Interpolation.linear(Table.T_EXP_UP, ( modMatrix.getModValueForDestination("Osc 2 Pitch")
                                                  +lfo[2].getLastValue() * bipolarValues[pLfo3Osc2Pitch]
                                                  )
                                                                         //* 0.0944881
                                                                         )
          ;


      osc[1].setCps(Interpolation.linear(Table.T_NOTE2CPS, temp2));
      osc[1].setSync(osc[0].getPhase());
      osc[1].tick();
      temp2 = osc[1].getLastValue() * (paramValues [ pOsc2Level ]
                                       +modMatrix.getModValueForDestination("Osc 2 Level")
                                       ) ;
    }

    if (paramValues[pOsc3Level] != 0) {
      temp3 = temp + bipolarValues[pOsc3Semi] +
          bipolarValues[pOsc3Fine]
          + Interpolation.linear(Table.T_EXP_UP, (modMatrix.getModValueForDestination("Osc 3 Pitch")
                                                  + lfo[3].getLastValue() * bipolarValues [pLfo4Osc3Pitch]
                                                  )
                                 //* 0.0944881
                                 )
          ;


      osc[2].setCps(Interpolation.linear(Table.T_NOTE2CPS, temp3));
      osc[2].setSync(osc[1].getPhase());
      osc[2].tick();
      temp3 = osc[2].getLastValue() * (paramValues [ pOsc3Level ]
                                       +modMatrix.getModValueForDestination("Osc 3 Level")
                                       ) ;
    }

    //now temp1,temp2,temp3 are osc 1,2,3 outputs

    //lastValueL = lastValueR = temp1+temp2+temp3;


    //comput osc pans put into temp4,5,6
    temp4 = paramValues[pOsc1Pan] + modMatrix.getModValueForDestination("Osc 1 Pan");
    temp5 = paramValues[pOsc2Pan] + modMatrix.getModValueForDestination("Osc 2 Pan");
    temp6 = paramValues[pOsc3Pan] + modMatrix.getModValueForDestination("Osc 3 Pan");

    //DSPSystem.DEBUG_OUT.println(temp4 + " " +temp5 + " " + temp6);
    lastValueL = temp1 * temp4
        + temp2 * temp5
        + temp3 * temp6;



    lastValueR = temp1 * (1-temp4)
        + temp2 * (1-temp5)
        + temp3 * (1-temp6);



   lastValueL *= this.ONE_OVER_THREE;
   lastValueR *=this.ONE_OVER_THREE;

    //now lastValueL, lastValueR are osc 1,2,3 mixed output




     //compute mod volume
    temp1 =  paramValues[pVolume] + modMatrix.getModValueForDestination("Volume")
        + lfo[0].getLastValue() * bipolarValues [pLfo1Volume];
    temp1 *= env[AMP].getLastValue() ;
    //compute mod pan
    temp2 = paramValues[pPan]
        + bipolarValues[pLfo2Pan]*lfo[1].getLastValue()
        + modMatrix.getModValueForDestination("Pan");

    lastValueL *= temp1
        * temp2
        ;
    lastValueR *= temp1
        * (1-temp2)
         ;


    //calculate cutoff mod, put in temp
    temp = paramValues[pFilterCutoff]
        + modMatrix.getModValueForDestination("Cutoff")
        + bipolarValues [pLfo2Cutoff] * lfo[1].getLastValue()
        + bipolarValues [pFilterEnvMod] + env[FILTER].getLastValue() * modMatrix.getModValueForDestination("Filter Env Mod")
        + bipolarValues [pFilterKBT] * arp.getRawNote()
        ;
    temp = Interpolation.linear(Table.T_CUTOFF,temp);
    filter[0].setCutoff(temp);
    filter[1].setCutoff(temp);

    //pass osc into filter
    lastValueL = filter[0].tick(lastValueL);
    lastValueR = filter[1].tick(lastValueR);

    chorus.tick(lastValueL,lastValueR);
    phaser.tick(lastValueL,lastValueR);

    //mix after go thur chorus and phaser separately
    lastValueL = lastValueL * chorus.getDryMixFactor()
        + chorus.getLastValueL()
        + lastValueL * phaser.getDryMixFactor()
        + phaser.getLastValueL();
    lastValueR = lastValueR * chorus.getDryMixFactor()
        + chorus.getLastValueR()
        + lastValueR * phaser.getDryMixFactor()
        + phaser.getLastValueR();

    //pass everything thru delay
    delay.tick(lastValueL,lastValueR);
    lastValueL = lastValueL * delay.getDryMixFactor() +
        delay.getLastValueL();
    lastValueR = lastValueR * delay.getDryMixFactor() +
        delay.getLastValueR();

  }

  public StringBuffer getStatus (){
    StringBuffer s = new StringBuffer ();
    for(int i =0; i < getParameterCount();i++){
         s.append( i + " " +getParameterName(i) + " = " + getParameterDisplay(i) + "\n");
       }

    return s;
  }



  public static void main(String[] args) {

    long start = new java.util.Date().getTime();
    Monolith m = new Monolith();
    System.out.println(m.getStatus().toString());
    //System.out.println (m.pPARAMCOUNT + " " + m.pChorusDepth);




   }




}
