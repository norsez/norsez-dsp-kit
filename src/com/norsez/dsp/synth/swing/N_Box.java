package com.norsez.dsp.synth.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.norsez.dsp.synth.*;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class N_Box
    extends JComboBox {
  private Parameter parameter;
  private JLabel label;
  private static BoxAction action = new BoxAction();
  public N_Box(Object[] obj, Parameter p, JLabel label) {
    super(obj);
    this.label = label;
    this.parameter = p;
    this.setSelectedIndex( (int) (p.getValue() * (p.getChoices().length - 1)));
    this.addItemListener(action);

  }

  public Parameter getParameter() {
    return this.parameter;
  }

  static class BoxAction
      implements ItemListener {
    public void itemStateChanged(ItemEvent e) {
      N_Box j = (N_Box) e.getSource();
      j.getParameter().setValue( (double) j.getSelectedIndex() /
                                (j.getItemCount() - 1));
    }
  }

}

