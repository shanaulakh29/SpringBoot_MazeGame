package org.example.Modal;

/**
 * Mouse class manages the location of mouse and provides methods to access or set the mouse location.
 */
public class Mouse {
    private char mouse;
    private int mouseLocationRow;
    private int mouseLocationColumn;

    public Mouse(char mouse, int mouseLocationRow, int mouseLocationColumn) {
        this.mouse = mouse;
        this.mouseLocationRow = mouseLocationRow;
        this.mouseLocationColumn = mouseLocationColumn;
    }

    public char getMouse() {
        return mouse;
    }

    public int getMouseLocationColumn() {
        return mouseLocationColumn;
    }

    public int getMouseLocationRow() {
        return mouseLocationRow;
    }

    public void setMouseLocation(int mouseLocationRow, int mouseLocationColumn) {

        this.mouseLocationRow = mouseLocationRow;
        this.mouseLocationColumn = mouseLocationColumn;

    }
}
