package com.norsez.dsp.block.oscillator;

/**
 * <p>Title: NoiseZeroMean </p>
 * <p>Description: Using S. Orfanidis's propose (Optimum Signal Processing) to generate better white noise.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Norsez Orankijanan</p>
 *
 * @author Norsez Orankijanan
 * @version 1.0
 */
public class NoiseZeroMean extends Noise {

    int num_of_vars, bias;
    final static int DEFAULT_VAR_NUM = 12;

    public NoiseZeroMean() {
        num_of_vars = DEFAULT_VAR_NUM;
        bias = (int) (0.5 * num_of_vars);
    }

    public NoiseZeroMean(int num_of_vars) {
        this.num_of_vars = num_of_vars;
        bias = (int) (0.5 * num_of_vars);
    }

    public double tick() {
        for (int i = 0; i < num_of_vars; i++)
            lastValue += Math.random();
        lastValue = lastValue - bias;
        if (Math.random() > 0.5)
            lastValue = -lastValue;
        return lastValue;
    }

    public void tickProcess(double[] input) {
        for (int k = 0, n = input.length; k < n; k++) {
            for (int i = 0; i < num_of_vars; i++)
                lastValue += Math.random();
            lastValue = lastValue - bias;
            input[k] = lastValue;
        }
    }

    public static void main(String[] args) {
        Noise n = new NoiseZeroMean();
        for (int i = 0; i < 500; i++)
            System.out.println(n.tick());
    }
}
