package com.norsez.dsp.test;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class BlockTest {
  final int times = 1000;

  long start;
  Test1 test1;
  public BlockTest() {

    test1 = new Test1();
    double[] buf = new double[10];
    start = new java.util.Date().getTime();
    for (int i = 0; i < buf.length; i++) {
      test1.tick();
    }
    System.out.println("sample by sample " +
                       ( (new java.util.Date().getTime()) - start));

    start = new java.util.Date().getTime();
    test1.tick(buf);
    System.out.println("block " + ( (new java.util.Date().getTime()) - start));
  }

  public static void main(String[] args) {
    new BlockTest();
  }

  class Sine {

    public double tick() {

      double t = 0;

      for (int i = 0; i < times; i++) {
        t += Math.sin(0.536 * Math.PI / i);
      }
      return t;
    }

    public void tick(double[] buf) {
      for (int i = 0; i < buf.length; i++) {
        for (int j = 0; j < times; j++) {
          buf[i] += Math.sin(0.536 * Math.PI / i);
        }
      }
    }
  }

  class Test1 {
    Sine s = new Sine();

    public double tick() {

      double t = 0;

      for (int i = 0; i < times; i++) {
        t += Math.sin(0.536 * Math.PI / i);
        t -= s.tick();
      }
      return t;
    }

    public void tick(double[] buf) {
      double[] b = new double[buf.length];
      for (int i = 0; i < buf.length; i++) {
        for (int j = 0; j < times; j++) {
          buf[i] += Math.sin(0.536 * Math.PI / i);
        }

      }

      s.tick(b);
      for (int i = 0; i < b.length; i++) {
        buf[i] -= b[i];
      }
    }
  }
}