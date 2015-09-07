package com.norsez.dsp.synth.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.norsez.dsp.synth.*;
import javax.swing.event.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class N_Slider
    extends JSlider {
  private JLabel label;
  private Parameter parameter;
  private static Action action = new Action();
  private static MAction maction = new MAction();
  private static java.text.DecimalFormat df = new java.text.DecimalFormat(
      "0.00####");

  public N_Slider(Parameter p, JLabel label) {

    super(0, 127, (int) (p.getValue() * 127));

    this.label = label;
    this.parameter = p;
    this.addChangeListener(action);
    this.addMouseListener(maction);
    action.stateChanged(new javax.swing.event.ChangeEvent(this));

  }

  public double getDoubleValue() {
    return parameter.getValue();
  }

  public Parameter getParameter() {
    return parameter;
  }

  static class MAction
      extends java.awt.event.MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2) {
        N_Slider s = (N_Slider) e.getSource();
        s.setValue( (int) (s.getParameter().getDefaultValue() * s.getMaximum()));
      }
    }
  }

  public void setDisplayValue(String s) {
    label.setText("<html><b>" + label.getName() + "</b> [" + s + "]</html>");
  }

  static class Action
      implements javax.swing.event.ChangeListener {
    public void stateChanged(javax.swing.event.ChangeEvent e) {

      N_Slider s = (N_Slider) e.getSource();
      double v = s.getValue() / 127.0;
      s.getParameter().setValue(v);
      if (s.getParameter().getDisplayer() != null) {
        s.setDisplayValue(s.getParameter().getDisplayer().getDisplay(v) + "");
      }
      else {
        s.setDisplayValue(ParameterDisplay.LINEAR.getDisplay(v) + "");

      }
    }
  }

  public void setValue(int v) {
    super.setValue(v);

  }
}

