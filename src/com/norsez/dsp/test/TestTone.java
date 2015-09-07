package com.norsez.dsp.test;
import java.awt.*;
import java.awt.event.*;
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
 * <p>Title: TestTone</p>
 * <p>Description: Outputs sine wave at 440.0 Hz</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public  class TestTone extends SynthModel2 {

    OscA osc1;

    public TestTone (){
      String _main = "main";
      pmgr.addParameter(new Parameter ("Vol",.5,_main,ParameterDisplay.dB));
      pmgr.addParameter(new Parameter ("Freq",0.5,_main,ParameterDisplay.NOTE_FREQUENCY));



      osc1 = new OscA ();
      osc1.setOscMode(OscA.SHAPE_SINE);
      osc1.setCps(DSPSystem.A4_FREQUENCY / DSPSystem.getSamplingRate());


    }

    public void tick (){
      osc1.tick();
      osc1.setCps( Table.getLinIntp(  pmgr.getParameterValue("Freq"), Table.T_ENV_UP) );
      lastValueL = lastValueR = osc1.getLastValue() * pmgr.getParameterValue("Vol");
    }

    public static void main(String[] args) {
      JOptionPane.showMessageDialog(null,new TestTone ().getEditorPanelA());
      System.exit ( 0 );
    }

  }
