package com.norsez.dsp.block.modulation;

/**
 * <p>Title: ModSource</p>
 * <p>Description: An integrated unit in a synth should have final but not static ModSource
 * that will return some values in itself for use in Modulation Matrix. For example,
 * <blockquote>
 * public class Lfo { <br>
 * private double lastValue; <br>
 * public final ModSource MOD1 = new ModSource ( "output") {<br>
 * public double getValue (){<br>
 * return lastValue;<br>
 * }     <br>
 * } <br>
 * </blockquote>
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public interface ModSource {

    public double getValue();


}