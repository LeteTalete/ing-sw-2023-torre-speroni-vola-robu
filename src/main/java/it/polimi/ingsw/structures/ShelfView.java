package it.polimi.ingsw.structures;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.enumerations.Couple;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class ShelfView implements Serializable {
    private Couple[][] shelfsMatrixView;

    public ShelfView(Shelf shelf){
        this.shelfsMatrixView = shelf.getShelfsMatrix();
    }

    public Couple[][] getShelfsMatrixView(){
        return this.shelfsMatrixView;
    }

}
