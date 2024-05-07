import java.util.HashSet;
import java.util.Objects;

public class LogicalEngine extends CONSTANTS{

    private String[][] Stage;

    public String[] miniWise;
    public String[][] RowWise, ColumnWise, BoxWise;

    public LogicalEngine(String[][] stage) {
        this.Stage = stage;

        miniWise = new String[STAGE_SIZE];
        RowWise = new String[STAGE_SIZE][STAGE_SIZE];
        ColumnWise = new String[STAGE_SIZE][STAGE_SIZE];
        BoxWise = new String[STAGE_SIZE/SEGMENT_SIZE][STAGE_SIZE];
    }

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

}
