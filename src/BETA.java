import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class BETA {

    private static final Scanner consoleInput = new Scanner(System.in);
    private static final String ASSET_PATH = "SUDOKU_GAME_JAVA/ASSETS/MAPS/";
    private static final String[] LOADING_MESSAGES = {
            "Initializing game engine...",
            "Loading assets...",
            "Generating terrain...",
            "Spawning NPCs...",
            "Setting up environment...",
            "Almost there..."
    };
    private static final String GREETING_MESSAGE = "Welcome to the Game!";
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
            System.out.println("""
                    What difficulty would you like to play?\s
                    1: BEGINNER
                    2: INTERMEDIATE
                    3: DOCTOR
                    4: PRODIGY""");
            String difficulty = consoleInput.next();

            switch (difficulty) {
                case "1": { return "EASY"; }
                case "2": { return "MEDIUM"; }
                case "3": { return "HARD"; }
                case "4": { return "EXTREME"; }
                default: { System.out.println("Invalid input. Please select a valid difficulty level."); }
            }
        }
    }

    public static void loadMaps(String difficulty, int numberOfLevels) {
        for (int i = 1; i <= numberOfLevels; i++) {
            System.out.println(STR."Level \{i}");
            renderMap(STR."\{ASSET_PATH}\{difficulty}/\{i}B");
        }
    }

    public static void renderMap(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println(STR."Error reading file: \{e.getMessage()}");
        }
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

    public static Map getMap(String difficulty) {
        System.out.println("What level would you like to play?");
        int levelChoice;
        int numberOfLevels = getNumberOfFilesInFolder(difficulty) / 2;
        while (true) {
            loadMaps(difficulty, numberOfLevels);
            System.out.print("Please enter the number of the map you would like: ");
            levelChoice = consoleInput.nextInt();
            if (levelChoice <= numberOfLevels) {
                return new Map(STR."\{ASSET_PATH}\{difficulty}/\{levelChoice}");
            }
        }
    }

    public static Map showMenu() {
        String difficulty = getDifficulty();
        return getMap(difficulty);
    }

    public static Map startUp() {
        showLoadingScreen();
        return showMenu();
    }

    public static Map simulate(Map gameState) {

    }

    public static boolean isEndGameState(Map gameState) {

    }

    public static void shutDown() {
        // Shutdown logic
        System.out.println("Shutting down...");
    }

    public static void main (String [] args) {
        Map gameState = startUp();

        do {
            gameState = simulate(gameState);
        } while (!isEndGameState(gameState));

        shutDown();
    }
}

