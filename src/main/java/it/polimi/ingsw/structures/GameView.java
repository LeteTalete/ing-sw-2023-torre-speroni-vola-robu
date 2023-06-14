package it.polimi.ingsw.structures;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Shelf;
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
    public GameView(ModelUpdate game){
        this.livingRoomView = new LivingRoomView(game.getGameBoard());
        this.commonGoalCards = game.getCommonGoalCards();
        this.endGame = game.getEndGame();
        game.getPlayers().forEach( player -> this.playersView.add( new PlayerView(player) ) );
        this.CGC1Points = game.getCommonGoalCards().get(0).getPoints().pop();
        this.CGC2Points = game.getCommonGoalCards().get(1).getPoints().pop();
        this.currentPlayerNickname = game.getCurrentPlayerNickname();
    }

    public LivingRoomView getGameBoardView(){
        return this.livingRoomView;
    }
    public ArrayList<PlayerView> getPlayersView(){
        return this.playersView;
    }

    public List<CommonGoalCard> getCommonGoalCards() {
        return commonGoalCards;
    }

    public String getEndGame() {
        return endGame;
    }

    public int getCGC1Points() {
        return CGC1Points;
    }

    public int getCGC2Points() {
        return CGC2Points;
    }

    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }

    /*public ArrayList<Shelf> getShelfPlayers(){
        //this.playersView
    }*/
}
