package model.board;

import model.enumerations.Couple;
import model.enumerations.State;

public class Shelf {
    public static final int ROWS = 6;
    public static final int COLUMNS = 5;
    private Couple[][] shelfsMatrix;
    private int freeSlots[];
    private int[] cardsAlreadyChecked;


    //getMaxFree takes as an argument the number of column in which we want to count the empty spaces
    //if numberColumn > 4 it means we're about to check all the columns and find the max of empty
    //slots in the entirety of our array

    public int getMaxFree(int numberColumns){
        int maximum = 0;
        if(numberColumns > 4)
        {
            for(int i=0; i<5; i++)
            {
                maximum = getMaxFree(i);
            }
        }
        else
        {
            //this needs to be rewritten asap, possibly using streams
            for(int i=0; i<6; i++)
            {
                if(this.shelfsMatrix[numberColumns][i].getState().equals(State.EMPTY))
                {
                    maximum = maximum+1;
                }
            }
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
    }

}
