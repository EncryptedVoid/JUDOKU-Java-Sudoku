public class GraphicalEngine extends CONSTANTS{

    private String[][] Board;
    private String[] SelectionBoard;

    public GraphicalEngine(String [][] Board, String[] SelectionBoard) {
        this.Board = Board;
        this.SelectionBoard = SelectionBoard;
    }

    public void renderStage(String[][] stage) {
        System.out.print(fullFloor());
        System.out.print("\n");
        for (int i = 0; i < STAGE_SIZE; i++) {
            System.out.print(WALL);
            for (int j = 0; j < STAGE_SIZE; j++) {
                System.out.print(stage[i][j]);
                if (j % SEGMENT_SIZE == 2) {
                    System.out.print(WALL);
                }
            }
            if (i % SEGMENT_SIZE == 2) {
                System.out.print(fullFloor());
            }
            System.out.print("\n");
        }
    }

    public void resetStage() {
        for (int i = 0; i < STAGE_SIZE; i++) {
            for (int j = 0; j < STAGE_SIZE; j++) {
                Board[i][j] = NULL_SYMBOL;
            }
        }
    }

    private String fullFloor() {
        return ("\n"
                + " < "
                + (FLOOR.repeat(Math.max(0, STAGE_SIZE + SEGMENT_SIZE - 1)))
                + " > ");
    }



}
