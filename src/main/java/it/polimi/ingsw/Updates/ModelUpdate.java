package it.polimi.ingsw.Updates;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.LivingRoom;

import java.util.List;

public class ModelUpdate {
    private LivingRoom gameBoard;
    private List<Player> players;

    public ModelUpdate(Game model){
        this.gameBoard = model.getGameBoard();
        this.players = model.getPlayers();
    }


    public LivingRoom getGameBoard() {
        return gameBoard;
    }

    public List<Player> getPlayers() {
        return players;
    }

}
