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
    // trainInputs for xor problem
    final double trainInputs[][] = {
            {0.00446, 0.11065, 0.10280, 0.0004, 0.04630, 0.2767, 0.40184, 0.05582}, //1 sad
            {0.15386, 0.0019, 0.01588, 0.00058, 0.00620, 0.17551, 0.00437, 0.64169}, //2 sports
            {0.00002, 0.00274, 0.00001, 0.00000, 0.00204, 0.99254, 0.00261, 0.00005}, //3 sad
            {0.00537, 0.00743, 0.03400, 0.00662, 0.00102, 0.54242, 0.37547, 0.02567}, //4 sad
            {0.00056, 0.00667, 0.00036, 0.00015, 0.00096, 0.96081, 0.01264, 0.01785}, //5 indie
            {0.00052, 0.00577, 0.00097, 0.00029, 0.02664, 0.94637, 0.01468, 0.00475}, //6 indie
            {0.01466, 0.00395, 0.01720, 0.00541, 0.59387, 0.23081, 0.01962, 0.11448}, //7 exciting concert
            {0.00057, 0.00025, 0.00171, 0.00044, 0.96123, 0.00902, 0.0109, 0.03568}, //8 exciting concert
            {0.01305, 0.06792, 0.00545, 0.00077, 0.00734, 0.60866, 0.29622, 0.00059}, //9 sad
            {0.01006, 0.02830, 0.00502, 0.00095, 0.01040, 0.57461, 0.36516, 0.00551}, //10 sad
            {0.00718, 0.02925, 0.00875, 0.00117, 0.03456, 0.74030, 0.24855, 0.02327}, //11 sad
            {0.00829, 0.06547, 0.00842, 0.00099, 0.02362,0.77196, 0.11691,0.00433}, //
            {0.00008, 0.0000, 0.00001, 0.00003, 0.99891, 0.00002, 0.00001, 0.00095}, //
            {0.00133, 0.12657, 0.00327, 0.00019, 0.07225, 0.78274, 0.01004, 0.00361}, //
            //anger , contempt, disgust,  fear, happiness, neutral, sad,   surprised
            {0.00032, 0.00031, 0.06608, 0.00000, 0.92005, 0.01232, 0.00009, 0.00084}, //15 excited
            {0.00231, 0.03012, 0.03986, 0.00000, 0.00172, 0.72274, 0.20298, 0.00027},// sad
            {0.00005, 0.00128, 0.00007, 0.0000, 0.00015, 0.99322, 0.00397, 0.00126}, // calm
            {0,0,0,0,1,0,0,0}, // happy

            {0.00069, 0.6691, 0.03186, 0.0007, 0.32659, 0.03998, 0.43196, 0.00130}, // sad
            {0.00052, 0.01214, 0.00174, 0.00006, 0.00591, 0.68988, 0.30027, 0.01948}, //20calm
            {0.13618, 0.14211, 0.10115, 0.0013, 0.00002, 0.02284, 0.59748, 0.00010}, // sad
            {0.00105, 0.00236, 0.00159, 0.00001, 0.90726, 0.08723, 0.00046, 0.00003}, // happy
            {0.03737, 0.00003, 0.00141, 0.00151, 0.22936, 0.00191, 0.00011, 0.72830}, // excited
            {0,0,0,0,1,0,0,0}, // happy

            {0.00002, 0.00129, 0, 0, 0.00288, 0.79535, 0.20045, 0}, //25 sad
            {0.00211, 0.00121, 0.00082, 0.00233, 0.00754, 0.07142, 0.00008, 0.91450}, // excited
            {0,0.00005,0,0,0,0.99993,0.0001,0}, // calm
            {0.00078, 0.00004, 0.00988, 0.00178, 0.45705,0.00328, 0.00036, 0.52683}, // excited
            {0.03761, 0.14217, 0.01652, 0.00048, 0.01588, 0.62113, 0.15516, 0.00105}, // sad
            {0,0,0,0,0.9999, 0.00001, 0,0}, //30 happy

            {0.00122, 0.05039, 0.00165, 0.00009, 0.59016, 0.35280,0.00326,0.00042}, // calm
            {0,0,0,0,1,0,0,0}, // excited
            {0.00025, 0.00457, 0.00006, 0.00003, 0, 0.30910, 0.68597, 0.00002}, // sad
            {0,00001, 0.00144, 0, 0, 0.00042, 0.99332, 0.00480, 0.00001}, // calm

            {0,0,0,0,0,0.5,0.5,0} //35 sad
    };

    // Corresponding outputs, xor training data
    // Corresponding outputs, xor training data
    final double trainOutputs[][] = {
            {0, 0.1, 0.85, 0.75}, //1
            {0, 0.15, 0.8, 0.95},
            {0.05, 0.9, 0.15, 0.8},
            {0.10, 0.95, 0.1, 0.9},
            {0.10, 0.5, 0.5, 0.4}, // 5
            {0.1, 0.75, 0.7, 0.4},
            {0.05, 0.05, 0.95, 0.95},
            {0, 0.1, 0.9, 0.9},
            {0, 0.65, 0.01, 0.65},
            {0, 0.4, 0.8, 0.2}, //10
            {0, 0.7, 0, 0.85},
            {0, 0.85, 0., 0.1},
            {0, 0, 1, 0.5},
            {0, 1, 1,0.5},
            {0,0,1,0}, // 15 excited
            {0,1,0,0.5}, // sad
            {0,1,0,0}, //calm
            {0.8,0,0.8,0.8}, // happy
            {0,1,0,0.5}, // sad
            {0,1,0,0}, // 20 calm
            {0,1,0,0.5}, // sad
            {0,0,1,0.5},// happy
            {0,0,1,1},// excited
            {0.9,0,0.9,1},// happy
            {0,1,0,0.5},// 25 sad
            {0.8,0,0,1},// excited
            {0,1,0,0},// calm
            {0,0,0.8,0.8},// excited
            {0,1,0,0}, // sad
            {0,0,0.8,0.5},// 30 happy
            {0,1,0,0},
            {1,0,0,1},
            {0,0.7,0,1},
            {0,1,0,0},
            {0,0,0.7,0} // 35
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
                System.out.println("Error NeuralNetwork init");
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
            for (int p = 0; p < Math.min(trainInputs.length, trainOutputs.length) ; p++) {
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
