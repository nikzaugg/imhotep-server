package ch.uzh.ifi.seal.soprafs17.entity.move;

import ch.uzh.ifi.seal.soprafs17.GameConstants;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "GET_CARD")
@DiscriminatorValue(value = GameConstants.GET_CARD)
@JsonTypeName(value = "GET_CARD")
public class GetCardMove extends AMove {

    public GetCardMove(){
        // Existence Reason: Hibernate also needs an empty constructor
    }

    public GetCardMove(String moveType){
        super(moveType);
    }

    @Column
    private Long shipId;

    @Column
    private int currentSubRoundPlayer;

    @Column
    private Long marketCardId;

    public int getCurrentSubRoundPlayer() {
        return currentSubRoundPlayer;
    }

    public void setCurrentSubRoundPlayer(int currentSubRoundPlayer) {
        this.currentSubRoundPlayer = currentSubRoundPlayer;
    }

    public Long getMarketCardId() {
        return marketCardId;
    }

    public void setMarketCardId(Long marketCardId) {
        this.marketCardId = marketCardId;
    }

    public Long getShipId() {
        return shipId;
    }

    public void setShipId(Long shipId) {
        this.shipId = shipId;
    }
}