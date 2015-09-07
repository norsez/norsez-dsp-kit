package com.norsez.dsp.synth.patch;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import com.norsez.dsp.block.*;
import com.norsez.dsp.block.sequencer.*;
import com.norsez.dsp.block.envelope.*;
import com.norsez.dsp.block.filter.*;
import com.norsez.dsp.block.filter.effects.*;
import com.norsez.dsp.block.oscillator.*;
import com.norsez.dsp.block.oscillator.multimode.*;
import com.norsez.dsp.player.*;
import com.norsez.dsp.synth.*;
import com.norsez.dsp.synth.swing.*;


/**
 * <p>Title: SimpleVoice</p>
 * <p>Description: A simple voice model used for Debugging. It's just a sine wave run thru an amp envelope.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public  class SimpleVoice
      extends VoiceModel {

    public OscA osc1;
    public EnvelopeADSR ampEnv;

    public SimpleVoice() {
      ampEnv = new EnvelopeADSR();
      osc1 = new OscA();
      osc1.setOscMode(osc1.SHAPE_SINE);
    }

    public void retrigger() {
      ampEnv.retrigger();
    }

    public boolean isStealable() {
      return ampEnv.isStealable();
    }

    public boolean isInUse() {
      return ampEnv.getLastValue() > 0;
    }

    public void setCps(double s) {
      lastCps = s;
      osc1.setCps(s);
    }

    public void tick(double l, double r) {
      DSPSystem.unsupported("use tick()");
    }

    public void tick(double[] l, double[] r) {
      DSPSystem.unsupported("use tick()");

    }

    public void tick() {
      osc1.tick();

      lastValueL = osc1.getLastValue() * ampEnv.getLastValue();
      lastValueR = lastValueL;
    }

    public String toString() {
      return "\n\nisInUSe = " + isInUse()
          + ", StealAble = " + isStealable()
          + ", Gate = " + getGate()
          + ", Cps = " + getCps()
          + ", Amp = " + ampEnv.getLastValue()
          + " Amp Stage = " + ampEnv.getStage();
    }
  }
