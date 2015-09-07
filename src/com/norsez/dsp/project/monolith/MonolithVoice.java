package com.norsez.dsp.project.monolith;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import com.norsez.dsp.block.*;
import com.norsez.dsp.synth.*;
import com.norsez.dsp.synth.swing.*;
import com.norsez.dsp.test.*;
import com.norsez.dsp.block.envelope.*;
import com.norsez.dsp.block.filter.*;
import com.norsez.dsp.block.filter.effects.*;
import com.norsez.dsp.block.oscillator.multimode.*;
import com.norsez.dsp.block.sequencer.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class MonolithVoice extends VoiceModel {

  public EnvelopeADSR filterEnv, ampEnv, env1, env2;
  public FatMoogFilter moogFilterL,moogFilterR;
  public OscA osc1,osc2,osc3;
  public LFO lfo1, lfo2, lfo3, lfo4;

  public MonolithVoice() {
  }

  public boolean isStealable (){
    return ampEnv.isStealable();
  }

  public boolean isInUse (){
    return ampEnv.isInUse();
  }

  public void retrigger(){
    ampEnv.retrigger();
    filterEnv.retrigger();
    env1.retrigger();
    env2.retrigger();
  }

  public void setCps (double cps ){
    throw new java.lang.UnsupportedOperationException("set cps of each osc by yourself with their own tuning");
  }



  public void tickControl (int steps,
                           double bpm,
                           double lfo1cps,
                           double lfo2cps,
                           double resonance,
                           double digiwave1, double digiwave2, double digiwave3,
                           double ssawdetune1, double ssawdetune2, double ssawdetune3,
                           double pwm1, double pwm2, double pwm3){

    lfo1.tick(steps);
    lfo1.setRate(lfo1cps);
    lfo2.tick(steps);
    lfo1.setRate(lfo2cps);
    lfo3.tick(steps);
    lfo4.tick(steps);



    osc1.setDigiWave(digiwave1);
    osc2.setDigiWave(digiwave2);
    osc3.setDigiWave(digiwave3);

    osc1.setPWM(pwm1);
    osc2.setPWM(pwm2);
    osc3.setPWM(pwm3);

    osc1.setSuperSawDetune(ssawdetune1);
    osc2.setSuperSawDetune(ssawdetune2);
    osc3.setSuperSawDetune(ssawdetune3);

    moogFilterL.setResonance(resonance);
    moogFilterR.setResonance(resonance);
  }

  final static double ONE_OVER_THREE = 1.0/Math.sqrt(3.0);

  public void tick (){
    throw new java.lang.UnsupportedOperationException("use tickMod() instead.");
  }

  double temp,temp1,temp2,temp3;
  public void tickMod (double gate,
                       double pan,
                       double volume,
                       double cps1,
                       double cps2,
                       double cps3,
                       double vol1, double pan1,
                       double vol2, double pan2,
                       double vol3, double pan3,
                       double cutoff

                       ){


    if (volume == 0){
      lastValueR = lastValueL =0;
      return;
    }

    ampEnv.tick(gate);
    filterEnv.tick(gate);
    env1.tick(gate);
    env2.tick(gate);

    osc1.tick();
    osc2.setSync(osc1.getPhase());
    osc2.tick();
    osc3.setSync(osc2.getPhase());
    osc3.tick();

    temp1 = osc1.getLastValue() * vol1;
    temp2 = osc2.getLastValue() * vol2;
    temp3 = osc3.getLastValue() * vol3;

    temp = cutoff * filterEnv.getLastValue();
    moogFilterL.setCutoff(temp);

    moogFilterR.setCutoff(temp);


    moogFilterL.tick(ONE_OVER_THREE * (temp1 * pan1 +
                                       temp2 * pan2 +
                                       temp3 * pan3));

    moogFilterR.tick(ONE_OVER_THREE * (temp1 * (1. - pan1) +
                                     temp2 * (1. - pan2) +
                                     temp3 * (1. - pan3)));


    temp = ampEnv.getLastValue() * volume;
    temp1 = moogFilterL.getLastValue()* temp;
    temp2 = moogFilterR.getLastValue()* temp;
    lastValueL = temp1 * pan;
    lastValueR = temp2 * ( 1.0- pan );

  }

}