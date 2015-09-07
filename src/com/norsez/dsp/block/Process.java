package com.norsez.dsp.block;

/**
 * <p>Title: Process</p>
 * <p>Description: This is an abstract for any process that is used to update a Unit.
 * This is an object that works as a functional pointer in C++.
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public abstract class Process {

    /**
     * Use this Process as a process that does nothing.
     */
    final static Process BLANK = new Process() {
        public void update(Object input, Object output) {
            //do nothing.
        }
    };

    public abstract void update(Object input, Object output);


}
