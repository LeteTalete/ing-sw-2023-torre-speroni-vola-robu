package it.polimi.ingsw.structures;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.LivingRoom;

import java.io.Serializable;
import java.util.ArrayList;

public class GameView implements Serializable {

    private ArrayList<PlayerView> playersView = new ArrayList<>();

    private LivingRoomView gameBoardView;

    public GameView(Game game){
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
