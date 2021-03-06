package ch.uzh.ifi.seal.soprafs17.web.rest.site;

import ch.uzh.ifi.seal.soprafs17.entity.site.BuildingSite;
import ch.uzh.ifi.seal.soprafs17.entity.site.MarketPlace;
import ch.uzh.ifi.seal.soprafs17.service.site.BuildingSiteService;
import ch.uzh.ifi.seal.soprafs17.service.site.MarketPlaceService;
import ch.uzh.ifi.seal.soprafs17.web.rest.GenericController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(SiteController.CONTEXT)
public class SiteController extends GenericController {

    // Standard URI Mapping of this class
    static final String CONTEXT = "games/{gameId}/sites";

    private final BuildingSiteService buildingSiteService;
    private final MarketPlaceService marketPlaceService;

    @Autowired
    public SiteController(BuildingSiteService buildingSiteService, MarketPlaceService marketPlaceService){
        this.buildingSiteService = buildingSiteService;
        this.marketPlaceService = marketPlaceService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{siteType}")
    @ResponseStatus(HttpStatus.OK)
    public BuildingSite getObelisk(@PathVariable("gameId") Long gameId, @PathVariable("siteType") String siteType) {
        return buildingSiteService.getBuildingSite(gameId, siteType);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/MARKET_PLACE")
    @ResponseStatus(HttpStatus.OK)
    public MarketPlace getObelisk(@PathVariable("gameId") Long gameId) {
        return marketPlaceService.getMarketPlace(gameId);
    }
}