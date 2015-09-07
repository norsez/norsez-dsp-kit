package com.norsez.dsp.block.oscillator.multimode;

import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Table;
import com.norsez.dsp.block.oscillator.DigiWave;
import com.norsez.dsp.block.oscillator.SuperSaw;
import com.norsez.dsp.block.oscillator.SyncableOversampledOsc;
import com.norsez.dsp.synth.Parameter;
import com.norsez.dsp.synth.ParameterDisplay;
import com.norsez.dsp.synth.ParameterManager;

/**
 * <p>Title: OscA</p>
 * <p>Description: Encapsulates an bandlimited oscillator unit that does the most basic oscillator job,
 * i.e. having selectable waveforms, namely Sine, Tri, PWM, Saw, SuperSaw, Digi and
 * Digix2 (morphing of two Digi mode's waves) . It can be used as either the slave or
 * the master in oscillator synchronization (except for SuperSaw).</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class OscA
        extends MultiModeOsc implements com.norsez.dsp.block.modulation.ModSource {

    protected final static int NUM_MODES = 7;
    protected final static int OS_TIMES = 4;
    protected double isSynced;
    protected double pwm;

    public final static OscMode SHAPE_SAW = new OscMode("Saw");
    public final static OscMode SHAPE_TRIANGLE = new OscMode("Triangle");
    public final static OscMode SHAPE_PULSE = new OscMode("Pulse");
    public final static OscMode SHAPE_SINE = new OscMode("Sine");
    public final static OscMode SHAPE_SUPERSAW = new OscMode("SuperSaw");
    public final static OscMode SHAPE_DIGI = new OscMode("Digi");
    public final static OscMode SHAPE_DIGI_MORPH = new OscMode("Digi x2");

    protected SyncableOversampledOsc osc;

    protected DigiWave digi;

    protected SuperSaw supersaw;

    public OscA() {

        osc = new SyncableOversampledOsc(OS_TIMES);
        makeModes();
        setOscMode(SHAPE_SAW);

        digi = new DigiWave();

        supersaw = new SuperSaw();

    }

    /**
     * Sets SuperSaw's effect level
     *
     * @param m mix level
     */
    public void setSuperSawMix(double m) {
        supersaw.setMix(m);
    }

    /**
     * Sets SuperSaw's detune value
     *
     * @param d detune value
     */
    public void setSuperSawDetune(double d) {
        supersaw.setDetune(d);
    }

    /**
     * Selects Digi wave table.
     *
     * @param i digi wave select value
     */
    public void setDigiWave(double i) {
        digi.setDigiWave(i);
    }

    /**
     * Selects Digix2 table
     *
     * @param w digi morph select value
     */
    public void setDigix2Wave(double w) {
        digi.setMorphPair(w);
    }

    /**
     * Selects Digix2 mod amount
     *
     * @param m morph value for digix2
     */
    public void setDigix2Amount(double m) {
        digi.setMorphAmount(m);
    }

    public void setOscMode(OscMode o) {
        super.setOscMode(o);

        if (oscMode == SHAPE_SINE) {
            osc.setWavetable(Table.T_BL_SAW);
            osc.setDrive(0.0);
        } else {
            osc.setDrive(1);
            if (o == SHAPE_SAW) {
                osc.setWavetable(Table.T_BL_SAW);
            } else if (o == SHAPE_PULSE) {
                osc.setWavetable(Table.T_BL_SAW);
            } else if (o == SHAPE_TRIANGLE) {
                osc.setWavetable(Table.T_BL_TRIANGLE);
            }
        }
    }

    public double tick() {
        if (oscMode == SHAPE_PULSE) {
            lastValue = osc.tickPhaseSubtraction(pwm);
        } else if (oscMode == SHAPE_DIGI) {
            lastValue = digi.tickSmooth();
        } else if (oscMode == SHAPE_DIGI_MORPH) {
            lastValue = digi.tickMorph();
        } else if (oscMode == SHAPE_SUPERSAW) {
            lastValue = supersaw.tick();
        } else {
            lastValue = osc.tick();
        }
        return lastValue;
    }

    public OscMode[] getAllModes() {
        return this.allModes;
    }

    public void setPWM(double pwm) {
        this.pwm = pwm;
    }

    public void setCps(double cps) {

        if (oscMode == SHAPE_DIGI || oscMode == SHAPE_DIGI_MORPH) {
            digi.setCps(cps);
        } else {
            osc.setCps(cps);
            supersaw.setCps(cps);
        }

    }

    public void setDrive(double drive) {
        if (oscMode == SHAPE_SINE) {
            osc.setDrive(0);
        } else {
            osc.setDrive(drive);
            supersaw.setDrive(drive);
        }
    }

    public double getPhase() {
        return osc.getPhase();
    }

    public void setIsSynced(double s) {
        osc.setIsSynced(s);
        digi.setIsSynced(s);
    }

    public void setSync(double p) {
        osc.setSync(p);
        digi.setSync(p);
    }

    public boolean isSynced() {
        if (isSynced == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void makeModes() {
        allModes = new OscMode[NUM_MODES];
        int i = 0;
        allModes[i++] = SHAPE_SAW;
        allModes[i++] = SHAPE_PULSE;
        allModes[i++] = SHAPE_SINE;
        allModes[i++] = SHAPE_TRIANGLE;
        allModes[i++] = SHAPE_SUPERSAW;
        allModes[i++] = SHAPE_DIGI;
        allModes[i++] = SHAPE_DIGI_MORPH;

    }

    /**
     * @return value for modualtion as a ModSource
     */
    public double getValue() {
        return lastValue;
    }


    public String toString() {
        return "osc Mode" + oscMode
                + ", freq = " + osc.getCps() * DSPSystem.getSamplingRate()
                + ", lastValue = " + lastValue


                ;

    }


    public void addToParameterManager(ParameterManager pm, String groupName, String prefix) {
        pm.addParameter(new Parameter(prefix + "Level", .7, groupName, ParameterDisplay.dB));
        OscMode[] modes = {SHAPE_SAW, SHAPE_PULSE, SHAPE_TRIANGLE, SHAPE_SINE, SHAPE_SUPERSAW, SHAPE_DIGI};
        pm.addParameter(new Parameter(prefix + "Mode", modes));
        pm.addParameter(new Parameter(prefix + "Semi", .5, groupName, ParameterDisplay.SEMI));
        pm.addParameter(new Parameter(prefix + "Fine", .5, groupName, ParameterDisplay.FINE));
        pm.addParameter(new Parameter(prefix + "Pulse Width", .5, groupName));
        pm.addParameter(new Parameter(prefix + "Digi Wave", .0, groupName));
        pm.addParameter(new Parameter(prefix + "SuperSaw Mix", .0, groupName));
        pm.addParameter(new Parameter(prefix + "SuperSaw Detune", .0, groupName));
    }
}