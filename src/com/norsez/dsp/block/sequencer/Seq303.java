package com.norsez.dsp.block.sequencer;

import com.norsez.dsp.block.DSPSystem;
import com.norsez.dsp.block.Interpolation;
import com.norsez.dsp.block.Note;
import com.norsez.dsp.block.Table;
import com.norsez.dsp.synth.Counter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Seq303 {

    public final static int pFirstParam = new Counter (0).getInt();
    public final static int pPlay = new Counter().getInt();
    public final static int pActiveSteps = new Counter().getInt();
    public final static int pTempo = new Counter().getInt(); // normalized BPM
    public final static int pNote = new Counter().getInt();
    public final static int pSwing = new Counter().getInt();
    public final static int pAccentLevel = new Counter().getInt();
    public final static int pNonAccentLevel = new Counter().getInt();


    public final static int pNote0 = new Counter().getInt();
    public final static int pNote1 = new Counter().getInt();
    public final static int pNote2 = new Counter().getInt();
    public final static int pNote3 = new Counter().getInt();
    public final static int pNote4 = new Counter().getInt();
    public final static int pNote5 = new Counter().getInt();
    public final static int pNote6 = new Counter().getInt();
    public final static int pNote7 = new Counter().getInt();
    public final static int pNote8 = new Counter().getInt();
    public final static int pNote9 = new Counter().getInt();
    public final static int pNote10 = new Counter().getInt();
    public final static int pNote11 = new Counter().getInt();
    public final static int pNote12 = new Counter().getInt();
    public final static int pNote13 = new Counter().getInt();
    public final static int pNote14 = new Counter().getInt();
    public final static int pNote15 = new Counter().getInt();
    public final static int pNote16 = new Counter().getInt();
    public final static int pNote17 = new Counter().getInt();
    public final static int pNote18 = new Counter().getInt();
    public final static int pNote19 = new Counter().getInt();
    public final static int pNote20 = new Counter().getInt();
    public final static int pNote21 = new Counter().getInt();
    public final static int pNote22 = new Counter().getInt();
    public final static int pNote23 = new Counter().getInt();
    public final static int pNote24 = new Counter().getInt();
    public final static int pNote25 = new Counter().getInt();
    public final static int pNote26 = new Counter().getInt();
    public final static int pNote27 = new Counter().getInt();
    public final static int pNote28 = new Counter().getInt();
    public final static int pNote29 = new Counter().getInt();
    public final static int pNote30 = new Counter().getInt();
    public final static int pNote31 = new Counter().getInt();

    public final static int pVelocity0 = new Counter().getInt();
    public final static int pVelocity1 = new Counter().getInt();
    public final static int pVelocity2 = new Counter().getInt();
    public final static int pVelocity3 = new Counter().getInt();
    public final static int pVelocity4 = new Counter().getInt();
    public final static int pVelocity5 = new Counter().getInt();
    public final static int pVelocity6 = new Counter().getInt();
    public final static int pVelocity7 = new Counter().getInt();
    public final static int pVelocity8 = new Counter().getInt();
    public final static int pVelocity9 = new Counter().getInt();
    public final static int pVelocity10 = new Counter().getInt();
    public final static int pVelocity11 = new Counter().getInt();
    public final static int pVelocity12 = new Counter().getInt();
    public final static int pVelocity13 = new Counter().getInt();
    public final static int pVelocity14 = new Counter().getInt();
    public final static int pVelocity15 = new Counter().getInt();
    public final static int pVelocity16 = new Counter().getInt();
    public final static int pVelocity17 = new Counter().getInt();
    public final static int pVelocity18 = new Counter().getInt();
    public final static int pVelocity19 = new Counter().getInt();
    public final static int pVelocity20 = new Counter().getInt();
    public final static int pVelocity21 = new Counter().getInt();
    public final static int pVelocity22 = new Counter().getInt();
    public final static int pVelocity23 = new Counter().getInt();
    public final static int pVelocity24 = new Counter().getInt();
    public final static int pVelocity25 = new Counter().getInt();
    public final static int pVelocity26 = new Counter().getInt();
    public final static int pVelocity27 = new Counter().getInt();
    public final static int pVelocity28 = new Counter().getInt();
    public final static int pVelocity29 = new Counter().getInt();
    public final static int pVelocity30 = new Counter().getInt();
    public final static int pVelocity31 = new Counter().getInt();

    public final static int pMute0 = new Counter().getInt();
    public final static int pMute1 = new Counter().getInt();
    public final static int pMute2 = new Counter().getInt();
    public final static int pMute3 = new Counter().getInt();
    public final static int pMute4 = new Counter().getInt();
    public final static int pMute5 = new Counter().getInt();
    public final static int pMute6 = new Counter().getInt();
    public final static int pMute7 = new Counter().getInt();
    public final static int pMute8 = new Counter().getInt();
    public final static int pMute9 = new Counter().getInt();
    public final static int pMute10 = new Counter().getInt();
    public final static int pMute11 = new Counter().getInt();
    public final static int pMute12 = new Counter().getInt();
    public final static int pMute13 = new Counter().getInt();
    public final static int pMute14 = new Counter().getInt();
    public final static int pMute15 = new Counter().getInt();
    public final static int pMute16 = new Counter().getInt();
    public final static int pMute17 = new Counter().getInt();
    public final static int pMute18 = new Counter().getInt();
    public final static int pMute19 = new Counter().getInt();
    public final static int pMute20 = new Counter().getInt();
    public final static int pMute21 = new Counter().getInt();
    public final static int pMute22 = new Counter().getInt();
    public final static int pMute23 = new Counter().getInt();
    public final static int pMute24 = new Counter().getInt();
    public final static int pMute25 = new Counter().getInt();
    public final static int pMute26 = new Counter().getInt();
    public final static int pMute27 = new Counter().getInt();
    public final static int pMute28 = new Counter().getInt();
    public final static int pMute29 = new Counter().getInt();
    public final static int pMute30 = new Counter().getInt();
    public final static int pMute31 = new Counter().getInt();


    public final static int pSlide0 = new Counter().getInt();
    public final static int pSlide1 = new Counter().getInt();
    public final static int pSlide2 = new Counter().getInt();
    public final static int pSlide3 = new Counter().getInt();
    public final static int pSlide4 = new Counter().getInt();
    public final static int pSlide5 = new Counter().getInt();
    public final static int pSlide6 = new Counter().getInt();
    public final static int pSlide7 = new Counter().getInt();
    public final static int pSlide8 = new Counter().getInt();
    public final static int pSlide9 = new Counter().getInt();
    public final static int pSlide10 = new Counter().getInt();
    public final static int pSlide11 = new Counter().getInt();
    public final static int pSlide12 = new Counter().getInt();
    public final static int pSlide13 = new Counter().getInt();
    public final static int pSlide14 = new Counter().getInt();
    public final static int pSlide15 = new Counter().getInt();
    public final static int pSlide16 = new Counter().getInt();
    public final static int pSlide17 = new Counter().getInt();
    public final static int pSlide18 = new Counter().getInt();
    public final static int pSlide19 = new Counter().getInt();
    public final static int pSlide20 = new Counter().getInt();
    public final static int pSlide21 = new Counter().getInt();
    public final static int pSlide22 = new Counter().getInt();
    public final static int pSlide23 = new Counter().getInt();
    public final static int pSlide24 = new Counter().getInt();
    public final static int pSlide25 = new Counter().getInt();
    public final static int pSlide26 = new Counter().getInt();
    public final static int pSlide27 = new Counter().getInt();
    public final static int pSlide28 = new Counter().getInt();
    public final static int pSlide29 = new Counter().getInt();
    public final static int pSlide30 = new Counter().getInt();
    public final static int pSlide31 = new Counter().getInt();


    public final static int pParamCount = new Counter().getInt();

    private long tillNextSample, samplesPerNote, halfNote;
    private double lastGate, lastNote;
    private boolean swingBeat;
    private int currentStep;


    private double[] parameters;

    public Seq303() {
        parameters = new double[pParamCount];

        setTempoByBpm(120.0, Note.ONE_SIXTEENTH);
        setNote(Note.ONE_SIXTHTH);
        setSwing(0);
        for (int i = 0; i < 32; i++)
            parameters[pNote0 + i] = Table.T_NOTE2CPS[59];
    }

    public void setActiveSteps(double i) {
        parameters[pActiveSteps] = i;
    }

    public void setSwing(double i) {
        parameters[pSwing] = i;
    }

    public void setNote(Note note) {
        Note[] allnotes = Note.getAllNotes();
        for (int i = 0; i < allnotes.length; i++)
            if (allnotes[i] == note) {
                parameters[pNote] = i / (allnotes.length - 1.0);
                break;
            }

        setTempo(parameters[pTempo]);
    }

    public void setNoteAtStep(int step, double note) {
        if (step >= 0 && step <= 31)
            parameters[pNote0 + step] = note;
    }

    public void setVelocityAtStep(int step, double volume) {
        if (step >= 0 && step <= 31)
            parameters[pVelocity0 + step] = volume;
    }

    public void setSlideAtStep(int step, double slide) {
        if (step >= 0 && step <= 31)
            parameters[pSlide0 + step] = slide;
    }


    public void setTempo(double t) {
        parameters[pTempo] = t;

        setTempoByBpm(Interpolation.mapToRangeLinear(0, DSPSystem.MIN_BPM, 1, DSPSystem.MAX_BPM, t),
                Note.getAllNotes()[(int) ((Note.getAllNotes().length * parameters[pNote]))]);
    }

    public void setMute(int step, double mute) {
        parameters[pMute0 + step] = mute;
    }


    public void setTempoByBpm(double bpm, Note note) {

        samplesPerNote = tillNextSample = (long) Note.getSamplesPerNote(bpm, note);

        swingBeat = false;
        currentStep = 0;
    }

    public double getLastNote() {
        return parameters[pNote0 + currentStep];
    }

    public double getLastGate() {
        return lastGate;
    }


    public void tick() {
        tillNextSample--;


        if (tillNextSample == 0) {
            tillNextSample = samplesPerNote;
            currentStep++;
            if (currentStep >= 32)
                currentStep = 0;
        }
    }
}