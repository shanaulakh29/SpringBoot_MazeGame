package org.example.Modal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The GameController class manages the entire game logic by utilizing the Mouse, Cat, and MazeGenerator classes.
 * It sets the starting positions of the mouse, cats, and cheese, and manages their movement within the maze.
 * GameController ensures valid moves for both the mouse and cats by updating their positions and
 * resetting the previously occupied cells to their original state.
 * Additionally, it checks for interactions between the mouse, cats and cheese,
 * tracks the total cheese collected, and determines win or lose conditions based on the game's progress.
 * The class increments the totalCheeseCollected when the mouse eats cheese and includes functionality for cheat codes,
 * allowing players to win more easily.
 * It also tracks the mouse’s visited paths and neighboring cells during each move.
 */
public class GameController {
    private List<List<Integer>> alreadyVisitedPathsAndDisclosedNeighboursAndBoundaryWalls = new ArrayList<>();
    private int cheeseCollectionRequirementToWin = 5;
    private char[][] maze;
    private Mouse mouse;
    private Cat firstCat;
    private Cat secondCat;
    private Cat thirdCat;
    private Cheese cheese;
    private MazeGenerator mazeGenerator;

    public GameController() {
        mazeGenerator = new MazeGenerator(10, 15);
        maze = MazeGenerator.getMazeByReference();
        mouse = new Mouse('@', 1, 1);
        firstCat = new Cat('!', 1, MazeGenerator.totalColumns - 2);
        secondCat = new Cat('!', MazeGenerator.totalRows - 2, 1);
        thirdCat = new Cat('!', MazeGenerator.totalRows - 2,
                MazeGenerator.totalColumns - 2);
        cheese = new Cheese('$');
        buildMaze();
    }

    public boolean isWall(int row, int col) {
        return maze[row][col] == MazeGenerator.WALL;
    }


    public Cell getMouseLocation() {
        return new Cell(mouse.getMouseLocationRow(), mouse.getMouseLocationColumn());
    }

    public Cell getCheeseLocation() {
        return new Cell(cheese.getCheeseLocationRow(), cheese.getCheeseLocationColumn());
    }

    public List<Cell> getCatsLocation() {
        List<Cell> catsLocation = new ArrayList<>();
        catsLocation.add(new Cell(firstCat.getCatLocationRow(), firstCat.getCatLocationColumn()));
        catsLocation.add(new Cell(secondCat.getCatLocationRow(), secondCat.getCatLocationColumn()));
        catsLocation.add(new Cell(thirdCat.getCatLocationRow(), thirdCat.getCatLocationColumn()));
        return catsLocation;
    }


    public void setInitialPositionOfGameComponentsOnMaze() {
        maze[mouse.getMouseLocationRow()][mouse.getMouseLocationColumn()] = mouse.getMouse();
        maze[firstCat.getCatLocationRow()][firstCat.getCatLocationColumn()] = firstCat.getCat();
        maze[secondCat.getCatLocationRow()][secondCat.getCatLocationColumn()] = secondCat.getCat();
        maze[thirdCat.getCatLocationRow()][thirdCat.getCatLocationColumn()] = thirdCat.getCat();
        placeCheeseRandomly();
        maze[cheese.getCheeseLocationRow()][cheese.getCheeseLocationColumn()] = cheese.getCheese();
    }



    public boolean isMouseCellMatchesCatCell(Mouse mouse, Cat anyCat) {
        return anyCat.getCatLocationRow() == mouse.getMouseLocationRow() &&
                anyCat.getCatLocationColumn() == mouse.getMouseLocationColumn();
    }


    public void placeCheeseRandomly() {
        Cell randomlyChoosenCheeseCell = cheese.getRandomValidLocationForCheese(mouse);
        cheese.setCheeseLocation(randomlyChoosenCheeseCell.getRow(), randomlyChoosenCheeseCell.getColumn());
        maze[randomlyChoosenCheeseCell.getRow()][randomlyChoosenCheeseCell.getColumn()] = cheese.getCheese();
    }

