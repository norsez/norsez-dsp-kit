package com.norsez.dsp.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class KbTest extends JPanel {
  public JButton b ;
  public com.norsez.dsp.synth.swing.Kb2MidiModel kb2midi;
  public KbTest() {
    kb2midi = new com.norsez.dsp.synth.swing.Kb2MidiModel ();
    this.setBorder(BorderFactory.createBevelBorder(01));



    this.add(kb2midi.getEditPanel());
  }
  public static void main(String[] args) {
    KbTest kbTest1 = new KbTest();
    JOptionPane.showMessageDialog(null,kbTest1);
    System.exit ( 0 );
  }

}