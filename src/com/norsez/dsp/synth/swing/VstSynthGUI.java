package com.norsez.dsp.synth.swing;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class VstSynthGUI extends JPanel {
  final private com.norsez.dsp.synth.VstSynthModel synth;
  private Controller controllers [];
  private JPanel mainPanel;
  private JToggleButton playButton;
  private Controller mainVol;
  private JTabbedPane tabbedPane;
  private JPanel tabbedPanels [];

  public VstSynthGUI( com.norsez.dsp.synth.VstSynthModel synthmodel) {
    this.synth = synthmodel;
    this.setLayout(new BorderLayout ());
    Font font = new java.awt.Font("Tahoma",java.awt.Font.PLAIN,7);
    GridLayout gridLayout= new GridLayout (2,1);

    //build on off button and mainvol

    JPanel southPanel = new JPanel (gridLayout);

    playButton = new JToggleButton ("Play");
    playButton.addChangeListener(new javax.swing.event.ChangeListener (){
      public void stateChanged ( javax.swing.event.ChangeEvent e ){
       synth.setBypass(  !((JToggleButton) e.getSource()).isSelected());
      }
    });

    mainVol = new Controller (synth,synth.getMainVolumeParamNumber());

    southPanel.add ( mainVol );
    southPanel.add ( playButton );
    this.add(southPanel, BorderLayout.SOUTH);



    //build main editing panel
    mainPanel = new JPanel ();

    this.add(new JScrollPane ( mainPanel ),BorderLayout.CENTER);


    controllers = new Controller [ synth.getNumParameters() ];
    int numGroups = synth.getNumGroups();
    if ( numGroups == 0){
      mainPanel.setLayout(new java.awt.GridLayout (synth.getNumParameters(),2,3,5));
      for (int i = 0; i < controllers.length; i++) {

        if (i == synth.getMainVolumeParamNumber())continue;

        controllers[i] = new Controller(this.synth, i);
        controllers[i].setFont(font);
        mainPanel.add(controllers[i]);
      }
    }
    else{
      mainPanel.setLayout(new BorderLayout());
      tabbedPane = new JTabbedPane ( JTabbedPane.TOP );
      tabbedPanels = new JPanel [numGroups];
      GridLayout grids  =new GridLayout(5,3,3,5);
      for(int i=0; i < numGroups; i++){
        tabbedPanels [i]= new JPanel (grids);
        tabbedPanels [i].setBorder(BorderFactory.createEtchedBorder());
        tabbedPane.addTab(synth.getGroupName(i),tabbedPanels[i]);
      }


      mainPanel.add(tabbedPane,BorderLayout.CENTER);

      for (int i = 0; i < controllers.length; i++) {

        if (i == synth.getMainVolumeParamNumber())continue;

        controllers[i] = new Controller(this.synth, i);
        controllers[i] .setPreferredSize(new Dimension(120,20));

        tabbedPanels[synth.getParameterGroup(i)].add(controllers[i]);
      }




    }

    playButton.setSelected(true);
    update_controllers_positions ();

  }

  private void update_controllers_positions (){

  }

}




class Controller extends JPanel {
  private JLabel label;
  private JSlider slider;
  private int param_number;
  final private com.norsez.dsp.synth.VstSynthModel synth;
  final Controller this_controller;
  private String param_name;

  private final static java.awt.GridLayout GRID = new java.awt.GridLayout ();
  private final static SliderBehavior SLIDING = new SliderBehavior();
  private final static Font FONT = new java.awt.Font("Tahoma",java.awt.Font.PLAIN,10);
  public Controller ( com.norsez.dsp.synth. VstSynthModel vstsynth, int param_no ){

    this.param_number = param_no;
    this.synth = vstsynth;
    this_controller = this;

    this.param_name = synth.getParameterName(param_no);
    this.setLayout(GRID);


    label = new JLabel (this.param_name);
    label.setFont(FONT);
    this.add ( label );
    label.setText(param_name + ": " + synth.getParameterDisplay(param_number));


    slider =  new CSlider (synth,label);
    slider.setValue((int)(synth.getParameter(param_number) * slider.getMaximum()));
    slider.addChangeListener(  SLIDING  );
    this.add ( slider );
  }



  public class CSlider
      extends JSlider {
    com.norsez.dsp.synth.VstSynthModel synth;
    JLabel label;
    public CSlider(com.norsez.dsp.synth.VstSynthModel synth, JLabel mylabel) {
      this.synth = synth;
      this.label = mylabel;
    }

    public void editParameter() {

        synth.setParameter(param_number, (double)slider.getValue() / slider.getMaximum());

       label.setText(param_name + ": " + synth.getParameterDisplay(param_number));
    }

  }

}

 class SliderBehavior implements javax.swing.event.ChangeListener {

    public void stateChanged (javax.swing.event.ChangeEvent e){

      ((Controller.CSlider) e.getSource() ).editParameter ();

    }
  }

