package com.norsez.dsp.synth;

/**
 * <p>Title: Enum </p>
 * <p>Description: Works to do the C++ enum integer thing.  For example,
 *                 <blockquote>
 *                  class Test {<br>
 *                     final int TOMATO = new Enum (0).getInt (); //0<br>
 *                     final int ORANGE = new Enum ().getInt (); //1<br>
 *                     final int APPLE = new Enum ().getInt ();  //2<br>
 *                     final int GRAPE = new Enum (0).getInt (); //0<br>
 *                     final int MANGO = new Enum ().getInt ();  //1<br>
 *                  }<br>
 *                 </blockquote> </p><p>This can be useful for universal index
 *                 as used in VST plug-in programming.
 *
 *
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Counter {

    static int i;


    public Counter (){
      i++;
    }

    public Counter (int i ){
      this.i = i;
    }

    public int getInt (){
      return i;
    }

    public String toString(){
      return ""+i;
    }
  }
