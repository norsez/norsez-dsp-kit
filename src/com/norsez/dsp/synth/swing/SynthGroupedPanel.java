package com.norsez.dsp.synth.swing;

import java.awt.*;
import javax.swing.*;

import com.norsez.dsp.synth.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class SynthGroupedPanel
    extends SynthPanel {

  private JTabbedPane tpane;

  public void addTab (String title,Component comp){
    tpane.addTab(title,comp);
  }

  public void setSynth (SynthModel synth){
    String[] groups = synth.getParameterManager().getGroupNames();
    for (int i = 0; i < groups.length; i++) {
      Parameter[] params = synth.getParameterManager().getParameterOfGroup(
          groups[i]);
      JPanel panel = new JPanel(new GridLayout(params.length, 2));

      for (int j = 0; j < params.length; j++) {
        JLabel l = new JLabel("<html><b>" + params[j].getName() + "</b></html>");
        l.setName(params[j].getName());
        l.setFont(myfont);
        //l.setHorizontalAlignment(JLabel.CENTER);
        panel.add(l);

        if (!params[j].hasValueObject()) {
          Slider s = new Slider(params[j], l);
          s.setFont(myfont);
          panel.add(s);
          l.setLabelFor(s);

        }
        else {
          Box b = new Box(params[j].getChoices(), params[j], l);
          b.setFont(myfont);
          panel.add(b);
          l.setLabelFor(b);
        }
      }

      tpane.addTab(groups[i], new JScrollPane(panel));

    }

  }

  public SynthGroupedPanel (){
    super();
    this.setLayout(new BorderLayout());
    tpane = new JTabbedPane();
    add(tpane);

  }

  public SynthGroupedPanel(SynthModel synth) {
    this ();
    setSynth (synth);
  }
}