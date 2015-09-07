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

public class MemTest {


  public MemTest() {
    /*
    Hashtable h = new Hashtable ();
    for (int i=0; i < 256; i++){
      h.put(new Integer(i), Integer.toHexString(i));
    }
        */



  }

  public static void main (String [] args ){

    MemTest m = new MemTest ();

    Runtime r = Runtime.getRuntime();
    System.out.println (r.maxMemory() + " " + (r.totalMemory() - r.freeMemory()));

  }

}