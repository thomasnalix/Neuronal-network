package environment;

import brain.Brain;
import utils.Coord;

public class Entity {

    public Brain brain;
    public int fitness;
    public Coord position;

    public Entity() {
        this.brain = new Brain(2, new int[]{ 2, 2 }, 4);
        this.position = new Coord(0, 0);
        this.fitness = 0;
    }
}
