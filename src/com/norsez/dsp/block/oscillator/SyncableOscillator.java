package com.norsez.dsp.block.oscillator;

/**
 * <p>Title: SyncableOscillator</p>
 * <p>Description: Band limited synable oscillator. The setSync (double) polls the phase of
 * the master oscillator. When the master's phase resets, this oscillator's phase resets too.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 * @extends AntiAliasedWavetable
 */

public class SyncableOscillator
        extends AntiAliasedWavetable {

    private double lastMasterPhase;
    private boolean synced;

    /**
     * The input is the phase of the Master Oscillator.
     */
    public void setSync(double p) {

        if (p < lastMasterPhase && synced) {
            this.phase = 0;
        }

        lastMasterPhase = p;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setIsSynced(double s) {
        if (s == 0) {
            synced = false;
        } else {
            synced = true;

        }
    }
}