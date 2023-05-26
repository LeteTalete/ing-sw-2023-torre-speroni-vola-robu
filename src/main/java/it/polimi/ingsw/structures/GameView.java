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

    public GameView(ModelUpdate game){
        this.livingRoomView = new LivingRoomView(game.getGameBoard());
        this.commonGoalCards = game.getCommonGoalCards();
        this.endGame = game.getEndGame();
        game.getPlayers().forEach( player -> this.playersView.add( new PlayerView(player) ) );
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

    /*public ArrayList<Shelf> getShelfPlayers(){
        //this.playersView
    }*/
}
