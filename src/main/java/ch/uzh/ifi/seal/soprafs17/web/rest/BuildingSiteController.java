package ch.uzh.ifi.seal.soprafs17.web.rest;

/**
 * Created by Cristi and Dave on 24.03.2017.
 */

import ch.uzh.ifi.seal.soprafs17.entity.*;
import ch.uzh.ifi.seal.soprafs17.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping(BuildingSiteController.CONTEXT)
public class BuildingSiteController {

    Logger log  = LoggerFactory.getLogger(GameController.class);
    private BuildingSiteService buildingSiteService;

    // Standard URI Mapping of this class
    static final String CONTEXT = "games";

    @Autowired
    public BuildingSiteController(BuildingSiteService buildingSiteService){
        this.buildingSiteService = buildingSiteService;
    }

    @RequestMapping(value="/Obelisk", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public BuildingSite triggerObelisk() {
       return buildingSiteService.obeliskEvent();
    }

    @RequestMapping(value="/BurrialChamber", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public BuildingSite triggerBurrialChamber() {
        return buildingSiteService.burrialChamberEvent();
    }

    @RequestMapping(value="/Temple", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public BuildingSite triggerTemple() {
        return buildingSiteService.templeEvent();
    }

    @RequestMapping(value="/Pyramid", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public BuildingSite triggerPyramid() {
        return buildingSiteService.pyramidEvent();
    }


   /* @RequestMapping(value = "/{cristi}" ,method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String testi(@PathVariable("cristi") String msg) {

        String str = "the path variable we got is: " + msg;
        return str;
    }*/

}