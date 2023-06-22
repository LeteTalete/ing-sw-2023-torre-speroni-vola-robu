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
    private List<Player> scoreboard;

    private String endGame;
    private List<CommonGoalCard> commonGoalCards;
    private String currentPlayerNickname;

    /**
     * Constructor ModelUpdate creates a new ModelUpdate instance. Given the new model, it saves the attributes
     * of the model so that the view can be updated.
     * @param model - the new updated model.
     */
    public ModelUpdate(Game model){
        this.gameBoard = model.getGameBoard();
        this.players = model.getPlayers();
        this.endGame = model.getEndGame();
        this.commonGoalCards = model.getCommonGoalCards();
        this.scoreboard = model.getScoreBoard();
        this.currentPlayerNickname = model.getCurrentPlayer().getNickname();
    }

    /**
     * Method getGameBoard returns the game board.
     * @return - the game board.
     */
    public LivingRoom getGameBoard() {
        return gameBoard;
    }

    /**
     * Method getPlayers returns the players.
     * @return - the players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Method getScoreboard returns the scoreboard.
     * @return - the scoreboard.
     */
    public String getEndGame() {
        return endGame;
    }

    /**
     * Method getCommonGoalCards returns the common goal cards.
     * @return - the common goal cards.
     */
    public List<CommonGoalCard> getCommonGoalCards() {
        return commonGoalCards;
    }

    /**
     * Method getCurrentPlayerNickname returns the current player nickname.
     * @return - the current player nickname.
     */
    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }
}
