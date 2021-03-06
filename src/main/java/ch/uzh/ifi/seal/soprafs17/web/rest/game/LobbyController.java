package ch.uzh.ifi.seal.soprafs17.web.rest.game;

import ch.uzh.ifi.seal.soprafs17.entity.game.Game;
import ch.uzh.ifi.seal.soprafs17.service.game.LobbyService;
import ch.uzh.ifi.seal.soprafs17.web.rest.GenericController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(LobbyController.CONTEXT)
public class LobbyController extends GenericController {

    // Standard URI Mapping of this class
    static final String CONTEXT = "/lobby";

    private LobbyService lobbyService;

    @Autowired
    public LobbyController(LobbyService lobbyService){
        this.lobbyService = lobbyService;
    }

    /**
     * Context: /lobby
     * Returns a list of all games
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Game> listGames() {
        return lobbyService.listGames();
    }

    /**
     * Context: /lobby/games
     * Creates a game
     * @Param Game a game body (at least all non-nullable fields), userId - User
     */
    @RequestMapping(value = "games", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Game createGame(@RequestBody Game game, @RequestParam Long userId){
        return lobbyService.createGame(game, userId);
    }

    /**
     * Context: /lobby/games/{gameId}
     * Let's a user join a game / let's a user become a player
     * @Param Game a game body (at least all non-nullable fields), userId - User
     */
    @RequestMapping( value = "games/{gameId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void joinGame(@PathVariable Long gameId, @RequestParam Long userId){
        lobbyService.joinGame(gameId, userId);
    }

    /**
     * Context: /lobby/games/{gameId}/start
     * Starting a Game
     * @Param gameId Game, playerId - The Owner
     */
    @RequestMapping(value = "games/{gameId}/start", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void startGame(@PathVariable Long gameId, @RequestParam("playerId") Long playerId) {
        lobbyService.startGame(gameId, playerId);
    }

    /**
     * Context: /lobby/games/{gameId}/delete
     * Removing a Game
     * @Param gameId Game
     */
    @RequestMapping(value = "games/{gameId}/delete", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void deleteGame(@PathVariable Long gameId) {
        lobbyService.deleteGame(gameId);
    }

    /**
     * Context: /lobby/games/{gameId}/players/{playerNr}/delete
     * Removing a Game
     * @Param gameId Game
     */
    @RequestMapping(value = "games/{gameId}/players/{playerId}/delete", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void deletePlayer(@PathVariable Long gameId, @PathVariable Long playerId) {
        lobbyService.removePlayer(gameId, playerId);
    }

    /**
    * Context: /lobby/games/{gameId}/fastforward
    * Fastforward function
    * @Param gameId
    */
    @RequestMapping(value = "games/{gameId}/fastforward", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void fastForward(@PathVariable Long gameId, @RequestParam("playerId") Long playerId) {
        lobbyService.fastForward(gameId, playerId);
    }
}
