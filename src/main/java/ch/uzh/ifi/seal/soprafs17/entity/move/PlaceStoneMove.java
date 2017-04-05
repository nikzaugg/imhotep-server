package ch.uzh.ifi.seal.soprafs17.entity.move;

import ch.uzh.ifi.seal.soprafs17.GameConstants;
import ch.uzh.ifi.seal.soprafs17.entity.game.Stone;

import javax.persistence.*;

@Entity(name = "PLACE_STONE")
@DiscriminatorValue(value = GameConstants.PLACE_STONE)
public class PlaceStoneMove extends AMove {

    public PlaceStoneMove(String moveType){
        super(moveType);
    }

    @Column
    private Long shipId;

    @Column
    private int stonePosition;

    @OneToOne(targetEntity = Stone.class, fetch = FetchType.LAZY)
    private Stone stone;

    public Long getShipId() {
        return shipId;
    }

    public void setShipId(Long shipId) {
        this.shipId = shipId;
    }

    public int getStonePosition() {
        return stonePosition;
    }

    public void setStonePosition(int stonePosition) {
        this.stonePosition = stonePosition;
    }

    public Stone getStone() {
        return stone;
    }

    public void setStone(Stone stone) {
        this.stone = stone;
    }
}
