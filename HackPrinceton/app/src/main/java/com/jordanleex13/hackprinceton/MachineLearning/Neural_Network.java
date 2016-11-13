package com.jordanleex13.hackprinceton.MachineLearning;

/**
 * Created by Jordan on 2016-11-12.
 */

import java.text.*;
import java.util.*;

public class Neural_Network {
    static {
        Locale.setDefault(Locale.ENGLISH);
    }

    final boolean isTrained = false;
    final DecimalFormat df;
    final Random rand = new Random();
    final ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
    final ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
    final ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();
    final Neuron bias = new Neuron();
    final int[] layers;
    final int randomWeightMultiplier = 1;

    final double epsilon = 0.00000000001;

    final double learningRate = 0.9f;
    final double momentum = 0.7f;

    // trainInputs for xor problem
    final double trainInputs[][] = {
            {0.00446, 0.01065, 0.00280, 0.0004, 0.34630, 0.2767, 0.00184, 0.35582}, // sports
            {0.05386, 0.0019, 0.01588, 0.00058, 0.00620, 0.27551, 0.00437, 0.64169}, // sports
            {0.00002, 0.00274, 0.00001, 0.00000, 0.00204, 0.99254, 0.00261, 0.00005}, // sad
            {0.00537, 0.00743, 0.03400, 0.00662, 0.00102, 0.54242, 0.37547, 0.02567}, // sad
            {0.00056, 0.00667, 0.00036, 0.00015, 0.00096, 0.96081, 0.01264, 0.01785}, // indie
            {0.00052, 0.00577, 0.00097, 0.00029, 0.02664, 0.94637, 0.01468, 0.00475}, // indie
            {0.01466, 0.00395, 0.01720, 0.00541, 0.59387, 0.23081, 0.01962, 0.11448}, // exciting concert
            {0.00057, 0.00025, 0.00171, 0.00044, 0.96123, 0.00902, 0.0109, 0.03568}, // exciting concert
            {0.01305, 0.06792, 0.00545, 0.00077, 0.00734, 0.60866, 0.29622, 0.00059}, // sad
            {0.01006, 0.02830, 0.00502, 0.00095, 0.01040, 0.87461, 0.06516, 0.00551}, // sad
            {0.00718, 0.02925, 0.00875, 0.00117, 0.03456, 0.74030, 0.24855, 0.02327} // sad
    };

    // Corresponding outputs, xor training data
    final double trainOutputs[][] = {
            {0.95, 0.1, 0.85, 0.95},
            {0.95, 0.15, 0.8, 0.95},
            {0.05, 0.9, 0.15, 0.8},
            {0.10, 0.95, 0.1, 0.88},
            {0.10, 0.5, 0.5, 0.2},
            {0.15, 0.45, 0.3, 0.15},
            {0.05, 0.05, 0.95, 0.95},
            {0.012, 0.1, 0.9, 0.9},
            {0.01, 0.75, 0.01, 0.75},
            {0.05, 0.70, 0.05, 0.70},
            {0.02, 0.80, 0.02, 0.80}
    };


    static double testInput[][] = {{0.00718, 0.02925, 0.00875, 0.00117, 0.03456, 0.74030, 0.24855, 0.02327}};
    double testOutput[][] = { { -1 }, { -1 }, { -1 }, { -1 } };
    double resultOutputs[][] = { { -1 }, { -1 }, { -1 }, { -1 } }; // dummy init
    double output[];

    // for weight update all
    final HashMap<String, Double> weightUpdate = new HashMap<String, Double>();


    public static double[] getMood(double[][] userInput) {
        testInput = userInput;
        Neural_Network nn = new Neural_Network(8, 5, 4);
        int maxRuns = 50000;
        double minErrorCondition = 0.001;
        nn.run(maxRuns, minErrorCondition);
        return nn.runResult(maxRuns, minErrorCondition);
    }

    public Neural_Network(int input, int hidden, int output) {
        this.layers = new int[] { input, hidden, output };
        df = new DecimalFormat("#.0#");

        /**
         * Create all neurons and connections Connections are created in the
         * neuron class
         */
        for (int i = 0; i < layers.length; i++) {
            if (i == 0) { // input layer
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    inputLayer.add(neuron);
                }
            } else if (i == 1) { // hidden layer
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    neuron.addInConnectionsS(inputLayer);
                    neuron.addBiasConnection(bias);
                    hiddenLayer.add(neuron);
                }
            }

