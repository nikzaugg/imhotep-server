package ch.uzh.ifi.seal.soprafs17.entity.site;

import ch.uzh.ifi.seal.soprafs17.entity.card.MarketCard;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "MarketPlace")
@DiscriminatorValue("MARKET_PLACE")
public class MarketPlace extends ASite{

    public MarketPlace(){};

    public MarketPlace(Long gameId){
        super.setGameId(gameId);
        super.setSiteType("MARKET_PLACE");
    }

    @OneToMany(targetEntity = MarketCard.class/*, cascade = CascadeType.ALL, orphanRemoval = true*/)
    private List<MarketCard> marketCards;

    public List<MarketCard> getMarketCards() {
        return marketCards;
    }

    public void setMarketCards(List<MarketCard> marketCards) {
        this.marketCards = marketCards;
    }
}
