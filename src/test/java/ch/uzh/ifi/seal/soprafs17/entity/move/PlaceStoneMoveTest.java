package ch.uzh.ifi.seal.soprafs17.entity.move;

/**
 * Created by Cristian on 13.04.2017.
 */

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.entity.game.Stone;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class PlaceStoneMoveTest {

    @Test
    public void setShipId() {
        PlaceStoneMove testPlaceStoneMove = new PlaceStoneMove();
        testPlaceStoneMove.setShipId(1L);
        Assert.assertNotNull(testPlaceStoneMove);
        Assert.assertEquals(testPlaceStoneMove.getShipId(),Long.valueOf(1L));
    }

    @Test
    public void setPlaceOnShip() {
        PlaceStoneMove testPlaceStoneMove = new PlaceStoneMove();
        testPlaceStoneMove.setPlaceOnShip(1);
        Assert.assertNotNull(testPlaceStoneMove);
        Assert.assertEquals(testPlaceStoneMove.getPlaceOnShip(),1);
    }

    @Test
    public void setStone() {
        PlaceStoneMove testPlaceStoneMove = new PlaceStoneMove();
        Stone testStone = new Stone();
        testPlaceStoneMove.setStone(testStone);
        Assert.assertNotNull(testPlaceStoneMove);
        Assert.assertNotNull(testStone);
        Assert.assertEquals(testPlaceStoneMove.getStone(), testStone);
    }
}