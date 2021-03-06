package ch.uzh.ifi.seal.soprafs17.repository;

import ch.uzh.ifi.seal.soprafs17.entity.game.Game;
import ch.uzh.ifi.seal.soprafs17.entity.user.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("gameRepository")
public interface GameRepository extends CrudRepository<Game, Long> {

	@Query("SELECT g FROM Game g WHERE g.name = :name")
	List<Game> findGamesByName(@Param("name") String name);

	@Query("SELECT g FROM Game g WHERE g.owner = :owner")
	List<Game> findGamesByOwner(@Param("owner") String owner);

	@Query("SELECT g FROM Game g WHERE g.id = :gameId")
	Game findById(@Param("gameId") Long id);

	@Query("SELECT g.numberOfPlayers FROM Game g WHERE g.id = :gameId")
	int findNrOfPlayers(@Param("gameId") Long gameId);

	@Query("SELECT g.players FROM Game g WHERE g.id = :gameId")
	List<Player> findPlayersByGameId(@Param("gameId") Long gameId);
}