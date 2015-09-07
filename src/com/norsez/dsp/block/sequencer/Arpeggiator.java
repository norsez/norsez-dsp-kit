package com.norsez.dsp.block.sequencer;

import com.norsez.dsp.block.DSPBlock;
import com.norsez.dsp.block.Interpolation;
import com.norsez.dsp.block.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * <p>Title: Arpeggiator </p>
 * <p>Description: <b>Outdated</b>. Use ArpSwing instead.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class Arpeggiator
        extends DSPBlock {

    //arp will not go pass note assigned at the maxStep
    private int maxStep;
    //maximum step possible for this arp
    private final int TOTAL_STEPS = 8;
    //current step of the arp
    private int currentStep;

    //set repeat octave of the arp
    private int maxOctave;

    //current octave of the arp
    private int currentOctave;

    private ClockSource clock;

    //midi note number transpose
    private int transpose;

    //maintains note number assigned for each step.
    private int notes[];

    //table that maps note number to cps
    private static final double[] T_NOTENUM_2_CPS = getNoteNum2CPS();

    //is arp on or off
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean b) {
        active = b;
        if (active == false) {

            this.lastValue = 0;
        }
    }

    private static double[] getNoteNum2CPS() {
        double[] temp = new double[128];

        for (int i = 0; i < temp.length; i++) {
            temp[i] = Interpolation.linear(Table.T_NOTE2CPS, i / 127.0);
        }

        return temp;
    }

    /**
     * restarts the arp step cycle
     */
    public void restartCycle() {
        currentOctave = 0;
        currentStep = 0;
    }

    /**
     * This sets time for the clock. Good for text based debugging only.
     */
    public void setRawCps(double i) {
        clock.setRawCps(i);
    }

    public Arpeggiator(ClockSource clock) {

        this.clock = clock;

        notes = new int[getTotalSteps()];

        setMaxStep((int) (getTotalSteps() * 0.5) - 1);

        for (int i = 0; i < getTotalSteps(); i++) {
            notes[i] = 60;
        }
        notes[0] = 60;
        notes[1] = 64;
        notes[2] = 67;
        notes[3] = 71;
    }

    /**
     * set Transpose by +/-midi note number.
     */
    public void setTranspose(int n) {
        transpose = n;
    }

    /**
     * @return Transpose by +/- midi note.
     */
    public int getTranspose() {
        return transpose;
    }

    public void setMaxStep(int s) {
        maxStep = s;
    }

    public int getTotalSteps() {
        return TOTAL_STEPS;
    }

    public int getMaxStep() {
        return maxStep;
    }

    public void setBPM(double bpm) {
        clock.setBPM(bpm);
    }

    public ClockSource getClockSource() {
        return clock;
    }

    /**
     * @return gate values only. Must call getNote or getCPS <b>after</b> tick () for the current note message.
     */
    public double tick() {

        if (isActive()) {

            lastValue = clock.tick();
            //mode on
            if (clock.isSignificantTick()) {
                //System.out.println ( "tick");
                if (currentStep + 1 >= maxStep) {
                    currentStep = 0;

                    if (currentOctave + 1 >= maxOctave) {
                        currentOctave = 0;
                    } else {
                        currentOctave += 1;

                    }
                } else {
                    currentStep += 1;
                }
            }

            return lastValue;
        } else {
            return 0;
        }

    }

    /**
     * @return BPM
     */
    public double getBPM() {
        return clock.getBPM();
    }

    /**
     * @return note number of the current step.
     */

    public int getNote() {
        int temp = notes[currentStep] + (12 * currentOctave) + transpose;

        if (temp < 128) {
            return temp;
        } else {
            return notes[currentStep];
        }

    }

    /**
     * adjusts clock's gate length
     */
    public void setGate(double d) {
        clock.setGate(d);
    }

    /**
     * @returns clock's gate length
     */
    public double getGate() {
        return clock.getGate();
    }

    /**
     * adjusts clock resolution
     */
    public void setResolution(ClockSource.Resolution r) {
        clock.setResolution(r);
    }

    /**
     * gets clock resolution
     */
    public ClockSource.Resolution getResolution() {
        return clock.getResolution();
    }

    /**
     * @return cps of note number of the current step.
     */
    public double getCPS() {
        return T_NOTENUM_2_CPS[getNote()];
    }

    /**
     * @param atStep  is the step at which the note is assigned to
     * @param noteNum midi note number
     */
    public void setNoteAtStep(int atStep, int notenum) {
        notes[atStep] = notenum;
    }

    /**
     * Sets the range of note repeat. (Starts with zero).
     */
    public void setRange(int r) {
        maxOctave = r;
    }

    /**
     * @return the range of note repeat.
     */
    public int getRange() {
        return maxOctave;
    }

    public int getNoteAtStep(int i) {
        return this.notes[i];
    }

    public JPanel getEditorPanelA() {
        JPanel p = new JPanel(new GridLayout(5, 1));
        Font font = new Font("Tahoma", 0, 9);

        final Arpeggiator arp = this;

        JPanel notepanel = new JPanel(new GridLayout(1, getTotalSteps()));
        NoteBox stepBoxes[] = new NoteBox[getTotalSteps()];
        NoteAction n = new NoteAction();
        for (int i = 0; i < stepBoxes.length; i++) {
            stepBoxes[i] = new NoteBox(Table.T_NOTE_NAMES, i, arp);
            notepanel.add(stepBoxes[i]);
            stepBoxes[i].setFont(font);
            stepBoxes[i].addActionListener(n);
            stepBoxes[i].setSelectedIndex(arp.getNoteAtStep(i));
            stepBoxes[i].setToolTipText("step " + (i + 1));
        }

        JPanel cpanel = new JPanel();

//add play switch
        JCheckBox play = new JCheckBox("Play");
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox c = (JCheckBox) e.getSource();
                arp.setActive(c.isSelected());
            }
        });
        play.setFont(font);
        cpanel.add(play);
        cpanel.setFont(font);

