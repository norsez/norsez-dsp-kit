package com.norsez.dsp.block.envelope;


/**
 * <p>Title: EnvelopeAHR</p>
 * <p>Description: Attack-Hold-Release Envelope. In the hold stage, it will hold the entire hold time, even when the gate is off during. </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */
public class EnvelopeAHR extends Envelope {

    public double attackTime, holdTime, releaseTime;
    private double delta, lastGate;
    private Stage stage;
    private boolean reset;

    public EnvelopeAHR() {

        stage = Stage.ATTACK;
    }


    public void setReset(boolean reset) {
        this.reset = reset;
    }

    /**
     * Reset to Attack stage.
     *
     * @param reset boolean if true, lastValue is also set to zero.
     */
    public void retrigger() {
        stage = Stage.ATTACK;
        delta = 0;
        if (reset) lastValue = 0;
    }

    /**
     * @param gate double 1 is gate on, 0 is gate off
     * @return double envelope energy output.
     */
    public double tick(double gate) {

        //gate management
        if (lastGate >= 1.0 && gate < 1.0 && stage == Stage.ATTACK) {
            stage = Stage.RELEASE;
            delta = 0;
        } else if (lastGate < 1.0 && gate >= 1.0) {
            stage = Stage.ATTACK;
            delta = 0;
            if (reset) lastValue = 0;
        }


        //envelope work
        if (stage == Stage.ATTACK && gate >= 1.0) {

            if (delta >= 1) {
                lastValue = 1.0;
                delta = 0;
                stage = Stage.HOLD_S;
            } else {
                delta += attackTime;
                lastValue = com.norsez.dsp.block.Interpolation.linear(com.norsez.dsp.block.Table.T_ENV_UP, delta);
            }

        } else if (stage == Stage.HOLD_S) {

            if (delta >= 1) {
                lastValue = 1.0;
                delta = 0;
                stage = Stage.RELEASE;
            } else {
                delta += holdTime;

                lastValue = 1.0;
            }

        } else if (stage == Stage.RELEASE) {
            if (delta >= 1) {
                lastValue = 0;

            } else {
                delta += releaseTime;
                lastValue = com.norsez.dsp.block.Interpolation.linear(com.norsez.dsp.block.Table.T_ENV_DOWN_EXP, delta);

            }

        }
        lastGate = gate;
        return lastValue;
    }


    public String toString() {
        double samplingrate = 44100.0;
        return "a=" + this.envTimetoSeconds(attackTime, samplingrate)
                + ",h=" + this.envTimetoSeconds(holdTime, samplingrate)
                + ",r=" + this.envTimetoSeconds(releaseTime, samplingrate) + ",delta=" + delta;
    }


    public static void main(String[] args) {
        EnvelopeAHR env = new EnvelopeAHR();
        env.attackTime = 1 / 2.0;
        env.holdTime = 1 / 15.0;
        env.releaseTime = 0.25;

        System.out.println(env.tick(0));
        System.out.println(env.tick(0));
        System.out.println(env.tick(1));
        System.out.println(env.tick(1));
        System.out.println(env.tick(1));
        System.out.println(env.tick(0));
        System.out.println(env.tick(0));
        System.out.println(env.tick(0));
        System.out.println(env.tick(0));
        System.out.println(env.tick(0));
        System.out.println(env.tick(0));
    }

}
