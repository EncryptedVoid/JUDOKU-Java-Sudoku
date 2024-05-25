import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class BETA extends Customizer{

    public static final int STAGE_SIZE = 9;
    public static final int SEGMENT_SIZE = 3;

    public static final String WALL = " | ";
    public static final String FLOOR = STR."\n < \{((" - ").repeat(Math.max(0, STAGE_SIZE + SEGMENT_SIZE - 1)))} > ";
    public static final String NULL_SYMBOL = " # ";

    public static String[][] Stage;
    public static String[] miniWise = new String[STAGE_SIZE];
    public static String[][] RowWise = new String[STAGE_SIZE][STAGE_SIZE],
            ColumnWise = new String[STAGE_SIZE][STAGE_SIZE],
            BoxWise = new String[STAGE_SIZE][STAGE_SIZE];

    public static final Scanner consoleInput = new Scanner(System.in);
    private static final String ASSET_PATH = "C:/Users/ashiq/OneDrive/Desktop/SUDOKO/SUDOKO_GAME_JAVA/ASSETS/MAPS/";
    private static final String[] LOADING_MESSAGES = {
            "Initializing game engine...",
            "Initializing logic engine...",
            "Loading assets...",
            "Generating maps...",
            "Spawning console...",
            "Setting up virtual environment...",
            "Extracting decision making model..."
    };

    public static int [] Decision = new int[3];

    private static final String GREETING_MESSAGE = "Welcome to JUDOKO - JAVA SUDOKO!";
    private static final int LOADING_DELAY_SECONDS = 2;

    public static void showLoadingScreen() {
        try {
            for (String message : LOADING_MESSAGES) {
                System.out.println(message);
                TimeUnit.SECONDS.sleep(LOADING_DELAY_SECONDS);
            }

            System.out.println();
            System.out.println("===================================");
            System.out.println(GREETING_MESSAGE);
            System.out.println("===================================");
        } catch (InterruptedException e) {
            System.err.println("Loading interrupted!");
        }
    }

    public static String getDifficulty() {
        while (true) {
            System.out.print("What difficulty would you like to play?\n1: BEGINNER\n2: INTERMEDIATE\n3: PROFESSOR\n4: PRODIGY\n--> ");
            String difficulty = consoleInput.next();

            if (difficulty.contains("1") || difficulty.contains("B")) { return "EASY"; }
            if (difficulty.contains("2") || difficulty.contains("T")) { return "MEDIUM"; }
            if (difficulty.contains("3") || difficulty.contains("S")) { return "HARD"; }
            if (difficulty.contains("4") || difficulty.contains("Y")) { return "EXTREME"; }
            else { System.out.println("Invalid input. Please select a valid difficulty level."); }
        }
    }

    public static void showMenu() {
        String difficulty = getDifficulty();
        getMapSelection(difficulty);
    }

    public static void startUp() {
        //showLoadingScreen();
        showMenu();
    }

    public static void shutDown() {
        System.out.println("Shutting down...");
    }

    public static String[][] extractMap(String mapFileLocation) {
        String[][] extractedMap = new String[9][9];

        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(mapFileLocation))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split(" ");
                extractedMap[i] = splitLine;
                i++;
            }
        } catch (IOException e) {
            System.err.println(STR."Error reading file: \{e.getMessage()}");
        }

        return extractedMap;
    }

    public static void getMapSelection(String difficulty) {
        System.out.println("What level would you like to play?");
        int levelChoice;

        while (true) {
            displayMapOptions(difficulty);
            System.out.print("Please enter the number of the map you would like: ");
            levelChoice = consoleInput.nextInt();

            int numberOfLevels = (getNumberOfFilesInFolder(difficulty) / 2);

            if (levelChoice <= numberOfLevels && levelChoice >= 0) {
                Stage = extractMap(STR."\{ASSET_PATH}\{difficulty}/\{levelChoice}B.txt");
                return;
            }
        }
    }

    public static void displayMapOptions(String difficulty) {
        int numberOfLevels = (getNumberOfFilesInFolder(difficulty) / 2);
        for (int i = 1; i <= numberOfLevels; i++) {
            System.out.print(STR."\n\t\t\t\tLevel \{i}");
            render(extractMap(STR."\{ASSET_PATH}\{difficulty}/\{i}B.txt"));
        }
        System.out.println();
    }

    public static int getNumberOfFilesInFolder(String folderName) {
        File folder = new File(ASSET_PATH + folderName);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                return files.length;
            }
        }
        return 0;
    }

    public static int[] decisionMaking(String[][] gameState) {
        String numChoice = null;
        int rowChoice, colChoice = 0;

        do {
            System.out.print("What number would you like to place?\n--> ");
            Decision[0] = consoleInput.nextInt();

            System.out.print("What row would you like to place this number on?\n--> ");
            Decision[1] = consoleInput.nextInt();

            System.out.print("What column would you like to place this number on?\n--> ");
            Decision[2] = consoleInput.nextInt();

        } while (!validateChoice(Decision[0], Decision[1], Decision[2], gameState));

        return Decision;
    }

    public static void generateSubspaces(String[][] gameState) {

        // ROW WISE GROUPINGS
        for(int rowIndex = 0, groupIndex = 0, elementIndex = 0; rowIndex < STAGE_SIZE; rowIndex++) {

            for(int colIndex = 0; colIndex < STAGE_SIZE; colIndex++) {
                miniWise[elementIndex] = gameState[rowIndex][colIndex];
                elementIndex++;
            }

            RowWise[groupIndex] = miniWise;
            groupIndex++;

        }

        miniWise = new String[STAGE_SIZE];
        // COLUMN WISE GROUPINGS
        for(int colIndex = 0, groupIndex = 0, elementIndex = 0; colIndex < STAGE_SIZE; colIndex++) {

            for(int rowIndex = 0; rowIndex < SEGMENT_SIZE; rowIndex++) {
                miniWise[elementIndex] = gameState[rowIndex][colIndex];
                elementIndex++;
            }

            ColumnWise[groupIndex] = miniWise;
            groupIndex++;

        }

        miniWise = new String[STAGE_SIZE];
        // BOX WISE GROUPINGS
        for(int boxStart = 0, elementIndex = 0; boxStart < SEGMENT_SIZE; boxStart++) {

            for(int rowIndex = boxStart; rowIndex < (boxStart + 1) * SEGMENT_SIZE; rowIndex++) {

                for(int colIndex = boxStart; colIndex < (boxStart + 1) * SEGMENT_SIZE; colIndex++) {
                    miniWise[elementIndex] = gameState[rowIndex][colIndex];
                    elementIndex++;
                }

            }

            BoxWise[boxStart] = miniWise;

        }

    }

    public static boolean hasEmptySpace(String[] subset) {
        for (String s : subset) {
            if (Objects.equals(s, NULL_SYMBOL)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasDuplicates(String[] subset) {
        HashSet<String> seen = new HashSet<>();
        for (String s : subset) {
            if (seen.contains(s)) {
                return true;
            }
            seen.add(s);
        }
        return false;
    }

    public static boolean isComplete(String[] subset) {
        return !hasEmptySpace(subset) && !hasDuplicates(subset);
    }

    public static boolean isEndGameState() {

        for (int i = 0; i < STAGE_SIZE; i++) {
            if( ! ((isComplete(RowWise[i])) && (isComplete(ColumnWise[i])) && (isComplete(BoxWise[i]))) ) {
                return false;
            }
        }

        return true;

    }

    public static boolean validateChoice(int choice, int row, int col, String [][] gameState) {

        generateSubspaces(gameState);

        String target = STR."\{choice}";

        return ! ( ((RowWise[row][col]).equals(target))
                || ((ColumnWise[row][col]).equals(target))
                || ((BoxWise[row][col]).equals(target))
        );
    }

    public static void simulate(String[][] gameState, int[] choice) {
        gameState[choice[1]][choice[2]] = STR."\{choice[0]}";
        render(gameState);
    }

    public static void render(String[][] gameState) {
        System.out.print(FLOOR);
        System.out.print("\n");
        for (int i = 0; i < STAGE_SIZE; i++) {
            System.out.print(WALL);
            for (int j = 0; j < STAGE_SIZE; j++) {

                if(gameState[i][j].equals(NULL_SYMBOL)) {
                    System.out.print(RED+" "+gameState[i][j]+" ");
                } else{
                    System.out.print(" "+gameState[i][j]+" ");
                }

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

    public static void main (String [] args) {
        startUp();
        int[] choice;

        render(Stage);
        do {
            choice = decisionMaking(Stage);
            simulate(Stage,choice);
        } while (!isEndGameState());

        shutDown();
    }
}