package com.norsez.dsp.synth;

import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class ParameterList {

  //maintains real copies of Parameter objects
  private Vector list;

  //maps parameter name to the real copies. This is to make getByName faster.
  private HashMap map;

  public ParameterList() {

    list = new Vector();
    map = new HashMap();
  }

  public void put(String name) {
    Parameter p = new Parameter(name);
    list.add(p);
    map.put(name, p);
  }

  public void put(String name, double initValue) {
    Parameter p = new Parameter(name);
    p.setValue(initValue);
    list.add(p);
    map.put(name, p);
  }

  public void put(String name, int ccno) {
    Parameter p = new Parameter(name);
    p.setControlChangeNumber(ccno);
    list.add(p);
    map.put(name, p);
  }

  public void put(String name, double initValue, int ccno) {
    Parameter p = new Parameter(name);
    p.setControlChangeNumber(ccno);
    p.setValue(initValue);
    list.add(p);
    map.put(name, p);
  }

  public Parameter getAt(int index) {
    return (Parameter) list.elementAt(index);
  }

  public int size() {
    return list.size();
  }

  public Parameter getByName(String name) {
    Parameter p = null;
    p = (Parameter) map.get(name);
    return p;
  }

  public String toString() {
    StringBuffer b = new StringBuffer();
    b.append("[");
    Parameter p;
    for (int i = 0; i < list.size(); i++) {
      p = (Parameter) list.elementAt(i);
      b.append("[" + p.getName() + "," + p.getValue() + "," +
               p.getControlChangeNumber()
               + "]\n");
    }
    b.append("]");
    return b.toString();

  }

}