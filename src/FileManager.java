import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class FileManager {
    private static final String MAPS_DIRECTORY = "C:/Users/ashiq/OneDrive/Desktop/SUDOKO/SUDOKO_GAME_JAVA/ASSETS/MAPS/";

    public String[][] loadMap(String difficulty) {
        System.out.println("Select the level you would like to play:");
        int levelChoice;
        Scanner consoleInput = new Scanner(System.in);

        while (true) {
            showAvailableMaps(difficulty);
            System.out.print("Enter the number corresponding to your chosen map: ");
            levelChoice = consoleInput.nextInt();

            int numberOfLevels = countFilesInDirectory(difficulty) / 2;

            if (levelChoice <= numberOfLevels && levelChoice >= 0) {
                return loadMapFromFile(MAPS_DIRECTORY + difficulty + "/" + levelChoice + "B.txt");
            }
        }
    }

    private String[][] loadMapFromFile(String mapFileLocation) {
        String[][] extractedMap = new String[SudokuBoard.BOARD_SIZE][SudokuBoard.BOARD_SIZE];

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

    private void showAvailableMaps(String difficulty) {
        int numberOfLevels = countFilesInDirectory(difficulty) / 2;
        for (int i = 1; i <= numberOfLevels; i++) {
            System.out.print("\n\t\t\t\tMap " + i);
            new SudokuBoard(loadMapFromFile(MAPS_DIRECTORY + difficulty + "/" + i + "B.txt")).displayBoard(loadMapFromFile(MAPS_DIRECTORY + difficulty + "/" + i + "B.txt"));
        }
        System.out.println();
    }

    private int countFilesInDirectory(String folderName) {
        File folder = new File(MAPS_DIRECTORY + folderName);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                return files.length;
            }
        }
        return 0;
    }
}
