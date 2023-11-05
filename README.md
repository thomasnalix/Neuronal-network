# README - Neural Network Training and Visualization

This project is a Java-based implementation of training and visualizing neural networks using a genetic algorithm. The neural networks are trained to navigate and adapt within a 2D environment represented by a grid. This README provides an overview of the project structure, its components, and how to use it.

## Table of Contents

1. [Project Description](#project-description)
2. [Usage](#usage)

## Project Description

The project is designed to train and evolve neural networks to navigate a 2D grid-based environment. It consists of three main components:

1. **ModelTrainerLoader**: This class is responsible for visualizing the training and loading of the neural network. It includes methods for loading a pre-trained network and training a new one using a genetic algorithm. The training and loading processes are visualized in a graphical user interface (GUI).

2. **World and Entity**: These classes define the 2D environment in which the neural networks are trained. The World class manages the grid and entities within it, while the Entity class represents an individual agent navigating the environment.

3. **Brain, Neuron and Synapse**: The Brain class represents the neural network itself. It includes methods for creating the network architecture, making predictions, applying mutations, and saving/loading network weights to/from files.

## Usage

1. **Clone the Repository**: Clone this repository to your local machine using the following command:

   ```shell
   git clone https://github.com/thomasnalix/Neuronal-network.git
   ```

2. **Compile and Run**: Compile and run the project using your Java IDE or command-line tools. The `Main` class contains the `main` method to start the program.

3. **Training**: To train new neural networks, modify the parameters in the `Main` class, such as `GENERATIONS`, `STEPS`, `NUM_ENTITIES`, `THRESHOLD`, and `MUTATION_RATE`. Then, execute the program.

   - The project is currently configured to train neural networks to navigate a 2D environment to reach x < 10 && y < 10. The goal can be changed by modifying the getFitness() method in the World class.
   - To modify neural network parameters, such as the number of hidden layers or neurons per layer, go to Entity and modify Brain constructor.


4. **Loading**: To load and interact with a pre-trained network, use the `cartePanel.load(SIZE_X, SIZE_Y);` method in the `Main` class.

5. **Visualization**: The 2D environment and neural network behavior will be visualized in a graphical user interface (GUI).



Happy neural network training and navigation! 🤖🌍