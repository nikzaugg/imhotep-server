package ch.uzh.ifi.seal.soprafs17.repository;

import ch.uzh.ifi.seal.soprafs17.entity.site.ASite;
import ch.uzh.ifi.seal.soprafs17.entity.site.BuildingSite;
import ch.uzh.ifi.seal.soprafs17.entity.site.MarketPlace;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("ASiteRepository")
public interface ASiteRepository  extends CrudRepository<ASite, Long> {

    @Query("SELECT b FROM BuildingSite b WHERE (b.siteType = :siteType) AND (b.gameId = :gameId)")
    public BuildingSite findBuildingSite(@Param("gameId") Long gameId, @Param("siteType") String siteType);

    @Query("SELECT b FROM BuildingSite b WHERE (b.gameId = :gameId)")
    public List<BuildingSite> findAllBuildingSites(@Param("gameId") Long gameId);

    @Query("SELECT m FROM MarketPlace m WHERE (m.gameId = :gameId)")
    public MarketPlace findMarketPlace(@Param("gameId") Long gameId);
}