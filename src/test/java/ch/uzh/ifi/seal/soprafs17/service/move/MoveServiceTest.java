package ch.uzh.ifi.seal.soprafs17.service.move;

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.GameConstants;
import ch.uzh.ifi.seal.soprafs17.constant.MarketCardType;
import ch.uzh.ifi.seal.soprafs17.entity.game.Game;
import ch.uzh.ifi.seal.soprafs17.entity.game.Round;
import ch.uzh.ifi.seal.soprafs17.entity.game.Stone;
import ch.uzh.ifi.seal.soprafs17.entity.move.*;
import ch.uzh.ifi.seal.soprafs17.entity.user.Player;
import ch.uzh.ifi.seal.soprafs17.entity.user.User;
import ch.uzh.ifi.seal.soprafs17.repository.AMoveRepository;
import ch.uzh.ifi.seal.soprafs17.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs17.repository.RoundRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the GameResource REST resource.
 *
 * @see MoveService
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class MoveServiceTest {

    private AMove move;
    private GetStonesMove move1;
    private GetStonesMove move4;
    private PlaceStoneMove move2;
    private PlaceStoneMove move5;
    private SailShipMove move3;
    private GetCardMove move6;
    private PlaceStoneMove move7;

    private Game game;

    @Autowired
    private MoveService moveService;
    @Autowired
    private AMoveRepository aMoveRepository;
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
    @Autowired
    private RoundRepository roundRepository;

    @Before
    public void createEnvironment(){
        // Set Up for supports()
        move = new GetStonesMove();
        Assert.assertNotNull(move);
        move.setMoveType(GameConstants.GET_STONES);
        move.setGameId(1L);
        move.setRoundNr(1);
        move.setPlayerNr(1);

        // Set Up for validate()
        game = this.gameService.createGame("Test", "test");
        Assert.assertNotNull(game);
        Assert.assertEquals(game, this.gameService.findById(game.getId()));

        User user1 = this.userService.createUser("test", "test");
        Assert.assertNotNull(user1);
        User user2 = this.userService.createUser("test2", "test2");
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

        Round round = this.roundRepository.findById(1L);

        game.getRounds().add(round);

        this.gameRepository.save(game);
    }

    @Before
    public void addEnvironement(){
        // TestMove1 - GET_STONES
        move1 = new GetStonesMove(GameConstants.GET_STONES);
        move1.setGameId(1L);
        move1.setRoundNr(1);
        move1.setPlayerNr(1);
        this.aMoveRepository.save(move1);

        // TestMove4 - GET_STONES
        move4 = new GetStonesMove(GameConstants.GET_STONES);
        move4.setGameId(1L);
        move4.setRoundNr(1);
        move4.setPlayerNr(2);
        this.aMoveRepository.save(move4);

        // TestMove2 - PLACE_STONE
        move2 = new PlaceStoneMove(GameConstants.PLACE_STONE);
        move2.setGameId(1L);
        move2.setRoundNr(1);
        move2.setPlayerNr(1);
        move2.setShipId(1L);
        move2.setPlaceOnShip(1);
        this.aMoveRepository.save(move2);

        // TestMove5 - PLACE_STONE
        move5 = new PlaceStoneMove(GameConstants.PLACE_STONE);
        move5.setGameId(1L);
        move5.setRoundNr(1);
        move5.setPlayerNr(2);
        move5.setShipId(1L);
        move5.setPlaceOnShip(2);
        this.aMoveRepository.save(move5);

        //TestMove7 - PLACE_STONE
        move7 = new PlaceStoneMove(GameConstants.PLACE_STONE);
        move7.setGameId(1L);
        move7.setRoundNr(1);
        move7.setPlayerNr(1);
        move7.setPlaceOnShip(3);
        move7.setShipId(1L);
        this.aMoveRepository.save(move7);

        // TestMove3 - SAIL_SHIP
        move3 = new SailShipMove(GameConstants.SAIL_SHIP);
        move3.setGameId(1L);
        move3.setRoundNr(1);
        move3.setPlayerNr(2);
        move3.setShipId(1L);
        move3.setTargetSiteId(1L);
        this.aMoveRepository.save(move3);

        // TestMove6 - GET_CARD
        move6 = new GetCardMove(GameConstants.GET_CARD);
        move6.setGameId(1L);
        move6.setRoundNr(1);
        move6.setPlayerNr(1);
        // Set the MarketCardId to a random from the Marketplace
        move6.setMarketCardId(game.getMarketPlace().getMarketCards().get(0).getId());
        this.aMoveRepository.save(move6);
    }

    @Test
    public void validateAndApplyGetStones(){

        this.moveService.validateAndApply(move1);

        // Assert that the Size of TheSupplySled is equal to the maxSize (5)
        Assert.assertEquals(game.getPlayerByPlayerNr(1).getSupplySled().getStones().size(), GameConstants.MAX_STONES_SUPPLY_SLED);
    }

    @Test
    public void validateAndApplyPlaceStones(){
        this.addEnvironement();

        // PlaceStoneMove of Player1 on Ship 1 - Position 1
        this.moveService.validateAndApply(move2);

        //Assert that 1 Stone has been placed on Ship1
        Assert.assertEquals(game.getRoundByRoundCounter().getShipById(1L).getStones().size(), 1);

        // PlaceStoneMove of Player2 on Ship 1 - Position 2
        this.moveService.validateAndApply(move5);

        //Assert that 1 Stone has been placed on Ship1
        Assert.assertEquals(game.getRoundByRoundCounter().getShipById(1L).getStones().size(), 2);

        // PlaceStoneMove of Player1 on Ship 1 - Position 3
        this.moveService.validateAndApply(move7);

        //Assert that 1 Stone has been placed on Ship1
        Assert.assertEquals(game.getRoundByRoundCounter().getShipById(1L).getStones().size(), 3);
    }

    @Test
    public void validateAndApplySailShip(){
        //Place the Stones on the Ship
        this.validateAndApplyPlaceStones();

        //SailShip by Player 2 to MarketPlace (ID = 1)
        this.moveService.validateAndApply(move3);

        game = this.gameService.findById(game.getId());

        // Assert the ship has sailed
        Assert.assertEquals(game.getRoundByRoundCounter().getShipById(1L).isHasSailed(), true);
        // Assert the Site has been docked
        Assert.assertEquals(game.getMarketPlace().isDocked(), true);
        Assert.assertEquals(game.getMarketPlace().getDockedShipId(), move3.getShipId());
    }

    @Test
    public void validateAndApplyGetCard(){
        this.validateAndApplySailShip();

        game.getMarketPlace().getMarketCardById(move6.getMarketCardId()).setColor(GameConstants.RED);
        game.getMarketPlace().getMarketCardById(move6.getMarketCardId()).setMarketCardType(MarketCardType.ENTRANCE);

        boolean isRed = false;
        String siteType = null;

        if (game.getMarketPlace().getMarketCardById(move6.getMarketCardId()).getColor().equals(GameConstants.RED)){
            isRed = true;
            switch (game.getMarketPlace().getMarketCardById(move6.getMarketCardId()).getMarketCardType()){
                case PAVED_PATH: siteType = GameConstants.OBELISK; break;
                case SARCOPHAGUS: siteType = GameConstants.BURIAL_CHAMBER; break;
                case ENTRANCE: siteType = GameConstants.PYRAMID;
            }
        }

        this.moveService.validateAndApply(move6);

        // Assert the MarketCard ID of the HandCard and the move are the same
        if (!isRed) {
            Assert.assertEquals(game.getPlayerByPlayerNr(1).getHandCards().get(0).getId(), move6.getMarketCardId());
        }
        else {
            Assert.assertEquals(game.getBuildingSite(siteType).getStones().size(), 1);
        }
    }

    @Test
    public void logMove(){
        // Create additional environment
        this.addEnvironement();

        // Game and Move must exist
        Assert.assertNotNull(move);
        Assert.assertNotNull(game);

        // Making sure there is no description yet
        Assert.assertNull(move.getDescription());

        // Logging the Move
        this.moveService.logMove(move, game);

        // Testing that the Description has been created
        Assert.assertNotNull(move.getDescription());
    }

    @Test
    public void findLastMoves(){
        // Create additional environment
        this.addEnvironement();

        // Game and Move must exist
        Assert.assertNotNull(move);
        Assert.assertNotNull(game);

        Page<AMove> result = this.moveService.findLastMoves(game.getId(), 5);
        Assert.assertEquals(result.getNumberOfElements(), 5);

        for (AMove move : result){
            System.out.println(move.getDescription());
        }
    }

}