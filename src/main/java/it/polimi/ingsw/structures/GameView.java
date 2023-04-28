package it.polimi.ingsw.structures;

import it.polimi.ingsw.controller.GameController;
import java.io.Serializable;
import java.util.ArrayList;

public class GameView implements Serializable {

    private ArrayList<PlayerView> playersView = new ArrayList<>();

    private LivingRoomView gameBoardView;

    public GameView(GameController game){
        this.gameBoardView = new LivingRoomView(game.getGameBoard());
        game.getPlayers().forEach( playerView -> this.playersView.add( new PlayerView(playerView) ) );
    }

    public LivingRoomView getGameBoardView(){
        return this.gameBoardView;
    }
    public ArrayList<PlayerView> getPlayersView(){
        return this.playersView;
    }
}
