package com.norsez.dsp.synth;

import java.io.*;
import java.util.*;

import com.norsez.dsp.midi.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class ParameterManager
    implements MidiDSPPort.MidiDSPPortListener {

  protected Vector parameters;
  protected HashMap nameMap;
  //groupMap keeps vectors of parameters that are in the same group
  protected HashMap groupMap;
  protected HashMap ccMap;

  public ParameterManager() {
    parameters = new Vector();
    nameMap = new HashMap();
    ccMap = new HashMap();
    groupMap = new HashMap();

  }

  /**
   *@return an array of Parameters that are associated with the group name
   */
  public Parameter[] getParameterOfGroup(String groupName) {

    Vector group = (Vector) groupMap.get(groupName);
    Parameter[] params = new Parameter[group.size()];

    for (int i = 0,n=group.size();i<n; i++) {
      params[i] = (Parameter) group.elementAt(i);
    }

    return params;

  }

  /**
   * @return all group names for iterating with getParameterOfGroup
   */
  public String[] getGroupNames() {
    Set set = groupMap.keySet();
    Vector v = new Vector();
    Iterator itr = set.iterator();
    while (itr.hasNext()) {
      v.add(itr.next());
    }
    Collections.sort(v);

    String[] s = new String[v.size()];
    for (int i = 0; i < v.size(); i++) {
      s[i] = v.elementAt(i).toString();
    }

    return s;
  }

  /**
   * VoiceModels update themselves by the value they get from this method.
   */
  public double getParameterValue(String paramName) {
    Parameter p = (Parameter) nameMap.get(paramName);
    if (p == null) {
      return 0;
    }
    else {
      return p.getValue();
    }
  }

  /**
   * @return the object which is the value of this parameter. The parameter's choices must not be null.
   */
  public Object getParameterObject(String paramName) {
    try {
      return ( (Parameter) nameMap.get(paramName)).getValueObject();
    }
    catch (java.lang.NullPointerException e) {
      System.err.println("\n" + paramName + "\n");
      return null;
    }
  }

  /**
   * SynthModel registers parameters with this method
   */
  public void addParameter(Parameter p) {
    parameters.add(p);
    nameMap.put(p.getName(), p);
    ccMap.put(new Integer(p.getControlChangeNumber()), p);
    if (p.getGroupName() != null && p.getGroupName().trim().length() > 0) {
      Vector v = (Vector) groupMap.get(p.getGroupName());
      if (v == null) {
        v = new Vector();
        groupMap.put(p.getGroupName(), v);
      }
      v.add(p);
    }
  }



  /**
   * What to do upon receiving Midi messages
   */
  public void onMidiReceived(MidiDSPPort.MidiDSPPortEvent e) {
    Parameter p;
    javax.sound.midi.ShortMessage s = (javax.sound.midi.ShortMessage) e.
        getEvent();

    if (s.getCommand() == s.CONTROL_CHANGE) {
      p = (Parameter) ccMap.get(new Integer(s.getData1()));
      if (p != null) {
        p.setValue(s.getData2() / 127.0);
      }
    }

  }

  /**
   * clear the vector that maintains the parameters.
   */
  public void clearList() {
    parameters.clear();
  }

  /**
   * @return the size of the parameter vector.
   */
  public int size() {
    return parameters.size();
  }

  /**
   * Lists all parameters' names.
   */
  public String getParameterNames() {
    StringBuffer buf = new StringBuffer();
    Parameter p;
    for (int i = 0; i < parameters.size(); i++) {
      p = (Parameter) parameters.elementAt(i);
      buf.append(p.getName());
    }
    return buf.toString();
  }

  /**
   * Saves current setting in a file.
   */
  public void saveSettingsToFile(String filename) {

    try {
      Properties prop = new Properties();
      Parameter p;
      for (int i = 0; i < parameters.size(); i++) {
        p = (Parameter) parameters.elementAt(i);
        prop.setProperty(p.getName(), p.getValue() + "");
      }
      prop.store(new FileOutputStream(new File(filename)), filename);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loades settings
   */
  public void loadSettingsToFile(String filename) {
    try {
      Properties prop = new Properties();
      Parameter p;
      prop.load(new FileInputStream(new File(filename)));
      Enumeration e = prop.keys();
      while (e.hasMoreElements()) {
        String s = (String) e.nextElement();
        setValue(s, Double.parseDouble( (String) prop.get(s)));

      }

    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets a parameter value
   */
  public void setValue(String paramName, double value) {
    Parameter p = (Parameter) nameMap.get(paramName);
    if (p != null) {
      p.setValue(value);
    }
  }

  /**
   *
   * Analogous to setValue
   *
   * @param paramName parameter name
   * @param o the object
   */
  public void setObject ( String paramName, Object o){
    Parameter p = (Parameter) nameMap.get(paramName);
    if (p != null) {
     Object l [] =p.getChoices();
     for (int i=0; i < l.length;i++)
       if (l[i] == o)
         p.setValue(i/(double)(l.length - 1.0));
    }

  }

  /**
   * @returns the ith Parameter that's added by addParameter ()
   */
  public Parameter getParameter(int index) {
    return (Parameter) parameters.elementAt(index);
  }

  /**
   * @return the number of Parameters added by addParameter()
   */
  public int getParameterCount() {
    return parameters.size();
  }

}