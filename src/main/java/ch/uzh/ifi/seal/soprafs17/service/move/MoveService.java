package ch.uzh.ifi.seal.soprafs17.service.move;

import ch.uzh.ifi.seal.soprafs17.GameConstants;
import ch.uzh.ifi.seal.soprafs17.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs17.entity.game.Game;
import ch.uzh.ifi.seal.soprafs17.entity.game.Ship;
import ch.uzh.ifi.seal.soprafs17.entity.move.AMove;
import ch.uzh.ifi.seal.soprafs17.entity.move.GetCardMove;
import ch.uzh.ifi.seal.soprafs17.entity.move.SailShipMove;
import ch.uzh.ifi.seal.soprafs17.entity.site.BuildingSite;
import ch.uzh.ifi.seal.soprafs17.exceptions.ApplyMoveException;
import ch.uzh.ifi.seal.soprafs17.exceptions.InternalServerException;
import ch.uzh.ifi.seal.soprafs17.exceptions.MoveValidationException;
import ch.uzh.ifi.seal.soprafs17.exceptions.http.BadRequestHttpException;
import ch.uzh.ifi.seal.soprafs17.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs17.service.GameService;
import ch.uzh.ifi.seal.soprafs17.service.game.RoundService;
import ch.uzh.ifi.seal.soprafs17.service.move.rule.RuleManager;
import ch.uzh.ifi.seal.soprafs17.service.move.validation.ValidationManager;
import ch.uzh.ifi.seal.soprafs17.service.scoring.ScoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ch.uzh.ifi.seal.soprafs17.GameConstants.*;

/**
 * Service class for managing moves.
 */
@Service
@Transactional
public class MoveService {

    private final Logger log = LoggerFactory.getLogger(MoveService.class);

    private final GameRepository gameRepository;
    private final ValidationManager validationManager;
    private final RuleManager ruleManager;
    private final GameService gameService;
    private final RoundService roundService;
    private final ScoringService scoringService;

    @Autowired
    public MoveService(GameRepository gameRepository, ValidationManager validationManager, RuleManager ruleManager, GameService gameService, RoundService roundService, ScoringService scoringService) {
        this.gameRepository = gameRepository;
        this.validationManager = validationManager;
        this.ruleManager = ruleManager;
        this.gameService = gameService;
        this.roundService = roundService;
        this.scoringService = scoringService;
    }

    public synchronized void validateAndApply(AMove move) throws BadRequestHttpException, InternalServerException {
        log.debug("Validates and applies the Move: {}", move);

        Game game = gameRepository.findById(move.getGameId());

        try {
            // Validate the move if it can be applied
            this.validationManager.validate(move, game);
        }
        catch (MoveValidationException moveValException) {
            throw new BadRequestHttpException(moveValException);
        }

        try {
            // Applying the Move to the Game
            game = ruleManager.applyRules(move, game);
        }
        catch (ApplyMoveException applyMoveException) {
            throw new InternalServerException(applyMoveException);
        }

        // Saving the changed Game state into the DB
        this.gameRepository.save(game);

        //Scoring the Pyramid
        this.scoringService.score(game, GameConstants.PYRAMID);

        // Checking if the Game is still in the Status: SUBROUND
        this.checkSubRound(move, game);
        // Checking if the game advances to the next Round
        this.checkNextRound(move, game);
    }

    public synchronized void checkNextRound(AMove move, Game game){
        // Advancing the Game to the next Player, only if the Game is in Status: RUNNING
        if (game.getStatus() == GameStatus.RUNNING) {
            // Advancing the currentPlayer
            game.setCurrentPlayer((game.getCurrentPlayer()) % (game.getPlayers().size()) + 1);

            // Checking whether all ships have been sailed or not
            if (this.roundService.goToNextRound(game.getRoundByRoundCounter())){
                // After six Rounds the Game will be ended
                if (game.getRoundCounter() == GameConstants.LAST_ROUND) {
                    // Scoring End of the Game (Burial_Chamber, MarketCards, Obelisk)
                    this.scoringService.score(game, GameConstants.OBELISK);
                    this.scoringService.score(game, GameConstants.BURIAL_CHAMBER);

                    // TODO: Score the Green and Violet Market Card here
                    // Stopping the Game -> Status Change -> Winning Screen
                    this.gameService.stopGame(game.getId());
                }
                // Game is not finished yet
                else {
                    // Clear all site harbors (remove sailed ships from last round)
                    for (BuildingSite buildingSite: game.getBuildingSites()) {
                        buildingSite.setDocked(false);
                    }
                    // Remove the sailedShip from the Market Place
                    game.getMarketPlace().setDocked(false);

                    // Scoring at the End of the Round (Temple)
                    this.scoringService.score(game, GameConstants.TEMPLE);

                    // All Ships have sailed -> Initialize a new Round
                    this.gameService.initializeRound(game.getId());
                }
            }
        }
        // Saving the changes to the DB
        this.gameRepository.save(game);
    }

    public synchronized void checkSubRound(AMove move, Game game){
        // Advancing the Sub-Round when the Game is in Status: SUBROUND && the Move is SailShipMove (initial Move only)
        if (game.getStatus() == GameStatus.SUBROUND && move instanceof SailShipMove){
            //Typecasting to the SailShipMove
            SailShipMove newMove = (SailShipMove) move;
            // Retrieving the correct Ship
            Ship ship = game.getRoundByRoundCounter().getShipById(newMove.getShipId());
            // Setting the next SubRoundPlayer
            this.nextSubRoundPlayer(game, ship);
        }
        // Advancing the Sub-Round when the Game is in Status: SUBROUND && the Move is GetCardMove
        if (game.getStatus() == GameStatus.SUBROUND && move instanceof GetCardMove){
            // Typecasting to the GetCardMove
            GetCardMove newMove = (GetCardMove) move;
            // Retrieving the correct Ship
            Ship ship = game.getRoundByRoundCounter().getShipById(newMove.getShipId());
            // Setting the next SubRoundPlayer
            this.nextSubRoundPlayer(game, ship);
        }

        // Saving the changes to the DB
        this.gameRepository.save(game);
    }

    private void nextSubRoundPlayer(Game game, Ship ship){
        // Setting the next currentSubRoundPlayer according to the next Stone on the ship according to the placeOnShip
        if (!ship.getStones().isEmpty()){
            forloop:
            for (int i = 1; i <= ship.getMAX_STONES(); i++){
                if (ship.getStoneByPlace(i) != null){
                    switch (ship.getStoneByPlace(i).getColor()){
                        case BLACK: game.setCurrentSubRoundPlayer(1); break;
                        case WHITE: game.setCurrentSubRoundPlayer(2); break;
                        case BROWN: game.setCurrentSubRoundPlayer(3); break;
                        case GRAY:  game.setCurrentSubRoundPlayer(4); break;
                    }
                    // Removing the stone from the Stones on the Ship & putting it back to the StoneQuarry
                    game.getStoneQuarry().getStonesByPlayerNr(game.getCurrentSubRoundPlayer()).add(ship.getStoneByPlace(i));
                    ship.getStones().remove(ship.getStoneByPlace(i));
                    // Only loop to the first hit -> afterwards break
                    break forloop;
                }
            }
        }
        // All stones have been unloaded from the Ship
        else {
            // Returning the Game back to its normal running state
            game.setStatus(GameStatus.RUNNING);
        }

        // Saving the changes to the DB
        this.gameRepository.save(game);
    }
}