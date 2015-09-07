package com.norsez.dsp.synth.swing;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import com.norsez.dsp.synth.*;
/**
 * <p>Title: PresentBrowser</p>
 * <p>Description: Encapsulates  browsing of patch files in a directory for SynthModel3.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class PresetBrowser {

  private File currentDir;
  public static final String PRESET_EXT = ".pch";

  public PresetBrowser() {
    currentDir = new File(".");
  }

  public void getProgram ( double [] program, String presetName){

    SynthModel3.loadProgramFromFile( program , presetName+PRESET_EXT);

  }

  public void saveProgram ( double [] program ,String presetName ){
    SynthModel3.saveProgramToFile(program,presetName);
  }

  public void askToSaveProgram ( double [] program ){
    JTextField tf = new JTextField ("Untitled");
    tf.selectAll();
    JLabel label = new JLabel ("Enter Preset Name");
    JPanel pn = new JPanel (new GridLayout(2,1));
    pn.setBorder(BorderFactory.createEtchedBorder());
    pn.add(label);
    pn.add(tf);

    int result = JOptionPane.CANCEL_OPTION;
    result = JOptionPane.showConfirmDialog(null,pn,"Save Preset",JOptionPane.OK_CANCEL_OPTION,
                                      JOptionPane.QUESTION_MESSAGE);
    if (result == JOptionPane.OK_OPTION){

      String name = tf.getText();
      if (!name.endsWith(PRESET_EXT))
        name += PRESET_EXT;

      saveProgram (program, name);

    }



  }

  public void askToLoadProgram ( double program [], String presetName ){


     File toLoad = new File ( presetName + PRESET_EXT);
     if (!toLoad.exists()){
       JOptionPane.showMessageDialog(null,presetName + " does not exist" ,"Error",JOptionPane.WARNING_MESSAGE);
     }
     else {

       int result = JOptionPane.CANCEL_OPTION;
       result = JOptionPane.showConfirmDialog(null, "Are you sure to load " + presetName + "?", "Are you sure?",
                                              JOptionPane.OK_CANCEL_OPTION,
                                              JOptionPane.QUESTION_MESSAGE);
       if (result == JOptionPane.OK_OPTION) {

         getProgram(program ,presetName);
       }

     }



  }


  public void setCurrentDirectory ( File newdir ){
    currentDir= newdir;
  }

  public String[] getPresetNames() {

    String[] files;
    files = currentDir.list(new FilenameFilter() {
      public boolean accept(File f, String s) {
        if (s.endsWith(PRESET_EXT))
          return true;
        else
          return false;
      }
    });

    for(int i=0; i < files.length; i++){
      files [i] = files [i].substring(0,files[i].indexOf(PRESET_EXT));
    }

    return files;
  }

  public String toString (){
    return "Current Dir = " + currentDir.getAbsolutePath()
        ;
  }


  public static void main (String [] args){
    System.out.println (new PresetBrowser ());
  }

}