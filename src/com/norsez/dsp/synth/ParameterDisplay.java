package com.norsez.dsp.synth;

import com.norsez.dsp.block.*;
import com.norsez.dsp.block.oscillator.multimode.*;

/**
 * <p>Title: ParameterDisplay </p>
 * <p>Description: This static class helps converts 0 to 1 value into something more meaningful.
 *                 Just call getDisplay ( double ) of the static preset.
 *
 *
 *                 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class ParameterDisplay {

  protected final static java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
  protected final static java.text.DecimalFormat df4 = new java.text.DecimalFormat("0.####");

  public ParameterDisplay() {




  }

  /**
   * @param value must be linear [0,1].
   */
  public String getDisplay(double value) {
    return "" + value;
  }

  public static final ParameterDisplay LINEAR = new ParameterDisplay() {
    public String getDisplay(double v) {
      return df.format(v);
    }
  };

  public static final ParameterDisplay LINEAR_BIPOLAR = new ParameterDisplay() {
    public String getDisplay(double v) {
      return df.format(Interpolation.mapToRangeLinear(0, -1, 1, 1, v));
    }
  };

  public static final ParameterDisplay PAN = new ParameterDisplay() {
    public String getDisplay(double v) {
      if (v == .5) {
        return "center";
      }
      else
      if (v > .5) {
        return (int) (Math.abs(Interpolation.mapToRangeLinear(0, -1, 1, 1, v)) *
                      100) + "R";
      }
      else {
        return (int) (Math.abs(Interpolation.mapToRangeLinear(0, -1, 1, 1, v)) *
                      100) + "L";
      }
    }
  };

  public static final ParameterDisplay EXP_UP = new ParameterDisplay() {
    public String getDisplay(double v) {
      return df.format(Interpolation.linear(Table.T_ENV_UP, v));
    }
  };

  public static final ParameterDisplay LFO_RATE = new ParameterDisplay() {
    public String getDisplay(double v) {
      return df.format(Interpolation.linear(Table.T_LFO_RATE, v) *
                       DSPSystem.getSamplingRate()) + " Hz";
    }
  };

  public static final ParameterDisplay CUTOFF = new ParameterDisplay() {
    public String getDisplay(double v) {
      return df.format(Interpolation.linear(Table.T_CUTOFF, v) *
                       DSPSystem.getSamplingRate()) + " Hz";
    }
  };

  public static final ParameterDisplay NOTE_FREQUENCY = new ParameterDisplay() {
    public String getDisplay(double v) {
      return df.format(Interpolation.linear(Table.T_NOTE2CPS, v) *
                       DSPSystem.getSamplingRate()) + " Hz";
    }
  };

  public static final ParameterDisplay ENV_TIME = new ParameterDisplay() {
    public String getDisplay(double v) {

      String result = "";
      String suffix = "s";

      v = (1.0 / com.norsez.dsp.block.envelope.Envelope.getEnvelopetime(v)) /
                        DSPSystem.getSamplingRate();

      if ( v < 0 && v >= 0.001){
        v = v * Math.pow(10,3);
        suffix = "ms";
      }
      else
      if ( v < 0.001){
        v = v * Math.pow(10, 9);
        suffix ="ns";
      }

        result = df4.format( v ) + suffix;

     return result;
    }
  };

  public static final ParameterDisplay SEMI = new ParameterDisplay() {
    public String getDisplay(double v) {
      return (int) (Interpolation.mapToRangeLinear(0, -MultiModeOsc.SEMI_RANGE,
          1, MultiModeOsc.SEMI_RANGE, v)) + " semi";
    }
  };

  public static final ParameterDisplay FINE = new ParameterDisplay() {
    public String getDisplay(double v) {
      return df.format(Interpolation.mapToRangeLinear(0,
          -1,1, 1, v)) + " Semi";
    }
  };

  public static final ParameterDisplay dB = new ParameterDisplay() {
    public String getDisplay(double v) {
      return df.format(20.0 * Math.log(v)) + " dB";
    }
  };

  public static final ParameterDisplay ON_OFF = new ParameterDisplay() {
    public String getDisplay(double v) {
      if (v == 0) {
        return "off";
      }
      else {
        return "on";
      }
    }
  };

  public static final ParameterDisplay DIGIWAVE = new ParameterDisplay() {
    public String getDisplay(double v) {
      return "digi " +
          (int) (v * com.norsez.dsp.block.oscillator.DigiWave.getDigiWavesCount());
    }
  };

  public static final ParameterDisplay NOTENAME = new ParameterDisplay() {
    public String getDisplay(double v) {
      return Table.T_NOTE_NAMES[ (int) (v * (Table.T_NOTE_NAMES.length - 1))];
    }
  };

  public static final ParameterDisplay DIGIMORPHWAVE = new ParameterDisplay() {
    public String getDisplay(double v) {
      return "digi table " +
          (int) (v * com.norsez.dsp.block.oscillator.DigiWave.getDigiMorphWavesCount());
    }
  };

  public static final ParameterDisplay BPM = new ParameterDisplay() {
    public String getDisplay(double v) {
      return "" +
          df.format(Interpolation.mapToRangeLinear(0, DSPSystem.MIN_BPM, 1,
          DSPSystem.MAX_BPM, v)) + "bpm";
    }
  };

}
