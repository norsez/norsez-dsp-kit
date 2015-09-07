package com.norsez.dsp.midi;

import java.util.*;
import javax.sound.midi.*;

/**
 * <p>Title: </p>
 * <p>Description: Typical use is first call showMidiOuts () and then look at the number of the device you want.
 *    Then use that number in getMidiOut() to get that midiout device.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class MidiDeviceSelector {

  private MidiDeviceSelector() {
  }

  public static MidiDevice.Info[] listMidiOuts() {
    Vector infos = new Vector();
    MidiDevice.Info[] allinfos = MidiSystem.getMidiDeviceInfo();
    MidiDevice dev = null;
    for (int i = 0; i < allinfos.length; i++) {
      try {
        dev = MidiSystem.getMidiDevice(allinfos[i]);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      if (dev.getMaxReceivers() != 0) {
        infos.add(allinfos[i]);
      }
    }

    MidiDevice.Info[] result = new MidiDevice.Info[infos.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = (MidiDevice.Info) infos.elementAt(i);
    }

    return result;
  }

  public static MidiDevice.Info[] listMidiIns() {
    Vector infos = new Vector();
    MidiDevice.Info[] allinfos = MidiSystem.getMidiDeviceInfo();
    MidiDevice dev = null;
    for (int i = 0; i < allinfos.length; i++) {
      try {
        dev = MidiSystem.getMidiDevice(allinfos[i]);
        //System.out.println ( dev.getDeviceInfo().getName() + " " + dev.getMaxTransmitters() );
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      if (dev.getMaxTransmitters() != 0) {

        infos.add(allinfos[i]);
      }
    }

    MidiDevice.Info[] result = new MidiDevice.Info[infos.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = (MidiDevice.Info) infos.elementAt(i);
    }

    return result;
  }

  public static String showMidiOuts() {
    MidiDevice.Info[] infos = listMidiOuts();
    String result = "";
    for (int i = 0; i < infos.length; i++) {
      result += i + " " + infos[i].getName() +
          System.getProperty("line.separator");
    }

    return result;
  }

  public static String showMidiIns() {
    MidiDevice.Info[] infos = listMidiIns();
    String result = "";
    for (int i = 0; i < infos.length; i++) {
      result += i + " " + infos[i].getName() +
          System.getProperty("line.separator");
    }

    return result;
  }

  public static MidiDevice getMidiOut(int i) throws javax.sound.midi.
      MidiUnavailableException {

    return MidiSystem.getMidiDevice(listMidiOuts()[i]);
  }

  public static MidiDevice getMidiIn(int i) {

    MidiDevice dev = null;
    try {
      dev = MidiSystem.getMidiDevice(listMidiIns()[i]);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return dev;
  }

  public static void main(String[] args) {
    System.out.println(showMidiIns());
  }

}