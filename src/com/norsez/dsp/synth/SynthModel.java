package com.norsez.dsp.synth;

import com.norsez.dsp.block.*;
import com.norsez.dsp.midi.*;
import com.norsez.dsp.synth.*;
import com.norsez.dsp.block.sequencer.*;
import com.norsez.dsp.synth.swing.*;
/**
 * <p>Title: SynthModel</p>
 * <p>Description: A template for synthezier. It has:
 *      <ul>
 *      <li>MidiDSPPort that listens to a Midi In port.
 *      <li>a set of VoiceModel that is the core of synthesizer voices.
 *      <li>VoiceAllocator that listens to MidiDSPPort's note, then trigger VoiceModel's gate.
 *      <li>VoiceMixer that sums all VoiceModels' output.
 *      <li>ParameterManager that maintains all synthsizer global, FX, and voice parameters.
 *          All DSPBlocks, VoiceMixer, VoiceAllocator, and SynthModel read and write their parameter value here.
 *          It listens to MidiDSPPort so it can modify parameters that are mapped to midi CC#s.
 *      </ul>
 *      </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public abstract class SynthModel
    extends com.norsez.dsp.block.DSPBlockStereo {

  protected ParameterManager parameterManager;
  protected VoiceManagerPoly vmgr;
  protected VoiceModel[] voices;
  //protected Kb2MidiModel kb2midi;
  //protected ArpSwingPoly arp;
  //protected SampleClock clock;
  //protected MidiDSPPort midiDSPPort;
  //protected VoiceMixer voiceMixer;

  public ParameterManager getParameterManager() {
    return this.parameterManager;
  }

  public abstract boolean isInUse();

  public VoiceModel[] getVoices() {
    return voices;
  }

  public SynthModel() {

    //midiDSPPort = new MidiDSPPort();
    parameterManager = new ParameterManager();
    //voiceAllocator = new VoiceAllocator(this);
    //vmgr = new VoiceManagerPoly (128);
    //kb2midi = new Kb2MidiModel ();
    //arp = new ArpSwingPoly ();
    //clock = new SampleClock ();

    //voiceMixer = new VoiceMixer(this);

    //midiDSPPort.addPortListener(parameterManager);
    //midiDSPPort.addPortListener(voiceAllocator);

    /*Add Global parameters
    ParameterManager pm = parameterManager;
    pm.addParameter(new Parameter("Master Volume", 0.5));
    pm.addParameter(new Parameter("Master Pan", 0.5));
    pm.addParameter(new Parameter("Voice Count", 1));
    pm.addParameter(new Parameter("Key Mode"));
    pm.addParameter(new Parameter("Portamento Switch"));
    pm.addParameter(new Parameter("Portamento Time", 0.3));
    pm.addParameter(new Parameter("Master Tune", 0.5));
    pm.addParameter(new Parameter("Master Transpose", 0.5));
    pm.addParameter(new Parameter("Unison Voices", 0.1));
    pm.addParameter(new Parameter("Unison Tune", 0.2));
    */
  }
  /*
  public void setMidiInDevice(javax.sound.midi.MidiDevice dev) {
    midiDSPPort.setMidiInDevice(dev);
  }
  */

  /*
   * @return number of voices that this synth can currently use. It must be less than voices.length. the return value is Parameter( "Voice Count" ).value * voices.length

  public int getMaxVoice() {
    return (int) (parameterManager.getParameterValue("Voice Count") *
                  voices.length);
  }

  public double getMasterLevel() {
    return ( (double) parameterManager.getParameterValue("Master Volume"));
  }

  public double getMasterPan() {
    return ( (double) parameterManager.getParameterValue("Master Pan"));
  }
  */

  public void saveSettingsToFile(String filename) {
    parameterManager.saveSettingsToFile(filename);
  }

  public void loadSettingsToFile(String filename) {
    parameterManager.loadSettingsToFile(filename);
  }

}