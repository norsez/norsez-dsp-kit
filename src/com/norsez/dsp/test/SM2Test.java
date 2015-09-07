package com.norsez.dsp.test;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
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
import com.norsez.dsp.project.monolith.*;

public class SM2Test extends JFrame{

  Monolith ml;
  double [] prog;
  String _file = "monolith.pch";


  public SM2Test() {
    super ( "Norsez DSP");
    ml= new Monolith ();
    ml.startPlayThread(true);

    prog = new double [ml.getParameterCount()];

    File config = new File (_file);

    boolean result = false;
    if (config.exists()){
      result = ml.loadProgramFromFile(prog,_file);
    }

    if (result == true)
      ml.setProgram(prog);
    else
      ml.initProgram();


    //getContentPane().setLayout(new GridLayout (2,1));
    getContentPane ().add(ml.getEditorPanelA());
    //getContentPane ().add(s1.kb.getEditPanel());

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize (500,500);
    setLocation ( 200,200);
    System.out.println (DSPSystem.getDSPSystemInfo());

    addWindowListener (new WindowAdapter () {
       public void windowClosing ( WindowEvent e) {
         ml.saveProgramToFile(ml.getProgram(),_file);
         //DSPSystem.DEBUG_OUT.close();
       }

    });

    setVisible(true);
  }

  public static void main ( String []args){

    new SM2Test ();



  }

  class SimpleSynth extends SynthModel2{
    com.norsez.dsp.synth.patch.SimpleVoice voices  [] ;
    VoiceManagerPoly vmgr;

    public Kb2MidiModel kb;
    public ArpSwing arp;
    public SampleClock clock;

    public SimpleSynth (){
      voices = new com.norsez.dsp.synth.patch.SimpleVoice [5];

      for(int i = 0; i < voices.length;i++){
        voices[i] = new com.norsez.dsp.synth.patch.SimpleVoice ();
        voices[i].ampEnv.setSustain(1);
        voices[i].ampEnv.setRelease(.55);
      }

      vmgr = new VoiceManagerPoly (voices.length);
      vmgr.setMode(VoiceManager.Mode.POLY);
      kb = new Kb2MidiModel ();

      arp = new ArpSwing (8);
      clock = new SampleClock ();

      arp.addParametersTo(pmgr,"arp", "arp ");

      pmgr.addParameter(new Parameter ("Volume",.12, "main",ParameterDisplay.dB));
    }

    public void tick (){

      arp.updateParameterValue(pmgr,"arp");

      clock.tick();
      arp.tick(clock.getLastValue());

      for(int i=0; i < voices.length;i++){
        vmgr.setStealable(i,true);
      }
      vmgr.tick(arp.getGateValue(),arp.getRawNote());

      lastValueL = 0;
      for(int i=0; i < voices.length;i++){
        voices[i].ampEnv.tick(vmgr.getGate(i));
        voices[i].setCps( Interpolation.linear(Table.T_NOTE2CPS, vmgr.getRawNote(i)));
        voices[i].tick();

        lastValueL += voices[i].getLastValueL();

      }
      lastValueL *= .25;
      lastValueR= lastValueL ;


    }
  }
}