            else if (i == 2) { // output layer
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    neuron.addInConnectionsS(hiddenLayer);
                    neuron.addBiasConnection(bias);
                    outputLayer.add(neuron);
                }
            } else {
                System.out.println("!Error NeuralNetwork init");
            }
        }

        // initialize random weights
        for (Neuron neuron : hiddenLayer) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
        for (Neuron neuron : outputLayer) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }

        // reset id counters
        Neuron.counter = 0;
        Connection.counter = 0;

        if (isTrained) {
            trainedWeights();
            updateAllWeights();
        }
    }

    // random
    double getRandom() {
        return randomWeightMultiplier * (rand.nextDouble() * 2 - 1); // [-1;1[
    }

    /**
     *
     * @param trainInputs
     *            There is equally many neurons in the input layer as there are
     *            in input variables
     */
    public void setInput(double trainInputs[]) {
        for (int i = 0; i < inputLayer.size(); i++) {
            inputLayer.get(i).setOutput(trainInputs[i]);
        }
    }

    public double[] getOutput() {
        double[] outputs = new double[outputLayer.size()];
        for (int i = 0; i < outputLayer.size(); i++)
            outputs[i] = outputLayer.get(i).getOutput();
        return outputs;
    }

    /**
     * Calculate the output of the neural network based on the input The forward
     * operation
     */
    public void activate() {
        for (Neuron n : hiddenLayer)
            n.calculateOutput();
        for (Neuron n : outputLayer)
            n.calculateOutput();
    }

    /**
     * all output propagate back
     *
     * @param expectedOutput
     *            first calculate the partial derivative of the error with
     *            respect to each of the weight leading into the output neurons
     *            bias is also updated here
     */
    public void applyBackpropagation(double expectedOutput[]) {

        // error check, normalize value ]0;1[
        for (int i = 0; i < expectedOutput.length; i++) {
            double d = expectedOutput[i];
            if (d < 0 || d > 1) {
                if (d < 0)
                    expectedOutput[i] = 0 + epsilon;
                else
                    expectedOutput[i] = 1 - epsilon;
            }
        }

        int i = 0;
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double ak = n.getOutput();
                double ai = con.leftNeuron.getOutput();
                double desiredOutput = expectedOutput[i];

                double partialDerivative = -ak * (1 - ak) * ai
                        * (desiredOutput - ak);
                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
            i++;
        }

        // update weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double aj = n.getOutput();
                double ai = con.leftNeuron.getOutput();
                double sumKoutputs = 0;
                int j = 0;
                for (Neuron out_neu : outputLayer) {
                    double wjk = out_neu.getConnection(n.id).getWeight();
                    double desiredOutput = (double) expectedOutput[j];
                    double ak = out_neu.getOutput();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }

                double partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
        }
    }

    void run(int maxSteps, double minError) {
        int i;
        // Train neural network until minError reached or maxSteps exceeded
        double error = 1;
        for (i = 0; i < maxSteps && error > minError; i++) {
            error = 0;
            for (int p = 0; p < trainInputs.length; p++) {
                setInput(trainInputs[p]);

                activate();

                output = getOutput();
                trainOutputs[p] = output;

                for (int j = 0; j < trainOutputs[p].length; j++) {
                    double err = Math.pow(output[j] - trainOutputs[p][j], 2);
                    error += err;
                }
                applyBackpropagation(trainOutputs[p]);
            }
        }

        //printResult();
    }
    double[] runResult(int maxSteps, double minError) {
        int i;
        // Train neural network until minError reached or maxSteps exceeded
        double error = 1;
        for (i = 0; i < maxSteps && error > minError; i++) {
            error = 0;
            for (int p = 0; p < testInput.length; p++) {
                setInput(testInput[p]);

                activate();

                output = getOutput();
                testOutput[p] = output;

                for (int j = 0; j < testOutput[p].length; j++) {
                    double err = Math.pow(output[j] - testOutput[p][j], 2);
                    error += err;
                }
            }
        }
        double[] mood = new double[4];
        for (int p = 0; p < testInput.length; p++) {
            System.out.print("You want: ");
            for (int x = 0; x < testOutput[p].length; x++) {
                System.out.print(testOutput[p][x] + " ");
                mood[x] = testOutput[p][x];
            }
            System.out.println();
        }
        printTestResult();
        return mood;
    }
    void printTestResult()
    {
        for (int p = 0; p < testInput.length; p++) {
            System.out.print("You want: ");
            for (int x = 0; x < testOutput[p].length; x++) {
                System.out.print(testOutput[p][x] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    void printResult()
    {
        for (int p = 0; p < trainInputs.length; p++) {
            System.out.print("trainInputs: ");
            for (int x = 0; x < layers[0]; x++) {
                System.out.print(trainInputs[p][x] + " ");
            }
            System.out.println();

            System.out.print("EXPECTED: ");
            for (int x = 0; x < layers[2]; x++) {
                System.out.print(trainOutputs[p][x] + " ");
            }
            System.out.println();

            System.out.print("ACTUAL: ");
            for (int x = 0; x < layers[2]; x++) {
                System.out.print(resultOutputs[p][x] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    String weightKey(int neuronId, int conId) {
        return "N" + neuronId + "_C" + conId;
    }

    /**
     * Take from hash table and put into all weights
     */
    public void updateAllWeights() {
        // update weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                double newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }
        // update weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String key = weightKey(n.id, con.id);
                double newWeight = weightUpdate.get(key);
                con.setWeight(newWeight);
            }
        }
    }

    // trained data
    void trainedWeights() {
        weightUpdate.clear();

        weightUpdate.put(weightKey(3, 0), 1.03);
        weightUpdate.put(weightKey(3, 1), 1.13);
        weightUpdate.put(weightKey(3, 2), -.97);
        weightUpdate.put(weightKey(4, 3), 7.24);
        weightUpdate.put(weightKey(4, 4), -3.71);
        weightUpdate.put(weightKey(4, 5), -.51);
        weightUpdate.put(weightKey(5, 6), -3.28);
        weightUpdate.put(weightKey(5, 7), 7.29);
        weightUpdate.put(weightKey(5, 8), -.05);
        weightUpdate.put(weightKey(6, 9), 5.86);
        weightUpdate.put(weightKey(6, 10), 6.03);
        weightUpdate.put(weightKey(6, 11), .71);
        weightUpdate.put(weightKey(7, 12), 2.19);
        weightUpdate.put(weightKey(7, 13), -8.82);
        weightUpdate.put(weightKey(7, 14), -8.84);
        weightUpdate.put(weightKey(7, 15), 11.81);
        weightUpdate.put(weightKey(7, 16), .44);
    }

    public void printWeightUpdate() {

    }

    public void printAllWeights() {

    }
}
