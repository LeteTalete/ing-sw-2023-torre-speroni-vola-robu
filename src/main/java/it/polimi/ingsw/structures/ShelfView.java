package it.polimi.ingsw.structures;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.board.Couple;

import java.io.Serializable;

public class ShelfView implements Serializable {
    private Couple[][] shelfsMatrixView;

    public ShelfView(Shelf shelf){
        this.shelfsMatrixView = shelf.getShelfsMatrix();
    }

    public Couple[][] getShelfsMatrixView(){
        return this.shelfsMatrixView;
    }

}
