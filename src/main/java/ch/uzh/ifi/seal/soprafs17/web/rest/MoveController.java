package ch.uzh.ifi.seal.soprafs17.web.rest;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.soprafs17.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs17.repository.MoveRepository;
import ch.uzh.ifi.seal.soprafs17.service.GameService;
import ch.uzh.ifi.seal.soprafs17.service.MoveService;
import ch.uzh.ifi.seal.soprafs17.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.seal.soprafs17.GameConstants;
import ch.uzh.ifi.seal.soprafs17.entity.Game;
import ch.uzh.ifi.seal.soprafs17.entity.Move;
import ch.uzh.ifi.seal.soprafs17.entity.User;
import ch.uzh.ifi.seal.soprafs17.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs17.repository.UserRepository;


@RestController
@RequestMapping(MoveController.CONTEXT)
public class MoveController extends GenericController {

    Logger log = LoggerFactory.getLogger(MoveController.class);

    // Standard URI Mapping of this class
    static final String CONTEXT = "/games/{gameId}/players/{playersId}/moves";

    private MoveRepository moveRepository;
    private MoveService moveService;

    @Autowired
    public MoveController(MoveRepository moveRepository, MoveService moveService) {
        this.moveRepository = moveRepository;
        this.moveService = moveService;
    }

    // TODO Correct the implementation: Controller calls the service to do a action
    // TODO Correct the implemenation: Service handles the request in service

    /*
     * Context: /game/{game-id}/move
     */
    @RequestMapping(value = CONTEXT)
    @ResponseStatus(HttpStatus.OK)
    public List<Move> listMoves(@PathVariable Long gameId, @PathVariable Long playerId) {
        //TODO this method must be implemented in the MoveController
        return moveService.getMoves(gameId, playerId);
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/players/{playerId}/moves", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addMove(@RequestBody Move move) {
        // TODO Mapping into MoveController + Execution of addMove in moveService
        return moveService.addMove(Move move);
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/players/{playerId}/moves/{moveId}")
    @ResponseStatus(HttpStatus.OK)
    public Move getMove(@PathVariable Long gameId, @PathVariable Integer moveId) {
        // TODO Mapping into MoveController + Execution of getMove in moveService
        log.debug("getMove: " + gameId);

        Game game = gameRepo.findOne(gameId);
        if (game != null) {
            return game.getMoves().get(moveId);
        }

        return null;
    }
}