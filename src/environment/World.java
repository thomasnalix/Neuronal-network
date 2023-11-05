package environment;

import utils.Coord;

import java.util.ArrayList;
import java.util.Random;

public class World {
    public int sizeX;
    public int sizeY;
    public int[][] map;

    private final ArrayList<Entity> entities = new ArrayList<>();

    public World(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.map = new int[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                this.map[i][j] = 0;
            }
        }
    }


    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public int getFitness(Entity entity) {
        int x = entity.position.x();
        int y = entity.position.y();
        if (x < 15 && y < 15) {
            return 1;
        }
        return 0;
    }

    public void moveEntity(Entity entity, Coord coord) {
        map[coord.x()][coord.y()] = -1;
        entity.position = coord;
    }

    public void reset() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                map[i][j] = 0;
            }
        }
        entities.clear();
    }

    public void addEntityToMap(Entity entity) {
        Random random = new Random();
        int[] position = {random.nextInt(sizeX - 1), random.nextInt(sizeY - 1)};
        while (map[position[0]][position[1]] != 0) {
            position[0] = random.nextInt(sizeX - 1);
            position[1] = random.nextInt(sizeY - 1);
        }
        entities.add(entity);
        moveEntity(entity, new Coord(position[0], position[1]));
    }

    public void executeOutput(Entity entity, Double[] outputValues) {
        moveTop(entity, outputValues[0]);
        moveRight(entity, outputValues[1]);
        moveBottom(entity, outputValues[2]);
        moveLeft(entity, outputValues[3]);
    }

    public void moveTop(Entity entity, double value) {
        Coord position = entity.position;
        int x = position.x();
        int y = position.y();
        if (value > 0 && x < sizeX - 1 && map[x + 1][y] == 0) {
            map[x][y] = 0;
            x++;
            moveEntity(entity, new Coord(x, y));
        }
    }

    public void moveBottom(Entity entity, double value) {
        Coord position = entity.position;
        int x = position.x();
        int y = position.y();
        if (value > 0 && x > 0 && map[x - 1][y] == 0) {
            map[x][y] = 0;
            x--;
            moveEntity(entity, new Coord(x, y));
        }
    }

    public void moveRight(Entity entity, double value) {
        Coord position = entity.position;
        int x = position.x();
        int y = position.y();
        if (value > 0 && y < sizeY - 1 && map[x][y + 1] == 0) {
            map[x][y] = 0;
            y++;
            moveEntity(entity, new Coord(x, y));
        }
    }

    public void moveLeft(Entity entity, double value) {
        Coord position = entity.position;
        int x = position.x();
        int y = position.y();
        if (value > 0 && y > 0 && map[x][y - 1] == 0) {
            map[x][y] = 0;
            y--;
            moveEntity(entity, new Coord(x, y));
        }
    }
}