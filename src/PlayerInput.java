import java.util.Scanner;

public class PlayerInput {
    private static final Scanner consoleInput = new Scanner(System.in);

    public String promptForDifficulty() {
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

    public int[] getPlayerMove(String[][] gameState) {
        int[] playerMove = new int[3];
        SudokuBoard sudokuBoard = new SudokuBoard(gameState);
        do {
            System.out.print("Enter the number you wish to place (1-9): ");
            playerMove[0] = consoleInput.nextInt();

            System.out.print("Enter the row number (0-8) where you want to place the number: ");
            playerMove[1] = consoleInput.nextInt();

            System.out.print("Enter the column number (0-8) where you want to place the number: ");
            playerMove[2] = consoleInput.nextInt();

        } while (!sudokuBoard.isValidMove(playerMove[0], playerMove[1], playerMove[2], gameState));

        return playerMove;
    }
}
