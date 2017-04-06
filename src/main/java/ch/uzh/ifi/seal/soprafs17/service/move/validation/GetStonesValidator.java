package ch.uzh.ifi.seal.soprafs17.service.move.validation;

import ch.uzh.ifi.seal.soprafs17.GameConstants;
import ch.uzh.ifi.seal.soprafs17.entity.game.Game;
import ch.uzh.ifi.seal.soprafs17.entity.move.AMove;
import ch.uzh.ifi.seal.soprafs17.entity.move.GetStonesMove;
import ch.uzh.ifi.seal.soprafs17.exceptions.MoveValidationException;

public class GetStonesValidator implements IValidator {

    @Override
    public boolean supports(AMove move) {
        return move instanceof GetStonesMove;
    }

    @Override
    public void validate(final AMove move, final Game game) throws MoveValidationException {
        if(!move.getMoveType().equals(GameConstants.GET_STONES)){
            String msg = "Validation for Move: " + move + " failed. Wrong MoveType";
            throw new MoveValidationException(msg);
        }
        else if (!move.getGameId().equals(game.getId())) {
            String msg = "Validation for Move: " + move + " failed. GameId of Move <> GameId of Game";
            throw new MoveValidationException(msg);
        }
        else if (move.getRoundNr() != game.getRoundCounter()) {
            String msg = "Validation for Move: " + move + " failed. RoundNr of Move <> RoundCounter of Game";
            throw new MoveValidationException(msg);
        }
        else if (move.getPlayerNr() != game.getCurrentPlayer()) {
            String msg = "Validation for Move: " + move + " failed. PlayerNr of Move <> CurrentPlayer of Game";
            throw new MoveValidationException(msg);
        }
        // Stones on the SupplySled must be less than the Maximum
        else if (game.getPlayers().get(game.getCurrentPlayer()).getSupplySled().getStones().size() == GameConstants.MAX_STONES_SUPPLY_SLED){
            String msg = "Validation for Move: " + move + " failed. SupplySled is already full!";
            throw new MoveValidationException(msg);
        }
        // Stones taken from StoneQuarry must be less than the maximal allowed amount
        else if (game.getStoneQuarry().getStonesByPlayerNr(game.getCurrentPlayer()).size() == 0){
            String msg = "Validation for Move: " + move + " failed. The Player: " + game.getCurrentPlayer() + " has already used the maximal amount of stones allowed!";
            throw new MoveValidationException(msg);
        }
    }
}