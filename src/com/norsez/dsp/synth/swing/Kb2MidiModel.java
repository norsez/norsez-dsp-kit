package com.norsez.dsp.synth.swing;
import com.norsez.dsp.block.*;
/**
 * <p>Title: Kb2MidiModel</p>
 * <p>Description: The model for using computer keyboard as a midi keyboard.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Kb2MidiModel {

  protected final int OCTAVE_ONE [] = {90,83,88,68,67,86,71,66,72,78,74,77,44,76,46,59,47};
  protected final int OCTAVE_TWO [] = {81,50,87,51,69,82,53,84,54,89,55,85,73,57,79,48,80,91,61,93};

  private int velocity, velrnd;
  private int octave, transpose;
  private double [] allNoteGates;

  public Kb2MidiModel() {
    allNoteGates = new double [128];
    setOctave ( 4);
    setVelocity ( 67 );
    setVelocityRandom ( 10 );
  }

  public double [] getAllNoteGates (){
    return this.allNoteGates;
  }

  public int isInArray ( int [] arr, int v ){
    for(int i=0; i < arr.length; i++){
      if (arr[i] == v){
        return i;
      }
    }
    return -1;
  }

  public void setGateByKeyCode (int keyCode, boolean isOn){
    int oct =0;
    int key = isInArray(OCTAVE_ONE,keyCode);

    if (key == -1 ){
      key = isInArray(OCTAVE_TWO, keyCode);
      if (key > -1)
        oct = 1;
    }

    if (key == -1) return;
    int note = Math.min ( 127, Math.max ( 0, ((octave + oct) * 12) + (key %12) + transpose));
    //System.out.println ( keyCode + " "  + key );
    setGate ( note
        ,isOn);


  }

  public void setGate (int noteNum, boolean isOn){
    if (!isOn)
      allNoteGates [ noteNum ] = 0;
    else
      allNoteGates [ noteNum ] = Math.min( 1, Math.max (0, DSPSystem.ONE_OVER_127 * (velocity + Math.random() * velrnd)));
  }

  public void setOctaveIncreament (boolean goUp){
    if (goUp) setOctave ( octave + 1 );
      else
        setOctave ( octave - 1);
  }

  public void setOctave (int o){

    if ( o < 0 ) o =0;
    if ( o > 8 ) o =12;
    octave = o;
  }

  public void setOctaveIncrement (boolean goUp){
    if (goUp) setTranspose ( transpose + 1 );
      else
        setTranspose ( transpose - 1);

  }

  public void setTranspose ( int t){

    transpose = t;
  }

  public void setVelocityRandom ( int r){
    velrnd = r;
  }

  public void setVelocity ( int v ){
    velocity = v;
  }


  public static void main(String[] args) {
    Kb2MidiModel kb2MidiModel1 = new Kb2MidiModel();
    javax.swing.JOptionPane.showMessageDialog(null,
    new javax.swing.JScrollPane(kb2MidiModel1.getEditPanel()));
  }

  public String toString (){
    String s = "";
    s += "octave = " +  octave;
    s += ", transpose = " + transpose;
    s += ", velocity = " + velocity;
    s += ", vel random = " + velrnd;
    s+=" [";
    for(int i=0; i < 128; i ++){
      if (allNoteGates[i] != 0){
        s += " ("
            +Table.T_NOTE_NAMES [i] + ","
            +allNoteGates [i] + ")"
            ;
      }
    }
    return s + " ]";
  }

  public javax.swing.JPanel getEditPanel (){
    javax.swing.JPanel p  = new javax.swing.JPanel (new java.awt.BorderLayout());
    javax.swing.JPanel kb = new javax.swing.JPanel ();
    final Kb2MidiModel kb2midi = this;
    javax.swing.JButton play = new javax.swing.JButton ("Play Here");
    play.addKeyListener(new java.awt.event.KeyAdapter(){
      public void keyPressed (java.awt.event.KeyEvent e){
        kb2midi.setGateByKeyCode(e.getKeyCode(),true);
        System.out.println ( kb2midi);
      }
      public void keyReleased (java.awt.event.KeyEvent e){
        kb2midi.setGateByKeyCode(e.getKeyCode(),false);
        System.out.println ( kb2midi);
      }

    });
    kb.add(play);

    javax.swing.JButton dump= new javax.swing.JButton ("Dump");
    dump.addActionListener(new java.awt.event.ActionListener(){
      public void actionPerformed ( java.awt.event.ActionEvent e){
        System.out.println (kb2midi.toString());
      }
    });
    kb.add(dump);
    p.add(kb,java.awt.BorderLayout.NORTH);

    javax.swing.JPanel ct = new javax.swing.JPanel (new java.awt.GridLayout(4,2));
    //add octave
    javax.swing.JLabel l1 = new javax.swing.JLabel ("Octave");
    l1.setHorizontalAlignment(l1.CENTER);
    javax.swing.JSlider s1 = new javax.swing.JSlider (0,12,kb2midi.octave);
    s1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    s1.setPaintTicks(true);
    s1.setMajorTickSpacing(1);
    s1.addChangeListener(new javax.swing.event.ChangeListener (){
      public void stateChanged (javax.swing.event.ChangeEvent e){
        javax.swing.JSlider s = (javax.swing.JSlider) e.getSource();
        kb2midi.setOctave(s.getValue());
        s.setToolTipText(s.getValue() +"");
      }
    });
    ct.add(l1);
    ct.add(s1);

    //add transpose
    javax.swing.JLabel l2 = new javax.swing.JLabel ("Transpose");
    l2.setHorizontalAlignment(l2.CENTER);
    javax.swing.JSlider s2 = new javax.swing.JSlider (-12,12,kb2midi.transpose);
    s2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    s2.setPaintTicks(true);
    s2.setMajorTickSpacing(1);
    s2.addChangeListener(new javax.swing.event.ChangeListener (){
      public void stateChanged (javax.swing.event.ChangeEvent e){
        javax.swing.JSlider s = (javax.swing.JSlider) e.getSource();
        s.setToolTipText(s.getValue() + "");
        kb2midi.setTranspose(s.getValue());
      }
    });
    ct.add(l2);
    ct.add(s2);

    //add velocity
    javax.swing.JLabel l3 = new javax.swing.JLabel ("Velocity");
    l3.setHorizontalAlignment(l3.CENTER);
    javax.swing.JSlider s3 = new javax.swing.JSlider (0,127,kb2midi.velocity);
    s3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    s3.setPaintTicks(true);
    s3.setMajorTickSpacing(16);
    s3.addChangeListener(new javax.swing.event.ChangeListener (){
      public void stateChanged (javax.swing.event.ChangeEvent e){
        javax.swing.JSlider s = (javax.swing.JSlider) e.getSource();
        s.setToolTipText(s.getValue()+"");
        kb2midi.setVelocity(s.getValue());
      }
    });
    ct.add(l3);
    ct.add(s3);

    //add velocity random
    javax.swing.JLabel l4 = new javax.swing.JLabel ("Velocity Random");
    l4.setHorizontalAlignment(l4.CENTER);
    javax.swing.JSlider s4 = new javax.swing.JSlider (10,45,kb2midi.velrnd);
    s4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    s4.setPaintTicks(true);
    s4.setMajorTickSpacing(5);
    s4.addChangeListener(new javax.swing.event.ChangeListener (){
      public void stateChanged (javax.swing.event.ChangeEvent e){
        javax.swing.JSlider s = (javax.swing.JSlider) e.getSource();
        s.setToolTipText(s.getValue()+"");
        kb2midi.setVelocityRandom(s.getValue());
      }
    });
    ct.add(l4);
    ct.add(s4);

    p.add(ct,java.awt.BorderLayout.CENTER);


    return p;
  }
}