package com.norsez.dsp.synth;

public abstract class VstSynthModel {



  public static class Program {
    //the data part should contains only edittable controller values.
    public double [] data;
  }




  protected Program  [] programs;



  protected double controller_values [];
  protected double synth_values [];


  public VstSynthModel (){

  }

  public void setParameter ( int index, double value){}
  public double getParameter ( int index ){return  0;}
  public String getParameterName ( int index ){return "";}
  public String getParameterDisplay ( int index ){return "";}
  public String getParameterLabel ( int index) {return "";}
  public String getGroupName ( int index) { return "";}
  public void setProgramName ( String name ){}
  public int getNumGroups () {return 0;};
  public int getNumPrograms ( ) {return 0;}
  public int getNumParameters () {return 0;}
  public int getParameterGroup (int index ){return 0;};
  public void process ( double [][] inputs, double [][] outputs, int sampleFrames ){processReplacing (inputs,outputs,sampleFrames);}
  public void processReplacing ( double [][] inputs, double [][] outputs, int sampleFrames ){}
  public int getMainVolumeParamNumber () {return 0;};
  public Program getCurrentProgram (){return new Program();};

  public abstract void initProgram ();

  protected boolean isBypassed;
  public void setBypass (boolean v ){
    isBypassed = v;
  };


  protected static final java.text.DecimalFormat dec2 = new java.text.DecimalFormat("0.###");
  public static String parameter2dB (double v){
    return dec2.format(20.0 * Math.log( v)) + " dB";
  }

  public static String parameterRound (double v){
    return dec2.format(v);
  }

  public static String parameter2Hz (double cps ){
    return dec2.format( cps * com.norsez.dsp.block.DSPSystem.getSamplingRate()  ) + " Hz";
  }






}
