package com.norsez.dsp.synth.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.norsez.dsp.synth.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public abstract class SynthPanel
    extends JPanel {
  protected SynthModel synth;
  protected Font myfont;

  public SynthPanel() {

    myfont = new Font("Tahoma", 0, 9);
    this.setFont(myfont);
    /*
         GridLayout grid = new GridLayout (synth.getParameterManager().getParameterCount(),2);
         this.setLayout( grid );
         Parameter p;
         for (int i=0; i < synth.getParameterManager().getParameterCount(); i++){
      p = (Parameter)synth.getParameterManager().getParameter(i);
      JLabel l = new JLabel (p.getName());
      l.setFont(myfont);
      this.add(l);
      if (!p.hasValueObject()){
        Slider s = new Slider (p);
        s.setFont(myfont);
        this.add(s);
      }
      else{
        Box j = new Box (p.getChoices(),p);
        j.setFont(myfont);
        this.add(j);
      }
         }
     */
  }

  static class Box
      extends JComboBox {
    private Parameter parameter;
    private JLabel label;
    private static BoxAction action = new BoxAction();
    public Box(Object[] obj, Parameter p, JLabel label) {
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
        Box j = (Box) e.getSource();
        j.getParameter().setValue( (double) j.getSelectedIndex() /
                                  (j.getItemCount() - 1));
      }
    }

  }

  static class Slider
      extends JSlider {
    private JLabel label;
    private Parameter parameter;
    private static Action action = new Action();
    private static MAction maction = new MAction();
    private static java.text.DecimalFormat df = new java.text.DecimalFormat(
        "0.00####");

    public Slider(Parameter p, JLabel label) {

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
          Slider s = (Slider) e.getSource();
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

        Slider s = (Slider) e.getSource();
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

}