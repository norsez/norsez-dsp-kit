package com.norsez.dsp.block.modulation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

/**
 * <p>Title: ModMatrix</p>
 * <p>Description: Faciliates moduation matrix manangement in a SynthModel.
 * <br>
 * How it works:
 * <ul>
 * <li>Each DSPBlock that can be a modulate source implements ModSource interface.
 * <li>Add DSPBlock to ModMatrix by calling addModSource (ModSource).
 * <li>Add ModDestinations to ModMatrix by calling addModDestination (String).
 * <li>ModMatrix internally builds a number of modulation slots. Each slot
 * consists of a ModSource, a number of destinations and a number of
 * modulation amounts.
 * <p/>
 * <li>ModMatrix needs no update method. <b>However</b>, all ModSources must update themselves
 * with their own normal tick () methods.
 * <li>When ModMatrix's getModValue ( string ) method is called, ModMatrix looks for each
 * destination and sums the modulation values of that destination in all
 * slots and return that sum value.
 * <p/>
 * </ul>
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public class ModMatrix {


    /**
     * each pair is the name of the ModSource
     * as displayed in the GUI and the ModSource itself.
     */
    private HashMap sources;
    /**
     * each element is a String served as the name reference of the
     * modulation destination.
     */
    private Vector destinations;
    private final int MAX_MOD_SLOTS;
    private final static int MAX_MOD_DESTINATIONS_PER_SLOT = 1;

    private ModSlot[] slots;

    public ModMatrix(int maxslots) {

        MAX_MOD_SLOTS = maxslots;
        slots = new ModSlot[MAX_MOD_SLOTS];
        for (int i = 0; i < slots.length; i++)
            slots[i] = new ModSlot();
        sources = new HashMap();
        destinations = new Vector();

    }

    /**
     * Should be called in a SynthModel's initialization to register mod sources.
     *
     * @param displayName name of source as displayed on the GUI
     * @param src         the ModSource associated with the displayName
     */
    public void addModSource(String displayName, ModSource src) {
        sources.put(displayName, src);
    }

    /**
     * Should be called in a SynthModel's initialization to register mod destinations.
     *
     * @param destinationName destination name
     */
    public void addModDestination(String destinationName) {

        destinations.add(destinationName);
    }

    /**
     * @param destinationName the destination's name as added to the ModMatrix
     * @return the sum of modulation values being done to the destination in this ModMatrix.
     */
    public double getModValue(String destinationName) {
        double sum = 0;

        for (int i = 0; i < MAX_MOD_SLOTS; i++) {
            sum += slots[i].getValueModulatingDestination(destinationName);
        }

        return sum;
    }


    public String toString() {
        String s = "Mod Matrix\n";
        for (int i = 0; i < this.MAX_MOD_SLOTS; i++) {
            s += "  " + i + " " + slots[i] + "\n";
        }
        return s;
    }

    public Object[] getSourceNames() {
        Object[] srcnames = new Object[sources.size() + 1];
        srcnames[0] = "None";
        Object[] names = sources.keySet().toArray();
        Arrays.sort(names);
        for (int i = 1; i < srcnames.length; i++) {
            srcnames[i] = names[i - 1];
        }
        return srcnames;
    }

    public Object[] getDestinationNames() {
        Object[] desnames = new Object[destinations.size() + 1];
        desnames[0] = "None";
        for (int i = 1; i < desnames.length; i++) {
            Object o = (Object) destinations.elementAt(i - 1);
            desnames[i] = o;
        }
        return desnames;
    }

    public void setModSource(int slotIndex, String modSourceName) {
        if (!modSourceName.equalsIgnoreCase("None"))
            slots[slotIndex].setSource((ModSource) sources.get(modSourceName));
        else
            slots[slotIndex].setSource(ModSlot.SRC_NONE);

    }


    public void setModDestination(int slotIndex, int desIndex, String modDesName) {
        slots[slotIndex].setDestination(desIndex,
                modDesName);
    }

    public void setModAmount(int slotIndex, int desIndex, double value) {
        slots[slotIndex].setModAmount(desIndex, value);

    }

    public JPanel getEditorPanelA() {
        JPanel panel = new JPanel(new GridLayout(MAX_MOD_SLOTS, 1));
        Font myfont = new Font("Verdana", 0, 10);
        // build src and des lists.
        Object[] srcnames = getSourceNames();

        Object[] desnames = getDestinationNames();

        //build each slot editor.
        for (int i = 0; i < MAX_MOD_SLOTS; i++) {
            JPanel sp = new JPanel(new GridLayout(1, 2));

            //build src selector
            JComboBox srcb = new JComboBox(srcnames);
            srcb.setSelectedIndex(0);
            srcb.setFont(myfont);

            final int slotIndex = i;

            srcb.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    JComboBox b = (JComboBox) e.getSource();
                    String str = b.getSelectedItem().toString();
                    setModSource(slotIndex, str);
                }
            });

            sp.add(srcb);
            ////destination selectors
            JPanel dp = new JPanel(new GridLayout(MAX_MOD_DESTINATIONS_PER_SLOT, 2));
            Hashtable labels = new Hashtable();
            labels.put(new Integer(0), new JLabel("-1"));
            labels.put(new Integer(64), new JLabel("0"));
            labels.put(new Integer(127), new JLabel("+1"));

            SliderAction sa = new SliderAction();
            for (int j = 0; j < MAX_MOD_DESTINATIONS_PER_SLOT; j++) {

                final int desIndex = j;
                //build mod des selectors
                JComboBox cb = new JComboBox(desnames);
                cb.setFont(myfont);
                cb.addItemListener(new DestSelect(slots[slotIndex], desIndex));
                //build mod amount sliders
                JSlider as = new JSlider(0, 127, 64);
                as.addMouseListener(sa);
                as.addChangeListener(new AmtSelect(slots[slotIndex], desIndex));
                as.setPaintLabels(true);
                as.setLabelTable(labels);
                as.setFont(myfont);
                dp.add(cb);
                dp.add(as);
            }
            sp.add(dp);
            panel.add(sp);
        }
        return panel;
    }

    /**
     * <p>Title: AmtSelect</p>
     * <p>Description: Mod Amount slider action.</p>
     * <p>Copyright: Copyright (c) 2003</p>
     * <p>Company: Norsez Orankijanan</p>
     *
     * @author Norsez Orankijanan
     * @version 1.0
     */
    static class AmtSelect implements ChangeListener {
        private int desIndex;
        private ModSlot slot;

        public AmtSelect(ModSlot slot, int desIndex) {
            this.slot = slot;
            this.desIndex = desIndex;
        }

        public void stateChanged(ChangeEvent e) {
            JSlider js = (JSlider) e.getSource();
            slot.setModAmount(desIndex,
                    com.norsez.dsp.block.Interpolation.mapToRangeLinear(0, -1, 127, 1, js.getValue()));
        }

    }

    /**
     * <p>Title: SliderAction</p>
     * <p>Description: Mod Amount slider double click will reset the value to zero.</p>
     * <p>Copyright: Copyright (c) 2003</p>
     * <p>Company: Norsez Orankijanan</p>
     *
     * @author Norsez Orankijanan
     * @version 1.0
     */
    static class SliderAction extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                ((JSlider) e.getSource()).setValue(64);
            }
        }
    }

    /**
     * <p>Title: DestSelect</p>
     * <p>Description: Destination selector</p>
     * <p>Copyright: Copyright (c) 2003</p>
     * <p>Company: Norsez Orankijanan</p>
     *
     * @author Norsez Orankijanan
     * @version 1.0
     */
    static class DestSelect implements ItemListener {
        private int desIndex;
        private ModSlot slot;

        public DestSelect(ModSlot slot, int desIndex) {
            this.slot = slot;
            this.desIndex = desIndex;
        }

        public void itemStateChanged(ItemEvent e) {
            JComboBox b = (JComboBox) e.getSource();
            String str = b.getSelectedItem().toString();
            slot.setDestination(desIndex,
                    b.getSelectedItem().toString());
        }

    }

    /**
     * <p>Title: ModSlot</p>
     * <p>Description: encapsulates a modulation slot methods and properties.</p>
     * <p>Copyright: Copyright (c) 2003</p>
     * <p>Company: Norsez Orankijanan</p>
     *
     * @author Norsez Orankijanan
     * @version 1.0
     */
    static class ModSlot {
        public final static NullSource SRC_NONE = new NullSource();

        private ModSource src;
        private String[] des;
        private double modAmount [];

        public ModSlot() {
            des = new String[MAX_MOD_DESTINATIONS_PER_SLOT];
            modAmount = new double[MAX_MOD_DESTINATIONS_PER_SLOT];
        }

        public void setSource(ModSource src) {
            this.src = src;
        }


        public void setDestination(int i, String des) {

            if (i >= MAX_MOD_DESTINATIONS_PER_SLOT)
                throw new java.lang.IllegalArgumentException("There are only " + MAX_MOD_DESTINATIONS_PER_SLOT + " destinations per mod slot"
                        + "\nCannot assign mod destination to slot #" + i);

            this.des[i] = des;
        }

        /**
         * @param i index of the mod destination
         * @param v value of mod amount
         */
        public void setModAmount(int i, double v) {
            if (i >= MAX_MOD_DESTINATIONS_PER_SLOT)
                throw new java.lang.IllegalArgumentException("There are only " + MAX_MOD_DESTINATIONS_PER_SLOT + " destinations per mod slot"
                        + "\nCannot assign mod amount value to slot #" + i);
            modAmount[i] = v;
        }

        /**
         * Called by ModMatrix when summing up total modulation value to a destination.
         *
         * @param des the name of destination
         * @return the modulation value in effect of the destination by the source.
         */
        public double getValueModulatingDestination(String des) {
            double result = 0;

            if (src == SRC_NONE || src == null || des == null) return result;

            for (int i = 0; i < MAX_MOD_DESTINATIONS_PER_SLOT; i++) {
                if (this.des[i] != null) {
                    if (this.des[i].equalsIgnoreCase(des))
                        try {
                            result += modAmount[i] * src.getValue();
                        } catch (NullPointerException e) {
                            System.err.println(des + " " + this.des[i] + " " + i + " " + src);
                            e.printStackTrace();
                        }
                }
            }

            return result;
        }


        private static class NullSource implements ModSource {
            public double getValue() {
                return 0;
            }
        }

        public String toString() {
            String s = "Src: " + this.src + " => ";
            for (int i = 0; i < des.length; i++) {
                s += "\n Des: " + des[i] + " = " + this.getValueModulatingDestination(des[i]) + "";
            }
            return s;
        }

    }


    public static void main(String[] args) {
        com.norsez.dsp.block.oscillator.multimode.OscA osc = new com.norsez.dsp.block.oscillator.multimode.OscA();
        osc.setCps(8520 / 44100.0);
        for (int i = 0; i < 500; i++)
            osc.tick();
        ModMatrix mm = new ModMatrix(2);
        mm.addModSource("osc 1"
                , osc);
        mm.addModDestination("pan");
        mm.addModDestination("vol");
        JOptionPane.showMessageDialog(null, mm.getEditorPanelA());
        System.out.println(mm);
        System.exit(0);
    }
}