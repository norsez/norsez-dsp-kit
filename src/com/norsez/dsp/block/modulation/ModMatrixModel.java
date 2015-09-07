package com.norsez.dsp.block.modulation;

/**
 * <p>Title: Model for doing Modulation Matrix. This has nothing to do with ModMatrix class.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */

public abstract class ModMatrixModel {

    private ModSlot[] slots;

    /**
     * Constructor
     *
     * @param sources      source names
     * @param destinations destination names
     */
    public ModMatrixModel(int slotNum) {
        this.slots = new ModSlot[slotNum];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new ModSlot();
        }
    }


    public void setSource(int slot, String srcName) {
        slots[slot].src = srcName;
    }

    public void setDestination(int slot, String desName) {
        slots[slot].des = desName;
    }

    public void setAmount(int slot, double value) {
        slots[slot].amount = value;
    }


    public abstract double getValueFromSource(String srcName);

    public double getModValueForDestination(String des) {
        double result = 0;
        for (int i = 0; i < slots.length; i++) {
            if (slots[i].amount != 0.5)
                if (slots[i].des.trim().equalsIgnoreCase(des)) {

                    result += getValueFromSource(slots[i].src) *
                            com.norsez.dsp.block.Interpolation.mapToRangeLinear(0, -1, 1, 1, slots[i].amount);
                }
        }


        return result;
    }

    /**
     * <p>Title: ModSlot</p>
     * <p>Description: represents a slot of modulation. The modulation range is assume to be bipolar. </p>
     * <p>Copyright: Copyright (c) 2003</p>
     * <p>Company: Norsez Orankijanan</p>
     *
     * @author Norsez Orankijanan
     * @version 1.0
     */
    static class ModSlot {
        public String src;
        public String des;
        public double amount;

        public ModSlot() {
            src = "none";
            des = "none";
            amount = 0.5;
        }
    }


}