//add range editor
        String r[] = {
            "1", "2", "3", "4"};
        JComboBox range = new JComboBox(r);
        range.setFont(font);
        range.setSelectedIndex(arp.getRange());
        range.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JComboBox b = (JComboBox) e.getSource();
                arp.setRange(b.getSelectedIndex() + 1);
            }
        });

        JLabel l1 = new JLabel("Range");
        l1.setFont(font);
        cpanel.add(l1);
        cpanel.add(range);
// add step editor
        String r1[] = new String[arp.getTotalSteps()];
        for (int i = 0; i < arp.getTotalSteps(); i++) {
            r1[i] = "" + (i + 1);
        }
        JComboBox step = new JComboBox(r1);
        step.setFont(font);
        step.setSelectedIndex(arp.getMaxStep());
        step.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JComboBox b = (JComboBox) e.getSource();
                arp.setMaxStep(b.getSelectedIndex() + 1);
            }
        });
        JLabel l2 = new JLabel("Steps");
        l2.setFont(font);
        cpanel.add(l2);
        cpanel.add(step);

//add resolution editor
        ClockSource.Resolution[] cm = {
            ClockSource.Resolution.TWO,
            ClockSource.Resolution.ONE,
            ClockSource.Resolution.ONE_HALF,
            ClockSource.Resolution.ONE_FOURTH, ClockSource.Resolution.ONE_EIGHTH,
            ClockSource.Resolution.ONE_SIXTEENTH,
            ClockSource.Resolution.ONE_THIRTY_SECOND};

        JComboBox res = new JComboBox(cm);
        res.setFont(font);
        res.setSelectedItem(arp.getResolution());
        res.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JComboBox b = (JComboBox) e.getSource();
                arp.setResolution((ClockSource.Resolution) b.getSelectedItem());
            }
        });
        JLabel l3 = new JLabel("Res");
        l3.setFont(font);
        cpanel.add(l3);
        cpanel.add(res);

