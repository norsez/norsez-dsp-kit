package com.norsez.dsp.test;
import com.norsez.dsp.midi.*;
import java.util.*;
import javax.sound.midi.*;
public class MidiTest implements MidiDSPPort.MidiDSPPortListener{

  MidiDSPPort port;

  public MidiTest() {
    try{

      MidiDevice.Info info [] = MidiSystem.getMidiDeviceInfo();
      /*
      for(int i=0; i< info.length; i++){
        System.out.println( i+ " "+ info [i].toString()) ;
      }
      */
      MidiDevice dev = MidiSystem.getMidiDevice(info[27]);

      port = new MidiDSPPort(dev);
      dev.open();
      port.addPortListener(this);
    }catch(Exception e ){
      e.printStackTrace();
    }

  }

  public void onMidiReceived ( MidiDSPPort.MidiDSPPortEvent e ){

    System.out.println(e);
  }

  public static void main ( String [] arg){
    new MidiTest ();
  }
}
