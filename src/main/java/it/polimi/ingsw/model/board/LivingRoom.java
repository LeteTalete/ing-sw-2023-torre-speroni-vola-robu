package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.Tile;
import it.polimi.ingsw.model.Deck;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class LivingRoom {
    private Couple[][] board;

    public Couple getCouple(Position p) {
        return board[p.getX()][p.getY()];
    }


    public void setCouple(Position p, Tile t, State s)
    {
        board[p.getX()][p.getY()].setTile(t);
        board[p.getX()][p.getY()].setState(s);
    }

    public boolean checkPlayerChoice(ArrayList<Position> choice)
    {
        //this method should check if the choice made by player is a valid choice or not
        //the positions chosen must identify PICKABLE couples which have at least one side free
        //if there is more than one position chosen, then the positions must identify adjacent couples in the same direction

        ArrayList<Integer> X = new ArrayList<Integer>();
        ArrayList<Integer> Y = new ArrayList<Integer>();
        boolean sameX = true;
        boolean sameY = true;

        for (Position p : choice)
        {
            if(getCouple(p).getState() != State.PICKABLE) return false;
            if(!atLeastOneSideFree(p)) return false;
            X.add(p.getX());
            Y.add(p.getY());
        }
        if(choice.size()>1) //there is more than 1 position chosen
        {
            for(int i=0;i<choice.size()-1;i++)
            {
                if(!X.get(i).equals(X.get(i+1))) sameX = false; //not horizontal direction
                if(!Y.get(i).equals(Y.get(i+1))) sameY = false; //not vertical direction
            }
            if(!sameX && !sameY) return false;
            if(sameX)
            {
                Collections.sort(Y);
                for(int i=0;i<choice.size()-1;i++)
                {
                   if(Y.get(i+1) - Y.get(i) != 1) return false; //not adjacent
                }
            }
            if(sameY)
            {
                Collections.sort(X);
                for(int i=0;i<choice.size()-1;i++)
                {
                    if(X.get(i+1) - X.get(i) != 1) return false; //not adjacent
                }
            }
        }

        return true;
    }

    public boolean atLeastOneSideFree(Position p)
    {
        int x = p.getX();
        int y = p.getY();

        if(x+1 < board.length)
        {
            if(board[x+1][y].getState() != State.PICKABLE) return true;
        }
        if(x-1 >= 0)
        {
            if(board[x-1][y].getState() != State.PICKABLE) return true;
        }
        if(y+1 < board[0].length)
        {
            if(board[x][y+1].getState() != State.PICKABLE) return true;
        }
        if(y-1 >= 0)
        {
            if(board[x][y-1].getState() != State.PICKABLE) return true;
        }

        return false;
    }

    public void updateCouples(ArrayList<Position> choice)
    {
        //this method should set the couples state chosen by the player as EMPTY
        //and the tile field should be set as null
        for(Position p : choice)
        {
            setCouple(p,null,State.EMPTY);
        }
    }

    public boolean checkForRefill()
    {
        //this method returns false if there are at least two couples adjacent containing a tile
        //otherwise it returns true (in this case a refill is needed)


        for(int i=0; i < board.length; i++)
        {
            for(int j=0; j < board[i].length; j++)
            {
                if(board[i][j].getState() == State.PICKABLE)
                {
                    if(i+1 < board.length)
                    {
                        if(board[i+1][j].getState() == State.PICKABLE) return false;
                    }
                    if(i-1 >= 0)
                    {
                        if(board[i-1][j].getState() == State.PICKABLE) return false;
                    }
                    if(j+1 < board[i].length)
                    {
                        if(board[i][j+1].getState() == State.PICKABLE) return false;
                    }
                    if(j-1 >= 0)
                    {
                        if(board[i][j-1].getState() == State.PICKABLE) return false;
                    }
                }
            }
        }
        return true;
    }

    public void printBoard()
    {
        //this method is only used for testing

        System.out.println();
        for(int i=0; i< board.length;i++)
        {
            for(int j=0; j<board[i].length;j++)
            {
                if(getCouple(new Position(i,j)).getState() == State.PICKABLE)
                {
                    //System.out.print(" "+ l.getCouple(new Position(i,j)).getTile().getTileType());
                    System.out.print(" G");
                }
                else if(getCouple(new Position(i,j)).getState() == State.EMPTY)
                {
                    System.out.print(" E");
                }
                else System.out.print(" X");
            }
            System.out.println();
        }
    }

    public void clearBoard()
    {
        for(int i=0;i<board.length;i++)
        {
            for(int j=0; j<board[i].length;j++)
            {
                if(getCouple(new Position(i,j)).getState() == State.PICKABLE)
                {
                    setCouple(new Position(i,j),null,State.EMPTY);
                }
            }
        }
    }

    public void refill(Deck d)
    {
        //this method refills the board in this way:
        //iterates the Couple matrix, if the state is EMPTY it draw a tile form the deck and puts it in the couple,
        //the state of that Couple will be set to PICKABLE
        for(int i=0;i<this.board.length;i++)
        {
            for(int j=0;j<this.board[i].length;j++)
            {
                if((this.board[i][j].getState() == State.EMPTY) && (d.getSize() > 0))
                {
                    this.board[i][j].setTile(d.draw());
                    this.board[i][j].setState(State.PICKABLE);
                }
            }
        }
    }

    public LivingRoom(int numberofplayers) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = LivingRoom.class.getClassLoader().getResourceAsStream("JSON/LivingRoom.json");
            JsonNode rootNode = mapper.readTree(inputStream);
            int[][] jsonMatrixCopy = mapper.convertValue(rootNode.get("LivingRoomBoardJSON"), int[][].class);

            this.board = new Couple[jsonMatrixCopy.length][jsonMatrixCopy[0].length];
            Deck deck = new Deck();
            int emptyUnusableCheck;

            for (int i = 0; i < jsonMatrixCopy.length; i++) {
                for (int j = 0; j < jsonMatrixCopy[i].length; j++) {

                    if (jsonMatrixCopy[i][j] == 0) {
                        emptyUnusableCheck = 1;
                        Couple couple = new Couple(emptyUnusableCheck);
                        this.board[i][j] = couple;
                    } else if (jsonMatrixCopy[i][j] == 2) {
                        Couple couple = new Couple(deck.draw());
                        this.board[i][j] = couple;
                    } else if (( jsonMatrixCopy[i][j] == 3 ) && ( numberofplayers >= 3 )) {
                        Couple couple = new Couple(deck.draw());
                        this.board[i][j] = couple;
                    } else if (( jsonMatrixCopy[i][j] == 4 ) && ( numberofplayers == 4 )) {
                        Couple couple = new Couple(deck.draw());
                        this.board[i][j] = couple;
                    } else { // If a space is not INVALID and doesn't meet any of the requirements then it set to EMPTY_AND_UNUSABLE (rework needed)
                        emptyUnusableCheck = 0;
                        Couple couple = new Couple(emptyUnusableCheck);
                        this.board[i][j] = couple;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading matrix from file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}