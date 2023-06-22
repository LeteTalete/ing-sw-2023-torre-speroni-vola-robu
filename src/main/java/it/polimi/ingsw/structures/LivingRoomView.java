package it.polimi.ingsw.structures;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.board.Couple;

import java.io.Serializable;

public class LivingRoomView implements Serializable{
    private Couple[][] boardView;

    /**
     * Constructor LivingRoomView creates a new LivingRoomView instance. It saves the board of the living room.
     * @param livingRoom - the living room.
     */
    public LivingRoomView(LivingRoom livingRoom){
        this.boardView = livingRoom.getBoard();

    }

    /**
     * Method getCouple returns the couple in the position p.
     * @param p - the position of the couple.
     * @return - the couple in the position p.
     */
    public Couple getCouple(Position p) {
        return boardView[p.getX()][p.getY()];
    }

    /**
     * Method getBoard returns the board.
     * @return - the board.
     */
    public Couple[][] getBoard(){
        return this.boardView;
    }

}
