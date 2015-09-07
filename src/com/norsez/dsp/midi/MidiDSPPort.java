package com.norsez.dsp.midi;

import java.util.*;
import javax.sound.midi.*;

import com.norsez.dsp.synth.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class MidiDSPPort {

  private Vector listeners;

  private MidiInAction midiinaction;
  private javax.sound.midi.MidiDevice inport;

  public MidiDSPPort() {
    listeners = new Vector();
    midiinaction = new MidiInAction(this);
  }

  public MidiDSPPort(MidiDevice inport) {
    this ();
    setMidiInDevice(inport);

  }

  public void setMidiInDevice(MidiDevice inport) {
    this.inport = inport;

    try {

      this.inport.getTransmitter().setReceiver(midiinaction);

    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addPortListener(MidiDSPPortListener l) {
    listeners.add(l);
  }

  public void removePortListener(MidiDSPPortListener l) {
    listeners.remove(l);
  }

  class MidiInAction
      implements Receiver {
    private MidiDSPPort source;
    public MidiInAction(MidiDSPPort source) {
      this.source = source;
    }

    public void close() {}

    public void send(MidiMessage message, long timeStamp) {

      MidiDSPPortListener l;
      Parameter p;

      if (message instanceof ShortMessage) {
        ShortMessage s;
        s = (ShortMessage) message;

        for (int i = 0; i < listeners.size(); i++) {
          l = (MidiDSPPortListener) listeners.elementAt(i);
          l.onMidiReceived(new MidiDSPPortEvent(source, s));
        }

      }

    }
  }

  public interface MidiDSPPortListener {

    public void onMidiReceived(MidiDSPPortEvent e);
  }

  public class MidiDSPPortEvent {

    private MidiDSPPort source;
    private double data1, data2;
    private ShortMessage event;

    public MidiDSPPortEvent(MidiDSPPort source, ShortMessage event) {
      this.source = source;
      this.event = event;

      if (event.getCommand() == ShortMessage.NOTE_OFF ||
          event.getCommand() == ShortMessage.NOTE_ON ||
          event.getCommand() == ShortMessage.CONTROL_CHANGE) {
        data1 = event.getData1() / 127.0;
        data2 = event.getData2() / 127.0;
      }

    }

    /**
     * This method assumes data 1 is a MIDI note number.
     * If the event is not a note event, the output makes no sense.
     */
    public double getCPS() {
      return com.norsez.dsp.block.DSPSystem.getMidiNoteToCPS(event.getData1());
    }

    public final ShortMessage getEvent() {

      return event;
    }

    public String toString (){
      return event.getChannel() + " " + Integer.toHexString( event.getCommand() ) + " " +
          event.getData1() + " " + event.getData2();
    }

    public boolean isNoteOn (){
      return event.getCommand() == ShortMessage.NOTE_ON;
    }

    public boolean isNoteOff (){
      return event.getCommand() == ShortMessage.NOTE_OFF;
    }

    public boolean isControlChange (){
      return event.getCommand() == ShortMessage.CONTROL_CHANGE;
    }

    public boolean isChannelPressure (){
      return event.getCommand() == ShortMessage.CHANNEL_PRESSURE;
    }

    public boolean isPolyPressure (){
      return event.getCommand() == ShortMessage.POLY_PRESSURE;
    }

    public boolean isPitchBend (){
      return event.getCommand() == ShortMessage.PITCH_BEND;
    }

    public boolean isProgramChange (){
      return event.getCommand() == ShortMessage.PROGRAM_CHANGE;
    }






  }
}
