package org.example.Modal;

/**
 * Cat class manages the location of cat and provides methods to get or set the cat location in every move.
 * Cat class is responsible for moving the cat randomly by checking the location validity.
 */
public class Cat {
    private char cat;
    private int catLocationRow;
    private int catLocationColumn;

    public Cat(char cat, int catLocationRow, int catLocationColumn) {
        this.cat = cat;
        this.catLocationRow = catLocationRow;
        this.catLocationColumn = catLocationColumn;
    }

    public char getCat() {
        return cat;
    }

    public int getCatLocationRow() {
        return catLocationRow;
    }

    public int getCatLocationColumn() {
        return catLocationColumn;
    }

    private boolean isValidIndex(int catLocationRow, int catLocationColumn) {
        char[][] maze = MazeGenerator.getMazeByReference();
        if (catLocationRow <= 0 || catLocationRow >= MazeGenerator.totalRows - 1 || catLocationColumn <= 0 ||
                catLocationColumn >= MazeGenerator.totalColumns - 1 || maze[catLocationRow][catLocationColumn]
                == MazeGenerator.WALL) {
            return false;
        }
        return true;
    }

    public void setCatLocation(int catLocationRow, int catLocationColumn) {
        this.catLocationRow = catLocationRow;
        this.catLocationColumn = catLocationColumn;
    }

    public Cell getCatNextMove() {
        int[][] directionsArray = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int randomValueToSelectIndex = (int) (Math.random() * directionsArray.length);
        int[] anyNeighbour = directionsArray[randomValueToSelectIndex];
        int updatedRowIndex = getCatLocationRow() + anyNeighbour[0];
        int updatedColumnIndex = getCatLocationColumn() + anyNeighbour[1];
        while (true) {
            if (!isValidIndex(updatedRowIndex, updatedColumnIndex)) {
                randomValueToSelectIndex = (int) (Math.random() * directionsArray.length);
                anyNeighbour = directionsArray[randomValueToSelectIndex];
                updatedRowIndex = getCatLocationRow() + anyNeighbour[0];
                updatedColumnIndex = getCatLocationColumn() + anyNeighbour[1];
            } else {
                break;
            }
        }
        return new Cell(updatedRowIndex, updatedColumnIndex);
    }
}
