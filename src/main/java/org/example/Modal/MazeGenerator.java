package org.example.Modal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;

/**
 * MazeGenerator class is responsible for building the maze. This class uses the Depth first Search to build the maze.
 * In order to make the gameplay fun, this class adds cycles in the maze so that there can be multiple paths to
 * reach source from any destination. generateMaze() can be used to build a game.
 */
public class MazeGenerator {
    private static char[][] maze;
    private final int NUMBER_OF_RANDOMLY_SELECTED_WALLS_REMOVED = 100;
    private final int[][] DIRECTIONS = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}};
    public static final char WALL = '#';
    public static final char PATH = '.';
    public static int totalRows;
    public static int totalColumns;

    public MazeGenerator(int totalRows, int totalColumns) {
        this.maze = new char[totalRows][totalColumns];
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
    }

    public static char[][] getMazeByReference() {
        return maze;
    }

    private void fillMazeWithWalls() {

        IntStream.range(0, totalRows)
                .forEach(row -> IntStream.range(0, totalColumns)
                        .forEach(column -> maze[row][column] = WALL));

    }

    private boolean isValidCell(int row, int column) {
        return (row > 0 && row < totalRows - 1) && (column > 0 && column < totalColumns - 1);
    }

    private void performDepthFirstSearchOnMaze() {
        Stack<Cell> stack = new Stack<>();
        Cell startCell = new Cell(1, 1);
        maze[startCell.getRow()][startCell.getColumn()] = PATH;
        stack.push(startCell);
        while (!stack.isEmpty()) {
            Cell currentCell = stack.peek();
            List<Cell> neighbours = new ArrayList<>();
            for (int[] direction : DIRECTIONS) {
                int newRow = currentCell.getRow() + direction[0];
                int newColumn = currentCell.getColumn() + direction[1];
                if (isValidCell(newRow, newColumn) && maze[newRow][newColumn] == WALL) {
                    Cell neighbourCell = new Cell(newRow, newColumn);
                    neighbours.add(neighbourCell);
                }
            }
            if (!neighbours.isEmpty()) {
                Collections.shuffle(neighbours);

                Cell anyNeighbourCell = neighbours.get(0);

                stack.push(anyNeighbourCell);

                maze[anyNeighbourCell.getRow()][anyNeighbourCell.getColumn()] = PATH;
                removeWallBetweenCells(currentCell, anyNeighbourCell);
            } else {
                stack.pop();
            }
        }
    }

    private void removeWallBetweenCells(Cell currentCell, Cell neighbourCell) {
        int row = (currentCell.getRow() + neighbourCell.getRow()) / 2;
        int column = (currentCell.getColumn() + neighbourCell.getColumn()) / 2;
        maze[row][column] = PATH;

    }

    private void removeRandomWallsToAddManyPaths() {
        for (int i = 0; i < NUMBER_OF_RANDOMLY_SELECTED_WALLS_REMOVED; i++) {
            Cell randomCell = getRandomCellToMakePath();
            while (true) {
                int row = randomCell.getRow();
                int column = randomCell.getColumn();
                if (maze[row][column] == PATH) {
                    randomCell = getRandomCellToMakePath();
                } else {
                    break;
                }
            }
            removeWallIfConstraintsMet(randomCell);
        }
    }

    private Cell getRandomCellToMakePath() {
        int randomRow = ((int) (Math.random() * (totalRows - 2)) + 1);
        int randomColumn = ((int) (Math.random() * (totalColumns - 2)) + 1);
        return new Cell(randomRow, randomColumn);
    }

    private void removeWallIfConstraintsMet(Cell cell) {
        int totalUndiscoveredPathsAroundSpecificCell = 0;
        if (maze[cell.getRow() - 1][cell.getColumn()] == PATH) {
            totalUndiscoveredPathsAroundSpecificCell++;
        }
        if (maze[cell.getRow() + 1][cell.getColumn()] == PATH) {
            totalUndiscoveredPathsAroundSpecificCell++;
        }
        if (maze[cell.getRow()][cell.getColumn() - 1] == PATH) {
            totalUndiscoveredPathsAroundSpecificCell++;
        }
        if (maze[cell.getRow()][cell.getColumn() + 1] == PATH) {
            totalUndiscoveredPathsAroundSpecificCell++;
        }

        if (totalUndiscoveredPathsAroundSpecificCell <= 1) {
            maze[cell.getRow()][cell.getColumn()] = PATH;
        }
    }

    public void generateMaze() {
        fillMazeWithWalls();
        performDepthFirstSearchOnMaze();
        removeRandomWallsToAddManyPaths();
    }
}
