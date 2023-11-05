public class Main {

    private static final int SCREEN_X = 900;
    private static final int SCREEN_Y = 900;
    private static final int SIZE_X = 128;
    private static final int SIZE_Y = 128;
    private static final int GENERATIONS = 100;
    private static final int STEPS = 500;
    private static final int NUM_ENTITIES = 100;
    private static final int THRESHOLD = 1;
    private static final double MUTATION_RATE = 0.1;


    public static void main(String[] args) {
        ModelTrainerLoader cartePanel = new ModelTrainerLoader(SCREEN_X, SCREEN_Y);
        cartePanel.train(SIZE_X, SIZE_Y,GENERATIONS, STEPS, NUM_ENTITIES, THRESHOLD, MUTATION_RATE, false);
        cartePanel.load(SIZE_X, SIZE_Y);
    }
}
