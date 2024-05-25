import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SudokuGame implements GameInitializer {
    private static final String WELCOME_MESSAGE = "Welcome to JUDOKO - JAVA SUDOKO!";
    private static final int LOADING_DELAY = 2;
    private static final String[] LOADING_STEPS = {
            "Starting the game engine...",
            "Loading the logic engine...",
            "Fetching game assets...",
            "Creating game maps...",
            "Preparing console interface...",
            "Configuring virtual environment...",
            "Loading decision-making model..."
    };

    private SudokuBoard sudokuBoard;
    private FileManager fileManager;
    private PlayerInput playerInput;

    public SudokuGame() {
        this.fileManager = new FileManager();
        this.playerInput = new PlayerInput();
    }

    public void initializeGame() {
        displayLoadingScreen();
        String difficulty = playerInput.promptForDifficulty();
        String[][] board = fileManager.loadMap(difficulty);
        this.sudokuBoard = new SudokuBoard(board);

        int[] choice;
        sudokuBoard.displayBoard(board);
        do {
            choice = playerInput.getPlayerMove(board);
            sudokuBoard.applyMove(choice);
        } while (!sudokuBoard.isGameComplete());

        terminateGame();
    }

    private void displayLoadingScreen() {
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

    private void terminateGame() {
        System.out.println("Thank you for playing! Shutting down the game...");
    }
}
