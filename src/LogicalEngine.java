import java.util.HashSet;
import java.util.Objects;

public class LogicalEngine extends CONSTANTS{

    private String[][] Stage;
    private String[] acceptedSymbols = SYMBOL_DECK;
    private String nullSymbol = NULL_SYMBOL;

    private String[] miniWise = new String[STAGE_SIZE];
    private static String[][] RowWise = new String[STAGE_SIZE][STAGE_SIZE],
            ColumnWise = new String[STAGE_SIZE][STAGE_SIZE],
            BoxWise = new String[STAGE_SIZE][STAGE_SIZE];

    public void generateGroupings(String[][] board) {

        // ROW WISE GROUPINGS
        for(int rowIndex = 0, groupIndex = 0, elementIndex = 0; rowIndex < STAGE_SIZE; rowIndex++) {

            for(int colIndex = 0; colIndex < STAGE_SIZE; colIndex++) {
                miniWise[elementIndex] = Stage[rowIndex][colIndex];
                elementIndex++;
            }

            RowWise[groupIndex] = miniWise;
            groupIndex++;

        }


        // COLUMN WISE GROUPINGS
        for(int colIndex = 0, groupIndex = 0, elementIndex = 0; colIndex < STAGE_SIZE; colIndex++) {

            for(int rowIndex = 0; rowIndex < SEGMENT_SIZE; rowIndex++) {
                miniWise[elementIndex] = Stage[rowIndex][colIndex];
                elementIndex++;
            }

            ColumnWise[groupIndex] = miniWise;
            groupIndex++;

        }


        // BOX WISE GROUPINGS
        for(int boxStart = 0, elementIndex = 0; boxStart < SEGMENT_SIZE; boxStart++) {

            for(int rowIndex = boxStart; rowIndex < (boxStart + 1) * SEGMENT_SIZE; rowIndex++) {

                for(int colIndex = boxStart; colIndex < (boxStart + 1) * SEGMENT_SIZE; colIndex++) {
                    miniWise[elementIndex] = Stage[rowIndex][colIndex];
                    elementIndex++;
                }

            }

            BoxWise[boxStart] = miniWise;

        }

    }

    public static boolean hasEmptySpace(String[] bundle) {
        for (String s : bundle) {
            if (Objects.equals(s, NULL_SYMBOL)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasDuplicates(String[] bundle) {
        HashSet<String> seen = new HashSet<>();
        for (String s : bundle) {
            if (seen.contains(s)) {
                return true;
            }
            seen.add(s);
        }
        return false;
    }

    public static boolean isComplete(String[] bundle) {
        return !hasEmptySpace(bundle) && !hasDuplicates(bundle);
    }

    public static boolean isGameComplete() {

        for (int i = 0; i < STAGE_SIZE; i++) {
            if( (!isComplete(RowWise[i]))
                    || (!isComplete(ColumnWise[i]))
                    || (!isComplete(BoxWise[i])) ) {
                return false;
            }
        }

        return true;

    }

    public static boolean validateChoice(String choice) {

        for(int i = 0; i < STAGE_SIZE; i++) {
            for(int j = 0; j < STAGE_SIZE; j++) {
                if( ((RowWise[i][j]).equals(choice))
                        || ((ColumnWise[i][j]).equals(choice))
                        || ((BoxWise[i][j]).equals(choice)) ) {
                    return false;
                }
            }
        }

        return true;
    }

}
