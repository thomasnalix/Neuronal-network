package brain;

public class Synapse {

    private final Neuron neuron1;
    private double weight;

    public Synapse(Neuron neuron1, double weight) {
        this.neuron1 = neuron1;
        this.weight = weight;
    }

    public void setWeight(double weight) {
        this.weight = Math.min(Math.max(weight, -4.0), 4.0);
    }

    public Neuron getNeuron1() {
        return this.neuron1;
    }

    public double getWeight() {
        return this.weight;
    }
}
