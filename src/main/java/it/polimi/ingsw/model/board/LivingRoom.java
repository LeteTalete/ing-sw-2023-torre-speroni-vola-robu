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
import java.util.ArrayList;


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

        return true;
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

        //this for checks horizontally
        for(int i=0;i<this.board.length;i++) //for each row
        {
            for(int j=0;j<this.board[i].length-1;j++)
            {
                if((this.board[i][j].getState() == State.PICKABLE) && this.board[i][j+1].getState() == State.PICKABLE)
                {
                    return false;
                }
            }
        }

        //this for checks vertically
        for(int i=0;i<this.board[0].length;i++) //for each column
        {
            for(int j=0;j<this.board.length-1;j++)
            {
                if((this.board[j][i].getState() == State.PICKABLE) && this.board[j+1][i].getState() == State.PICKABLE)
                {
                    return false;
                }
            }
        }
        return true;
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
            File jsonFile = new File("src/main/java/JSONFile/LivingRoom.json");
            JsonNode rootNode = mapper.readTree(jsonFile);
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