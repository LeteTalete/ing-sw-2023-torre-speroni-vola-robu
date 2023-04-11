package game.model.board;

import game.model.enumerations.Couple;
import game.model.enumerations.State;
import game.model.enumerations.Tile;

import java.util.ArrayList;

public class Shelf {
    public static final int ROWS = 6;
    public static final int COLUMNS = 5;
    private Couple[][] shelfsMatrix;
    private int freeSlots[];
    private int[] cardsAlreadyChecked;

    /**this method inserts the chosen tiles into the player's shelf.
    the first tile to be inserted is at the end of the list, while the last tile to be inserted
    is the first tile of the list**/
    public void insertTiles(int col, ArrayList<Tile> toInsert)
    {
        int pos = 3;
        for(int i = 0; i<ROWS; i++)
        {
            if(this.shelfsMatrix[i][col].getState().equals(State.EMPTY))
            {
                this.shelfsMatrix[i][col].setTile(toInsert.get(pos));
                pos--;
                if(pos==0){break;}
            }
        }
    }
    //getMaxFree takes as an argument the number of column in which we want to count the empty spaces
    //if numberColumn > 4 it means we're about to check all the columns and find the max of empty
    //slots in the entirety of our array


    public int getFreeCol(int nCol) {
        int output = 0;
        for(int i=0; i<ROWS; i++)
        {
            if(this.shelfsMatrix[i][nCol].getState().equals(State.EMPTY)){
                output++;
            }
        }
        return output;
    }

    public int getMaxFree(int numberColumns){
        int maximum = 0;
        if(numberColumns > 4)
        {
            for(int i=0; i<COLUMNS; i++){
                if(getFreeCol(i)>maximum)
                maximum = getFreeCol(i);
            }
        }
        else
        {
            getFreeCol(numberColumns);
        }
        return maximum;
    }

    public boolean checkShelfFull()
    {
        if(getMaxFree(7)==0) {return true;}
        return false;
    }

    public void setFreeSlots(int row, int column)
    {
        this.shelfsMatrix[row][column].setState(State.EMPTY);
    }

    public void setCoordinate(int row, int column, Couple chosen)
    {
        Couple insert = new Couple();
        insert.setTile(chosen.getTile());
        insert.setState(chosen.getState());
        this.shelfsMatrix[row][column] = insert;
    }

    public Couple getCoordinate(int column, int row)
    {
        return this.shelfsMatrix[column][row];
    }

    public int additionalPoints(){
        int scoring = 0;
        return scoring;
    }

    public Couple[][] getShelfsMatrix() {
        return this.shelfsMatrix;
    }

    public int[] getCardsAlreadyChecked(){
        return this.cardsAlreadyChecked;
    }


    public Shelf(){
        this.shelfsMatrix = new Couple[ROWS][COLUMNS];
        this.cardsAlreadyChecked = new int[12];
        for(int i = 0; i<ROWS; i++) {
            for(int j=0; j<COLUMNS; j++) {
                this.shelfsMatrix[i][j] = new Couple();
                this.shelfsMatrix[i][j].setState(State.EMPTY);
            }
        }
    }

}
