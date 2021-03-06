package ch.uzh.ifi.seal.soprafs17.service.move.validation;


import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.GameConstants;
import ch.uzh.ifi.seal.soprafs17.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs17.entity.game.Game;
import ch.uzh.ifi.seal.soprafs17.entity.game.Stone;
import ch.uzh.ifi.seal.soprafs17.entity.move.AMove;
import ch.uzh.ifi.seal.soprafs17.entity.move.GetStonesMove;
import ch.uzh.ifi.seal.soprafs17.entity.move.PlaceStoneMove;
import ch.uzh.ifi.seal.soprafs17.entity.user.Player;
import ch.uzh.ifi.seal.soprafs17.entity.user.User;
import ch.uzh.ifi.seal.soprafs17.exceptions.MoveValidationException;
import ch.uzh.ifi.seal.soprafs17.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs17.repository.StoneQuarryRepository;
import ch.uzh.ifi.seal.soprafs17.service.GameService;
import ch.uzh.ifi.seal.soprafs17.service.user.PlayerService;
import ch.uzh.ifi.seal.soprafs17.service.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Test class for the GameResource REST resource.
 *
 * @see GetStonesValidator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class GetStonesValidatorTest {

    private GetStonesValidator getStonesValidator;
    private AMove move;
    private Game game;

    @Autowired
    private GameService gameService;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private StoneQuarryRepository stoneQuarryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PlayerService playerService;

    @Before
    public void create(){
        // Set Up for supports()
        move = new GetStonesMove();
        Assert.assertNotNull(move);
        move.setMoveType(GameConstants.GET_STONES);
        move.setGameId(1L);
        move.setRoundNr(1);
        move.setPlayerNr(1);

        getStonesValidator = new GetStonesValidator();
        Assert.assertNotNull(getStonesValidator);

        // Set Up for validate()
        game = this.gameService.createGame("Test", "test");
        Assert.assertNotNull(game);
        Assert.assertEquals(game, this.gameService.findById(game.getId()));

        User user1 = userService.createUser("test1");
        User user2 = userService.createUser("test2");
        Assert.assertNotNull(user1);
        Assert.assertNotNull(user2);

        Player player1 = this.playerService.createPlayer(game.getId(), user1.getId());
        Assert.assertNotNull(player1);
        Assert.assertEquals(player1, this.playerService.findPlayerById(player1.getId()));
        this.playerService.initializePlayer(game.getId(), player1);
        this.gameService.addPlayer(game.getId(), player1);
        this.gameService.updateNrOfPlayers(game.getId());

        Player player2 = this.playerService.createPlayer(game.getId(), user2.getId());
        Assert.assertNotNull(player2);
        Assert.assertEquals(player2, this.playerService.findPlayerById(player2.getId()));
        this.playerService.initializePlayer(game.getId(), player2);
        this.gameService.addPlayer(game.getId(), player2);
        this.gameService.updateNrOfPlayers(game.getId());

        this.gameService.startGame(game.getId());

        game = this.gameService.findById(game.getId());
        game.setStoneQuarry(this.stoneQuarryRepository.findOne(1L));

        Assert.assertNotNull(game.getStoneQuarry());
        Assert.assertNotNull(game.getStoneQuarry().getBlackStones());
        Assert.assertNotNull(game.getStoneQuarry().getWhiteStones());

        this.gameRepository.save(game);
    }

    @Test
    public void supports() {
        // Check that the move is supported
        Assert.assertEquals(getStonesValidator.supports(move), true);

        // Checking that the move is not supported
        move = new PlaceStoneMove();
        Assert.assertEquals(getStonesValidator.supports(move), false);
    }

    @Test
    public void validateAllTrue(){
        // Satisfying all requirements
        Game testGame = this.gameService.findById(game.getId());
        getStonesValidator.validate(move, testGame);
    }

    @Test(expected = MoveValidationException.class)
    public void validateStatus(){
        // Finding the Game
        Game testGame = this.gameService.findById(game.getId());

        // Dissatisfying the requirements: STATUS
        Assert.assertNotNull(move);
        testGame.setStatus(GameStatus.PENDING);
        // Throws the exception
        getStonesValidator.validate(move, testGame);
    }

    @Test(expected = MoveValidationException.class)
    public void validateMoveType(){
        // Finding the Game
        Game testGame = this.gameService.findById(game.getId());

        // Dissatisfying the requirements: MOVE_TYPE
        move.setMoveType(GameConstants.PLACE_STONE);
        Assert.assertNotNull(move);
        // Throws the exception
        getStonesValidator.validate(move, testGame);
    }

    @Test(expected = MoveValidationException.class)
    public void validatePlayerNumber(){
        // Finding the Game
        Game testGame = this.gameService.findById(game.getId());

        // Dissatisfying the requirements: Game_ID
        Assert.assertNotNull(move);
        move.setPlayerNr(2);
        // Throws the exception
        getStonesValidator.validate(move, testGame);
    }

    @Test(expected = MoveValidationException.class)
    public void validateSupplySledSize(){
        // Finding the Game
        Game testGame = this.gameService.findById(game.getId());

        // Dissatisfying the requirements: Size of SupplySled != 5
        Stone stone = stoneQuarryRepository.findOne(1L).getBlackStones().remove(0);
        Assert.assertNotNull(stone);
        testGame.getPlayerByPlayerNr(game.getCurrentPlayer()).getSupplySled().getStones().add(stone);

        Stone stone2 = stoneQuarryRepository.findOne(1L).getBlackStones().remove(0);
        Assert.assertNotNull(stone2);
        testGame.getPlayerByPlayerNr(game.getCurrentPlayer()).getSupplySled().getStones().add(stone2);

        Stone stone3 = stoneQuarryRepository.findOne(1L).getBlackStones().remove(0);
        Assert.assertNotNull(stone3);
        testGame.getPlayerByPlayerNr(game.getCurrentPlayer()).getSupplySled().getStones().add(stone3);

        Assert.assertEquals(testGame.getPlayerByPlayerNr(game.getCurrentPlayer()).getSupplySled().getStones().size(), 5);

        // Throws the exception
        getStonesValidator.validate(move, testGame);
    }

    @Test(expected = MoveValidationException.class)
    public void validateStoneQuarry(){
        // Finding the Game
        Game testGame = this.gameService.findById(game.getId());

        // Dissatisfying the requirements: Size of StoneQuarry not empty
        testGame.getStoneQuarry().setBlackStones(new ArrayList<>());
        Assert.assertEquals(testGame.getStoneQuarry().getBlackStones().size(), 0);

        // Throws the exception
        getStonesValidator.validate(move, testGame);
    }
}