package com.norsez.dsp.test;

public class curve {
  public curve() {


  }

  public void run (){
    java.util.Locale.setDefault(new java.util.Locale("En","US"));
    java.text.DecimalFormat df = new java.text.DecimalFormat ("0.0000" );


    double [] values = {1.0/127.0, 15/127.0, 25/127.0,35/127.0,45/127.0,50/127.0,60/127.0,90/127.0,1.0};
    for(int i=0; i < values.length; i++){
     System.out.println (
      df.format(values[i]*127)
      + "," +
      df.format(scaler(values[i]))
                         + "," + df.format( 0.001* getValueInSecs ( curve ( scaler ( values [i]) ) )) );
    }

  }

  final double FS = 44100.00;
  final double K_CURVE = 10.0;
  final double SCALE = .5;
  private double scaler ( double v ) {

    return 1- ( Math.exp(SCALE * (1-v)) - 1.0 ) / (Math.exp(SCALE  ) - 1.00);

  }

  private double curve (double value){
    return ( Math.exp(K_CURVE * value) - 1.0 ) / (Math.exp(K_CURVE  ) - 1.00);
  }

  private double getValueInSecs ( double v ){
    return (FS/(1.0/v));
  }

  public static void main(String args [] ){
    new curve ().run ();
  }
}
