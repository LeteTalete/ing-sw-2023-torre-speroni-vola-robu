package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.Tile;

import java.util.ArrayList;
import java.util.List;

public class Shelf {
    public static final int ROWS = 6;
    public static final int COLUMNS = 5;
    private Couple[][] shelfsMatrix;
    private int freeSlots[];
    private List<Integer> cardsAlreadyChecked;

    /**this method inserts the chosen tiles into the player's shelf.
    the first tile to be inserted is at the end of the list, while the last tile to be inserted
    is the first tile of the list**/
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
            maximum = getFreeCol(numberColumns);
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
        this.shelfsMatrix[row][column].setTile(null);
        this.shelfsMatrix[row][column].setState(State.EMPTY);
    }

    public void setCoordinate(int row, int column, Couple chosen)
    {
        Couple insert = new Couple();
        insert.setTile(chosen.getTile());
        insert.setState(chosen.getState());
        this.shelfsMatrix[row][column] = insert;
    }

    public Couple getCoordinate(int row, int col)
    {
        return this.shelfsMatrix[row][col];
    }

    public Couple getCouple(Position p)
    {
        return this.shelfsMatrix[p.getX()][p.getY()];
    }

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

    public Couple[][] getShelfsMatrix() {
        return this.shelfsMatrix;
    }

    public List<Integer> getCardsAlreadyChecked(){
        return this.cardsAlreadyChecked;
    }

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

    public Shelf(){
        this.shelfsMatrix = new Couple[ROWS][COLUMNS];
        this.cardsAlreadyChecked = new ArrayList<>();
        for(int i = 0; i<ROWS; i++) {
            for(int j=0; j<COLUMNS; j++) {
                this.shelfsMatrix[i][j] = new Couple();
                this.shelfsMatrix[i][j].setState(State.EMPTY);
            }
        }
    }

}
