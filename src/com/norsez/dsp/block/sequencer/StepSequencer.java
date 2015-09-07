package com.norsez.dsp.block.sequencer;

import javax.sound.midi.ShortMessage;


public class StepSequencer extends com.norsez.dsp.block.DSPBlock {
    StepSequencerInfo info;
    Track[] tracks;

    double swing;
    double bpm;
    int curStep;

    long curSample, samplesSwingOffset;
    //sample numbers that mark steps
    long[] stepMarks;
    //sample numbers that mark the firing of note on. stepMarks[0] tells you the sample number that curStep should increment to 1.
    long[] tickSamples;
    //sample numbers that mark the firing of note off.
    long[] stopSamples;
    //number of samples per click related to beat for minute value.
    long samplesPerClick;
    //the sample that marks the end of going one round of steps
    long endSample;
    //the maximum factor of a swing note to be delayed.
    final static double MAX_SWING_FACTOR = 0.50;


    public StepSequencer(StepSequencerInfo info) {
        this.info = info;
        tracks = new Track[info.num_tracks];

        for (int i = 0; i < info.num_tracks; i++)
            tracks[i] = new Track(info.num_steps);

        setBpm(120.0);


        stepMarks = new long[info.num_steps];
        curStep = 0;
        stepMarks[0] = samplesPerClick;
        for (int i = 0; i < info.num_steps; i++)
            stepMarks[i] = (i + 1) * samplesPerClick;

        recomputeTickSamples();

    }

    public void setEventOfTrackAtStep(Event e, int track, int step) {
        tracks[track].setStep(step, e);
    }

    /*
    public void saveSettings ( java.util.Properties prop ){
      prop.setProperty("BPM",""+bpm);
      prop.setProperty("SWING",""+swing);
      prop.setProperty("NUM_TRACKS",""+info.num_tracks);
      prop.setProperty("NUM_STEPS",""+info.num_steps);
      prop.setProperty("SAMPLINGRATE",""+info.samplingRate);
      Object e [];
      for(int i=0; i < info.num_tracks; i++){
        prop.setProperty("Track_Mono"+i,""+isTrackMono(i));
        prop.setProperty("Track_Tie"+i,""+isTrackMono(i));
        for (int j = 0; j < info.num_steps; j++) {
          e = this.getStepOfTrack(j,i);
          for(int k=0; k < e.length; k++){
            if ( e[k] != null)
              prop.setProperty(""+i+"_"+j+"_"+k, e[k].toString());
          }
        }
      }

    }
  */




    private void recomputeTickSamples() {


        tickSamples = new long[info.num_steps];
        stopSamples = new long[info.num_steps];
        long stopLength = (long) (samplesPerClick * 0.5);


        tickSamples[0] = 0;

        stopSamples[0] = stopLength;

        for (int i = 1; i < info.num_steps; i++) {

            if (i % 2 == 0) {
                tickSamples[i] = (i * samplesPerClick);
            } else {
                tickSamples[i] = (i * samplesPerClick) + samplesSwingOffset;
            }

            stopSamples[i] = tickSamples[i] + stopLength;

        }


    }

    private static final String[] NOTES = {"C", "D", "E", "F", "G", "A", "B"};

    public static int getNoteNumber(String note) {
        note = note.trim().toUpperCase();
        int base = -1;
        String s = note.substring(0, 1);
        for (int i = 0; i < NOTES.length; i++) {
            if (s.equalsIgnoreCase(NOTES[i]))
                base = i;
        }

        if (note.length() == 3)
            s = note.substring(2, 3);
        else
            s = note.substring(1, 2);

        base = base + (Integer.parseInt(s) + 1) * 12;

        if (note.length() == 3)
            s = note.substring(1, 2);


        if (s.equalsIgnoreCase("#"))
            base += 1;

        return base;
    }

    public void setSwing(double s) {
        swing = s;
        samplesSwingOffset = (long) (s * samplesPerClick * MAX_SWING_FACTOR);
        recomputeTickSamples();
    }

    public void setBpm(double b) {
        bpm = b;
        samplesPerClick = (long) (info.samplingRate * 60.0 * 0.25 / bpm);
        endSample = samplesPerClick * info.num_steps;
    }

    public final static double IS_TICK_WITH_DATA = 1.0;
    public final static double IS_TICK_WITHOUT_DATA = 0.0;

    boolean isTickStep;

    public double tick() {


        //curStep management
        if (curSample == stepMarks[curStep]) {
            curStep = (curStep + 1);

        }

        //note on/off
        if (curSample == tickSamples[curStep] || curSample == stopSamples[curStep]) {
            lastValue = IS_TICK_WITH_DATA;
        } else {
            lastValue = IS_TICK_WITHOUT_DATA;
        }

        curSample++;

        if (curSample == endSample) {
            curStep = 0;
            curSample = 0;
        }

        return lastValue;
    }

    public Event getCurrentEvent(int track) {
        return tracks[track].getStep(curStep).getEvent();
    }

    public int getCurrentStep() {
        return curStep;
    }


    public static class StepSequencerInfo {

        public int num_tracks;
        public int num_steps;
        public double samplingRate;
    }

    public class Track {

        private Step[] steps;

        public Track(int numSteps) {
            steps = new Step[numSteps];
            for (int i = 0; i < numSteps; i++) {
                steps[i] = new Step();
            }
        }

        public void clearStep(int index) {
            steps[index] = new Step();
        }

        public Step getStep(int index) {
            return steps[index];
        }

        public void setStep(int index, Event e) {
            steps[index].setEvent(e);
        }


    }

    public class Step {

        private Event event;

        public Step() {
            event = new Event();
        }

        public void setEvent(Event e) {
            event = e;
        }

        public Event getEvent() {
            return event;
        }


    }

    public class Event extends ShortMessage implements Comparable {
        private boolean isTie;

        public Event() {


            try {
                this.setMessage(ShortMessage.NOTE_ON, 0, 0, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public int compareTo(Object o) {
            return this.getData1() - ((Event) o).getData1();
        }

        public String toString() {
            return "[" + this.getCommand() + "," + this.getData1() + "," + this.getData2() + "]";
        }

        public void setTie(boolean t) {
            isTie = t;
        }

        public boolean isTie() {
            return isTie;
        }

    }

}
