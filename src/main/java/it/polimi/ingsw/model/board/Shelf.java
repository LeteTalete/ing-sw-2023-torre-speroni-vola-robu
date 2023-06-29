package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.enumerations.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shelf implements Serializable {
    public static final int ROWS = 6;
    public static final int COLUMNS = 5;
    private Couple[][] shelfsMatrix;
    private List<Integer> cardsAlreadyClaimed;

    /** Constructor Shelf creates a new Shelf instance, it creates a new shelf's matrix and sets all its couples to empty. */
    public Shelf(){
        this.shelfsMatrix = new Couple[ROWS][COLUMNS];
        this.cardsAlreadyClaimed = new ArrayList<>();
        for(int i = 0; i<ROWS; i++) {
            for(int j=0; j<COLUMNS; j++) {
                this.shelfsMatrix[i][j] = new Couple();
                this.shelfsMatrix[i][j].setState(State.EMPTY);
            }
        }
    }


    /**
     * Method insertTiles inserts the chosen tiles into the player's shelf.
     * The first tile to be inserted is at the end of the list, while the last tile to be inserted
     * is the first tile of the list.
     * @param col - the shelf's column chosen by the player
     * @param toInsert - the tiles to insert
     */
    public void insertTiles(int col, ArrayList<Tile> toInsert)
    {
        boolean flag;

        for(int i=0;i<toInsert.size();i++)
        {
            flag = true;
            for(int j=ROWS-1;j>=0;j--)
            {
                if(flag && shelfsMatrix[j][col].getState() == State.EMPTY)
                {
                    shelfsMatrix[j][col].setTile(toInsert.get(i));
                    shelfsMatrix[j][col].setState(State.PICKABLE);
                    flag = false;
                }
            }
        }

    }

    /**
     * Method getFreeCol takes as an argument the column number and returns the number of empty slots in that column.
     * @param nCol - the column number.
     * @return - the number of empty slots in that column.
     */
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


    /**
     * Method getMaxFree takes as an argument the number of column in which we want to count the empty spaces.
     * If numberColumn > 4 it means we're about to check all the columns and find the max of empty
     * slots in the entirety of the shelf.
     * @param numberColumns - the number of the column to check or a number > 4 for checking all the columns
     * @return the number of empty spaces for a certain column or at all
     */
    public int getMaxFree(int numberColumns){
        int maximum = 0;
        if(numberColumns > 4) {
            for(int i=0; i<COLUMNS; i++) {
                if(getFreeCol(i)>maximum) {
                    maximum = getFreeCol(i);
                }
            }
        } else {
            maximum = getFreeCol(numberColumns);
        }

        return maximum;
    }

    /**
     * Method checkShelfFull returns true if the shelf is full, false otherwise.
     * @return - true if the shelf is full, false otherwise.
     */
    public boolean checkShelfFull()
    {
        if(getMaxFree(7)==0) {return true;}
        return false;
    }

    /**
     * Method checkEnoughSpace takes as an argument the list of tiles to be inserted and returns true if there is
     * enough space in the shelf to insert them, false otherwise.
     * @param choice - the list of tiles to be inserted.
     * @return - true if there is enough space in the shelf to insert them, false otherwise.
     */
    public boolean checkEnoughSpace(ArrayList<Position> choice)
    {
        if(getMaxFree(5) < choice.size()) return false;
        else return true;
    }

    /**
     * Method setFreeSlots takes as an argument the row and the column of the tile to be removed from the shelf and
     * sets the tile and the state of the corresponding couple to null and empty respectively.
     * @param row - the row of the tile.
     * @param column - the column of the tile.
     */
    public void setFreeSlots(int row, int column)
    {
        this.shelfsMatrix[row][column].setTile(null);
        this.shelfsMatrix[row][column].setState(State.EMPTY);
    }


    /**
     * Method getCoordinate takes as an argument the row and the column of the tile to be returned and returns the
     * corresponding couple.
     * @param row - the row of the tile.
     * @param col - the column of the tile.
     * @return - the tile's couple.
     */
    public Couple getCoordinate(int row, int col)
    {
        return this.shelfsMatrix[row][col];
    }

    /**
     * Method getCouple takes as an argument the position of the couple to be returned and returns the corresponding
     * couple from the shelf.
     * @param p - the position of the couple.
     * @return - the couple.
     */
    public Couple getCouple(Position p)
    {
        return this.shelfsMatrix[p.getX()][p.getY()];
    }

    /**
     * Method additionalPoints calculates the additional points that the player earns by placing tiles in groups of the
     * same tile type inside the shelf.
     * @return - the additional points.
     */
    public int additionalPoints(){
        //this method will scan the shelf looking for adjacent same tile kinds groups and return points in this way:
        //3 -> +2
        //4 -> +3
        //5 -> +5
        //6+ -> +8

        int scoring = 0;
        int x,y;
        int count = 0;
        ArrayList<Position> group = new ArrayList<Position>();
        ArrayList<ArrayList<Position>> groups = new ArrayList<ArrayList<Position>>();
        ArrayList<Couple> visited = new  ArrayList<Couple>();

        //finding groups
        for(int i=0; i<ROWS; i++)
        {
            for(int j=0; j<COLUMNS; j++)
            {
                if((shelfsMatrix[i][j].getState() == State.PICKABLE) && (!visited.contains(shelfsMatrix[i][j])))
                {
                    group = new ArrayList<Position>();
                    group.add(new Position(i,j));
                    visited.add(shelfsMatrix[i][j]);
                    for(int k=0; k<group.size(); k++)
                    {
                        x = group.get(k).getX();
                        y = group.get(k).getY();

                        if(y+1 < COLUMNS)
                        {
                            if((getCoordinate(x,y+1).getState() == State.PICKABLE) &&
                                    (getCoordinate(x,y+1).getTile().getTileType() == getCoordinate(x,y).getTile().getTileType()) &&
                                    (!visited.contains(getCoordinate(x,y+1))))
                            {
                                group.add(new Position(x,y+1));
                                visited.add(shelfsMatrix[x][y+1]);
                            }
                        }
                        if(x+1 < ROWS)
                        {
                            if((getCoordinate(x+1,y).getState() == State.PICKABLE) &&
                                    (getCoordinate(x+1,y).getTile().getTileType() == getCoordinate(x,y).getTile().getTileType()) &&
                                    (!visited.contains(getCoordinate(x+1,y))))
                            {
                                group.add(new Position(x+1,y));
                                visited.add(shelfsMatrix[x+1][y]);
                            }
                        }
                        if(y-1 >= 0)
                        {
                            if((getCoordinate(x,y-1).getState() == State.PICKABLE) &&
                                    (getCoordinate(x,y-1).getTile().getTileType() == getCoordinate(x,y).getTile().getTileType()) &&
                                    (!visited.contains(getCoordinate(x,y-1))))
                            {
                                group.add(new Position(x,y-1));
                                visited.add(shelfsMatrix[x][y-1]);
                            }
                        }
                        if(x-1 >= 0)
                        {
                            if((getCoordinate(x-1,y).getState() == State.PICKABLE) &&
                                    (getCoordinate(x-1,y).getTile().getTileType() == getCoordinate(x,y).getTile().getTileType()) &&
                                    (!visited.contains(getCoordinate(x-1,y))))
                            {
                                group.add(new Position(x-1,y));
                                visited.add(shelfsMatrix[x-1][y]);
                            }
                        }
                    }
                    groups.add(group);
                }
                else visited.add(shelfsMatrix[i][j]);
            }
        }

        //calculate score
        for(ArrayList<Position> g : groups)
        {
            count = 0;
            for (Position p : g)
            {
                count ++;
            }
            if(count == 3) scoring +=2;
            else if(count == 4) scoring += 3;
            else if(count == 5) scoring += 5;
            else if(count >= 6) scoring += 8;
        }


        return scoring;
    }

    /**
     * Getter getShelfMatrix returns the shelf's matrix.
     * @return - shelfsMatrix.
     */
    public Couple[][] getShelfMatrix() {
        return this.shelfsMatrix;
    }

    /**
     * Method getCardsAlreadyClaimed returns the list of the cards that have already been claimed inside the shelf.
     * @return - cardsAlreadyClaimed.
     */
    public List<Integer> getCardsAlreadyClaimed(){
        return this.cardsAlreadyClaimed;
    }

    /** Method clearShelf clears the shelf. Used only for test purposes. */
    public void clearShelf()
    {
        for(int i=0;i<ROWS;i++)
        {
            for(int j=0;j<COLUMNS;j++)
            {
                setFreeSlots(i,j);
            }

        }
    }


    /** Method printShelf prints the shelf. Used only for test purposes. */
    public void printShelf() {
        System.out.println("------------------------");
        for( int i = 0; i < ROWS; i++) {
            for( int j = 0 ; j < COLUMNS; j++) {

                if (shelfsMatrix[i][j].getState().equals(State.EMPTY)) {
                    System.out.print( " " + " " + " ");
                } else {
                    switch (shelfsMatrix[i][j].getTile().getTileType().toString()) {
                        case "CAT":
                            System.out.print( "\033[0;102m" + "\033[1;90m" + " " + shelfsMatrix[i][j].getTile().getTileType().toString().charAt(0) + " " + "\033[0m"  );
                            break;
                        case "PLANT":
                            System.out.print( "\033[0;105m" + "\033[1;90m" + " " + shelfsMatrix[i][j].getTile().getTileType().toString().charAt(0) + " " + "\033[0m" );
                            break;
                        case "FRAME":
                            System.out.print( "\033[0;104m" + "\033[1;90m" + " " + shelfsMatrix[i][j].getTile().getTileType().toString().charAt(0) + " " + "\033[0m" );
                            break;
                        case "TROPHY":
                            System.out.print( "\033[0;106m" + "\033[1;90m" + " " + shelfsMatrix[i][j].getTile().getTileType().toString().charAt(0) + " " + "\033[0m" );
                            break;
                        case "BOOK":
                            System.out.print( "\033[0;107m" + "\033[1;90m" + " " + shelfsMatrix[i][j].getTile().getTileType().toString().charAt(0) + " " + "\033[0m"  );
                            break;
                        case "GAMES":
                            System.out.print( "\033[0;103m" + "\033[1;90m" + " " + shelfsMatrix[i][j].getTile().getTileType().toString().charAt(0) + " " + "\033[0m"  );
                            break;
                    }
                }

            }
            System.out.println();
        }
        System.out.println("------------------------");
    }

}
