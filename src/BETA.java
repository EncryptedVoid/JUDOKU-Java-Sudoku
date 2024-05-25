import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class BETA extends Customizer {

    // The size of the Sudoku board (9x9)
    public static final int BOARD_SIZE = 9;
    // The size of each smaller 3x3 box within the Sudoku board
    public static final int BOX_SIZE = 3;

    // String constants for visual separation in the board display
    public static final String COLUMN_SEPARATOR = " | ";
    public static final String ROW_SEPARATOR = "\n < {" + "-".repeat(BOARD_SIZE + BOX_SIZE - 1) + "} > ";
    public static final String EMPTY_CELL_SYMBOL = " # ";

    // The main Sudoku board represented as a 2D array of strings
    private static String[][] sudokuBoard;
    // Buffer for storing rows while updating the board
    private static final String[] rowBuffer = new String[BOARD_SIZE];
    // Separate arrays to keep track of rows, columns, and boxes
    private static final String[][] rows = new String[BOARD_SIZE][BOARD_SIZE];
    private static final String[][] columns = new String[BOARD_SIZE][BOARD_SIZE];
    private static final String[][] boxes = new String[BOARD_SIZE][BOARD_SIZE];

    // Scanner object for reading user input from the console
    private static final Scanner consoleInput = new Scanner(System.in);
    // Directory path where the Sudoku map files are stored
    private static final String MAPS_DIRECTORY = "C:/Users/ashiq/OneDrive/Desktop/SUDOKO/SUDOKO_GAME_JAVA/ASSETS/MAPS/";
    // Loading messages to be displayed during the game's startup process
    private static final String[] LOADING_STEPS = {
            "Starting the game engine...",
            "Loading the logic engine...",
            "Fetching game assets...",
            "Creating game maps...",
            "Preparing console interface...",
            "Configuring virtual environment...",
            "Loading decision-making model..."
    };

    // Array to store player's move (number, row, column)
    private static final int[] playerMove = new int[3];

    // Welcome message displayed to the player
    private static final String WELCOME_MESSAGE = "Welcome to JUDOKO - JAVA SUDOKO!";
    // Delay time (in seconds) for each loading step
    private static final int LOADING_DELAY = 2;

    /**
     * Displays the loading screen with messages and delays.
     */
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

    /**
     * Prompts the user to select a difficulty level.
     *
     * @return the selected difficulty level as a string.
     */
    public static String promptForDifficulty() {
        while (true) {
            System.out.print("Select your desired difficulty level:\n1: Beginner\n2: Intermediate\n3: Professor\n4: Prodigy\n--> ");
            String difficulty = consoleInput.next();

            // Return the corresponding difficulty string based on user input
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

    /**
     * Displays the main menu and handles difficulty selection.
     */
    public static void displayMenu() {
        String difficulty = promptForDifficulty();
        promptForMapSelection(difficulty);
    }

    /**
     * Initializes the game by displaying the menu.
     */
    public static void initializeGame() {
        // Uncomment the line below to enable the loading screen
        // displayLoadingScreen();
        displayMenu();
    }

    /**
     * Terminates the game and displays a shutdown message.
     */
    public static void terminateGame() {
        System.out.println("Thank you for playing! Shutting down the game...");
    }

    /**
     * Loads a Sudoku map from a specified file location.
     *
     * @param mapFileLocation the file location of the Sudoku map.
     * @return a 2D array representing the loaded Sudoku map.
     */
    public static String[][] loadMapFromFile(String mapFileLocation) {
        String[][] extractedMap = new String[BOARD_SIZE][BOARD_SIZE];

        try (BufferedReader br = new BufferedReader(new FileReader(mapFileLocation))) {
            String line;
            int i = 0;
            // Read each line of the file and split it into an array of strings
            while ((line = br.readLine()) != null) {
                extractedMap[i] = line.split(" ");
                i++;
            }
        } catch (IOException e) {
            System.err.println("Unable to read the map file: " + e.getMessage());
        }

        return extractedMap;
    }

    /**
     * Prompts the user to select a map based on the chosen difficulty.
     *
     * @param difficulty the selected difficulty level.
     */
    public static void promptForMapSelection(String difficulty) {
        System.out.println("Select the level you would like to play:");
        int levelChoice;

        while (true) {
            showAvailableMaps(difficulty);
            System.out.print("Enter the number corresponding to your chosen map: ");
            levelChoice = consoleInput.nextInt();

            int numberOfLevels = countFilesInDirectory(difficulty) / 2;

            // Load the selected map if the level choice is valid
            if (levelChoice <= numberOfLevels && levelChoice >= 0) {
                sudokuBoard = loadMapFromFile(MAPS_DIRECTORY + difficulty + "/" + levelChoice + "B.txt");
                return;
            }
        }
    }

    /**
     * Displays the available maps for the selected difficulty.
     *
     * @param difficulty the selected difficulty level.
     */
    public static void showAvailableMaps(String difficulty) {
        int numberOfLevels = countFilesInDirectory(difficulty) / 2;
        for (int i = 1; i <= numberOfLevels; i++) {
            System.out.print("\n\t\t\t\tMap " + i);
            displayBoard(loadMapFromFile(MAPS_DIRECTORY + difficulty + "/" + i + "B.txt"));
        }
        System.out.println();
    }

    /**
     * Counts the number of files in a specified directory.
     *
     * @param folderName the name of the directory.
     * @return the number of files in the directory.
     */
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

    /**
     * Gets a valid move from the player.
     *
     * @param gameState the current state of the game.
     * @return an array containing the player's move (number, row, column).
     */
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

    /**
     * Updates the rows, columns, and boxes arrays based on the current game state.
     *
     * @param gameState the current state of the game.
     */
    public static void updateSubspaces(String[][] gameState) {
        // Update rows array
        for (int rowIndex = 0; rowIndex < BOARD_SIZE; rowIndex++) {
            System.arraycopy(gameState[rowIndex], 0, rows[rowIndex], 0, BOARD_SIZE);
        }

        // Update columns array
        for (int colIndex = 0; colIndex < BOARD_SIZE; colIndex++) {
            for (int rowIndex = 0; rowIndex < BOARD_SIZE; rowIndex++) {
                columns[colIndex][rowIndex] = gameState[rowIndex][colIndex];
            }
        }

        // Update boxes array
        for (int boxStart = 0; boxStart < BOX_SIZE; boxStart++) {
            for (int rowIndex = boxStart * BOX_SIZE; rowIndex < (boxStart + 1) * BOX_SIZE; rowIndex++) {
                for (int colIndex = boxStart * BOX_SIZE; colIndex < (boxStart + 1) * BOX_SIZE; colIndex++) {
                    boxes[boxStart * BOX_SIZE + (rowIndex % BOX_SIZE)][(colIndex % BOX_SIZE)] = gameState[rowIndex][colIndex];
                }
            }
        }
    }

    /**
     * Checks if a subset contains any empty cells.
     *
     * @param subset the subset to check.
     * @return true if the subset contains no empty cells, false otherwise.
     */
    public static boolean containsEmptyCells(String[] subset) {
        for (String s : subset) {
            if (Objects.equals(s, EMPTY_CELL_SYMBOL)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a subset contains any duplicate values.
     *
     * @param subset the subset to check.
     * @return true if the subset contains duplicates, false otherwise.
     */
    public static boolean containsDuplicates(String[] subset) {
        HashSet<String> seen = new HashSet<>();
        for (String s : subset) {
            if (!seen.add(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a subset is complete (no empty cells and no duplicates).
     *
     * @param subset the subset to check.
     * @return true if the subset is complete, false otherwise.
     */
    public static boolean isSubsetComplete(String[] subset) {
        return !containsEmptyCells(subset) && !containsDuplicates(subset);
    }

    /**
     * Checks if the game is complete (all rows, columns, and boxes are complete).
     *
     * @return true if the game is complete, false otherwise.
     */
    public static boolean isGameComplete() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!(isSubsetComplete(rows[i]) && isSubsetComplete(columns[i]) && isSubsetComplete(boxes[i]))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates if a move is valid (no duplicates in row, column, or box).
     *
     * @param choice the number to be placed.
     * @param row the row index where the number will be placed.
     * @param col the column index where the number will be placed.
     * @param gameState the current state of the game.
     * @return true if the move is valid, false otherwise.
     */
    public static boolean isValidMove(int choice, int row, int col, String[][] gameState) {
        updateSubspaces(gameState);

        String target = String.valueOf(choice);

        return !((rows[row][col].equals(target))
                || (columns[row][col].equals(target))
                || (boxes[row / BOX_SIZE * BOX_SIZE + col / BOX_SIZE][(row % BOX_SIZE) * BOX_SIZE + col % BOX_SIZE].equals(target)));
    }

    /**
     * Applies the player's move to the game state.
     *
     * @param gameState the current state of the game.
     * @param choice an array containing the player's move (number, row, column).
     */
    public static void applyMove(String[][] gameState, int[] choice) {
        gameState[choice[1]][choice[2]] = String.valueOf(choice[0]);
        displayBoard(gameState);
    }

    /**
     * Displays the current state of the Sudoku board.
     *
     * @param gameState the current state of the game.
     */
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

    /**
     * Main method to start the game and handle the game loop.
     *
     * @param args command line arguments.
     */
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
