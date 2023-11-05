package brain;

import java.util.ArrayList;

public class Neuron {

    private final ArrayList<Synapse> synapses;
    private double sum;

    public Neuron() {
        this.synapses = new ArrayList<>();
        this.sum = 0.0;
    }

    public void addSynapseInput(Synapse synapse) {
        this.synapses.add(synapse);
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getSum() {
        return this.sum;
    }

    public void activate() {
        this.sum = 0.0;
        for (Synapse synapse : this.synapses) {
            this.sum += synapse.getNeuron1().getSum() * synapse.getWeight();
        }
        this.sum = tanh(this.sum);
    }

    private static double exp(double val) {
        final long tmp = (long) (1512775 * val + (1072693248 - 60801));
        return Double.longBitsToDouble(tmp << 32);
    }

    private static double tanh(double x) {
        if (x > 3.0) {
            return 1.0;
        } else if (x < -3.0) {
            return -1.0;
        } else {
            double exp2x = exp(2 * x);
            return (exp2x - 1) / (exp2x + 1);
        }
    }
}
