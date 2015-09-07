package com.norsez.dsp.block.envelope;

import com.norsez.dsp.block.Interpolation;
import com.norsez.dsp.block.Table;

/**
 * <p>Title: EnvelopeAD</p>
 * <p>Description: Attack Decay Envelope</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class EnvelopeAD
        extends Envelope {

    private double attack, decay, _attack, _decay;

    public EnvelopeAD() {
        stage = Stage.ATTACK;
        stealablePoint = 0.25e-20; // -950 dB

    }

    public void setAttack(double time) {
        _attack = time;
        attack = this.getEnvelopetime(time);
        //attack = time;
    }

    public void setDecay(double time) {
        _decay = time;
        decay = this.getEnvelopetime(time);
        //System.out.println ((1.0 / decay) / 44100.0);
        //decay = time;
    }

    public double getAttack() {
        return _attack;
    }

    public double getDecay() {
        return _decay;
    }

    public void retrigger() {
        point = 0;
        lastValue = 0;
        lastValueOfLastStage = 0;
        stage = Stage.ATTACK;
    }

    public double tick(double gate) {

        if (lastGate == 0 && gate > 0) {
            retrigger();

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
            }

        } else { //decay stage

            if (point + decay < 1 && lastValue > stealablePoint) {

                lastValue = Interpolation.linear(Table.T_ENV_DOWN_EXP, point) *
                        lastValueOfLastStage;

                point += decay;
            } else {
                lastValue = 0;
                lastValueOfLastStage = lastValue;
                point = 0;
            }

        }

        lastGate = gate;
        return lastValue;

    }

    public static void main(String[] args) {
        EnvelopeAD env = new EnvelopeAD();
        env.setAttack(0.1);
        env.setDecay(.00000007);
        for (int i = 0; i < 44100; i++) {
            if (i > 25000) {
                System.out.println(env.tick(0));
            } else {
                System.out.println(env.tick(1));
            }
        }
    }

}