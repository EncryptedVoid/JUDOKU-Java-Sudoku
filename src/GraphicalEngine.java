public class GraphicalEngine extends CONSTANTS {

    private String[][] Stage;
    private String[] SelectionSymbols;
    private int[] SelectionCounter;

    public GraphicalEngine(String [][] Stage) {
        this.Stage = Stage;
        this.SelectionSymbols = SYMBOL_DECK;
        this.SelectionCounter = SELECTION_COUNTER;

        resetStage();
    }

    public void renderStage() {
        System.out.print(FLOOR);
        System.out.print("\n");
        for (int i = 0; i < STAGE_SIZE; i++) {
            System.out.print(WALL);
            for (int j = 0; j < STAGE_SIZE; j++) {
                System.out.print(Stage[i][j]);
                if (j % SEGMENT_SIZE == 2) {
                    System.out.print(WALL);
                }
            }
            if (i % SEGMENT_SIZE == 2) {
                System.out.print(FLOOR);
            }
            System.out.print("\n");
        }
    }

    public void resetStage() {
        for (int i = 0; i < STAGE_SIZE; i++) {
            for (int j = 0; j < STAGE_SIZE; j++) {
                Stage[i][j] = NULL_SYMBOL;
            }
        }
    }



}
