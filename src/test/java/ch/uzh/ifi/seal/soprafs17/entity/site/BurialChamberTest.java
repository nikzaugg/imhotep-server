package ch.uzh.ifi.seal.soprafs17.entity.site;

/**
 * Created by Cristian on 20.04.2017.
 */

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.GameConstants;
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
public class BurialChamberTest {

    @Test
    public void BurialChamber() {
        BurialChamber testChamber = new BurialChamber(1L);
        Assert.assertNotNull(testChamber);
        Assert.assertEquals(testChamber.getGameId(),Long.valueOf(1L));
        Assert.assertEquals(testChamber.getSiteType(), GameConstants.BURIAL_CHAMBER);
    }
}
