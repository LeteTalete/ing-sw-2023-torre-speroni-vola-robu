package it.polimi.ingsw.model.board;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.enumerations.State;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public class LivingRoom implements Serializable {
    private Couple[][] board;
    private Deck deck;
    private int errorTilesCode;

    /**
     * Constructor for the LivingRoom, given the number of players it reads the LivingRoom.json file and creates
     * the board and the deck. For more information about the board see the documentation.
     * @param numberOfPlayers - the number of players in the game.
     */
    public LivingRoom(int numberOfPlayers) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = LivingRoom.class.getClassLoader().getResourceAsStream("JSON/LivingRoom.json");
            JsonNode rootNode = mapper.readTree(inputStream);
            int[][] jsonMatrixCopy = mapper.convertValue(rootNode.get("LivingRoomBoardJSON"), int[][].class);

            this.board = new Couple[jsonMatrixCopy.length][jsonMatrixCopy[0].length];
            deck = new Deck();

            for (int i = 0; i < jsonMatrixCopy.length; i++) {
                for (int j = 0; j < jsonMatrixCopy[i].length; j++) {

                    if (jsonMatrixCopy[i][j] == 0) {
                        Couple couple = new Couple();
                        couple.setState(State.INVALID);
                        this.board[i][j] = couple;
                    } else if (jsonMatrixCopy[i][j] == 2) {
                        Couple couple = new Couple(deck.draw());
                        this.board[i][j] = couple;
                    } else if (( jsonMatrixCopy[i][j] == 3 ) && ( numberOfPlayers >= 3 )) {
                        Couple couple = new Couple(deck.draw());
                        this.board[i][j] = couple;
                    } else if (( jsonMatrixCopy[i][j] == 4 ) && ( numberOfPlayers == 4 )) {
                        Couple couple = new Couple(deck.draw());
                        this.board[i][j] = couple;
                    } else {
                        Couple couple = new Couple();
                        couple.setState(State.INVALID);
                        this.board[i][j] = couple;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading matrix from file: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Method checkPlayerChoice checks if the choice made by player is a valid choice or not.
     * The tiles chosen must be PICKABLE, must be adjacent and must have at least one side free.
     * @param choice - the tiles chosen by the player.
     * @return - true if the choice is valid, false otherwise.
     */
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
            if(getCouple(p).getState() != State.PICKABLE){
                errorTilesCode = 5;
                return false;
            }
            if(!atLeastOneSideFree(p)){
                errorTilesCode = 3;
                return false;
            }
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
            if(!sameX && !sameY) {
                errorTilesCode = 2;
                return false;
            }
            if(sameX)
            {
                Collections.sort(Y);
                for(int i=0;i<choice.size()-1;i++)
                {
                   if(Y.get(i+1) - Y.get(i) != 1) {
                       errorTilesCode = 1;
                       return false; //not adjacent
                   }
                }
            }
            if(sameY)
            {
                Collections.sort(X);
                for(int i=0;i<choice.size()-1;i++)
                {
                    if(X.get(i+1) - X.get(i) != 1) {
                        errorTilesCode = 1;
                        return false; //not adjacent
                    }
                }
            }
        }

        return true;
    }

    /**
     * Method atLeastOneSideFree checks if the couple passed as an argument has at least one side that is not
     * occupied by another couple with state PICKABLE.
     * @param p - the position of the couple.
     * @return - true if the couple has at least one side free, false otherwise.
     */
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

    /**
     * Method updateCouples removes the tiles chosen by the player from the board.
     * @param choice - the tiles chosen by the player.
     */
    public void updateCouples(ArrayList<Position> choice)
    {
        //this method should set the couples state chosen by the player as EMPTY
        //and the tile field should be set as null
        for(Position p : choice)
        {
            setCouple(p,null,State.EMPTY);
        }
    }

    /**
     * Method checkForRefill checks if there are at least two adjacent couples, if not a refill is needed.
     * @return - true if a refill is needed, false otherwise.
     */
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

    /** Method printBoard prints the board on the screen. Used only for testing. */
    public void printBoard()
    {
        //this method is only used for testing
        for( int i = 0; i < board.length; i++) {
            for( int j = 0 ; j < board[0].length; j++) {

                if (getCouple(new Position(i,j)).getState().equals(State.EMPTY) ) {
                    System.out.print( " " + " " + " ");
                } else if (getCouple(new Position(i,j)).getState().equals(State.INVALID)) {
                    System.out.print( "\033[0;100m" + " " + " " + " " + "\033[0m" );
                } else {
                    switch (getCouple(new Position(i, j)).getTile().getTileType().toString()) {
                        case "CAT" ->
                                System.out.print("\033[0;102m" + "\033[1;90m" + " " + getCouple(new Position(i, j)).getTile().getTileType().toString().charAt(0) + " " + "\033[0m");
                        case "PLANT" ->
                                System.out.print("\033[0;105m" + "\033[1;90m" + " " + getCouple(new Position(i, j)).getTile().getTileType().toString().charAt(0) + " " + "\033[0m");
                        case "FRAME" ->
                                System.out.print("\033[0;104m" + "\033[1;90m" + " " + getCouple(new Position(i, j)).getTile().getTileType().toString().charAt(0) + " " + "\033[0m");
                        case "TROPHY" ->
                                System.out.print("\033[0;106m" + "\033[1;90m" + " " + getCouple(new Position(i, j)).getTile().getTileType().toString().charAt(0) + " " + "\033[0m");
                        case "BOOK" ->
                                System.out.print("\033[0;107m" + "\033[1;90m" + " " + getCouple(new Position(i, j)).getTile().getTileType().toString().charAt(0) + " " + "\033[0m");
                        case "GAMES" ->
                                System.out.print("\033[0;103m" + "\033[1;90m" + " " + getCouple(new Position(i, j)).getTile().getTileType().toString().charAt(0) + " " + "\033[0m");
                    }
                }

            }
            System.out.println();
        }

    }

    /** Method clearBoard clears the board. Used only for testing. */
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

    /** Method refill refills the board with tiles from the deck. It sets the state of the couples to PICKABLE. */
    public void refill() {
        for (Couple[] couples : this.board) {
            for (Couple couple : couples) {
                if (( couple.getState() == State.EMPTY ) && ( deck.getSize() > 0 )) {
                    couple.setTile(deck.draw());
                    couple.setState(State.PICKABLE);
                }
            }
        }
    }

    /**
     * Method getCouple returns the couple in the given position.
     * @param p - the position of the couple.
     * @return - the couple in the given position.
     */
    public Couple getCouple(Position p) {
        return board[p.getX()][p.getY()];
    }

    /**
     * Method getBoard returns the board.
     * @return - the board.
     */
    public Couple[][] getBoard(){
        return this.board;
    }

    /**
     * Method setCouple sets the couple in the given position.
     * @param p - the position of the couple.
     * @param t - the tile of the couple.
     * @param s - the state of the couple.
     */
    public void setCouple(Position p, Tile t, State s)
    {
        board[p.getX()][p.getY()].setTile(t);
        board[p.getX()][p.getY()].setState(s);
    }


    /**
     * Method getErrorTilesCode returns the error code corresponding to the mistake the player made when choosing the
     * tiles from the board
     * @return error code
     */
    public int getErrorTilesCode() {
        return errorTilesCode;
    }
}