import java.util.HashSet;
import java.util.Objects;

public class SudokuBoard implements BoardDisplayable, MoveValidator {
    public static final int BOARD_SIZE = 9;
    public static final int BOX_SIZE = 3;
    public static final String EMPTY_CELL_SYMBOL = " # ";

    private String[][] board;
    private String[][] rows = new String[BOARD_SIZE][BOARD_SIZE];
    private String[][] columns = new String[BOARD_SIZE][BOARD_SIZE];
    private String[][] boxes = new String[BOARD_SIZE][BOARD_SIZE];

    public SudokuBoard(String[][] board) {
        this.board = board;
        updateSubspaces();
    }

    public void displayBoard(String[][] gameState) {
        String columnSeparator = " | ";
        String rowSeparator = "\n < {" + "-".repeat(BOARD_SIZE + BOX_SIZE - 1) + "} > ";

        System.out.print(rowSeparator);
        System.out.print("\n");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(columnSeparator);
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(" " + gameState[i][j] + " ");
                if (j % BOX_SIZE == 2) {
                    System.out.print(columnSeparator);
                }
            }
            if (i % BOX_SIZE == 2) {
                System.out.print(rowSeparator);
            }
            System.out.print("\n");
        }
    }

    private void updateSubspaces() {
        // ROW WISE GROUPINGS
        for (int rowIndex = 0; rowIndex < BOARD_SIZE; rowIndex++) {
            System.arraycopy(board[rowIndex], 0, rows[rowIndex], 0, BOARD_SIZE);
        }

        // COLUMN WISE GROUPINGS
        for (int colIndex = 0; colIndex < BOARD_SIZE; colIndex++) {
            for (int rowIndex = 0; rowIndex < BOARD_SIZE; rowIndex++) {
                columns[colIndex][rowIndex] = board[rowIndex][colIndex];
            }
        }

        // BOX WISE GROUPINGS
        for (int boxStart = 0; boxStart < BOX_SIZE; boxStart++) {
            for (int rowIndex = boxStart * BOX_SIZE; rowIndex < (boxStart + 1) * BOX_SIZE; rowIndex++) {
                for (int colIndex = boxStart * BOX_SIZE; colIndex < (boxStart + 1) * BOX_SIZE; colIndex++) {
                    boxes[boxStart * BOX_SIZE + (rowIndex % BOX_SIZE)][(colIndex % BOX_SIZE)] = board[rowIndex][colIndex];
                }
            }
        }
    }

    public boolean isValidMove(int choice, int row, int col, String[][] gameState) {
        updateSubspaces();

        String target = String.valueOf(choice);

        return !((rows[row][col].equals(target))
                || (columns[row][col].equals(target))
                || (boxes[row / BOX_SIZE * BOX_SIZE + col / BOX_SIZE][(row % BOX_SIZE) * BOX_SIZE + col % BOX_SIZE].equals(target)));
    }

    public boolean isGameComplete() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!(isSubsetComplete(rows[i]) && isSubsetComplete(columns[i]) && isSubsetComplete(boxes[i]))) {
                return false;
            }
        }
        return true;
    }

    private boolean isSubsetComplete(String[] subset) {
        return !containsEmptyCells(subset) && !containsDuplicates(subset);
    }

    private boolean containsEmptyCells(String[] subset) {
        for (String s : subset) {
            if (Objects.equals(s, EMPTY_CELL_SYMBOL)) {
                return false;
            }
        }
        return true;
    }

    private boolean containsDuplicates(String[] subset) {
        HashSet<String> seen = new HashSet<>();
        for (String s : subset) {
            if (!seen.add(s)) {
                return true;
            }
        }
        return false;
    }

    public void applyMove(int[] choice) {
        board[choice[1]][choice[2]] = String.valueOf(choice[0]);
        displayBoard(board);
    }
}
