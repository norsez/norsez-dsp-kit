package com.norsez.dsp.synth;

import com.norsez.dsp.block.*;

/**
 * <p>Title: VoiceManager</p>
 * <p>Description: Encapsulates the algorithms that allocate voices. <i>This version is suitable for
 *                 monophonic input (like ArpSwing) only.</i>
 *                 This is not a MVC design like VoiceAllocation class.
 *                 It will just hold gates and note values in arrays of double for the voices to read off of. This is
 *                 suitable for voices that uses polling approach to updating their gate values and pitch notes.
 *                 </p><p>
 *                 A typical use in a SynthModel for a polyphonic synth is as the following:
 *                 <blockquote>
 *
 *                       VoiceModel [] voices; <br>
 *                       VoiceManager vmgr;<br>
 *                       ArpSwing arp;<br>
 *                       <br>.<br>.<br>.<br>
 *                       public void tick (){
 *                       <br>.<br>.<br>.<br>
 *                             for(int i=0; i < voices.length; i++)
 *                                   vmgr.setStealable(i,voices[i].isStealable());
 *                        <br>
 *                       vmgr.tick(arp.getLastValue(),arp.getRawNote());
 *                       <br>.<br>.<br>.<br>
 *                       for(int i = 0; i < vmgr.getActiveVoiceCount();i++){
 *                           ((SimpleVoice) voices[i]).ampEnv.tick(vmgr.getGate(i));<br>
 *                                voices[i].setCps(Interpolation.linear(Table.T_NOTE2CPS, vmgr.getRawNote(i)));<br>
 *       voices[i].tick();<br>
 *      lastValueL += voices[i].getLastValueL() *
 *          pmgr.getParameterValue("Vol") ;<br>
 *       lastValueR = lastValueL;<br>
 *
 *     }
 *
 *
 *                 </blockquote>
 *
 *    </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class VoiceManager {
  protected double[] gates;
  protected double[] rawNotes;
  /**
   * voices must write steablable status here before each VoiceMananger ticks so that VoiceManager knows
   * if the voice is stealable.
   */
  protected boolean[] isStealable;
  protected Mode mode;
  protected final int NUM_VOICES;

  protected int numActiveVoices;

  protected double unisonDetune;

  /**
   * A flag that reminds tick() to turn gate on. Used in MonoLegato process.
   */
  private boolean needsGateOn[];

  private final double ONE_OVER_NUM_VOICES;

  private boolean isMonoLegato;

  public VoiceManager(int numOfVoices) {

    NUM_VOICES = numOfVoices;
    ONE_OVER_NUM_VOICES = 1.0/NUM_VOICES;
    gates = new double[NUM_VOICES];
    rawNotes = new double[NUM_VOICES];
    isStealable = new boolean[NUM_VOICES];
    needsGateOn = new boolean[NUM_VOICES];
    for (int i = 0; i < isStealable.length; i++) {
      isStealable[i] = true;

    }
    setActiveVoiceCount(NUM_VOICES);
    setMode(Mode.MONO);

  }

  /**
   * Voices use this method to get the lastest gate.
   * @param voiceNum voice index
   * @return the latest gate value
   */
  public double getGate(int voiceNum) {
    return gates[voiceNum];
  }

  /**
   * Voices use this method to get the latest note.
   * @param voiceNum voice index
   * @return the latest raw note
   */
  public double getRawNote(int voiceNum) {
    return rawNotes[voiceNum];
  }


  public void setUnisonTune (double t){
    unisonDetune = t;
  }
  public void setMode(Mode m) {
    mode = m;
    //turn off every voice
    for (int i = 0; i < NUM_VOICES; i++) {
      gates[i] = 0;

    }

  }

  public void setStealable(int voiceNumber, boolean b) {
    isStealable[voiceNumber] = b;
  }

  public void setActiveVoiceCount(int i) {
    numActiveVoices = i;
  }

  public int getActiveVoiceCount() {
    return numActiveVoices;
  }

  /**
   * Checks if a voice is stealable, i.e. whether the gate is on for the voice.
   *
   * @param voiceNumber voice index
   * @return whether the voice is being held by a gate on.
   */
  public boolean isInUse(int voiceNumber) {
    return gates[voiceNumber] != 0;
  }

  /**
   * Checks if a voice is stealable, i.e. whether its voice is below the steable point.
   * For this method to work, voices must write their last level at every sample, preferrably
   * when they come to poll their gate and note values.
   *
   * @param voiceNumber voice index
   * @return whether the voice is stealable.
   */
  public boolean isStealable(int voiceNumber) {
    return isStealable[voiceNumber];
  }



  /**
   * This is the tick method suitable for use with a monophonic input
   * like an ArpSwing object.
   *
   * @param gate gate value
   * @param rawnote raw note associated with the gate
   */
  public void tick(double gate, double rawnote) {

    if (mode == Mode.MONO) {

      //if voice is in use
      if (isInUse(0)) {

        if (gate != 0) { //note on message

          if (needsGateOn[0]) {
            gates[0] = gate;
            needsGateOn[0] = false;
          }

          if (rawnote != rawNotes[0]) {
            if (!isMonoLegato()) {
              needsGateOn[0] = true;
              gates[0] = 0;
            }
          }

          rawNotes[0] = rawnote;


        }
        else { //note off message
          if (rawnote == rawNotes[0]) {
            gates[0] = gate;

          }
        }

      }
      //if voice is not in use
      else {

        if (gate != 0) {

          gates[0] = gate;
          rawNotes[0] = rawnote;

        }

      }

    }
    else
    if (mode == Mode.POLY) {

      //note off
      if (gate == 0) {
        for (int i = 0; i < numActiveVoices; i++) {
          if (rawnote == rawNotes[i]) {
            gates[i] = 0;
            return;
          }
        }
      }
      else { //note on
        for (int i = 0; i < numActiveVoices; i++) {
          if (needsGateOn[i]) {
            gates[i] = gate;
            needsGateOn[i] = false;

          }
        }

        //if note is already on, exit
        for (int i = 0; i < numActiveVoices; i++) {
          if (rawnote == rawNotes[i]) {
            if (isInUse(i)) {
              needsGateOn[i] = true;
              gates[i] = 0;
              return;
            }
          }

        }

        //find free voice
        for (int i = 0; i < numActiveVoices; i++) {
          if (!isInUse(i) && isStealable(i)) {
            //found the voice to steal
            gates[i] = gate;
            rawNotes[i] = rawnote;

            return;
          }
        }

        int min = 0;
        double minnote = 1.2;

        //find stealable voice
        for (int i = 0; i < numActiveVoices; i++) {
          if (isStealable(i)) {
            //found the voice to steal
            gates[i] = gate;
            rawNotes[i] = rawnote;

            return;
          }
          else {
            if (rawNotes[i] < minnote) {
              min = i;
              minnote = rawNotes[i];
            }
          }
        }

        gates[min] = gate;
        rawNotes[min] = rawnote;

      }

    }
    else
    if (mode == Mode.UNISON) {
      //if voice is in use
      if (isInUse(0)) {

        if (gate != 0) { //note on message

          if (needsGateOn[0]) {
            for (int i = 0; i < this.getActiveVoiceCount(); i++) {
              gates[i] = gate;
              needsGateOn[i] = false;

            }
        }

          if (rawnote != rawNotes[0]) {
            if (!isMonoLegato()) {
              needsGateOn[0] = true;
              for (int i = 0; i < this.getActiveVoiceCount(); i++) {
                gates[i] = 0;
              }
            }
          }
          for (int i = 0; i < this.getActiveVoiceCount(); i++)
            rawNotes[i] = rawnote + com.norsez.dsp.block.oscillator.multimode.OscA.getFineRawTune(
                unisonDetune  ) * this.ONE_OVER_NUM_VOICES* i;

        }
        else { //note off message
          if (rawnote == rawNotes[0]) {
            for (int i = 0; i < this.getActiveVoiceCount(); i++)
              gates[i] = gate;

          }
        }

      }
    }

  }

  public void setMonoLegato(double i) {
    if (i == 0) {
      isMonoLegato = false;
    }
    else {
      isMonoLegato = true;
    }
  }

  public boolean isMonoLegato() {
    return isMonoLegato;
  }

  public static class Mode {
    private final String name;
    private static int nextOrdinal = 0;
    private final int ordinal = nextOrdinal++;
    private Mode(String name) {
      this.name = name;
    }

    public String toString() {
      return name;
    }

    public static final Mode MONO = new Mode("Mono");
    public static final Mode POLY = new Mode("Poly");
    public static final Mode UNISON = new Mode("Unison");

  }

  public String toString() {

    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");

    if (mode == Mode.MONO) {
      return "Mono Mode  gate = " + df.format(gates[0]) + " raw note = " +
          df.format(rawNotes[0]);
    }
    else {
      String s = "PolyMode";
      s += "\ngates  " + printArray(gates);
      s += "\nlevels " + printArray(isStealable);
      //s += "notes " + printArray (rawNotes);

      return s;

    }
  }

  /**
   * for debugging only.
   * @param d double array
   * @return the double values in the array
   */
  protected String printArray(double[] d) {
    java.text.DecimalFormat df = new java.text.DecimalFormat("0.0000");
    String s = "[";
    for (int i = 0; i < d.length; i++) {
      if (d[i] > 0) {
        s += df.format(d[i]) + " ";
      }
      else {
        s += " - ";
      }
    }
    return s + "]";
  }

  /**
   * for debugging only.
   * @param d boolean array
   * @return the double values in the array
   */
  protected String printArray(boolean[] d) {

    String s = "[";
    for (int i = 0; i < d.length; i++) {
      s += d[i] + " ";

    }
    return s + "]";
  }
