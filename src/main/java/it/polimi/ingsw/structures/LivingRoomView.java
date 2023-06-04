package it.polimi.ingsw.structures;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.board.Couple;

import java.io.Serializable;

public class LivingRoomView implements Serializable{
    private Couple[][] boardView;


    public LivingRoomView(LivingRoom livingRoom){
        this.boardView = livingRoom.getBoard();

    }

    public Couple getCouple(Position p) {
        return boardView[p.getX()][p.getY()];
    }

    public Couple[][] getBoard(){
        return this.boardView;
    }

}
