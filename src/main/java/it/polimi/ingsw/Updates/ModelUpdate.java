package it.polimi.ingsw.Updates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.cards.CommonGoalCard;

import java.io.Serializable;
import java.util.List;

public class ModelUpdate implements Serializable{
    private LivingRoom gameBoard;
    private List<Player> players;
    private String endGame;
    private List<CommonGoalCard> commonGoalCards;
    public ModelUpdate(Game model){
        this.gameBoard = model.getGameBoard();
        this.players = model.getPlayers();
        this.endGame = model.getEndGame();
        this.commonGoalCards = model.getCommonGoalCards();
    }


    public LivingRoom getGameBoard() {
        return gameBoard;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getEndGame() {
        return endGame;
    }

    public List<CommonGoalCard> getCommonGoalCards() {
        return commonGoalCards;
    }
}
