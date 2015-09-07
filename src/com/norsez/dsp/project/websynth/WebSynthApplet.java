package com.norsez.dsp.project.websynth;

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

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class WebSynthApplet extends JApplet {
  private boolean isStandalone = false;
  private String model;
  private SynthModel2 s1;

  //Get a parameter value
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  //Construct the applet
  public WebSynthApplet() {


    //this.getContentPane().add(s1.getEditorPanelA());

  }
  //Initialize the applet
  public void init() {
    try {


      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void start (){
    s1.startPlayThread(true);
  }

  public void stop (){
    s1.startPlayThread(false);
  }

  public void destroy  (){
    s1.destroyPlayer();
  }

  //Component initialization
  private void jbInit() throws Exception {

    model = this.getParameter("model", "TestTone");
    if (model.equalsIgnoreCase("Alias"))
      s1 = new AliasDemo ();
    else
    if (model.equalsIgnoreCase("RingSync"))
      s1 = new RingSync ();
    else
      s1 = new TestTone ();


    this.getContentPane().add(s1.getEditorPanelA(), BorderLayout.CENTER);

    this.setSize(new Dimension(400,300));
  }
  //Get Applet information
  public String getAppletInfo() {
    return "WebSynth Applet by Norsez Orankijanan"
        +"\nCopyright 2003"
        +"\nDemostrates some sounds for synth beginner.";
  }
  //Get parameter info
  public String[][] getParameterInfo() {
    String[][] pinfo =
      {
      {"model", "String", ""},
      };
    return pinfo;

  }

  //static initializer for setting look & feel
  static {
    try {
      //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    catch(Exception e) {
    }
  }
}

/**
 *
 * <p>Title: DemoSynth</p>
 * <p>Description: Template for synths that WebSynthApplet will display.
 *                 Just keep on inherit from this template. But don't forget
 *                 to tick the clock and arp if you use them.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */
class DemoSynth extends SynthModel2{
  protected ArpSwing arp;
  protected SampleClock clock;
  protected final int MOD_MARK = 100;
  protected final int UPDATE_MARK = 1000;
  protected int modCount, updateCount;

  public DemoSynth (){
    clock = new SampleClock ();
    arp = new ArpSwing (8);




  }

  public void tick (){
    clock.tick();
    arp.tick(clock.getLastValue());
  }
}


/**
 *
 * <p>Title: RingSync </p>
 * <p>Description: Demonstate the sound of Oscillator Ring and Sync.
 *                 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */
class RingSync extends DemoSynth {
  private OscA osc1,osc2;
  private LFO lfo1;
  private RingModulator ring;
  private final String _arp = "1 Arp";

  private SmoothingFilter lp;

  public RingSync (){
    osc1 = new OscA ();
    osc1.setCps ( 440.0/DSPSystem.getSamplingRate());
    osc2 = new OscA ();
    osc2.setIsSynced(1);
    String _osc = "Main";

    lp = new SmoothingFilter ();
    lp.setCutoff(DSPSystem.NYQUIST_CPS);



    Object [] modes = {osc1.SHAPE_SAW,osc1.SHAPE_PULSE,osc1.SHAPE_TRIANGLE,osc1.SHAPE_SINE};
    pmgr.addParameter(new Parameter("O1 Shape",modes,_osc) );
    ///pmgr.addParameter(new Parameter ("O1 Level",0,_osc,ParameterDisplay.dB));
    pmgr.addParameter(new Parameter ("O1 Semi",0.5,_osc,ParameterDisplay.SEMI));

    pmgr.addParameter(new Parameter("O2 Shape",modes,_osc) );
    pmgr.addParameter(new Parameter ("O2 Pulse Width",.5,_osc,ParameterDisplay.LINEAR_BIPOLAR));
    ///pmgr.addParameter(new Parameter ("O2 Level",1.0,_osc,ParameterDisplay.dB));
    pmgr.addParameter(new Parameter ("O2 Semi",0.5,_osc,ParameterDisplay.SEMI));

    lfo1 = new LFO ();
        lfo1.setShape(LFO.Shape.TRIANGLE);
        pmgr.addParameter(new Parameter ("O2 Lfo Pitch Rate",.51,_osc,ParameterDisplay.LFO_RATE));
        pmgr.addParameter(new Parameter ("O2 Lfo Pitch Amount",0,_osc));


    Object [] m = {OMode.RING,OMode.SYNC,OMode.RINGSYNC};
    pmgr.addParameter(new Parameter("O Mode",m,_osc) );
    pmgr.addParameter(new Parameter ("All Transpose",0.5,_osc,ParameterDisplay.SEMI));
    pmgr.addParameter(new Parameter ("Volume",.2,_osc,ParameterDisplay.dB));

    ring = new RingModulator ( 2 );


  }

  static class OMode {
    private final String name;
    private OMode ( String n) { name = n;}
    public String toString (){ return name;}
    public static final OMode RING = new OMode ("Ring");
    public static final OMode SYNC = new OMode ("Sync");
    public static final OMode RINGSYNC = new OMode ("Ring+Sync");
  }

  public void tick (){


    if (updateCount >= UPDATE_MARK){
      updateCount=0;

      osc1.setOscMode((OscMode) pmgr.getParameterObject("O1 Shape"));
      osc2.setOscMode((OscMode) pmgr.getParameterObject("O2 Shape"));
      osc2.setPWM(pmgr.getParameterValue("O2 Pulse Width"));


      lfo1.setRate( Interpolation.linear(Table.T_LFO_RATE,
          pmgr.getParameterValue("O2 Lfo Pitch Rate")));


    }
    updateCount++;

    if (modCount >= MOD_MARK){
      modCount=0;

      lfo1.tick(MOD_MARK);
      double tune = osc1.getSemiRawTune(pmgr.getParameterValue("All Transpose"));
      osc2.setCps(Interpolation.linear(Table.T_NOTE2CPS,
                                       0.4724409 // note #60
                                       + lfo1.getLastValue () * pmgr.getParameterValue("O2 Lfo Pitch Amount") //* .18897
                                       + osc2.getSemiRawTune(pmgr.getParameterValue("O2 Semi"))
                                       + tune
                                       ));
      osc1.setCps(Interpolation.linear(Table.T_NOTE2CPS,
                                       0.4724409 // note #60
                                       + osc1.getSemiRawTune(pmgr.getParameterValue("O1 Semi"))
                                       + tune
                                       ));

    }
    modCount++;

    osc1.tick ();


    if (pmgr.getParameterObject("O Mode") == OMode.RING){
      osc2.tick ();
      ring.tick(osc1.getLastValue(),osc2.getLastValue());
      lastValueL = lastValueR = ring.getLastValue()
          * pmgr.getParameterValue("Volume") * 0.5;
    }
    else
    if (pmgr.getParameterObject("O Mode") == OMode.SYNC){

      osc2.tick();
      osc2.setSync(osc1.getPhase());
      lastValueL = lastValueR = osc2.getLastValue()
          * pmgr.getParameterValue("Volume") * 0.5;
    }
    else
    {

      osc2.tick();
      osc2.setSync(osc1.getPhase());
      ring.tick(osc1.getLastValue(),osc2.getLastValue());
      lastValueL = lastValueR = (osc2.getLastValue() + ring.getLastValue())
          * pmgr.getParameterValue("Volume") * 0.25;

    }

  }

}



/**
 *
 * <p>Title: AliasDemo </p>
 * <p>Description: Demonstates the effects that aliases have on pure waveforms.
 *                 It allows you to adjust the alias content amount to compare.
 *                 The artifacts have more impact on the purity of the sound
 *                 when the pitch of sound is modulated.
 *                 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */
class AliasDemo extends DemoSynth {
  private OscA osc1;
  private LFO lfo1;
  private final String _arp = "1 Arp";

  private SmoothingFilter lp;

  public AliasDemo (){
    osc1 = new OscA ();
    String _osc = "0 Demo";

    lp = new SmoothingFilter ();
    lp.setCutoff(DSPSystem.NYQUIST_CPS);

    Object [] modes = {osc1.SHAPE_SAW,osc1.SHAPE_PULSE,osc1.SHAPE_TRIANGLE,osc1.SHAPE_SINE};
    pmgr.addParameter(new Parameter("Shape",modes,_osc) );
    pmgr.addParameter(new Parameter ("Pulse Width",.5,_osc,ParameterDisplay.LINEAR_BIPOLAR));
    pmgr.addParameter(new Parameter ("Aliases Amount",.5,_osc,ParameterDisplay.LINEAR_BIPOLAR));
    pmgr.addParameter(new Parameter ("Volume",.1,_osc,ParameterDisplay.dB));

    pmgr.setValue("Aliases Amount",0.7);


    lfo1 = new LFO ();
    lfo1.setShape(LFO.Shape.TRIANGLE);
    pmgr.addParameter(new Parameter ("Lfo Pitch Rate",.1,_osc,ParameterDisplay.LFO_RATE));
    pmgr.addParameter(new Parameter ("Lfo Pitch Amount",0,_osc));

    arp.setTempo(.275);
    arp.setNote(Note.ONE_EIGHTH);
    arp.setRange(.75);
    arp.setTranspose(.18);
    arp.setGateLength(.75);
    arp.setActive(true);

    arp.addParametersTo(pmgr,_arp, "ARP ");
    //pmgr.setValue(_arp + " Play",1 );

  }

  public void tick (){


    if (updateCount >= UPDATE_MARK){
      updateCount=0;

      osc1.setOscMode((OscMode) pmgr.getParameterObject("Shape"));
      osc1.setPWM(pmgr.getParameterValue("Pulse Width"));
      osc1.setDrive(
          pmgr.getParameterValue("Aliases Amount") * 2);

      lfo1.setRate( Interpolation.linear(Table.T_LFO_RATE,
          pmgr.getParameterValue("Lfo Pitch Rate")));

      arp.updateParameterValue(pmgr,
                               "ARP ");
    }
    updateCount++;

    if (modCount >= MOD_MARK){
      modCount=0;

      lfo1.tick(MOD_MARK);
      osc1.setCps(Interpolation.linear(Table.T_NOTE2CPS,
                                       arp.getRawNote()
                                       + lfo1.getLastValue () * pmgr.getParameterValue("Lfo Pitch Amount") * .18897
                                       ));
    }
    modCount++;

    clock.tick();
    arp.tick(clock.getLastValue());


    osc1.tick();
    lastValueL = lastValueR = osc1.getLastValue() * lp.tick(arp.getGateValue() )*
       pmgr.getParameterValue("Volume") * 0.5;
  }

}