    public void resetCellsOccupiedByCatsAndRedisplayCheeseIfCatStepOverCheese() {

        maze[firstCat.getCatLocationRow()][firstCat.getCatLocationColumn()] = MazeGenerator.PATH;
        maze[secondCat.getCatLocationRow()][secondCat.getCatLocationColumn()] = MazeGenerator.PATH;
        maze[thirdCat.getCatLocationRow()][thirdCat.getCatLocationColumn()] = MazeGenerator.PATH;
        maze[cheese.getCheeseLocationRow()][cheese.getCheeseLocationColumn()] = cheese.getCheese();
    }

    public void moveAllCatsRandomly() {
        resetCellsOccupiedByCatsAndRedisplayCheeseIfCatStepOverCheese();
        Cell firstCatNextMoveCell = firstCat.getCatNextMove();
        firstCat.setCatLocation(firstCatNextMoveCell.getRow(), firstCatNextMoveCell.getColumn());
        maze[firstCatNextMoveCell.getRow()][firstCatNextMoveCell.getColumn()] = firstCat.getCat();

        Cell secondCatNextMoveCell = secondCat.getCatNextMove();
        secondCat.setCatLocation(secondCatNextMoveCell.getRow(), secondCatNextMoveCell.getColumn());
        maze[secondCatNextMoveCell.getRow()][secondCatNextMoveCell.getColumn()] = secondCat.getCat();

        Cell thirdCatNextMoveCell = thirdCat.getCatNextMove();
        thirdCat.setCatLocation(thirdCatNextMoveCell.getRow(), thirdCatNextMoveCell.getColumn());
        maze[thirdCatNextMoveCell.getRow()][thirdCatNextMoveCell.getColumn()] = thirdCat.getCat();
    }


    public void resetCellsOccupiedByMouse() {
        int currentMouseIndexRow = mouse.getMouseLocationRow();
        int currentMouseIndexColumn = mouse.getMouseLocationColumn();
        maze[currentMouseIndexRow][currentMouseIndexColumn] = MazeGenerator.PATH;
    }

    public boolean checkValidIndex(int LocationIndexRow, int LocationIndexColumn) {
        if (LocationIndexRow <= 0 || LocationIndexRow >= MazeGenerator.totalRows - 1 || LocationIndexColumn <= 0 ||
                LocationIndexColumn == MazeGenerator.totalColumns - 1 ||
                maze[LocationIndexRow][LocationIndexColumn] == MazeGenerator.WALL) {
            return false;
        }
        return true;
    }

    public void moveMouseAsPerUserInput(int updatedMouseLocationRow, int updatedMouseLocationColumn) {
        resetCellsOccupiedByMouse();
        mouse.setMouseLocation(updatedMouseLocationRow, updatedMouseLocationColumn);
        maze[mouse.getMouseLocationRow()][mouse.getMouseLocationColumn()] = mouse.getMouse();
    }

    public Cell getNewLocationToMoveMouseUp() {
        if (checkValidIndex(mouse.getMouseLocationRow() - 1, mouse.getMouseLocationColumn())) {
            return new Cell(mouse.getMouseLocationRow() - 1, mouse.getMouseLocationColumn());
        }
        return null;
    }

    public Cell getNextLocationToMoveMouseDown() {
        if (checkValidIndex(mouse.getMouseLocationRow() + 1, mouse.getMouseLocationColumn())) {
            return new Cell(mouse.getMouseLocationRow() + 1, mouse.getMouseLocationColumn());
        }

        return null;
    }

    public Cell getNextLocationToMoveMouseRight() {
        if (checkValidIndex(mouse.getMouseLocationRow(), mouse.getMouseLocationColumn() + 1)) {
            return new Cell(mouse.getMouseLocationRow(), mouse.getMouseLocationColumn() + 1);
        }
        return null;
    }

