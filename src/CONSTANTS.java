public class CONSTANTS extends Customizer{

    public static final int STAGE_SIZE = 9;
    public static final int SEGMENT_SIZE = 3;

    public static final String WALL = " | ";
    public static final String FLOOR = STR."\n < \{((" - ").repeat(Math.max(0, STAGE_SIZE + SEGMENT_SIZE - 1)))} > ";
    public static final String NULL_SYMBOL = " # ";

    public static final String[] SYMBOL_DECK = {"1","2","3","4","5","6","7","8","9"};
    public static int [] SELECTION_COUNTER = {STAGE_SIZE,STAGE_SIZE,STAGE_SIZE
                                            ,STAGE_SIZE,STAGE_SIZE,STAGE_SIZE
                                            ,STAGE_SIZE,STAGE_SIZE,STAGE_SIZE};

}
