# JUDOKO - Java Sudoku Game

![Game Preview](https://via.placeholder.com/800x400?text=JUDOKO+Game+Preview)

## Project Overview

JUDOKO is a feature-rich console-based Sudoku game implemented in Java. This application provides players with an interactive Sudoku experience complete with multiple difficulty levels, colorful console interface, and a robust game engine that enforces Sudoku rules.

## Features

- **Multiple Difficulty Levels**: Choose between Beginner, Intermediate, Professor, and Prodigy difficulty levels
- **Map Selection**: Multiple puzzles available for each difficulty level
- **Interactive Console Interface**: Clean and readable board display with grid separators
- **Game Validation**: Real-time validation of moves to ensure they follow Sudoku rules
- **Game State Tracking**: Automatic tracking of game completion
- **Color Customization**: Terminal color support for enhanced visual experience
- **Object-Oriented Design**: Modular, extensible architecture using interfaces and well-defined classes

## Project Structure

The project follows solid object-oriented design principles with a focus on maintainability and extensibility:

### Core Classes

- **`SudokuGame`**: Main game controller implementing the `GameInitializer` interface
- **`SudokuBoard`**: Represents the Sudoku board with display and validation functionality
- **`PlayerInput`**: Handles user input validation and processing
- **`FileManager`**: Manages loading game maps from files

### Interfaces

- **`GameInitializer`**: Defines the contract for game initialization
- **`BoardDisplayable`**: Defines the contract for displaying game boards
- **`MoveValidator`**: Defines the contract for validating player moves

### Utility Classes

- **`Customizer`**: Provides console color customization functions
- **`Main`**: Entry point for the application

## Technical Implementation

### Board Representation

The Sudoku board is represented as a 2D array of strings, with advanced tracking of rows, columns, and 3x3 boxes:

```java
private String[][] board;
private String[][] rows = new String[BOARD_SIZE][BOARD_SIZE];
private String[][] columns = new String[BOARD_SIZE][BOARD_SIZE];
private String[][] boxes = new String[BOARD_SIZE][BOARD_SIZE];
```

This organization allows for efficient validation of Sudoku rules during gameplay.

### Move Validation

The game implements thorough move validation to ensure all Sudoku rules are followed:

```java
public boolean isValidMove(int choice, int row, int col, String[][] gameState) {
    updateSubspaces();

    String target = String.valueOf(choice);

    return !((rows[row][col].equals(target))
            || (columns[row][col].equals(target))
            || (boxes[row / BOX_SIZE * BOX_SIZE + col / BOX_SIZE][(row % BOX_SIZE) * BOX_SIZE + col % BOX_SIZE].equals(target)));
}
```

This ensures that:
- No duplicate numbers exist in any row
- No duplicate numbers exist in any column
- No duplicate numbers exist in any 3x3 box

### Game Completion Detection

The game continuously checks for completion by verifying that all rows, columns, and boxes are properly filled:

```java
public boolean isGameComplete() {
    for (int i = 0; i < BOARD_SIZE; i++) {
        if (!(isSubsetComplete(rows[i]) && isSubsetComplete(columns[i]) && isSubsetComplete(boxes[i]))) {
            return false;
        }
    }
    return true;
}
```

### File System Integration

The game loads puzzles from external files organized by difficulty:

```
ASSETS/
└── MAPS/
    ├── EASY/
    │   ├── 1B.txt
    │   ├── 1S.txt
    │   └── ...
    ├── MEDIUM/
    │   ├── 1B.txt
    │   ├── 1S.txt
    │   └── ...
    └── ...
```

## Game Flow

1. **Loading**: The game displays a loading sequence with timed messages
2. **Difficulty Selection**: Player selects a difficulty level
3. **Map Selection**: Player chooses a specific puzzle
4. **Gameplay**: Player makes moves by entering:
   - The value to place (1-9)
   - The row position (0-8)
   - The column position (0-8)
5. **Validation**: The game validates each move against Sudoku rules
6. **Completion**: The game continues until the board is solved correctly

## Console Interface

The game presents a clean, formatted console interface with:

- Clear board boundaries and separators
- Distinct marking of 3x3 grid blocks
- Visual separation between different UI elements
- Colored text for better readability (where terminal supports ANSI colors)

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Command-line terminal with ANSI color support (for color features)

### Running the Game

1. Compile all Java files:
   ```
   javac *.java
   ```

2. Run the game:
   ```
   java Main
   ```

3. Follow the on-screen instructions to select difficulty and play the game

## Project Architecture

### Interface-Based Design

The project uses interfaces to establish clear contracts:

- `GameInitializer`: Defines game startup behavior
- `BoardDisplayable`: Defines board rendering behavior
- `MoveValidator`: Defines move validation logic

This approach allows for potential extension and alternative implementations.

### Data Management

The game uses a sophisticated approach for tracking board state:

- Board data stored in a 2D array
- Parallel data structures maintained for rows, columns, and boxes
- Real-time synchronization between these structures

### Input Handling

The `PlayerInput` class provides robust input handling with:

- Input validation
- Re-prompting for invalid inputs
- Conversion between user-friendly and internal coordinates

## Future Enhancements

- Graphical user interface (GUI) implementation
- Game timer and scoring system
- Puzzle generation capabilities
- Save/load game functionality
- Hint system for difficulty assistance
- Undo/redo functionality

## Acknowledgments

- This project was created as a demonstration of Java programming principles
- Inspired by the classic Sudoku puzzle game
- Implements standard Sudoku rules and gameplay mechanics