    public Cell getNextLocationToMoveMouseLeft() {
        if (checkValidIndex(mouse.getMouseLocationRow(), mouse.getMouseLocationColumn() - 1)) {
            return new Cell(mouse.getMouseLocationRow(), mouse.getMouseLocationColumn() - 1);
        }
        return null;
    }

    public boolean checkIfMouseLocationMatchesCheeseLocation() {
        return ((mouse.getMouseLocationRow() == cheese.getCheeseLocationRow()) && (mouse.getMouseLocationColumn()
                == cheese.getCheeseLocationColumn()));
    }


    public boolean checkIfAnyCatLocationMatchesMouseLocation() {
        return isMouseCellMatchesCatCell(mouse, firstCat) || isMouseCellMatchesCatCell(mouse, secondCat) ||
                isMouseCellMatchesCatCell(mouse, thirdCat);
    }

    public int getCheeseCollectionRequirementToWin() {
        return cheeseCollectionRequirementToWin;
    }

    public void changeWinningRequirementToOneCheese() {
        cheeseCollectionRequirementToWin = 1;
    }

    public int getTotalCheeseCollected() {
        return cheese.getTotalCheeseCollected();
    }

    public void collectCheeseWhenCheeseLocationMatchesMouse() {
        cheese.addOneToTotalCheeseCollected();
    }

    public List<List<Integer>>
    includeAllBoundaryIndexesInList(int row, int column,
                                    List<List<Integer>> alreadyVisitedPathsAndDisclosedNeighboursAndBoundaryWalls) {
        if ((row == 0 && column < MazeGenerator.totalColumns) || (row < MazeGenerator.totalRows && column == 0) ||
                (row == MazeGenerator.totalRows - 1 && column < MazeGenerator.totalColumns) ||
                (column == MazeGenerator.totalColumns - 1 && row < MazeGenerator.totalRows)) {
            List<Integer> currentIndex = Arrays.asList(row, column);
            if (!alreadyVisitedPathsAndDisclosedNeighboursAndBoundaryWalls.contains(currentIndex)) {
                alreadyVisitedPathsAndDisclosedNeighboursAndBoundaryWalls.add(currentIndex);
            }

        }
        return alreadyVisitedPathsAndDisclosedNeighboursAndBoundaryWalls;
    }

    public List<List<Integer>> getListOfCurrentAndPreviousDisclosedNeighboursAndAllVisitedPathsAndAllBoundaries(){
        int[][] allEightNeighbours = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, -1}, {-1, 1}, {1, 1}};
        for (int[] neighbour : allEightNeighbours) {
            int mouseNeighbourRow = mouse.getMouseLocationRow() + neighbour[0];
            int mouseNeighbourColumn = mouse.getMouseLocationColumn() + neighbour[1];
            if ((mouseNeighbourRow > 0 && mouseNeighbourRow < MazeGenerator.totalRows - 1) &&
                    (mouseNeighbourColumn > 0 && mouseNeighbourColumn < MazeGenerator.totalColumns - 1)) {
                List<Integer> currentIndex = Arrays.asList(mouseNeighbourRow, mouseNeighbourColumn);
                if (!alreadyVisitedPathsAndDisclosedNeighboursAndBoundaryWalls.contains(currentIndex)) {
                    alreadyVisitedPathsAndDisclosedNeighboursAndBoundaryWalls.add(currentIndex);
                }
            }
        }
        //include Boundaries
        for (int row = 0; row < MazeGenerator.totalRows; row++) {
            for (int column = 0; column < MazeGenerator.totalColumns; column++) {
                alreadyVisitedPathsAndDisclosedNeighboursAndBoundaryWalls = includeAllBoundaryIndexesInList(row, column,
                        alreadyVisitedPathsAndDisclosedNeighboursAndBoundaryWalls);
            }
        }
        return alreadyVisitedPathsAndDisclosedNeighboursAndBoundaryWalls;
    }

    public void buildMaze() {
        mazeGenerator.generateMaze();
        setInitialPositionOfGameComponentsOnMaze();

    }

}
