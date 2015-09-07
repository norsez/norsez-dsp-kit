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
 * <p>Title: SynthTester</p>
 * <p>Description: This class provides quick test to your SynthModel. Just make an instance of this class with your
 *        SynthModel. You will be to test run the SynthModel with its parameter editor. </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class MainSynthApp
    extends JFrame {

  private SynthModel synth;
  private Player16Bit player;
  private SynthGroupedPanel panel;

  public MainSynthApp(SynthModel s) {

    this.synth = s;
    final String filename = "testsave.ini";
    synth.loadSettingsToFile(filename);
    player = new Player16Bit(DSPSystem.getSamplingRate(), synth);
    player.setBufferSize(8192);
    panel = new SynthGroupedPanel(synth);
    player.start();
    player.setActive(true);
    //JPanel editor = new JPanel (new GridLayout(2,1));
    JPanel editor = new JPanel(new BorderLayout());
    editor.add(panel);
    //editor.add(((Test)synth).getArp().getEditorPanelA());

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent w) {
        player.setActive(false);

        player = null;
        synth.saveSettingsToFile(filename);
        //DSPSystem.DEBUG_OUT.close();
      }
    });

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.getContentPane().add(editor);
    this.setSize(500, 500);
    this.setLocation(200, 100);
    this.setVisible(true);

  }

  public static void main(String[] args) {

    java.util.Locale.setDefault(new java.util.Locale("En","US"));

    //SynthTester testModel1 = new SynthTester (new Test());
    MainSynthApp testModel1 = new MainSynthApp(new Test());
    //DSPSystem.DEBUG_OUT.println(DSPSystem.getDSPSystemInfo());
    //Runtime rt = Runtime.getRuntime();
    //System.out.println ( "Memory used = " + (rt.totalMemory() - rt.freeMemory()) * 0.001 + " kb");
  }

  public static class Test3
      extends SynthModel {

    public ArpSwing arp;
    public SampleClock clock;
    OscA osc [];
    EnvelopeADSR ampenv [];

    public boolean isInUse() {
      //return arp.isActive();
      return true;
    }

    ParameterManager pmgr = this.getParameterManager();


    public Test3() {

      final int VOICE_COUNT  = 2;
      osc = new OscA [ VOICE_COUNT ];
      ampenv = new EnvelopeADSR [VOICE_COUNT];


      clock = new SampleClock();
      arp = new ArpSwing(8);
      arp.setTempo(100);
      arp.setActive(true);

      arp.setGateLength(.86);
      arp.setSwing(0);



      pmgr.clearList();

      String _arp = "Arpeggiator";
      pmgr.addParameter(new Parameter("Arp Play", 0, _arp,
                                      ParameterDisplay.ON_OFF));
      pmgr.addParameter(new Parameter("Arp Tempo", .4, _arp));
      pmgr.addParameter(new Parameter("Arp Swing", 0, _arp));
      pmgr.addParameter(new Parameter("Arp Gate", .4, _arp));
      pmgr.addParameter(new Parameter("Arp Note", Note.getAllNotes(), _arp));

      pmgr.addParameter(new Parameter("Vol", 0.25, _arp, ParameterDisplay.dB));
      pmgr.addParameter(new Parameter("amp A", 0.1, _arp,
                                      ParameterDisplay.ENV_TIME));
      pmgr.addParameter(new Parameter("amp D", 0.5, _arp,
                                      ParameterDisplay.ENV_TIME));
      pmgr.addParameter(new Parameter("amp S", 0.5, _arp, ParameterDisplay.dB));
      pmgr.addParameter(new Parameter("amp R", 0.45, _arp,
                                      ParameterDisplay.ENV_TIME));

    }

    final int CONTROL_MARK = 100;
    final int UPDATE_MARK = 1000;
    int control_count, update_count;

    public void tick() {

      if (update_count >= UPDATE_MARK) {
        update_count = 0;

        arp.setTempo(pmgr.getParameterValue("Arp Tempo"));
        arp.setSwing(pmgr.getParameterValue("Arp Swing"));
        arp.setGateLength(pmgr.getParameterValue("Arp Gate"));
        arp.setNote( (Note) pmgr.getParameterObject("Arp Note"));
        arp.setActive(pmgr.getParameterValue("Arp Play"));

        for (int i = 0; i < vmgr.getActiveVoiceCount(); i++) {
          ( (SimpleVoice) voices[i]).ampEnv.setAttack(pmgr.getParameterValue(
              "amp A"));
          ( (SimpleVoice) voices[i]).ampEnv.setDecay(pmgr.getParameterValue(
              "amp D"));
          ( (SimpleVoice) voices[i]).ampEnv.setSustain(pmgr.getParameterValue(
              "amp S"));
          ( (SimpleVoice) voices[i]).ampEnv.setRelease(pmgr.getParameterValue(
              "amp R"));
        }

      }
      update_count++;

      if (control_count >= CONTROL_MARK) {
        control_count = 0;
      }
      control_count++;

      clock.tick();
      arp.tick(clock.getLastValue());

      for (int i = 0; i < voices.length; i++) {
        vmgr.setStealable(i, voices[i].isStealable());

      }
      vmgr.tick(arp.getLastValue(), arp.getRawNote());

      lastValueL = lastValueR = 0;

      for (int i = 0; i < vmgr.getActiveVoiceCount(); i++) {
        ( (SimpleVoice) voices[i]).ampEnv.tick(vmgr.getGate(i));
        voices[i].setCps(Interpolation.linear(Table.T_NOTE2CPS,
                                              vmgr.getRawNote(i)));
        voices[i].tick();
        lastValueL += voices[i].getLastValueL() *
            pmgr.getParameterValue("Vol");
        lastValueR = lastValueL;

      }
      /*
             if (clock.getLastValue() % 20000 == 0){
        DSPSystem.DEBUG_OUT.println("lastValueL = "
                                    + lastValueL
                                    + "\nvmgr" + vmgr
                                    );
        for(int i=0; i < voices.length;i++)
          DSPSystem.DEBUG_OUT.println( "\namp " + i +" " +
                                       ((SimpleVoice) voices[i]).ampEnv
              );
             }
       */
    }
  }

  public static class SimpleVoice
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

  public static class Test2
      extends SynthModel {
    private boolean active;

    public boolean isInUse() {
      return true;
    }

    private RingModulator rmod;
    private com.norsez.dsp.block.oscillator.AntiAliasedWavetable osc1;
    private SyncableOversampledOsc osc2;
    private LFO lfo;

    public Test2() {

      this.getParameterManager().addParameter(new Parameter("Play", 0, "Main"));
      this.getParameterManager().addParameter(new Parameter("Volume", 0.25,
          "Main"));

      osc1 = new AntiAliasedWavetable();
      osc1.setWavetable(Table.T_BL_SAW);
      osc2 = new SyncableOversampledOsc(1);
      osc2.setWavetable(Table.T_BL_SQUARE);

      rmod = new RingModulator(1);

      lfo = new LFO();
      lfo.setShape(LFO.Shape.TRIANGLE);

      this.getParameterManager().addParameter(new Parameter("Osc1 Pitch", 0.5,
          "Osc"));
      this.getParameterManager().addParameter(new Parameter("Osc1 Drive", 1,
          "Osc"));

      this.getParameterManager().addParameter(new Parameter("Osc2 Pitch", 0.25,
          "Osc"));
      this.getParameterManager().addParameter(new Parameter("Osc2 Drive", 1,
          "Osc"));
      this.getParameterManager().addParameter(new Parameter("Osc2 Sync", 0,
          "Osc"));

      this.getParameterManager().addParameter(new Parameter("LFO Pitch Rate",
          0., "Osc"));
      this.getParameterManager().addParameter(new Parameter("LFO Pitch Amount",
          0., "Osc"));

    }

    private double temp;
    private final int UPDATE_MARK = 100;
    private final int SLOW_MARK = (int) (0.5 * DSPSystem.getSamplingRate());
    private int update_count, slow_count;

    public void tick() {

      if (slow_count >= SLOW_MARK) {
        slow_count = 0;

        temp = this.getParameterManager().getParameterValue("Play");
        if (temp == 0) {
          active = false;
        }
        else {
          active = true;

        }
        osc1.setDrive(Interpolation.none(Table.T_EXP_UP,
                                         getParameterManager().
                                         getParameterValue("Osc1 Drive")));
        osc2.setDrive(Interpolation.none(Table.T_EXP_UP,
                                         getParameterManager().
                                         getParameterValue("Osc2 Drive")));
        osc2.setIsSynced(this.getParameterManager().getParameterValue(
            "Osc2 Sync"));

      }

      slow_count++;

      if (update_count >= UPDATE_MARK) {
        update_count = 0;

        lfo.tick(UPDATE_MARK);

        osc1.setCps(Interpolation.linear(Table.T_NOTE2CPS,
                                         getParameterManager().
                                         getParameterValue("Osc1 Pitch")));
        osc2.setCps(Interpolation.linear(Table.T_NOTE2CPS,
                                         getParameterManager().
                                         getParameterValue("Osc2 Pitch")
                                         +
                                         lfo.getLastValue() *
                                         getParameterManager().
                                         getParameterValue("LFO Pitch Amount")

                                         ));
        temp = Interpolation.none(Table.T_EXP_UP,
                                  getParameterManager().
                                  getParameterValue("LFO Pitch Rate"));
        temp *= 5.0 / DSPSystem.getSamplingRate();
        lfo.setRate(temp);

      }

      update_count++;

      osc1.tick();
      osc2.tick();
      //rmod.tick(osc1.getLastValue(),osc2.getLastValue());
      osc2.setSync(osc1.getPhase());
      lastValueL = osc2.getLastValue() *
          getParameterManager().getParameterValue("Volume");
      lastValueR = lastValueL;

    }

    public void tick(double[] l, double[] r) {

      DSPSystem.unsupported("use tick () instead");

    }

    public void tick(double l, double r) {
      DSPSystem.unsupported("use tick () instead");
    }
  }

  /**
   *
   *
   *
   *
   *
   *
   */
  public static class Test
      extends SynthModel {

    ArpSwing arp;
    SampleClock clock;
    SmoothingFilter sm;
    FatMoogFilter mgf;
    AntiAliasedWavetable osc;
    OSWavetable oswav;
    Noise noise;
    EnvelopeADSR envAD, env2;
    DelayTempoStereo delay;
    LFO lfo, lfo2;
    Chorus chorus;
    Phaser phaser;
    EQLowShelf loweq;
    Waveshaper ws;
    AGC wsAGC;
    OscA osc2;

    static int CONTROL_MARK = 10;
    static int SLOW_MARK = (int) (0.25 * DSPSystem.getSamplingRate());
    int control_count, slow_count;

    //scale the final output by the number of audible signal generators.
    private final double vScale = 1.0;
        /// 2;

    public void tick(double l, double r) {}

    public boolean isInUse() {
      //return arp.isActive();
      return true;
    }

    public Test() {
      clock = new SampleClock();

      //arp.loadSettingsFromFile("arp.ini");
      this.getParameterManager().clearList();

      arp = new ArpSwing(8);
      arp.setTempo(0);

      String _arp = " 0 ARP";
      this.getParameterManager().addParameter(new Parameter("ARP Play", .5,
          _arp, ParameterDisplay.ON_OFF));
      this.getParameterManager().addParameter(new Parameter("ARP Tempo", .5,
          _arp, ParameterDisplay.BPM));
      this.getParameterManager().addParameter(new Parameter("ARP Resolution",
          Note.getAllNotes(), _arp));
      this.getParameterManager().addParameter(new Parameter("ARP Swing", .5,
          _arp));
      this.getParameterManager().addParameter(new Parameter("ARP Gate", .5,
          _arp));
      this.getParameterManager().addParameter(new Parameter("ARP Range", 0,
          _arp, arp.RANGE));
      this.getParameterManager().addParameter(new Parameter("ARP Transpose",
          0.5, _arp, arp.TRANSPOSE));
      this.getParameterManager().addParameter(new Parameter("ARP Active Steps",
          0.5, _arp));

      for (int i = 0; i < arp.getTotalSteps(); i++) {
        this.getParameterManager().addParameter(new Parameter("ARP Step " + i,
            .6, _arp, ParameterDisplay.NOTENAME));
      }

      String _mainGroup = "0 Main";
      this.getParameterManager().addParameter(new Parameter("Volume", 0.05,
          _mainGroup, ParameterDisplay.dB));

      noise = new Noise();
      this.getParameterManager().addParameter(new Parameter("Noise Volume", 0.0,
          _mainGroup, ParameterDisplay.dB));

      //oswav = new OSWavetable (128);
      //oswav.setWavetable(Table.T_BL_SAW[488]);

      osc = new com.norsez.dsp.block.oscillator.AntiAliasedWavetable();
      osc.setWavetable(Table.T_BL_SAW);

      this.getParameterManager().addParameter(new Parameter("Osc Level", 1,
          _mainGroup, ParameterDisplay.dB));
      this.getParameterManager().addParameter(new Parameter("Osc Semi", .5,
          _mainGroup, ParameterDisplay.SEMI));
      this.getParameterManager().addParameter(new Parameter("Osc Fine", .5,
          _mainGroup));
      this.getParameterManager().addParameter(new Parameter("Osc Drive", 1,
          _mainGroup));

      osc2 = new OscA();

      this.getParameterManager().addParameter(new Parameter("Osc2 Level", 1,
          _mainGroup, ParameterDisplay.dB));
      this.getParameterManager().addParameter(new Parameter("Osc2 PW", 0.5,
          _mainGroup, ParameterDisplay.LINEAR_BIPOLAR));
      this.getParameterManager().addParameter(new Parameter("Osc2 Digi", 0,
          _mainGroup, ParameterDisplay.DIGIWAVE));
      this.getParameterManager().addParameter(new Parameter("Osc2 Digi LFO2",
          0, _mainGroup));
      this.getParameterManager().addParameter(new Parameter("Osc2 Digix2 Wave",
          0, _mainGroup, ParameterDisplay.DIGIMORPHWAVE));
      this.getParameterManager().addParameter(new Parameter("Osc2 Digix2 Amt",
          0, _mainGroup));
      this.getParameterManager().addParameter(new Parameter("Osc2 Digix2 LFO2",
          0, _mainGroup));
      this.getParameterManager().addParameter(new Parameter("Osc2 Osc Mode",
          osc2.getAllModes(), _mainGroup));
      this.getParameterManager().addParameter(new Parameter("Osc2 Supersaw Mix", 0.,
          _mainGroup, ParameterDisplay.dB));
      this.getParameterManager().addParameter(new Parameter("Osc2 Supersaw Detune", 0.,
          _mainGroup));

      this.getParameterManager().addParameter(new Parameter("Osc2 Pitch", 0.25,
          _mainGroup, ParameterDisplay.NOTE_FREQUENCY));
      this.getParameterManager().addParameter(new Parameter("Osc2 Drive", 1,
          _mainGroup));
      this.getParameterManager().addParameter(new Parameter("Osc2 Sync", 0,
          _mainGroup, ParameterDisplay.ON_OFF));

      lfo2 = new LFO();
      this.getParameterManager().addParameter(new Parameter("LFO O2 Pitch Rate",
          0., _mainGroup, ParameterDisplay.LFO_RATE));
      this.getParameterManager().addParameter(new Parameter(
          "LFO O2 Pitch Amount", 0., _mainGroup));

      loweq = new EQLowShelf(Effect.Channel.MONO);
      loweq.setFrequency(470.0 / DSPSystem.getSamplingRate());
      this.getParameterManager().addParameter(new Parameter("Low Gain", 0,
          _mainGroup));

      String _filter = "1 Filter";
      mgf = new FatMoogFilter();
      this.getParameterManager().addParameter(new Parameter("Cutoff", 1,
          _filter, ParameterDisplay.CUTOFF));
      this.getParameterManager().addParameter(new Parameter("Resonance", 0,
          _filter));
      this.getParameterManager().addParameter(new Parameter("Fat", 0,
          _filter));
      this.getParameterManager().addParameter(new Parameter("Gain", 1/3.,
          _filter));
      Object[] modes = mgf.getSupportedModes();
      this.getParameterManager().addParameter(new Parameter("Filter Mode",
          modes, _filter));

      envAD = new EnvelopeADSR();
      this.getParameterManager().addParameter(new Parameter("Amp Attack", 0.0,
          _mainGroup, ParameterDisplay.ENV_TIME));
      this.getParameterManager().addParameter(new Parameter("Amp Decay", 0.5,
          _mainGroup, ParameterDisplay.ENV_TIME));
      this.getParameterManager().addParameter(new Parameter("Amp Sustain", 0.5,
          _mainGroup, ParameterDisplay.dB));
      this.getParameterManager().addParameter(new Parameter("Amp Release", 0.1,
          _mainGroup, ParameterDisplay.ENV_TIME));

      env2 = new EnvelopeADSR();
      this.getParameterManager().addParameter(new Parameter("Filter Attack",
          0.0, _filter, ParameterDisplay.ENV_TIME));
      this.getParameterManager().addParameter(new Parameter("Filter Decay", 0.5,
          _filter, ParameterDisplay.ENV_TIME));
      this.getParameterManager().addParameter(new Parameter("Filter Sustain",
          0.5, _filter, ParameterDisplay.ENV_TIME));
      this.getParameterManager().addParameter(new Parameter("Filter Release",
          0.1, _filter, ParameterDisplay.ENV_TIME));

      this.getParameterManager().addParameter(new Parameter("Filter Env", 0.5,
          _filter));

      sm = new SmoothingFilter();
      sm.setCutoff(800.0 / DSPSystem.getSamplingRate());

      String _delay = "EFX Delay";
      delay = new DelayTempoStereo();

      this.getParameterManager().addParameter(new Parameter("Delay L Tempo",
          Note.getAllNotes(), _delay));
      this.getParameterManager().addParameter(new Parameter("Delay L Feedback",
          0.0, _delay));
      this.getParameterManager().addParameter(new Parameter("Delay L CrossFeed",
          0., _delay));
      this.getParameterManager().addParameter(new Parameter("Delay L Swing",
          0.5, _delay));
      this.getParameterManager().addParameter(new Parameter("Delay LP", 0.9,
          _delay, ParameterDisplay.CUTOFF));
      this.getParameterManager().addParameter(new Parameter("Delay HP", 0.24,
          _delay, ParameterDisplay.CUTOFF));
      this.getParameterManager().addParameter(new Parameter("Delay L Level",
          0.7, _delay, ParameterDisplay.dB));
      this.getParameterManager().addParameter(new Parameter("Delay R Tempo",
          Note.getAllNotes(), _delay));
      this.getParameterManager().addParameter(new Parameter("Delay R Feedback",
          0.0, _delay));
      this.getParameterManager().addParameter(new Parameter("Delay R CrossFeed",
          0., _delay));
      this.getParameterManager().addParameter(new Parameter("Delay R Swing",
          0.5, _delay));
      this.getParameterManager().addParameter(new Parameter("Delay R Level",
          0.7, _delay, ParameterDisplay.dB));
      this.getParameterManager().addParameter(new Parameter(
          "Delay Master Level", 0., _delay, ParameterDisplay.dB));
      this.getParameterManager().addParameter(new Parameter("Delay Input Level",
          1., _delay, ParameterDisplay.dB));

      lfo = new LFO();
      this.getParameterManager().addParameter(new Parameter("Lfo Shape",
          LFO.Shape.ALL_SHAPES, _filter));
      this.getParameterManager().addParameter(new Parameter("Lfo Tempo",
          Note.getAllNotes(), _filter));
      this.getParameterManager().addParameter(new Parameter("Lfo Cutoff Amt",
          0.5, _filter));

      chorus = new Chorus(6);
      String _chorus = "EFX Chorus";
      this.getParameterManager().addParameter(new Parameter("Chorus Depth",
          0.45, _chorus));
      this.getParameterManager().addParameter(new Parameter("Chorus Mod", 0.35,
          _chorus));
      this.getParameterManager().addParameter(new Parameter("Chorus Speed", 0.5,
          _chorus));
      this.getParameterManager().addParameter(new Parameter("Chorus Feedback",
          0.125, _chorus));
      this.getParameterManager().addParameter(new Parameter("Chorus Spread", 0,
          _chorus));
      this.getParameterManager().addParameter(new Parameter("Chorus Thickness",
          1, _chorus));
      this.getParameterManager().addParameter(new Parameter("Chorus Level", 0.0,
          _chorus, ParameterDisplay.dB));
      this.getParameterManager().addParameter(new Parameter(
          "Chorus Input Level", 1.0, _chorus, ParameterDisplay.dB));

      phaser = new Phaser(6);
      String _phaser = "EFX Phaser";
      this.getParameterManager().addParameter(new Parameter("Phaser Split", 0.5,
          _phaser));
      this.getParameterManager().addParameter(new Parameter("Phaser Width",
          0.45, _phaser));
      this.getParameterManager().addParameter(new Parameter("Phaser Frequency",
          0.1, _phaser, ParameterDisplay.CUTOFF));
      this.getParameterManager().addParameter(new Parameter("Phaser Tempo",
          Note.getAllNotes(), _phaser));
      this.getParameterManager().addParameter(new Parameter("Phaser Feedback",
          0.0, _phaser));

      this.getParameterManager().addParameter(new Parameter("Phaser Poles", 1,
          _phaser));
      this.getParameterManager().addParameter(new Parameter("Phaser Level", 0.0,
          _phaser, ParameterDisplay.dB));
      this.getParameterManager().addParameter(new Parameter(
          "Phaser Input Level", 1.0, _phaser, ParameterDisplay.dB));

      ws = new WaveshaperOversampling(Effect.Channel.MONO, 2);
      //ws = new Waveshaper ( Effect.Channel.MONO);
      ws.setTable(Table.T_SINE);
      ws.setInputLevel(1.0);
      String _ws = "EFX Wave Shaper";
      this.getParameterManager().addParameter(new Parameter("Shaper Drive", 0.0,
          _ws, ParameterDisplay.dB));
      this.getParameterManager().addParameter(new Parameter("Shaper Level", 1.0,
          _ws, ParameterDisplay.dB));

      wsAGC = new AGC();
      wsAGC.setCutoff( (0.00001) / DSPSystem.getSamplingRate());
      wsAGC.setMaxAmp(0.95);
      wsAGC.setMinAmp(.00);

    }

    public void tick() {

    }

    public void saveSettingsToFile(String filename) {
      super.saveSettingsToFile(filename);
      //arp.saveSettingsToFile("arp.ini");
    }

    ArpSwing getArp() {
      return arp;
    }

    double last, temp;
    public double getLastValueL() {

      if (slow_count >= SLOW_MARK) {
        slow_count = 0;
        arp.setActive(parameterManager.getParameterValue("ARP Play"));
        arp.setSwing(parameterManager.getParameterValue("ARP Swing"));
        arp.setTranspose(parameterManager.getParameterValue("ARP Transpose"));
        arp.setRange(parameterManager.getParameterValue("ARP Range"));
        arp.setTempo(parameterManager.getParameterValue("ARP Tempo"));
        arp.setGateLength(parameterManager.getParameterValue("ARP Gate"));
        arp.setActiveSteps(parameterManager.getParameterValue(
            "ARP Active Steps"));
        arp.setNote( (Note) parameterManager.getParameterObject(
            "ARP Resolution"));
        for (int i = 0; i < arp.getTotalSteps(); i++) {
          arp.setNoteAt(i, parameterManager.getParameterValue("ARP Step " + i));
        }

        osc.setDrive(Interpolation.none(Table.T_EXP_UP,
                                        parameterManager.
                                        getParameterValue("Osc Drive")));

        osc2.setOscMode( (OscMode) parameterManager.getParameterObject(
            "Osc2 Osc Mode"));
        osc2.setIsSynced(this.getParameterManager().getParameterValue(
            "Osc2 Sync"));
        osc2.setPWM(this.getParameterManager().getParameterValue("Osc2 PW"));
       // osc2.setDigiWave(this.getParameterManager().getParameterValue(
       //     "Osc2 Digi"));
        osc2.setDrive(Interpolation.none(Table.T_EXP_UP,
                                         parameterManager.
                                         getParameterValue("Osc2 Drive")));

        osc2.setDigix2Wave(this.getParameterManager().getParameterValue(
            "Osc2 Digix2 Wave"));

        osc2.setSuperSawDetune(this.getParameterManager().getParameterValue(
            "Osc2 Supersaw Detune"));

        osc2.setSuperSawMix(this.getParameterManager().getParameterValue(
            "Osc2 Supersaw Mix"));



        temp = Interpolation.none(Table.T_LFO_RATE,
                                  getParameterManager().
                                  getParameterValue("LFO O2 Pitch Rate"));
        lfo2.setRate(temp);

        loweq.setGain(Interpolation.none(Table.T_EXP_UP,
                                         parameterManager.
                                         getParameterValue("Low Gain")));

        envAD.setAttack(parameterManager.getParameterValue("Amp Attack"));
        envAD.setDecay(parameterManager.getParameterValue("Amp Decay"));
        envAD.setSustain(parameterManager.getParameterValue("Amp Sustain"));
        envAD.setRelease(parameterManager.getParameterValue("Amp Release"));

        env2.setAttack(parameterManager.getParameterValue("Filter Attack"));
        env2.setDecay(parameterManager.getParameterValue("Filter Decay"));
        env2.setRelease(parameterManager.getParameterValue("Filter Release"));
        env2.setSustain(parameterManager.getParameterValue("Filter Sustain"));

        mgf.setMode( (Filter.Mode) parameterManager.getParameterObject(
            "Filter Mode"));
        mgf.setResonance(parameterManager.getParameterValue("Resonance"));
        mgf.setOutGain(parameterManager.getParameterValue("Gain"));
        mgf.setFat(parameterManager.getParameterValue("Fat"));

        delay.setInputLevel(Interpolation.none(Table.T_EXP_UP,
                                               parameterManager.
                                               getParameterValue("Delay Input Level")));
        delay.setOutputLevel(Interpolation.none(Table.T_EXP_UP,
                                                parameterManager.
                                                getParameterValue(
            "Delay Master Level")));
        //delay.setPan(0,parameterManager.getParameterValue("Delay L Pan"));
        delay.setDelayByTempo(0, arp.getTempo(),
                              (Note) parameterManager.
                              getParameterObject("Delay L Tempo"));
        delay.setFeedback(0,
                          Interpolation.none(Table.T_EXP_UP,
                                             parameterManager.
                                             getParameterValue("Delay L Feedback")));
        delay.setCrossfeed(0,
                           Interpolation.none(Table.T_EXP_UP,
                                              parameterManager.
                                              getParameterValue("Delay L Crossfeed")));
        delay.setLevel(0,
                       Interpolation.none(Table.T_EXP_UP,
                                          parameterManager.getParameterValue("Delay L Level")));
        delay.setSwing(0,
                       Interpolation.none(Table.T_EXP_UP,
                                          parameterManager.getParameterValue("Delay L Swing")));
        delay.setLpCutoff(0,
                          Interpolation.none(Table.T_EXP_UP,
                                             0.5 *
                                             parameterManager.getParameterValue("Delay LP")));
        delay.setHpCutoff(0,
                          Interpolation.none(Table.T_CUTOFF,
                                             parameterManager.
                                             getParameterValue("Delay HP")));

        delay.setDelayByTempo(1, arp.getTempo(),
                              (Note) parameterManager.
                              getParameterObject("Delay R Tempo"));
        delay.setFeedback(1,
                          Interpolation.none(Table.T_EXP_UP,
                                             parameterManager.
                                             getParameterValue("Delay R Feedback")));
        delay.setSwing(1,
                       Interpolation.none(Table.T_EXP_UP,
                                          parameterManager.getParameterValue("Delay R Swing")));
        delay.setLpCutoff(1,
                          Interpolation.none(Table.T_CUTOFF,
                                             parameterManager.
                                             getParameterValue("Delay LP")));
        delay.setHpCutoff(1,
                          Interpolation.none(Table.T_CUTOFF,
                                             parameterManager.
                                             getParameterValue("Delay HP")));
        delay.setCrossfeed(1,
                           Interpolation.none(Table.T_EXP_UP,
                                              parameterManager.
                                              getParameterValue("Delay R Crossfeed")));
        delay.setLevel(1,
                       Interpolation.none(Table.T_EXP_UP,
                                          parameterManager.getParameterValue("Delay R Level")));

        lfo.setRateByTempo(arp.getTempo(),
                           (Note) parameterManager.
                           getParameterObject("Lfo Tempo"));
        lfo.setShape( (LFO.Shape) parameterManager.getParameterObject(
            "Lfo Shape"));

        chorus.setDepth(Interpolation.linear(Table.T_EXP_UP,
                                             parameterManager.
                                             getParameterValue("Chorus Depth")));
        chorus.setPanSpread(Interpolation.linear(Table.T_EXP_UP,
                                                 parameterManager.
                                                 getParameterValue(
            "Chorus Spread")));
        chorus.setModulation(Interpolation.linear(Table.T_EXP_UP,
                                                  parameterManager.
                                                  getParameterValue(
            "Chorus Mod")));
        chorus.setRate(Interpolation.linear(Table.T_EXP_UP,
                                            parameterManager.
                                            getParameterValue("Chorus Speed")));
        chorus.setOutputLevel(parameterManager.getParameterValue("Chorus Level"));
        chorus.setInputLevel(parameterManager.getParameterValue(
            "Chorus Input Level"));
        chorus.setActiveLines(parameterManager.getParameterValue(
            "Chorus Thickness"));
        chorus.setFeedback(Interpolation.none(Table.T_EXP_UP,
                                              parameterManager.
                                              getParameterValue("Chorus Feedback")));

        temp = parameterManager.getParameterValue("Phaser Level");
        phaser.setOutputLevel(Interpolation.none(Table.T_EXP_UP, temp));
        phaser.setInputLevel(Interpolation.none(Table.T_EXP_UP,
                                                parameterManager.
                                                getParameterValue(
            "Phaser Input Level")));
        phaser.setFeedback(parameterManager.getParameterValue("Phaser Feedback"));
        phaser.setFrequency(Interpolation.linear(Table.T_CUTOFF,
                                                 parameterManager.
                                                 getParameterValue(
            "Phaser Frequency")));
        phaser.setWidth(parameterManager.getParameterValue("Phaser Width"));
        phaser.setSplit(parameterManager.getParameterValue("Phaser Split"));
        phaser.setActivePoles(parameterManager.getParameterValue("Phaser Poles"));
        phaser.setRateByTempo(arp.getTempo(),
                              (Note) parameterManager.
                              getParameterObject("Phaser Tempo"));

        ws.setDrive(Interpolation.linear(Table.T_EXP_UP,
                                         parameterManager.
                                         getParameterValue("Shaper Drive")));
        temp = Interpolation.none(Table.T_EXP_UP,
                                  parameterManager.
                                  getParameterValue("Shaper Level"));
        ws.setOutputLevel(temp);

      }

      control_count++;
      slow_count++;



      if (control_count >= CONTROL_MARK) {
        control_count = 0;
        lfo.tick(CONTROL_MARK);
        lfo2.tick(CONTROL_MARK);

        temp = arp.getRawNote()
            +
            MultiModeOsc.getSemiRawTune(getParameterManager().
                                        getParameterValue("Osc Semi")
                                        )
            +
            MultiModeOsc.getFineRawTune(getParameterManager().
                                        getParameterValue("Osc Fine")
                                        );
        osc.setCps(Interpolation.linear(Table.T_NOTE2CPS,
                                        temp)
                   );
        //osc.setDrive(temp);

        temp = arp.getRawNote() +
            lfo2.getLastValue() *
            getParameterManager().getParameterValue("LFO O2 Pitch Amount");
        osc2.setCps(Interpolation.linear(Table.T_NOTE2CPS,
                                         temp));

        //osc2.setDrive(Interpolation.linear(Table.T_DRIVE,temp));

        osc2.setDigix2Amount(this.getParameterManager().getParameterValue(
            "Osc2 Digix2 Amt")
                             +
                             lfo2.getLastValue() *
                             this.getParameterManager().getParameterValue("Osc2 Digix2 LFO2"));

          temp = this.getParameterManager().getParameterValue("Osc2 Digi")
              +
              lfo2.getLastValue() *
              this.getParameterManager().getParameterValue("Osc2 Digi LFO2");

          osc2.setDigiWave(temp);



      }

      if (!arp.isActive()) {
        lastValueL = lastValueR = 0;
        return 0;
      }


      temp = lfo.getLastValue();

      mgf.setCutoff(Interpolation.linear(Table.T_CUTOFF,
                                         sm.tick(parameterManager.
                                                 getParameterValue("Cutoff"))
                                         +
                                         env2.getLastValue() *
                                         parameterManager.
                                         getParameterValue("Filter Env")
                                         //+ Interpolation.mapToRangeLinear(0,-1,1,1, lfo.getLastValue() )
                                         +
                                         lfo.getLastValue() *
                                         parameterManager.
                                         getParameterValue("Lfo Cutoff Amt")
                                         ));
      clock.tick();
      arp.tick(clock.getLastValue());
      env2.tick(arp.getLastValue());

      osc.tick();

      osc2.tick();

      osc2.setSync(osc.getPhase());

      last = osc.getLastValue() *
          parameterManager.getParameterValue("Osc Level")
          +
          osc2.getLastValue() * parameterManager.getParameterValue("Osc2 Level");

      last += noise.tick() *
          Interpolation.none(Table.T_EXP_UP,
                             parameterManager.getParameterValue("Noise Volume"));
      last = mgf.tick(last * 0.3) * 1.3;
      last *= envAD.tick(arp.getLastValue());
      loweq.tick(last, 0);
      last = loweq.getLastValueL();
      ws.tick(last, 0);
      last = ws.getLastValueL();
      //wsAGC.tick(last);
      //last = wsAGC.getLastValue();

      chorus.tick(last, last);
      phaser.tick(last, last);

      last = 0.5 * ( chorus.getLastValueL() + last * chorus.getDryMixFactor() + phaser.getLastValueL()
                     + last * phaser.getDryMixFactor());

      delay.tick( last,last );

      lastValueL = last * delay.getDryMixFactor()  + delay.getLastValueL();

      return lastValueL *
          Interpolation.none(Table.T_EXP_UP,
                             parameterManager.getParameterValue("Volume")) *
          vScale;
    }

    public double getLastValueR() {

      lastValueR = last *delay.getDryMixFactor() +
          delay.getLastValueR() ;

      return lastValueR *
          Interpolation.none(Table.T_EXP_UP,
                             parameterManager.getParameterValue("Volume")) *
          vScale;
    }

    public void tick(double[] L, double[] r) {

    }

  }
}
