package com.norsez.dsp.test;

import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class FuncTest {

  func f = new func();
  long start;
  public FuncTest() {

    final int times = 2000000;
    start = new Date().getTime();

    for (int i = 0; i < times; i++) {
      f.tick(i);
    }

    System.out.println(new Date().getTime() - start);

    start = new Date().getTime();

    for (int i = 0; i < times; i++) {
      if (i == 3) {
        Math.sin(0.58 * i);
      }
      else
      if (i == 100) {
        Math.sin(0.58 * i);
      }
      else
      if (i == 9) {
        Math.sin(0.58 * i);
      }
      else {
        Math.sin(0.58 * i);
      }
    }

    System.out.print(new Date().getTime() - start);

  }

  class func {
    public double tick(int i) {
      return Math.sin(0.58 * i);
    }
  }

  public static void main(String[] args) {
    FuncTest funcTest1 = new FuncTest();
    Runtime rt = Runtime.getRuntime();
    System.out.println("\nMemory used = " +
                       (rt.totalMemory() - rt.freeMemory()) * 0.001 +
                       " kb");
  }
}