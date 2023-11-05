package brain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Brain {

    private final List<Synapse> synapses;
    private final Neuron[] input_neurons;
    private final Neuron[][] hidden_neurons;
    private final Neuron[] output_neurons;

    private final Double[] output_values;

    public Brain(int nb_input_neurons, int[] hidden_layer, int nb_output_neurons) {
        int nb_layers = hidden_layer.length;
        this.hidden_neurons = new Neuron[nb_layers][];
        for (int i = 0; i < nb_layers; i++) {
            this.hidden_neurons[i] = new Neuron[hidden_layer[i]];
        }
        this.input_neurons = new Neuron[nb_input_neurons];
        this.output_neurons = new Neuron[nb_output_neurons];
        this.output_values = new Double[nb_output_neurons];

        int totalSynapses = nb_input_neurons;
        if (nb_layers > 0) {
            totalSynapses *= hidden_layer[0];
        }
        for (int i = 0; i < nb_layers - 1; i++) {
            totalSynapses += hidden_layer[i] * hidden_layer[i + 1];
        }
        totalSynapses += hidden_layer[nb_layers - 1] * nb_output_neurons;

        this.synapses = new ArrayList<>(totalSynapses);

        for (int i = 0; i < nb_input_neurons; i++) {
            this.input_neurons[i] = new Neuron();
        }
        for (int i = 0; i < nb_layers; i++) {
            for (int j = 0; j < hidden_layer[i]; j++) {
                this.hidden_neurons[i][j] = new Neuron();
            }
        }
        for (int i = 0; i < nb_output_neurons; i++) {
            this.output_neurons[i] = new Neuron();
        }

        for (Neuron input_neuron : this.input_neurons) {
            for (Neuron hidden_neuron : this.hidden_neurons[0]) {
                this.addSynapse(input_neuron, hidden_neuron);
            }
        }

        int last_hidden_layer_index = 0;
        for (int i = 0; i < nb_layers - 1; i++) {
            for (Neuron hidden_neuron_i : this.hidden_neurons[i]) {
                for (Neuron hidden_neuron_j : this.hidden_neurons[i + 1]) {
                    this.addSynapse(hidden_neuron_i, hidden_neuron_j);
                }
            }
            last_hidden_layer_index = i;
        }

        for (Neuron hidden_neuron : this.hidden_neurons[last_hidden_layer_index]) {
            for (Neuron output_neuron : this.output_neurons) {
                this.addSynapse(hidden_neuron, output_neuron);
            }
        }
    }

    public void addSynapse(Neuron neuron1, Neuron neuron2) {
        Synapse synapse = new Synapse(neuron1, Math.random() * 2 - 1);
        neuron2.addSynapseInput(synapse);
        this.synapses.add(synapse);
    }

    public Double[] predict(double[] input_values) {
        for(int i = 0; i < this.input_neurons.length; i++) {
            this.input_neurons[i].setSum(input_values[i]);
        }

        for (Neuron[] neuronsLayer : this.hidden_neurons) {
            for (Neuron neuron : neuronsLayer) {
                neuron.activate();
            }
        }

        for (int j = 0; j < this.output_neurons.length; j++) {
            this.output_neurons[j].activate();
            this.output_values[j] = this.output_neurons[j].getSum();
        }

        return output_values;
    }

    public double[] getBrainState() {
        double[] brain_state = new double[this.synapses.size()];
        int i = 0;
        for (Synapse synapse : this.synapses) {
            brain_state[i] = synapse.getWeight();
            i++;
        }
        return brain_state;
    }


    public void setBrainState(double[] brain_state) {
        for (int i = 0; i < brain_state.length; i++) {
            this.synapses.get(i).setWeight(brain_state[i]);
        }
    }

    public void applyMutation(double mutation_rate) {
        int random_synapse = (int) (Math.random() * this.synapses.size());
        this.synapses.get(random_synapse).setWeight(
                this.synapses.get(random_synapse).getWeight() + Math.random() * 2 * mutation_rate - mutation_rate);
    }

    public void saveToFile(String filename) {
        try {
            double[] state = this.getBrainState();
            FileWriter writer = new FileWriter(filename);
            for (double v : state) {
                writer.write(v + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBrain(String filename) {
        List<Double> state = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    double value = Double.parseDouble(line);
                    state.add(value);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBrainState(state.stream().mapToDouble(Double::doubleValue).toArray());
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Neurons:\n");
        str.append(" ==> Input Layer\n");
        for (Neuron neuron : this.input_neurons) {
            str.append("  - neuron: ").append(neuron.getSum()).append("\n");
        }
        for (Neuron[] neuronsLayer : this.hidden_neurons) {
            str.append(" ==> Hidden Layer\n");
            for (Neuron neuron : neuronsLayer) {
                str.append("  - neuron: ").append(neuron.getSum()).append("\n");
            }
        }
        str.append(" ==> Output Layer\n");
        for (Neuron neuron : this.output_neurons) {
            str.append("  - neuron: ").append(neuron.getSum()).append("\n");
        }

        str.append("Synapses:\n");
        for (Synapse synapse : this.synapses) {
            str.append("brain.Synapse: ").append(synapse.getWeight()).append("\n");
        }
        return str.toString();
    }
}
