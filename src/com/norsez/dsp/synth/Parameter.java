package com.norsez.dsp.synth;

import java.util.*;

/**
 * <p>Title: Parameter</p>
 * <p>Description: A class that encapsulates a Parameter. If the parameter value is just a double, then
 *                 it just does set and get for that double value. If the parameter value is an object,
 *                 e.g. Envelope.Mode, then it is constructed with Parameter(name, Object[]) or Parameter(name, Object[], double).
 *                 The get and set still works by returning the value of the index/(length-1).</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Parameter {

  private String name;
  private String groupName;
  private double value;
  private Vector listeners;
  private Object[] choices;
  private double defaultValue;
  private ParameterDisplay displayer;
  private String tooltiptext;

  //holds control change number if assigned.
  private int cc = -1;

  public void addParameterListener(ParameterListener l) {
    listeners.add(l);
  }

  public void setControlChangeNumber(int c) {
    cc = c;
  }

  public int getControlChangeNumber() {
    return cc;
  }

  public void removeParameterListener(ParameterListener l) {
    listeners.remove(l);
  }

  public Parameter(String name) {
    this.name = name;
  }

  public Parameter(String name, double value) {
    this(name);
    setValue(value);
    setDefaultValue(value);
  }

  public Parameter(String name, double value, String groupName,
                   ParameterDisplay displayer) {
    this(name, value);
    this.groupName = groupName;
    this.displayer = displayer;

  }

  public ParameterDisplay getDisplayer() {
    return displayer;
  }

  public Parameter(String name, double value, String groupName) {
    this(name, value);
    this.groupName = groupName;

  }

  public double getDefaultValue() {
    return this.defaultValue;
  }

  public void setDefaultValue(double v) {
    this.defaultValue = v;
  }

  /**
   * @param name parameter's name.
   * @param choices  For example, choices = {Mode.ON,Mode.HOLD,Mode.OFF}. Then setValue (0) selects Mode.ON and setValue (1) selects Mode.OFF. setValue (anything in between 0 and 1) selects Mode.HOLD.
   */
  public Parameter(String name, Object[] choices) {
    this(name);

    if (choices == null) {
      throw new java.lang.IllegalArgumentException(
          "Parameter choices cannot be null.");
    }

    this.choices = choices;
  }

  public Parameter(String name, Object[] choices, String groupName) {
    this(name, choices);

    this.groupName = groupName;

  }

  public Parameter(String name, Object[] choices, String groupName,
                   ParameterDisplay displayer) {
    this(name, choices);

    this.groupName = groupName;
    this.displayer = displayer;

  }

  public Parameter(String name, Object[] choices, double value) {
    this(name, choices);
    setValue(value);
  }

  public String getName() {
    return name;
  }

  /**
   * checks if this parameter has Objects as values
   */
  public boolean hasValueObject() {
    return (choices != null);
  }

  /**
   * @return the object which the value represents. If choices is null, it returns null.
   */

  public Object getValueObject() {
    if (choices == null) {
      return null;
    }
    else {
      return choices[ (int) ( (choices.length - 1) * value)];
    }
  }

  public Object[] getChoices() {
    return choices;
  }

  public void setValue(double v) {
    value = v;

  }

  public double getValue() {

    return value;

  }

  public String getGroupName() {
    return groupName;
  }

  private void fireEvent(int dataChanged) {

    ParameterListener p;
    for (int i = 0,n= listeners.size();i<n; i++) {
      p = (ParameterListener) listeners.elementAt(i);
      p.onParameterChanged(new ParameterEvent(this, dataChanged));
    }
  }

  public class ParameterEvent {
    public static final int DATA1 = 0;
    public static final int DATA2 = 1;
    private Object source;
    private int dataChanged;
    public ParameterEvent(Object source, int dataChanged) {
      this.source = source;
      this.dataChanged = dataChanged;
    }

    public Object getSource() {
      return source;
    }

    public int getDataChanged() {
      return dataChanged;
    }
  }

  public interface ParameterListener {

    public void onParameterChanged(ParameterEvent e);
  }

}