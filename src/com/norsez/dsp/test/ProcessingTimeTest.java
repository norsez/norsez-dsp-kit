package com.norsez.dsp.test;

public class ProcessingTimeTest {
  ToTest t;
  public ProcessingTimeTest() {

  }

  public static void test ( ToTest t , int times ){

    long start;
    long delta [] = new long [ times ];

    for(int i=0; i < times;i++){
      start = new java.util.Date().getTime();

      t.run();

      delta [i] = new java.util.Date().getTime() - start;
    }

    //find mean
    double sum  = 0;
    for(int i=0; i< times;i++)
      sum += delta[i];

    System.out.println ("Avg Processing Time is " + (sum/times) + " milliseconds.");


  }

  public static double getStandardDeviation (long [] deltas){
    double sum = 0;
    for (int i = 0; i < deltas.length; i++)
      sum += deltas[i];
    double x_bar = sum / deltas.length;

    sum = 0;
    for (int i = 0; i < deltas.length; i++)
      sum += Math.pow(deltas[i] - x_bar,2);

    return Math.sqrt(sum/deltas.length );
  }


  public static void test ( ToTest t , int times , long samples){

    long start;
    long delta [] = new long [ times ];

    for(int i=0; i < times;i++){
      //System.out.println ("lapse number " + (i+1));
      start = new java.util.Date().getTime();

      for (long l = 0; l < samples; l++) {
        t.run();
      }


      delta [i] = new java.util.Date().getTime() - start;
    }

    //find mean
    double sum  = 0;
    for(int i=0; i< times;i++)
      sum += delta[i];

    System.out.println ("Avg Processing Time is " + (sum/times) + " milliseconds. (with a std dev of "
                        + ProcessingTimeTest.getStandardDeviation (delta)+ ")");


  }




  public interface ToTest {
    public void run ();
  }

}