//add bpm editor
        JPanel p4 = new JPanel();
        JSlider bs = new JSlider(200, 2500, 1350);
        //bs.setValue((int)( arp.getClockSource().getBPM() * 10));
        //System.out.println ((int)( arp.getClockSource().getBPM() * 10));
        bs.addChangeListener(new javax.swing.event.ChangeListener() {
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");

            public void stateChanged(javax.swing.event.ChangeEvent e) {
                JSlider j = (JSlider) e.getSource();
                arp.setBPM(j.getValue() / 10.0);
                j.setToolTipText("" + df.format(j.getValue() / 10.0));
            }
        });
        JLabel l4 = new JLabel("BPM");
        l4.setFont(font);
        p4.add(l4);
        p4.add(bs);
//add gate editor
        JPanel p5 = new JPanel();

        JSlider gs = new JSlider(0, 100);
        gs.setValue((int) (arp.getGate() * gs.getMaximum()));
        gs.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                JSlider j = (JSlider) e.getSource();
                arp.setGate(j.getValue() / (double) j.getMaximum());
            }
        });

        JLabel l5 = new JLabel("Gate Length");
        l5.setFont(font);

//add tranposer
        JPanel p6 = new JPanel();
        JSlider ts = new JSlider(-24, 24);
        ts.setValue(arp.getTranspose());
        ts.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                JSlider j = (JSlider) e.getSource();
                arp.setTranspose(j.getValue());
                j.setToolTipText(arp.getTranspose() + "");
            }
        });

        JLabel l6 = new JLabel("Transpose");
        l6.setFont(font);
        p5.add(l5);
        p5.add(gs);
        p6.add(l6);
        p6.add(ts);

        p.add(notepanel);
        p.add(cpanel);
        p.setFont(font);
        p.add(p4);
        p.add(p5);
        p.add(p6);
        return p;
    }

    class NoteBox
            extends JComboBox {
        public int step;
        public Arpeggiator arp;

        public NoteBox(Object[] items, int step, Arpeggiator arp) {
            super(items);
            this.step = step;
            this.arp = arp;
        }
    }

    public class NoteAction
            implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            NoteBox b = (NoteBox) e.getSource();
            b.arp.setNoteAtStep(b.step, b.getSelectedIndex());
        }
    }

    static class Mode {
        private final String name;
        private static int nextOrdinal = 0;
        private final int ordinal = nextOrdinal++;

        private Mode(String name) {
            this.name = name;
        }

        public static Mode UP = new Mode("UP");
        public static Mode DOWN = new Mode("DOWN");

    }

    public void saveSettingsToFile(String filename) {
        Properties props = new Properties();

        props.setProperty("BPM", "" + getBPM());
        props.setProperty("GATE", "" + getGate());
        props.setProperty("MAX STEP", "" + getMaxStep());
        props.setProperty("RANGE", "" + (getRange() + 1));
        props.setProperty("TRANSPOSE", "" + getTranspose());
        props.setProperty("RESOLUTION", getResolution().getFactor() + "");
        for (int i = 0; i < TOTAL_STEPS; i++) {
            props.setProperty("NOTE " + i, getNoteAtStep(i) + "");
        }

        try {
            props.store(new FileOutputStream(filename), filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadSettingsFromFile(String filename) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(filename));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        this.setBPM(Double.parseDouble(props.getProperty("BPM")));
        this.setGate(Double.parseDouble(props.getProperty("GATE")));
        int steps = Integer.parseInt(props.getProperty("MAX STEP"));
        this.setMaxStep(steps);
        for (int i = 0; i < steps; i++) {
            this.setNoteAtStep(i, Integer.parseInt(props.getProperty("NOTE " + i)));
        }
        this.setRange(Integer.parseInt(props.getProperty("RANGE")) - 1);
        this.setTranspose(Integer.parseInt(props.getProperty("TRANSPOSE")));
        ClockSource.Resolution[] all = ClockSource.Resolution.getAllResolutions();
        double r = Double.parseDouble(props.getProperty("RESOLUTION"));
        for (int i = 0; i < all.length; i++) {
            if (all[i].getFactor() == r) {
                this.setResolution(all[i]);
            }
        }

        this.restartCycle();
    }

    public static void main(String[] arg) {
        Arpeggiator a = new Arpeggiator(new ClockSource());
        JOptionPane.showMessageDialog(null, new JScrollPane(a.getEditorPanelA()));
        System.exit(0);
    }

}