/*
  public static void main(String[] args) {

    SampleClock clock = new SampleClock();
    ArpSwing arp = new ArpSwing(8);
    arp.setRawSampleLength(6);
    arp.setActive(true);
    arp.setGateLength(.95);
    arp.setSwing(.0);
    arp.setActiveSteps(.5);

    int num = 3;
    VoiceManager v = new VoiceManager(num);
    v.setMonoLegato(1);
    v.setMode(VoiceManager.Mode.POLY);

    com.norsez.dsp.block.envelope.EnvelopeADSR env[] = new com.norsez.dsp.block.envelope.EnvelopeADSR[
        num];
    for (int i = 0; i < num; i++) {
      env[i] = new com.norsez.dsp.block.envelope.EnvelopeADSR();
      env[i].setRawEnvelope(1 / 2.0, 1. / 15., .1, 1 / 57.);
    }

    int t = 100;
    while (t > 0) {
      clock.tick();
      arp.tick(clock.getLastValue());

      for (int i = 0; i < num; i++) {
        v.setStealable(i, env[i].isStealable());
      }
      v.tick(arp.getLastValue(), arp.getRawNote());

      for (int i = 0; i < num; i++) {
        env[i].tick(v.gates[i]);

      }
      if (v.mode == VoiceManager.Mode.MONO) {
        DSPSystem.DEBUG_OUT.println(arp.getGateValue() + "##" + v + "\n" +
                                    env[0] + "\n");
      }
      else {
        DSPSystem.DEBUG_OUT.println(clock.getLastValue() + "##\n" + v);
        for (int i = 0; i < num; i++) {
          DSPSystem.DEBUG_OUT.println("env " + i + " " + env[i]);

        }
        DSPSystem.DEBUG_OUT.println();
      }

      t--;
    }

    DSPSystem.DEBUG_OUT.close();

  }
*/
}