package com.norsez.dsp.synth.swing;
import com.norsez.dsp.synth.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class DraggerModel {
  private int tag;

  private SynthModel3 synth;
  private int min, max, init, value;
  private java.awt.Color bgColor, fgColor;

  public DraggerModel(SynthModel3 synth, int tag, int min, int max, int init) {

    if (min > max) throw new java.lang.IllegalArgumentException("min > max");
    if (init < min || init > max ) throw new java.lang.IllegalArgumentException("init must be in between min and max");

    this.tag = tag;
    this.synth = synth;
    this.max = max;
    this.min = min;
    this.init = init;
    this.value = init;

    bgColor = java.awt.Color.black;
    fgColor = java.awt.Color.green;
  }

  public void setTag (int t ){
    this.tag = t;
    value = (int)( synth.getParameterValue(t) * this.max);
  }

  public void increment (){
    if (value < max ){
      value++;

    }

    synth.setParameterValue(tag,value / (double)max);
  }

  public int getMinimum (){
    return min;
  }

  public int getMaximum(){
    return max;
  }

  public void decrement (){
    if( value > min)
      value--;
    synth.setParameterValue(tag,value / (double)max);
  }

  public void increment10 (){
    for(int i=0; i < 10; i++){
      increment();
    }
    synth.setParameterValue(tag,value / (double)max);
  }

  public void decrement10(){
    for(int i=0; i < 10; i++){
      decrement();
    }
    synth.setParameterValue(tag,value / (double)max);

  }

  public void setValue (int v){
    if (v < min || v > max ) throw new java.lang.IllegalArgumentException("value must be in between min and max");
    value = v;
    synth.setParameterValue(tag, value / (double)max);
  }

  public String getValueDisplay ( ){
    return synth.getParameterDisplay( tag );
  }

  public String getParameterName (){
    return synth.getParameterName ( tag );
  }

  public int getValue (){
    return value;
  }

  public void reset (){
    synth.setParameterValue(tag,init / (double)max);
    value = init;
  }

  public void setForegroundColor ( java.awt.Color c ){
    fgColor = c;
  }

  public void setBackgroundColor ( java.awt.Color c ){
    bgColor = c;
  }

  public java.awt.Color getForegroundColor (){
    return fgColor;
  }

  public java.awt.Color getBackgroundColor (){
    return bgColor;
  }

  public static interface DraggerListener {
    public void draggerChanged ( DraggerEvent e );
  }

  public static class DraggerEvent{

  }

}
