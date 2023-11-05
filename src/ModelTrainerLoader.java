import environment.Entity;
import environment.World;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModelTrainerLoader extends JPanel {

    private final BufferedImage buffer;
    private final int sizeScreenX;
    private final int sizeScreenY;

    public ModelTrainerLoader(int sizeScreenX, int sizeScreenY) {
        this.sizeScreenX = sizeScreenX;
        this.sizeScreenY = sizeScreenY;
        buffer = new BufferedImage(this.sizeScreenX, this.sizeScreenY, BufferedImage.TYPE_INT_ARGB);
    }

    private static double[] getInputValues(Entity entity) {
        int x = entity.position.x();
        int y = entity.position.y();
        return new double[]{x, y};
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buffer, 0, 0, this);
    }

    public void updateMap(World world) {
        Graphics g = buffer.getGraphics();
        int cellSize = sizeScreenX / world.sizeX;
        Color color = new Color(52, 235, 152);
        for (int i = 0; i < world.sizeX; i++) {
            for (int j = 0; j < world.sizeY; j++) {
                if (world.map[i][j] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                } else if (world.map[i][j] == -1) {
                    g.setColor(color);
                    g.fillOval(j * cellSize, i * cellSize, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
        g.dispose();
        repaint();
    }

    public void load(int sizeX, int sizeY) {
        JFrame frame = new JFrame("Loading - Neural Network");
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(sizeScreenX, sizeScreenY);
        frame.setVisible(true);

        World world = new World(sizeX, sizeY);
        Entity entity = new Entity();
        world.addEntityToMap(entity);
        entity.brain.loadBrain("best_brain.txt");

        while (true) {
            world.executeOutput(entity, entity.brain.predict(getInputValues(entity)));
            this.updateMap(world);
            if (world.getFitness(entity) >= 1) {
                world.reset();
                world.addEntityToMap(entity);
            }
        }
    }

    /**
     * Train the neural network with a genetic algorithm
     * @param sizeX Size of the map in X
     * @param sizeY Size of the map in Y
     * @param threshold Select the best entities with a fitness >= threshold
     * @param mutationRate Apply a mutation rate between -mutationRate and +mutationRate to a random synapse
     * @param gui Enable the GUI
     */
    public void train(int sizeX, int sizeY, int generations, int steps, int nbEntities, int threshold, double mutationRate, boolean gui) {
        World world = new World(sizeX, sizeY);
        Random random = new Random();

        if (gui) {
            JFrame frame = new JFrame("Training - Neural Network");
            frame.add(this);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(sizeScreenX, sizeScreenY);
            frame.setVisible(true);
        }

        for (int i = 0; i < nbEntities; i++) world.addEntityToMap(new Entity());

        for (int gen = 0; gen < generations; gen++) {
            ArrayList<Entity> entities = world.getEntities();
            for (int step = 0; step < steps; step++) {
                for (Entity entity : entities) {
                    world.executeOutput(entity, entity.brain.predict(getInputValues(entity)));
                    entity.fitness = world.getFitness(entity);
                }
                if (gui && gen % 10 == 0) this.updateMap(world);
            }
            entities.sort((e1, e2) -> Integer.compare(e2.fitness, e1.fitness));

            int numberOfEntityWithFitness = (int) entities.stream().filter(entity -> entity.fitness >= threshold).count();
            System.out.println("Generation: " + gen + " - Number of entity with fitness > " + threshold + " : " + numberOfEntityWithFitness);

            if (numberOfEntityWithFitness == nbEntities) break;

            List<Entity> entitiesGood = new ArrayList<>(entities.stream()
                    .filter(entity -> entity.fitness >= threshold)
                    .toList());

            world.reset();
            for (Entity goodEntity : entitiesGood) {
                goodEntity.brain.applyMutation(mutationRate);
                goodEntity.fitness = 0;
                world.addEntityToMap(goodEntity);
            }
            if (entitiesGood.isEmpty()) {
                entitiesGood.add(new Entity());
            }

            for (int i = 0; i < nbEntities - entitiesGood.size(); i++) {
                int rdm = random.nextInt(entitiesGood.size());
                Entity entity = new Entity();
                entity.brain.setBrainState(entitiesGood.get(rdm).brain.getBrainState());
                entity.brain.applyMutation(mutationRate);
                entity.fitness = 0;
                world.addEntityToMap(entity);
            }
        }
        world.getEntities().get(0).brain.saveToFile("best_brain.txt");
        if (gui) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
        }
    }
}
