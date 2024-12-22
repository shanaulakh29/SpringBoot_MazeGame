package org.example.DTO;

import org.example.Modal.GameController;
import org.example.Modal.MazeGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * ApiBoardDTO is a data transfer object class. It contains fields that are initialized from the modal data and
 * send to the client when a request is made.
 */
public class ApiBoardDTO {
    public int boardWidth;
    public int boardHeight;
    public ApiLocationDTO mouseLocation;
    public ApiLocationDTO cheeseLocation;
    public List<ApiLocationDTO> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;


    public static ApiBoardDTO createBoardWithAllItsComponents(int boardWidth, int boardHeight,
                                                              ApiLocationDTO mouseLocation,
                                                              ApiLocationDTO cheeseLocation,
                                                              List<ApiLocationDTO> catLocations,
                                                              GameController gameController,
                                                              ApiGameDTO game) {
        ApiBoardDTO board = new ApiBoardDTO();
        board.boardWidth = boardWidth;
        board.boardHeight = boardHeight;
        board.mouseLocation = mouseLocation;
        board.cheeseLocation = cheeseLocation;
        board.catLocations = catLocations;
        board.hasWalls = new boolean[boardWidth][boardHeight];
        board.isVisible = new boolean[boardWidth][boardHeight];

        for (int row = 0; row < boardWidth; row++) {
            for (int column = 0; column < boardHeight; column++) {
                board.hasWalls[row][column] = gameController.isWall(row, column);
                fillIsVisibleFieldWithTheCurrentModalState(game, row, column, board.isVisible, gameController);
            }
        }
        return board;
    }

    private static void fillIsVisibleFieldWithTheCurrentModalState(ApiGameDTO game, int row, int column,
                                                                   boolean[][] isVisible,
                                                                   GameController gameController) {
        if (game.showAllGameComponents) {
            isVisible[row][column] = true;
        } else {
            List<List<Integer>> CurrentAndPreviousDisclosedNeighboursAndAllVisitedPathsAndAllBoundaries =
                    gameController
                            .getListOfCurrentAndPreviousDisclosedNeighboursAndAllVisitedPathsAndAllBoundaries();
            List<Integer> currentIndex = Arrays.asList(row, column);
            isVisible[row][column] =
                    CurrentAndPreviousDisclosedNeighboursAndAllVisitedPathsAndAllBoundaries
                            .contains(currentIndex);
        }
    }

}
