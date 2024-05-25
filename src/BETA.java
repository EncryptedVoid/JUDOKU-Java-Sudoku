import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class BETA extends Customizer {

    public static final int BOARD_SIZE = 9;
    public static final int BOX_SIZE = 3;

    public static final String COLUMN_SEPARATOR = " | ";
    public static final String ROW_SEPARATOR = "\n < {" + "-".repeat(BOARD_SIZE + BOX_SIZE - 1) + "} > ";
    public static final String EMPTY_CELL_SYMBOL = " # ";

    private static String[][] sudokuBoard;
    private static final String[] rowBuffer = new String[BOARD_SIZE];
    private static final String[][] rows = new String[BOARD_SIZE][BOARD_SIZE];
    private static final String[][] columns = new String[BOARD_SIZE][BOARD_SIZE];
    private static final String[][] boxes = new String[BOARD_SIZE][BOARD_SIZE];

    private static final Scanner consoleInput = new Scanner(System.in);
    private static final String MAPS_DIRECTORY = "C:/Users/ashiq/OneDrive/Desktop/SUDOKO/SUDOKO_GAME_JAVA/ASSETS/MAPS/";
    private static final String[] LOADING_STEPS = {
            "Starting the game engine...",
            "Loading the logic engine...",
            "Fetching game assets...",
            "Creating game maps...",
            "Preparing console interface...",
            "Configuring virtual environment...",
            "Loading decision-making model..."
    };

    private static final int[] playerMove = new int[3];

    private static final String WELCOME_MESSAGE = "Welcome to JUDOKO - JAVA SUDOKO!";
    private static final int LOADING_DELAY = 2;

    public static void displayLoadingScreen() {
        try {
            for (String message : LOADING_STEPS) {
                System.out.println(message);
                TimeUnit.SECONDS.sleep(LOADING_DELAY);
            }

            System.out.println();
            System.out.println("===================================");
            System.out.println(WELCOME_MESSAGE);
            System.out.println("===================================");
        } catch (InterruptedException e) {
            System.err.println("Loading process was interrupted! Please try again.");
        }
    }

    public static String promptForDifficulty() {
        while (true) {
            System.out.print("Select your desired difficulty level:\n1: Beginner\n2: Intermediate\n3: Professor\n4: Prodigy\n--> ");
            String difficulty = consoleInput.next();

            switch (difficulty) {
                case "1":
                case "B":
                    return "EASY";
                case "2":
                case "T":
                    return "MEDIUM";
                case "3":
                case "S":
                    return "HARD";
                case "4":
                case "Y":
                    return "EXTREME";
                default:
                    System.out.println("Invalid selection. Please enter a valid difficulty level (1-4).");
            }
        }
    }

    public static void displayMenu() {
        String difficulty = promptForDifficulty();
        promptForMapSelection(difficulty);
    }

    public static void initializeGame() {
        // displayLoadingScreen();
        displayMenu();
    }

    public static void terminateGame() {
        System.out.println("Thank you for playing! Shutting down the game...");
    }

    public static String[][] loadMapFromFile(String mapFileLocation) {
        String[][] extractedMap = new String[BOARD_SIZE][BOARD_SIZE];

        try (BufferedReader br = new BufferedReader(new FileReader(mapFileLocation))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                extractedMap[i] = line.split(" ");
                i++;
            }
        } catch (IOException e) {
            System.err.println("Unable to read the map file: " + e.getMessage());
        }

        return extractedMap;
    }

    public static void promptForMapSelection(String difficulty) {
        System.out.println("Select the level you would like to play:");
        int levelChoice;

        while (true) {
            showAvailableMaps(difficulty);
            System.out.print("Enter the number corresponding to your chosen map: ");
            levelChoice = consoleInput.nextInt();

            int numberOfLevels = countFilesInDirectory(difficulty) / 2;

            if (levelChoice <= numberOfLevels && levelChoice >= 0) {
                sudokuBoard = loadMapFromFile(MAPS_DIRECTORY + difficulty + "/" + levelChoice + "B.txt");
                return;
            }
        }
    }

    public static void showAvailableMaps(String difficulty) {
        int numberOfLevels = countFilesInDirectory(difficulty) / 2;
        for (int i = 1; i <= numberOfLevels; i++) {
            System.out.print("\n\t\t\t\tMap " + i);
            displayBoard(loadMapFromFile(MAPS_DIRECTORY + difficulty + "/" + i + "B.txt"));
        }
        System.out.println();
    }

    public static int countFilesInDirectory(String folderName) {
        File folder = new File(MAPS_DIRECTORY + folderName);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                return files.length;
            }
        }
        return 0;
    }

    public static int[] getPlayerMove(String[][] gameState) {
        do {
            System.out.print("Enter the number you wish to place (1-9): ");
            playerMove[0] = consoleInput.nextInt();

            System.out.print("Enter the row number (0-8) where you want to place the number: ");
            playerMove[1] = consoleInput.nextInt();

            System.out.print("Enter the column number (0-8) where you want to place the number: ");
            playerMove[2] = consoleInput.nextInt();

        } while (!isValidMove(playerMove[0], playerMove[1], playerMove[2], gameState));

        return playerMove;
    }

    public static void updateSubspaces(String[][] gameState) {
        // ROW WISE GROUPINGS
        for (int rowIndex = 0; rowIndex < BOARD_SIZE; rowIndex++) {
            System.arraycopy(gameState[rowIndex], 0, rows[rowIndex], 0, BOARD_SIZE);
        }

        // COLUMN WISE GROUPINGS
        for (int colIndex = 0; colIndex < BOARD_SIZE; colIndex++) {
            for (int rowIndex = 0; rowIndex < BOARD_SIZE; rowIndex++) {
                columns[colIndex][rowIndex] = gameState[rowIndex][colIndex];
            }
        }

        // BOX WISE GROUPINGS
        for (int boxStart = 0; boxStart < BOX_SIZE; boxStart++) {
            for (int rowIndex = boxStart * BOX_SIZE; rowIndex < (boxStart + 1) * BOX_SIZE; rowIndex++) {
                for (int colIndex = boxStart * BOX_SIZE; colIndex < (boxStart + 1) * BOX_SIZE; colIndex++) {
                    boxes[boxStart * BOX_SIZE + (rowIndex % BOX_SIZE)][(colIndex % BOX_SIZE)] = gameState[rowIndex][colIndex];
                }
            }
        }
    }

    public static boolean containsEmptyCells(String[] subset) {
        for (String s : subset) {
            if (Objects.equals(s, EMPTY_CELL_SYMBOL)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsDuplicates(String[] subset) {
        HashSet<String> seen = new HashSet<>();
        for (String s : subset) {
            if (!seen.add(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSubsetComplete(String[] subset) {
        return !containsEmptyCells(subset) && !containsDuplicates(subset);
    }

    public static boolean isGameComplete() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!(isSubsetComplete(rows[i]) && isSubsetComplete(columns[i]) && isSubsetComplete(boxes[i]))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidMove(int choice, int row, int col, String[][] gameState) {
        updateSubspaces(gameState);

        String target = String.valueOf(choice);

        return !((rows[row][col].equals(target))
                || (columns[row][col].equals(target))
                || (boxes[row / BOX_SIZE * BOX_SIZE + col / BOX_SIZE][(row % BOX_SIZE) * BOX_SIZE + col % BOX_SIZE].equals(target)));
    }

    public static void applyMove(String[][] gameState, int[] choice) {
        gameState[choice[1]][choice[2]] = String.valueOf(choice[0]);
        displayBoard(gameState);
    }

    public static void displayBoard(String[][] gameState) {
        System.out.print(ROW_SEPARATOR);
        System.out.print("\n");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(COLUMN_SEPARATOR);
            for (int j = 0; j < BOARD_SIZE; j++) {

                if (gameState[i][j].equals(EMPTY_CELL_SYMBOL)) {
                    System.out.print(RED + " " + gameState[i][j] + " ");
                } else {
                    System.out.print(" " + gameState[i][j] + " ");
                }

                if (j % BOX_SIZE == 2) {
                    System.out.print(COLUMN_SEPARATOR);
                }

            }
            if (i % BOX_SIZE == 2) {
                System.out.print(ROW_SEPARATOR);
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {
        initializeGame();
        int[] choice;

        displayBoard(sudokuBoard);
        do {
            choice = getPlayerMove(sudokuBoard);
            applyMove(sudokuBoard, choice);
        } while (!isGameComplete());

        terminateGame();
    }
}
