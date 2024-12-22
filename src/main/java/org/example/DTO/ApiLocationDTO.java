package org.example.DTO;

import org.example.Modal.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * ApiLcationDTO is a data transfer object class. It gets row and column value of cells from the modal data and
 * after initialization is send to the client when a request is made.
 * As modal is dealing in terms of row and column, so here x is given the value of column nd y is given the value of
 * row
 */
public class ApiLocationDTO {
    public int x;
    public int y;

    private ApiLocationDTO(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static ApiLocationDTO getMouseOrCheeseLocation(int x, int y) {
        return new ApiLocationDTO(x, y);
    }

    public static List<ApiLocationDTO> getCatsLocation(List<Cell> catsLocation) {
        List<ApiLocationDTO> catsLocations = new ArrayList<>();
        for (Cell cell : catsLocation) {
            catsLocations.add(new ApiLocationDTO(cell.getColumn(), cell.getRow()));
        }
        return catsLocations;

    }

}

