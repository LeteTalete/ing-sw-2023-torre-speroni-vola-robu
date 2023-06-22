package it.polimi.ingsw.structures;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.board.Couple;

import java.io.Serializable;

public class ShelfView implements Serializable {
    private Couple[][] shelfsMatrixView;

    /**
     * Constructor ShelfView creates a new ShelfView instance. It saves the shelf matrix.
     * @param shelf - the shelf.
     */
    public ShelfView(Shelf shelf){
        this.shelfsMatrixView = shelf.getShelfMatrix();
    }

    /**
     * Method getShelfsMatrixView returns the shelf matrix.
     * @return - the shelf matrix.
     */
    public Couple[][] getShelfsMatrixView(){
        return this.shelfsMatrixView;
    }

}
