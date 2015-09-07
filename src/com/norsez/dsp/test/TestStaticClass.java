package com.norsez.dsp.test;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class TestStaticClass {

  public TestStaticClass() {

    SObject s1 = new SObject(1);
    SObject s2 = new SObject(2);

    System.out.println(s1);
    System.out.println(s2);

  }

  static class SObject {
    private int num;
    private static Num n = new Num(7);
    public SObject(int i) {
      num = i;
      n.setI(6);
    }

    public String toString() {
      return " = " + num + n;
    }

    static class Num {
      int num;
      public Num(int i) {
        num = i;

      }

      public void setI(int i) {
        num = i;
      }

      public String toString() {
        return " >> " + num;
      }
    }
  }

  public static void main(String[] args) {
    TestStaticClass testStaticClass1 = new TestStaticClass();
  }
}