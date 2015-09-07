package com.norsez.dsp.synth;
import com.norsez.dsp.block.*;
import com.norsez.dsp.player.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.norsez.dsp.synth.swing.*;

/**
 * <p>Title: SynthModel2 </p>
 * <p>Description: The easiest way to implement a synth. This class comes with a built in audio player and
 *              a parameter manager. It also has a JPanel containing all the controls of this SynthModel.<br>
 *              Inherit from this synth. Put your signal generation code in void tick (). In the constructor,
 *              register parameters (that will be on its JPanel editor) by calling pmgr.addParameter ( ). Then
 *              in your main application or applet, just instantiate your model and add its JPanel editor to
 *              your main app or applet.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class SynthModel2 extends SynthModel{

  protected Player16Bit2 player;
  protected ParameterManager pmgr;

  protected boolean isActive;

  /**
   * Main tabbed pane that shows the editor. You can add additional editor to this pane by
   * calling addTab ().
   */
  protected JTabbedPane tpane;

  public JTabbedPane getTabbedPaneOfEditor (){
    return tpane;
  }

  public SynthModel2() {

    pmgr = new ParameterManager ();
    tpane = new JTabbedPane ();
    player = new Player16Bit2 (DSPSystem.getSamplingRate(),this);
    player.startPlayThread();
  }

  /**
   * Since SynthModel2 is designed for JApplet. This is caution in Thread.
   * In an appplication, you can just startPlayThread ( true ) after
   * instantiate a SynthModel2 in the JFrame constructor.<br>
   * However, that is prohibited in a JApplet. First, instantiate the
   * SynthModel2 in the the JApplet constructor. Then call startPlayThread (true)
   * in void start () and call startPlayThread (false) in void stop (). This is
   * the only way to avoid JApplet locking up.
   *
   * @param b start the thread
   */
  public void startPlayThread (boolean b){
    player.setPaused(b);
  }

  /**
   * When using SynthModel2 in JApplet. Must call destroyPlayer
   * in the applet's destroy method to exit gracefully.
   */
  public void destroyPlayer (){
    player.stopPlayThread();
  }

  public ParameterManager getParameterManager (){
    return pmgr;
  }


  public boolean isInUse (){
    return isActive;
  }

  public void setActive (boolean n){
    isActive=n;
  }

  public void setInUse ( boolean n){
    setActive ( n );
  }

  public void tick (){

  }
  public static void main(String[] args) {
    SynthModel2 synthModel21 = new SynthModel2();
    JOptionPane.showMessageDialog(null,synthModel21.getEditorPanelA());
  }


  /**
   *
   * @return a JPanel containing the play button and the editor of all parameters in this SynthModel.
   */
  public JPanel getEditorPanelA (){
    JPanel mainpanel = new JPanel (new BorderLayout ());
    Font myfont = new Font ("Verdana",0,9);

    //play button
    javax.swing.JToggleButton playBtn = new javax.swing.JToggleButton ("<html><font color=green><b>Play</b></font></html>");
    final SynthModel2 synth = this;
    playBtn.addActionListener(new ActionListener (){
      public void actionPerformed (ActionEvent e){
        synth.setActive(((JToggleButton)e.getSource()).isSelected());

        if (((JToggleButton)e.getSource()).isSelected()){
          ((JToggleButton)e.getSource()).setText("<html><font color=red><b>Play</b></font></html>");
          player.setPaused(false);
        }
        else{
          ( (JToggleButton) e.getSource()).setText(
              "<html><font color=green><b>Play</b></font></html>");
          player.setPaused(true);
        }
      }
    });
    mainpanel.add ( playBtn , BorderLayout.SOUTH);



    String[] groups = pmgr.getGroupNames();
    for (int i = 0; i < groups.length; i++) {
      Parameter[] params = pmgr.getParameterOfGroup(
          groups[i]);
      JPanel panel = new JPanel(new GridLayout(params.length, 2));

      for (int j = 0; j < params.length; j++) {
        JLabel l = new JLabel("<html><b>" + params[j].getName() + "</b></html>");
        l.setName(params[j].getName());
        l.setFont(myfont);
        //l.setHorizontalAlignment(JLabel.CENTER);
        panel.add(l);

        if (!params[j].hasValueObject()) {
          N_Slider s = new N_Slider(params[j], l);
          s.setFont(myfont);
          panel.add(s);
          l.setLabelFor(s);

        }
        else {
          N_Box b = new N_Box(params[j].getChoices(), params[j], l);
          b.setFont(myfont);
          panel.add(b);
          l.setLabelFor(b);
        }
      }

      tpane.addTab(groups[i], new JScrollPane(panel));

    }

    mainpanel.add (tpane,BorderLayout.CENTER);

    return mainpanel;
  }
}