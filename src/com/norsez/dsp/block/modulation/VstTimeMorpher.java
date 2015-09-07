package com.norsez.dsp.block.modulation;

import java.util.Iterator;
import java.util.Vector;

/**
 * <p>Title: VstTimeMorpher</p>
 * <p>Description: A kind of modulator for VSTSynthModel. It morphs from current program to another program.
 * So there must be at least 2 programs in the event list for the tick method to morph.
 * It's designed to be called at a slow control rate.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */
public class VstTimeMorpher extends com.norsez.dsp.block.DSPBlock {

    //the synth to morph
    com.norsez.dsp.synth.VstSynthModel synth;

    //list of morphing events
    Vector events;

    //internal clock
    long current_sample;


    int cur_event;

    public VstTimeMorpher() {
        current_sample = 0;
        events = new Vector();

        cur_event = 1;
    }


    public void setCurrentEventIndex(int i) {
        if (i < 1) i = 1;
        if (i >= events.size()) i = events.size();

        this.cur_event = i;

    }

    public int getCurrentEventIndex() {
        return this.cur_event;
    }


    /**
     * @param program      Program the program to morph to (from the synth's current program).
     * @param start_sample long the sample number that morphing starts.
     * @param end_sample   long the sample number that morphing finishes.
     */
    public void addEvent(com.norsez.dsp.synth.VstSynthModel.Program program, long start_sample, long end_sample) {
        events.add(new Event(program, start_sample, end_sample));

    }


    public void setSynth(com.norsez.dsp.synth.VstSynthModel synth) {
        this.synth = synth;
    }

    public double tick() {

        throw new java.lang.UnsupportedOperationException("Use tick ( int ) instead.");
    }

    /**
     * Returns the internal clock sample number. This is the sample number you wanna compare to when
     * you use the addEvent method.
     *
     * @return long
     */
    public long getCurrentSample() {
        return current_sample;
    }

    /**
     * Say, the morphing has already been finished its round. This method
     * will modify all start and end times in each event in the list so that
     * the morhphing will start again.
     */
    public void restartNow() {
        //no use working when no morphing possible.
        if (events.size() < 2) return;

        long cur_time = this.current_sample;
        Event e;
        for (Iterator itr = events.iterator(); itr.hasNext();) {
            e = (Event) itr.next();
            e.end_sample += cur_time;
            e.start_sample += cur_time;
        }

        this.cur_event = 1;
    }

    public void tick(int steps) {

        //ticking the internal clock.
        for (int i = 0; i < steps; i++) {
            current_sample++;
        }

        //no use working without anything to morph.
        if (events.size() < 2 || this.cur_event >= events.size()) return;

        //get current event.
        Event e = (Event) events.get(this.cur_event);
        //if a event is past, increment current event.
        if (e.end_sample <= current_sample) {
            e = (Event) events.get(++this.cur_event);
        }

        //returns, if not time for doing work yet.
        if (e.start_sample > current_sample) return;
        //find the morphing point in time
        double morph_point = com.norsez.dsp.block.Interpolation.mapToRangeLinear(e.
                start_sample, 0, e.end_sample, 1, current_sample);
        //do the morph with the previous event's program.
        Event prev_e = (Event) events.get(this.cur_event - 1);
        com.norsez.dsp.synth.VstSynthModel.Program cur_prog = prev_e.program;
        for (int i = 0, n = cur_prog.data.length; i < n; i++) {
            synth.setParameter(i,
                    com.norsez.dsp.block.Interpolation.mapToRangeLinear(0,
                            cur_prog.data[i], 1, e.program.data[i], morph_point));
        }

    }

    /**
     * <p>Title: Event</p>
     * <p>Description: </p>
     * <p>Copyright: Copyright (c) 2003</p>
     * <p>Company: Norsez Orankijanan</p>
     *
     * @author Norsez Orankijanan
     * @version 1.0
     */
    public static class Event {

        public Event(com.norsez.dsp.synth.VstSynthModel.Program program, long start_sample, long end_sample) {
            this.end_sample = end_sample;
            this.start_sample = start_sample;
            this.program = program;
        }

        public com.norsez.dsp.synth.VstSynthModel.Program program;
        public long start_sample, end_sample;
    }
}
