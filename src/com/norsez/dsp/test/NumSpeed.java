package com.norsez.dsp.test;
import java.util.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class NumSpeed {
  public NumSpeed() {


  }


  public  void test1 (){

    final int TIMES = 1500 * 48000;
    double d;
    Double dd;

    long start = new Date ().getTime();


    for (int i=0; i < TIMES;i++){

      Math.sqrt(2);
    }

    System.out.println (new Date().getTime() - start);

    dd = new Double(2.0);
    start = new Date().getTime();

    for (int i=0; i < TIMES;i++){

      Math.sqrt(dd.doubleValue());
    }

    System.out.println (new Date().getTime() - start);

  }

  public static class DoMath {

    public void doSqrt(){
      //Math.sqrt(12.0);
      Math.sin(Math.random());
    }
  }

  public void test2 (){

    final int LEN = 48000 * 20;
    Object [] d = new Object [LEN];
    for(int i=0; i < d.length; i++){
      d[i] = new DoMath();
    }
    Vector dd = new Vector ();
    for(int i=0; i < d.length; i++){
      dd .add (new DoMath(  ) );
    }


    long start = new Date ().getTime();


    for (int i=0; i < LEN;i++){
      ((DoMath)d[i]).doSqrt();;
    }

    System.out.println (new Date().getTime() - start);

    start = new Date().getTime();

    for (int i=0; i < LEN;i++){

      ((DoMath)dd.elementAt(i)).doSqrt();
    }

    System.out.println (new Date().getTime() - start);

  }

  public static void main(String[] args) {
    NumSpeed numSpeed1 = new NumSpeed();
    numSpeed1.test2();
  }

}