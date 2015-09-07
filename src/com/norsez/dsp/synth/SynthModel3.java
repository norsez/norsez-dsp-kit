package com.norsez.dsp.synth;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import com.norsez.dsp.player.*;
import com.norsez.dsp.block.*;
import com.norsez.dsp.synth.swing.*;
import java.io.*;
/**
 * <p>Title: SynthModel3</p>
 * <p>Description: VST style synth model.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 *
 */

public abstract class SynthModel3 extends SynthModel2{



  public abstract String getParameterName ( int index);
  public abstract String getParameterGroupName ( int index);
  public abstract double getParameterValue ( int index);
  public abstract String getParameterDisplay ( int index);
  public abstract int getParameterCount ();
  public abstract int getParameterGroupCount ();

  public abstract void setParameterValue ( int i , double value);
  /**
   * set parameter program (patch ) of th synth.
   * @param prog array of double that contains  values of all parameters
   */
  public abstract void setProgram ( double [] prog );
  public abstract double [] getProgram();
  public abstract void initProgram ();
  /**
   * Save program double values in a file
   *
   * @param program double array containing program data
   * @param filename the filename to save to
   * @return true if successful
   */
  public static boolean saveProgramToFile ( double [] program, String filename ){
    try{
      ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(
          filename));

      for (int i=0; i < program.length; i++){
        os.writeDouble(program[i]);
      }
      os.close();
      return true;

    }catch ( Exception e){
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Assumes the data in the file are doubles.
   *
   * @param program the array to put the read program into
   * @param filename the file to read from
   * @return true if successful, false if unsuccessful.
   */
  public static boolean loadProgramFromFile ( double [] program, String filename  ){

    boolean result = false;
    try{
      ObjectInputStream os = new ObjectInputStream(new FileInputStream(
          filename));

      for (int i=0; i < program.length; i++)
        program[i] = os.readDouble();

        result = true;

    }catch ( Exception e){
      e.printStackTrace();
      result = false;

    }
    return result;

  }

  /**
   * maintains all sliders in the editor panel.
   */
  protected Slider [] sliders = null;

  protected Dragger [] draggers = null;

  public JPanel getEditorPanelB (){
    JPanel mainpanel = new JPanel (new BorderLayout ());
   Font myfont = new Font ("Verdana",0,9);
   final SynthModel3 synth = this;
    //sliders = new Slider [ synth.getParameterCount()];
    draggers = new Dragger [synth.getParameterCount()];
   //play button
   javax.swing.JToggleButton playBtn = new javax.swing.JToggleButton ("<html><font color=green><b>Play</b></font></html>");

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
   /*
   //init button
   JButton initBtn = new JButton ( "Initialize");
   initBtn.addActionListener ( new ActionListener (){
     public void actionPerformed ( ActionEvent e ){
       synth.initProgram();
       double prog [] = synth.getProgram();
       if (draggers != null)
       for(int i=0; i < prog.length;i++)
         //sliders[i].setValue((int)(prog[i] * sliders[i].getMaximum()));

         draggers[i].setValue((int)(prog[i] * draggers[i].getMaximum()));
     }
   });
   final PresetBrowser pbrowser = new PresetBrowser ();
   //preset load box
   final JComboBox loadbox = new JComboBox (pbrowser.getPresetNames());
   JButton loadBtn = new JButton ( "Load Preset.." );
   loadBtn.addActionListener(new ActionListener (){
     public void actionPerformed(ActionEvent e) {
         double [] data = new double [synth.getParameterCount()];
         pbrowser.askToLoadProgram( data,loadbox.getSelectedItem().toString());
         if (data != null){
           synth.setProgram(data);
           updateDraggers ();
         }
     }
   });


   JButton saveBtn = new JButton ("Save Preset..");
   saveBtn.addActionListener (new ActionListener (){
     public void actionPerformed ( ActionEvent e ){
       pbrowser.askToSaveProgram(synth.getProgram());

     }
   });


   trp.add(loadbox);
   trp.add(loadBtn);
   trp.add(saveBtn);

   trp.add(initBtn);
   */
   JPanel trp = new JPanel ();
   trp.add(playBtn);

   mainpanel.add ( trp , BorderLayout.SOUTH);

   JPanel centerp = new JPanel ( );
   String [] params = new String [synth.getParameterCount()];
   for(int i=0; i <  params.length;i++){
     params[i] = synth.getParameterName(i);
   }

   final JLabel label = new JLabel ();
   label.setHorizontalTextPosition(label.CENTER);
   final Dragger drg = new Dragger (synth,0,0);
   drg.setPreferredSize(new Dimension (100,18));
   final JComboBox paramBox = new JComboBox ( params );
   paramBox.addItemListener(new ItemListener (){
     public void itemStateChanged ( ItemEvent e ){
       int t = ((JComboBox)e.getSource()).getSelectedIndex();
       //label.setText("<html><b>"+ synth.getParameterN(t) +"</b></html>");
       drg.setTag(t);
     }
   });

   centerp.add ( paramBox );
   //centerp.add(label);
   centerp.add ( drg );

   mainpanel.add( new JScrollPane (centerp), BorderLayout.CENTER);

   return mainpanel;

  }

  /**
   *
   * @return a JPanel containing the play button and the editor of all parameters in this SynthModel.
   */
  public JPanel getEditorPanelA (){
    JPanel mainpanel = new JPanel (new BorderLayout ());
    Font myfont = new Font ("Verdana",0,9);
    final SynthModel3 synth = this;
     //sliders = new Slider [ synth.getParameterCount()];
     draggers = new Dragger [synth.getParameterCount()];
    //play button
    javax.swing.JToggleButton playBtn = new javax.swing.JToggleButton ("<html><font color=green><b>Play</b></font></html>");

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

    //init button
    JButton initBtn = new JButton ( "Initialize");
    initBtn.addActionListener ( new ActionListener (){
      public void actionPerformed ( ActionEvent e ){
        synth.initProgram();
        double prog [] = synth.getProgram();
        for(int i=0; i < prog.length;i++)
          //sliders[i].setValue((int)(prog[i] * sliders[i].getMaximum()));
          draggers[i].setValue((int)(prog[i] * draggers[i].getMaximum()));
      }
    });
    final PresetBrowser pbrowser = new PresetBrowser ();
    //preset load box
    final JComboBox loadbox = new JComboBox (pbrowser.getPresetNames());
    JButton loadBtn = new JButton ( "Load Preset.." );
    loadBtn.addActionListener(new ActionListener (){
      public void actionPerformed(ActionEvent e) {
          double [] data = new double [synth.getParameterCount()];
          pbrowser.askToLoadProgram( data,loadbox.getSelectedItem().toString());
          if (data != null){
            synth.setProgram(data);
            updateDraggers ();
          }
      }
    });


    JButton saveBtn = new JButton ("Save Preset..");
    saveBtn.addActionListener (new ActionListener (){
      public void actionPerformed ( ActionEvent e ){
        pbrowser.askToSaveProgram(synth.getProgram());

      }
    });

    JPanel trp = new JPanel ();
    trp.add(loadbox);
    trp.add(loadBtn);
    trp.add(saveBtn);
    trp.add(playBtn);
    trp.add(initBtn);

    mainpanel.add ( trp , BorderLayout.SOUTH);

    String[] groups = new String[this.getParameterGroupCount()];
    Action ac = new Action ();
    MAction mac = new MAction ();
    final int maxvalue = 255;
    JPanel centerp = new JPanel (new GridLayout (this.getParameterCount() , 1));
    for (int i=0; i < this.getParameterCount(); i++){

      JLabel l = new JLabel("<html><b>" + this.getParameterName(i) +"</b>"
                            //+ "[" + this.getParameterDisplay(i)+"]"
                            +"</html>");
      l.setName(this.getParameterName(i));
      //l.setFont(myfont);
      //centerp.add(l);
      //Slider s = new Slider(0,maxvalue,(int)(this.getParameterValue(i) * maxvalue),
      //                      i,l,this);
      Dragger s = new Dragger (this,i,(int)(this.getParameterValue(i) * maxvalue));
      s.setPrefix(this.getParameterName(i) + " = ");
      //s.setMaximumSize(new Dimension(150,22));
      //s.addChangeListener(ac);
      //s.addMouseListener(mac);
      //s.setFont(myfont);
      //sliders [i] = s;
      draggers[i]= s;
      centerp.add(s);
      //l.setLabelFor(s);


    }
    mainpanel.add( new JScrollPane (centerp), BorderLayout.CENTER);

    return mainpanel;
  }

  public void updateSliders (){
   if (sliders != null){

     for (int i = 0; i < sliders.length; i++)
     {
       sliders[i].setValue((int)(this.getParameterValue(i)* sliders[i].getMaximum()));
     }

   }
 }

 public void updateDraggers (){
  if (draggers != null){

    for (int i = 0; i < draggers.length; i++)
    {
      draggers[i].setValue((int)(this.getParameterValue(i)* draggers[i].getMaximum()));
    }

  }
}




  static class Slider
      extends JSlider {
    public int tag,init;
    public JLabel label;
    public SynthModel3 synth;
    public Slider(int min, int max, int init, int paramIndex, JLabel label,
                  SynthModel3 synth) {
      super(min, max, init);
      tag = paramIndex;
      this.label = label;
      this.synth = synth;
      this.init = init;
    }

  }
  /**
   *
   * <p>Title: </p>
   * <p>Description: Action for the editor's slider </p>
   * <p>Copyright: Copyright (c) 2003</p>
   * <p>Company: Norsez Orankijanan</p>
   * @author Norsez Orankijanan
   * @version 1.0
   */
  static class Action
      implements ChangeListener {
    public void stateChanged(ChangeEvent e) {
      Slider s = (Slider) e.getSource();
      double v = s.getValue() / (double)s.getMaximum();
      if (s.getValue() == s.getMaximum() * 0.5)
        v = 0.5;
      s.synth.setParameterValue(s.tag, v);
      s.label.setText("<html><b>"+ s.label.getName() + "</b> [" + s.synth.getParameterDisplay(s.tag) +
                      "]</html>");

    }


  }
  static class MAction extends MouseAdapter {
    public void mouseClicked (MouseEvent e){
      if (e.getClickCount() == 2 ){
        Slider s = (Slider)e.getSource();
        s.setValue(s.init);
        s.synth.setParameterValue(s.tag,(double)s.getValue() / (double) s.getMaximum());
      }
    }
  }
}