package com.norsez.dsp.synth;

import com.norsez.dsp.block.*;

/**
 * <p>Title: VoiceModel </p>
     * <p>Description: Encapsulates one "voice" of a synth. Assumes stereo output</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public abstract class VoiceModel
    extends DSPBlockStereo {

  //The level which if the voice is lower than, the voice is stealable.
  protected static double stealableLevel;

  //gate value also determines velocity of the gate. 0 is gate off (note off).
  protected double lastGate;
  //current note number
  private int noteNumber;

  public double getGate() {
    return lastGate;
  }

  /**
   *
   * @param gate =0 means no gate (note not in use). >0 means in use and this is also the velocity value.
   */

  public void setGate(double gate) {
    lastGate = gate;
  }

  //@return the current note number of this voice
  public int getNoteNumber() {
    return noteNumber;
  }

  public void setNoteNumber(int n) {
    noteNumber = n;
  }

  protected double lastCps;
  public abstract void setCps(double cps);

  public double getCps() {
    return lastCps;
  }

  /**
   * VoiceManager calls this to make VoiceModel's env retrigger.
   */
  public abstract void retrigger();

  public VoiceModel() {

    setStealableLevel(0.05);
    //setVoicePan ( 0.5 );

  }

  /**
   * This is the level that determines whether this voice is stealable.
   * @param v the stealable voice, e.g. 0.05 (default value) equals 20 log ( 0.05)  \equiv -60dB. If the voice level is lower than -60dB, the voice is stealable.
   *
   */

  public void setStealableLevel(double v) {
    stealableLevel = v;
  }

  /**
   * @return whether the voice is in use and therefore needs to be rendered.
   */
  public abstract boolean isInUse();

  /**
   * VoiceAllocator class calls this method to see if this voice is stealable
   */
  public abstract boolean isStealable();

  private double voicePan;

  public void setVoicePan(double p) {
    voicePan = p;
  }

  public double getVoicePan() {
    return voicePan;
  }

}