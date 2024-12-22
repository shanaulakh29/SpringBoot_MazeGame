package org.example.DTO;

import org.example.Modal.GameController;

/**
 * ApiGameDTO is a data transfer object class. It contains fields that are initialized from the modal data and
 * send to the client when a request is made.
 */
public class ApiGameDTO {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;
    public boolean showAllGameComponents = false;

    private GameController gameController;

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public static ApiGameDTO createGame(int gameNumber, int numCheeseFound, int numCheeseGoal) {
        ApiGameDTO gameDTO = new ApiGameDTO();
        gameDTO.gameNumber = gameNumber;
        gameDTO.isGameWon = false;
        gameDTO.isGameLost = false;
        gameDTO.numCheeseFound = numCheeseFound;
        gameDTO.numCheeseGoal = numCheeseGoal;
        return gameDTO;
    }
}
