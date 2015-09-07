package com.norsez.dsp.block;

/**
 * <p>Title: Unit </p>
 * <p>Description: The most basic unit in the com.norsez.dsp package when taking optimization in mind. It's supposed to hold together
 * parameter values of a synth circuit component. For example, it could represents
 * an oscillator. So it holds together an oscillator's phase, last value, frequency, etc.
 * <p/>
 * <BR><BR>
 * <p/>
 * However, the process the updates the Unit should be put in the Process object to help with
 * the processing power optimization. It saves processing power much more when:-
 * <p/>
 * <li>Use a common update method to update several similar Units. (In this package, we encapsulate methods in Process object.)
 * <li>Passing the least numbers of objects (or primitive variables) into an update method.
 * <li>Use if statements rather than defaulting an update method by using many +,-,*,/ operations.
 * <li>Not using primitive type wrappers.
 * <li>Multiplying 2 values which are both less than 1.0.
 * <li>Hard-coding catch exceptions.
 * <p/>
 * <p/>
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */
public class Unit {

    /**
     * Use this static object to represent a null Unit.
     */
    final static Unit BLANK_UNIT = new Unit();

    public double lastValue;
}
