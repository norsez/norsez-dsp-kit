package com.norsez.dsp.block.envelope;

import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Interpolation;
import com.norsez.dsp.block.Table;
import com.norsez.dsp.synth.Parameter;
import com.norsez.dsp.synth.ParameterDisplay;
import com.norsez.dsp.synth.ParameterManager;

/**
 * <p>Title:  EnvelopeADSR</p>
 * <p>Description: Attack Decay Sustain Release Envelope. DSP quality. Should be updated at sample rate.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class EnvelopeADSR
        extends Envelope implements com.norsez.dsp.block.modulation.ModSource {

    private double attack, decay, _attack, _decay, sustain, release;
    private boolean isGateReset;

    public EnvelopeADSR() {
        //stage = Stage.ATTACK;
        stage = Stage.RELEASE;
        stealablePoint = DSPSystem.STEALABLE_POINT;
        setAttack(0);
        setDecay(0);
        setSustain(1);
        setRelease(.2);
    }

    public void setGateReset(boolean b) {
        this.isGateReset = b;
    }

    public void setAttack(double time) {
        _attack = time;
        attack = this.getEnvelopetime(time);

    }

    public void setSustain(double time) {

        sustain = time;

    }

    public void setRelease(double time) {

        release = this.getEnvelopetime(time);

    }

    public void setDecay(double time) {
        _decay = time;
        decay = this.getEnvelopetime(time);

    }

    public double getAttack() {
        return _attack;
    }

    public double getDecay() {
        return _decay;
    }

    /**
     * Forces envelope to restart.
     */
    public void retrigger() {
        point = 0;
        lastValue = 0;
        lastValueOfLastStage = 0;
        stage = Stage.ATTACK;
    }

    /**
     * Forces envelope to go to its release stage.
     */
    public void release() {

        lastValueOfLastStage = lastValue;
        point = 0;
        /*
             if (stage == Stage.ATTACK ||
            stage == Stage.DECAY){
          lastValue = lastValueOfLastStage = sustain;
             }
         */

        stage = Stage.RELEASE;

    }

    /**
     * @param gate Gate value input. Must be called at every sample for the best result.
     * @return the output of the envelope
     */
    public double tick(double gate) {

        if (lastGate == 0 && gate == 0 && stage != Stage.RELEASE) {
            return lastValue;
        } else if (lastGate == 0 && gate > 0) {
            retrigger();
            //System.out.println ("ken triggered" );

        } else if (lastGate != 0 && gate == 0) {
            release();
            //System.out.println ("key release" );
        }

        if (stage == Stage.ATTACK) {
            if (point + attack < 1) {
                lastValue = Interpolation.linear(Table.T_ENV_UP, point);
                lastValueOfLastStage = lastValue;
                point += attack;
            } else {
                stage = Stage.DECAY;
                point = 0;
                lastValue = lastValueOfLastStage = 1;
                //    System.out.println ("going decay" );
            }

        } else if (stage == Stage.DECAY) {

            if (point + decay < 1 && lastValue > sustain &&
                    lastValue > stealablePoint) {

                //System.out.println ("Decay laststage =" + lastValueOfLastStage );
//
                //lastValue = Interpolation.linear(Table.T_ENV_DOWN_LOG,point,sustain, lastValueOfLastStage) ;
                lastValue = Interpolation.linear(Table.T_ENV_DOWN_LOG, point);
                lastValue = (lastValueOfLastStage - sustain) * (lastValue) + sustain;
                //lastValueOfLastStage = lastValue;
                point += decay;
            } else {
                stage = Stage.SUSTAIN;
                lastValueOfLastStage = lastValue = sustain;
                point = 0;
                //System.out.println ("going sustain" );
            }

        } else if (stage == Stage.SUSTAIN && lastValue > stealablePoint) {
            lastValueOfLastStage = lastValue = sustain;

        } else { //release

            if (point + release < 1 && lastValue > stealablePoint) {

                lastValue = Interpolation.linear(Table.T_ENV_DOWN_EXP, point) *
                        lastValueOfLastStage;

                point += release;
            } else {

                if (this.isGateReset)
                    lastValue = 0;

                lastValueOfLastStage = lastValue;
                point = 0;
            }

        }

        lastGate = gate;
        return lastValue;

    }

    /*
       public static void main(String[] args) {
      com.norsez.dsp.block.SampleClock clock = new com.norsez.dsp.block.SampleClock();
      com.norsez.dsp.block.ArpSwing arp = new com.norsez.dsp.block.ArpSwing(8);
      int len = 12;
      arp.setRawSampleLength(len);
      arp.setSwing(.25);
      arp.setGateLength(.7);
      EnvelopeADSR env = new EnvelopeADSR();
      env.setRawEnvelope(1 / 1.5, 1 / 5.0, .256, 1 / 5.0);
      arp.setActive(true);
      for (int i = 0; i < 100; i++) {
        if (i % len == 0) {
          DSPSystem.DEBUG_OUT.println("**************Tick! ");
        }
        DSPSystem.DEBUG_OUT.println("sample#" + i + " "
                                    + clock.getLastValue()
                                    + "\narp::" + arp
                                    + "\nEnv::" + env + "\n");
        clock.tick();
        arp.tick(clock.getLastValue());
        env.tick(arp.getLastValue());
      }
      DSPSystem.DEBUG_OUT.close();
       }
     */

    /**
     * @return some info about the envelope's current stage.
     */
    public String toString() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.0000");
        if (lastValue > 0) {
            return "gate " + lastGate
                    + ", amp = " + df.format(getLastValue())
                    + ", stage = " + stage
                    ;
        } else {
            return " inactive";
        }
    }

    /**
     * @return whether the envelope is <i>not</> in the Release stage
     */
    public boolean isInUse() {
        return stage != Stage.RELEASE;
    }

    /**
     * @return whether the envelope is in the Release stage <b>and</b> the lastValue (output) is lower than DSPSystem.STEALABLE_POINT
     */
    public boolean isStealable() {
        return lastValue <= stealablePoint &&
                stage == Stage.RELEASE;
    }

    /**
     * @return the current stage of the envelope.
     */
    public Stage getStage() {
        return stage;
    }

    public double getValue() {
        return lastValue;
    }

    /**
     * The times won't be scaled propriately for the sampling rate.
     * This is for debugging purposes.
     *
     * @param a attack
     * @param d decay
     * @param s sustain
     * @param r release
     */
    public void setRawEnvelope(double a, double d, double s, double r) {
        attack = a;
        decay = d;
        sustain = s;
        release = r;
    }


    public void addToParameterManager(ParameterManager pm, String groupName, String prefix) {

        pm.addParameter(new Parameter(prefix + "Attack", 0, groupName, ParameterDisplay.ENV_TIME));
        pm.addParameter(new Parameter(prefix + "Decay", .39, groupName, ParameterDisplay.ENV_TIME));
        pm.addParameter(new Parameter(prefix + "Sustain", 0.1, groupName, ParameterDisplay.dB));
        pm.addParameter(new Parameter(prefix + "Release", 0.4, groupName, ParameterDisplay.ENV_TIME));

    }

}
