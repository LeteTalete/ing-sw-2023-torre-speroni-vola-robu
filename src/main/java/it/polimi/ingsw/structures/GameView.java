package it.polimi.ingsw.structures;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.model.cards.CommonGoalCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameView implements Serializable {

    private ArrayList<PlayerView> playersView = new ArrayList<>();
    private LivingRoomView livingRoomView;
    private List<CommonGoalCard> commonGoalCards;
    private String endGame;
    private int CGC1Points;
    private int CGC2Points;
    private String currentPlayerNickname;
    private ArrayList<PlayerView> scoreBoard;

    /**
     * Constructor GameView creates a new GameView instance. GameView is the view of the game, and it contains all the
     * information that the view needs to show to the player.
     * @param game - the new ModelUpdate (view update).
     */
    public GameView(ModelUpdate game){
        this.livingRoomView = new LivingRoomView(game.getGameBoard());
        this.commonGoalCards = game.getCommonGoalCards();
        this.endGame = game.getEndGame();
        game.getPlayers().forEach( player -> this.playersView.add( new PlayerView(player) ) );
        if (game.getCommonGoalCards().get(0).getPoints().size() == 1 && game.getCommonGoalCards().get(0).getPoints().get(0).equals(0)) {
            this.CGC1Points = 0;
        } else this.CGC1Points = game.getCommonGoalCards().get(0).getPoints().pop();
        if (game.getCommonGoalCards().get(1).getPoints().size() == 1 && game.getCommonGoalCards().get(1).getPoints().get(0).equals(0)) {
            this.CGC2Points = 0;
        } else this.CGC2Points = game.getCommonGoalCards().get(1).getPoints().pop();
        this.currentPlayerNickname = game.getCurrentPlayerNickname();
        if ( game.getScoreboard() != null ) {
            this.scoreBoard = new ArrayList<>();
            game.getScoreboard().forEach( player -> this.scoreBoard.add( new PlayerView(player) ) );
        }
    }

    /**
     * Method getGameBoardView returns the living room view.
     * @return - the living room view.
     */
    public LivingRoomView getGameBoardView(){
        return this.livingRoomView;
    }

    /**
     * Method getPlayersView returns the players view.
     * @return - the players view.
     */
    public ArrayList<PlayerView> getPlayersView(){
        return this.playersView;
    }

    /**
     * Method getCommonGoalCards returns the common goal cards.
     * @return - the common goal cards.
     */
    public List<CommonGoalCard> getCommonGoalCards() {
        return commonGoalCards;
    }

    /**
     * Method getEndGame returns the endGame token.
     * @return - the endGame token.
     */
    public String getEndGame() {
        return endGame;
    }

    /**
     * Method getCGC1Points returns the points of the first common goal card.
     * @return - the points of the first common goal card.
     */
    public int getCGC1Points() {
        return CGC1Points;
    }

    /**
     * Method getCGC2Points returns the points of the second common goal card.
     * @return - the points of the second common goal card.
     */
    public int getCGC2Points() {
        return CGC2Points;
    }

    /**
     * Method getCurrentPlayerNickname returns the nickname of the current player.
     * @return - the nickname of the current player.
     */
    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }

    /**
     * Method getScoreboard returns the scoreboard.
     * @return - the scoreboard.
     */
    public ArrayList<PlayerView> getScoreboard() {
        return scoreBoard;
    }
}
