package ch.uzh.ifi.seal.soprafs17.entity;

/**
 * Created by Cristian on 06.04.2017.
 */

import ch.uzh.ifi.seal.soprafs17.Application;
import ch.uzh.ifi.seal.soprafs17.constant.BuildingSiteType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Transactional
public class BuildingSiteTest {

    @Test
    public void getGameId() {
        BuildingSite testBuildingSite = new BuildingSite();
        testBuildingSite.setGameId(1L);
        Assert.assertEquals(testBuildingSite.getGameId(), Long.valueOf(1L));
    }

    @Test
    public void setSiteType() {
        BuildingSite testBuildingSite = new BuildingSite();
        testBuildingSite.setSiteType("TEST_NAME");
        Assert.assertEquals(testBuildingSite.getSiteType(),"TEST_NAME");
    }

    @Test
    public void setBuildingSiteType() {
        BuildingSite testBuildingSite = new BuildingSite();
        testBuildingSite.setBuildingSiteType(BuildingSiteType.OBELISK);
        Assert.assertEquals(testBuildingSite.getBuildingSiteType(),BuildingSiteType.OBELISK);
    }

    @Test
    public void setStones() {
        BuildingSite testBuildingSite = new BuildingSite();
        List<Stone> testStones = new ArrayList<>();
        testBuildingSite.setStones(testStones);
        Assert.assertEquals(testBuildingSite.getStones(),testStones);
    }

    @Test
    public void setDockedShip() {
        BuildingSite testBuildingSite = new BuildingSite();
        Ship testDockedShip = new Ship();
        testBuildingSite.setDockedShip(testDockedShip);
        Assert.assertEquals(testBuildingSite.getDockedShip(),testDockedShip);
    }
}

