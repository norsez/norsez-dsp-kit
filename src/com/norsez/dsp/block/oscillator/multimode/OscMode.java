package com.norsez.dsp.block.oscillator.multimode;

/**
 * <p>Title: OscMode</p>
 * <p>Description: Oscillator mode class. Immutable object that a MultiModeOsc class uses
 * as its public static oscillator mode parameter. </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class OscMode {
    private String name;

    public OscMode(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}