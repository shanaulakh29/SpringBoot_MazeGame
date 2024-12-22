package org.example.Modal;

/**
 * Cell class stores the location in the form of row and column and provide methods to get row and column.
 * Cell class doesnot have the setters for row and column as they are not needed.
 */
public class Cell {
    private int row;
    private int column;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
