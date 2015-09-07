package com.norsez.dsp.synth;

/**
 * <p>Title: VoiceManagerPoly</p>
 * <p>Description: The polyphonic <b>input</b> (as opposed to output which is always polyphonic)
 *                 of its parent class, VoiceManager. Call tick (double [] noteGates) at every
 *                 sample. noteGates assumes the size of 128 cells of doubles. Each cell represents
 *                 each midi note's gate value. When there are changes in these gate vales, the
 *                 VoiceManagerPoly will automatically allocate voices using its parent's tick(double)
 *                 method. For a typical use of this call, see VoiceManager. </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class VoiceManagerPoly extends VoiceManager{

  /**
   * table for a fast lookup conversion of midi note number to rawNote
   */
  private static final double rawNoteValues [] = getRawNoteValues ();

  /**
   * Maintains the history of input note gates for comparing with the new one.
   */
  private double [] lastNoteGates;

  /**
   * Keeps track of how many voices are being used.
   */
  private int usedVoiceCount;

  private static double [] getRawNoteValues (){
    double [] table = new double [128];

    for(int i=0; i< 128; i++){
      table[i] = i/127.0;
    }
    return table;
  }

  public VoiceManagerPoly(int numOfVoices) {
    super ( numOfVoices );
    lastNoteGates = new double [128];
  }

  public int getUsedVoiceCount (){
    return this.usedVoiceCount;
  }

  /**
   *
   * @param noteGates assumes 128 in size. noteGates [i] represents the gate value of the midi note i.
   */
  public void tick (double [] noteGates){


    for (int i = 0; i < 128; i++){

      if (usedVoiceCount >= this.getActiveVoiceCount())
        return;

      if (noteGates[i] != lastNoteGates[i]){
        //note on
        if ( noteGates[i] !=0 && lastNoteGates[i] ==0){
          usedVoiceCount++;

        }
        else
        //note off
        if (noteGates[i] ==0 && lastNoteGates[i] != 0){
          usedVoiceCount--;
        }

        super.tick(noteGates[i],rawNoteValues[i]);
      }
      lastNoteGates[i] = noteGates[i];
    }
  }


  public String toString (){
    java.text.DecimalFormat df = new java.text.DecimalFormat("0.0000");
    String s = super.toString();
    s += "\nlastNoteGates" + printArray (this.lastNoteGates);
    s += "\nVoices Used = " + this.usedVoiceCount;

    return s;
  }

  public static void main(String[] args) {
    //VoiceManagerPoly voiceManagerPoly1 = new VoiceManagerPoly();
  }

}