package it.polimi.ingsw.structures;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.board.Shelf;

import java.io.Serializable;
import java.util.ArrayList;

public class GameView implements Serializable {

    private ArrayList<PlayerView> playersView = new ArrayList<>();

    private LivingRoomView livingRoomView;

    public GameView(GameController game){
        this.livingRoomView = new LivingRoomView(game.getGameBoard());
        game.getPlayers().forEach( playerView -> this.playersView.add( new PlayerView(playerView) ) );
    }

    public LivingRoomView getGameBoardView(){
        return this.livingRoomView;
    }
    public ArrayList<PlayerView> getPlayersView(){
        return this.playersView;
    }

    /*public ArrayList<Shelf> getShelfPlayers(){
        //this.playersView
    }*/
}
