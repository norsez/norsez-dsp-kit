package com.norsez.dsp.midi;

import javax.sound.midi.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class MidiMonitor {

  private boolean showSysex = true;
  private boolean showNote = true;
  private boolean showPitchBend = true;
  private boolean showController = true;

  private MidiDevice[] devices;

  public MidiMonitor() {

    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    devices = new MidiDevice[infos.length];
    for (int i = 0; i < infos.length; i++) {
      try {
        devices[i] = MidiSystem.getMidiDevice(infos[i]);
        if (devices[i].getMaxTransmitters() != 0) {
          devices[i].getTransmitter().setReceiver(new MidiInAction(infos[i].
              getName()));
          devices[i].open();
          System.out.println(infos[i].getName() + " Opened.");
        }
      }
      catch (javax.sound.midi.MidiUnavailableException mex) {
        System.out.println(infos[i].getName());
      }

    }
  }

  class MidiInAction
      implements javax.sound.midi.Receiver {
    private String name;
    public MidiInAction(String name) {
      super();
      this.name = name;
    }

    public void close() {}

    public void send(MidiMessage msg, long time) {
      String status;
      if (msg instanceof ShortMessage) {
        ShortMessage sm = (ShortMessage) msg;
        if (sm.getCommand() != 0xf0) {

          switch (sm.getCommand()) {
            case 0x90:
              if (sm.getData2() == 0) {
                status = "Note Off";
              }
              else {
                status = "Note On";
              }
              break;
            case 0xa0:
              status = "Poly Aftertouch ";
              break;
            case 0xb0:
              status = "Controller";
              break;
            case 0xc0:
              status = "Program Change";
              break;
            case 0xd0:
              status = "Channel Aftertouch ";
              break;
            case 0xe0:
              status = "PitchBend ";
              break;
            default:
              status = "Unknown";
          }

          System.out.println(name + ": " + sm.getChannel() + " " + status + " " +
                             sm.getData1() + " " + sm.getData2());
        }
      }
      else
      if (msg instanceof SysexMessage) {
        SysexMessage sm = (SysexMessage) msg;
        for (int i = 0; i < sm.getData().length; i++) {
          if (sm.getData()[i] >= 0) {
            System.out.print(Integer.toHexString(sm.getData()[i]) + " ");
          }
          else {
            System.out.print(Integer.toHexString(sm.getData()[i] + 256) + " ");

          }
          if (i % 16 == 15) {
            System.out.println();
          }
        }
        System.out.println("\n");
      }
    }
  }

  public static void main(String[] args) {
    new MidiMonitor();
  }
}