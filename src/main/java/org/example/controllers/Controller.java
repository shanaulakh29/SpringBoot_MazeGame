package org.example.controllers;

import org.example.DTO.ApiBoardDTO;
import org.example.DTO.ApiGameDTO;
import org.example.DTO.ApiLocationDTO;
import org.example.Modal.Cat;
import org.example.Modal.Cell;
import org.example.Modal.GameController;
import org.example.Modal.MazeGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class is responsible for listening to requests made by browser to various endpoints. This class
 * uses the model data and convert it into data transfer objects to send back to the client.
 */

@RestController
@RequestMapping("/api")
public class Controller {

    List<ApiGameDTO> games = new ArrayList<>();

    @GetMapping("/about")
    public String getHelloMessage() {
        return "Gurshan Singh Aulakh";
    }

    @GetMapping("/games")
    public List<ApiGameDTO> getGames() {
        return games;

    }

    //I got idea from chatgpt to use ResponsEntity as to have a complete control on what i can
// send as a response to the browser. Also, Prof did mentioned this technique in his lecture.
    @PostMapping("/games")
    public ResponseEntity<ApiGameDTO> createNewGame() {
        int gameId = games.size();
        GameController gameController = new GameController();
        ApiGameDTO newGame = ApiGameDTO.createGame(gameId, gameController.getTotalCheeseCollected(),
                gameController.getCheeseCollectionRequirementToWin());
        newGame.setGameController(gameController);
        games.add(newGame);

        return new ResponseEntity<>(newGame, HttpStatus.CREATED);
    }

    private void checkIfMouseLocationMatchesCheeseLocation(ApiGameDTO game, GameController gameController) {
        if (!game.isGameLost) {
            if (gameController.checkIfMouseLocationMatchesCheeseLocation()) {
                gameController.collectCheeseWhenCheeseLocationMatchesMouse();
                game.numCheeseFound++;
                gameController.placeCheeseRandomly();
            }
            if (game.numCheeseFound == game.numCheeseGoal) {
                game.isGameWon = true;
            }
        }

    }

    @GetMapping("/games/{ID}")
    public ResponseEntity<ApiGameDTO> getGameById(@PathVariable Long ID) {
        for (ApiGameDTO game : games) {
            if (game.gameNumber == ID) {
                GameController gameController = game.getGameController();
                if (gameController.checkIfAnyCatLocationMatchesMouseLocation()) {
                    game.isGameLost = true;
                }
                checkIfMouseLocationMatchesCheeseLocation(game, gameController);

                return new ResponseEntity<>(game, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<HttpStatus> doTaskBasedOnCheatCodeValue(ApiGameDTO game, String cheatCode) {
        switch (cheatCode) {
            case "1_CHEESE" -> {
                GameController gameController = game.getGameController();
                gameController.changeWinningRequirementToOneCheese();
                game.numCheeseGoal = 1;
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
            case "SHOW_ALL" -> {
                game.showAllGameComponents = true;
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PostMapping("/games/{ID}/cheatstate")
    public ResponseEntity<HttpStatus> cheatState(@PathVariable Long ID,
                                                 @RequestBody String cheatCode) {
        for (ApiGameDTO game : games) {
            if (game.gameNumber == ID) {
                return doTaskBasedOnCheatCodeValue(game, cheatCode);

            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/games/{ID}/board")
    public ResponseEntity<ApiBoardDTO> getBoard(@PathVariable Long ID) {

        for (ApiGameDTO game : games) {
            if (game.gameNumber == ID) {
                GameController gameController = game.getGameController();
                Cell mouseLocationCell = gameController.getMouseLocation();
                Cell cheeseLocationCell = gameController.getCheeseLocation();
                List<Cell> catsLocationCells = gameController.getCatsLocation();
                //My modal is dealing with row and column. but as APILocationDTO is dealing with x and y, i have
                // to give column as x and row as y to APILocationDTO x and y fields.
                ApiLocationDTO mouseLocation =
                        ApiLocationDTO.getMouseOrCheeseLocation(mouseLocationCell.getColumn(),
                                mouseLocationCell.getRow());
                ApiLocationDTO cheeseLocation =
                        ApiLocationDTO.getMouseOrCheeseLocation(cheeseLocationCell.getColumn(),
                                cheeseLocationCell.getRow());
                List<ApiLocationDTO> catsLocations = ApiLocationDTO.getCatsLocation(catsLocationCells);
                ApiBoardDTO board =
                        ApiBoardDTO.createBoardWithAllItsComponents(MazeGenerator.totalRows, MazeGenerator.totalColumns,
                                mouseLocation, cheeseLocation, catsLocations, gameController, game);
                return new ResponseEntity<>(board, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/games/{ID}/moves")
    public ResponseEntity<HttpStatus> makeMouseAndCatsMove(@PathVariable long ID, @RequestBody String reqBody) {
        for (ApiGameDTO game : games) {
            if (game.gameNumber == ID) {
                Cell nextMove = null;
                GameController gameController = game.getGameController();
                switch (reqBody) {
                    case "MOVE_UP" -> nextMove = gameController.getNewLocationToMoveMouseUp();
                    case "MOVE_DOWN" -> nextMove = gameController.getNextLocationToMoveMouseDown();
                    case "MOVE_LEFT" -> nextMove = gameController.getNextLocationToMoveMouseLeft();
                    case "MOVE_RIGHT" -> nextMove = gameController.getNextLocationToMoveMouseRight();
                    case "MOVE_CATS" -> {
                        gameController.moveAllCatsRandomly();
                        return new ResponseEntity<>(HttpStatus.ACCEPTED);
                    }
                    default -> {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                }
                if (nextMove != null) {
                    gameController.moveMouseAsPerUserInput(nextMove.getRow(), nextMove.getColumn());
                    return new ResponseEntity<>(HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
