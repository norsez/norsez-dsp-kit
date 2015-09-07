package com.norsez.dsp.test;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class EnumTest {

  final int MARK = new Enum ().getInt();
  final int TOM = new Enum ().getInt();
  final int BRAIN = new Enum ().getInt();
  final int RICK = new Enum ().getInt();
  final int TEDDY = new Enum (0).getInt();

  public EnumTest() {

    System.out.println (MARK);
    System.out.println (TOM);
    System.out.println (BRAIN);
    System.out.println (RICK);
    System.out.println (TEDDY);
    System.out.println (Test2.RADO);
    System.out.println (Test2.POTATO);

  }

  static class Test2{
    public static final int POTATO = new Enum(0).getInt();
    public static final int TOMATO = new Enum().getInt();
    public static final int RADO =new Enum().getInt ();
    public static final int BIBO =new Enum(0).getInt ();
    public static final int MEADOW =new Enum().getInt ();
  }

  public static void main (String [] args ){
    new EnumTest ();
  }

  static class Enum {

    static int i;


    public Enum (){
      i++;
    }

    public Enum (int i ){
      this.i = i;
    }

    public int getInt (){
      return i;
    }

    public String toString(){
      return ""+i;
    }
  }
}
