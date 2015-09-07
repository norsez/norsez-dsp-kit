package com.norsez.dsp.test;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class SM2Applet extends JApplet {
  private boolean isStandalone = false;

  TestTone s1;
  private String model;
  //Get a parameter value
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  //Construct the applet
  public SM2Applet() {


    //this.getContentPane().add(s1.getEditorPanelA());

  }
  //Initialize the applet
  public void init() {
    try {
      model = this.getParameter("model", "TestTone");

      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void start (){
    s1.startPlayThread(true);
  }

  public void stop (){
    s1.startPlayThread(false);
  }

  public void destroy  (){
    s1.destroyPlayer();
  }

  //Component initialization
  private void jbInit() throws Exception {

    s1 = new TestTone ();
      this.getContentPane().add(s1.getEditorPanelA(),BorderLayout.CENTER);

    this.setSize(new Dimension(400,300));
  }
  //Get Applet information
  public String getAppletInfo() {
    return "Applet Information";
  }
  //Get parameter info
  public String[][] getParameterInfo() {
    return null;
  }

  //static initializer for setting look & feel
  static {
    try {
      //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    catch(Exception e) {
    }
  }
}