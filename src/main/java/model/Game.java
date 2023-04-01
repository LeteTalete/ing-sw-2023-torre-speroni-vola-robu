package model;

import model.Player;
import model.board.LivingRoom;

import java.util.ArrayList;

public class Game{
    private ArrayList<Player> players;

    private Player currentPlayer;

    public void nextTurn(){
    }

    public int calculateScore(){
        return 0;
    }

    // addplayers should initialize every player attribute to 0
    public void addPlayers(Player[] Array, String nickname){
    }

    public void startGame(LivingRoom board, Player[] array){
        // while(!endgame)
        // ask player which tile | ( up to
        // check tile            |         3 times )
        // ask player tile order
        // check tile
        // ask if player is sure
        // insert tiles inside shelfie
        // if shelfie is full endgame = 1
        // else new turn

    }


    // TO DO:
    // - Generate 2 random numbers from 1 to 12
    // - Read a .txt ( or a .json file ) and instantiate the cards written on the file corresponding to the 2 random numbers
    //   Those will be the common goal cards
    //   By doing it this way we only have to read and instantiate only 2 cards
    //   Instantiate = save somewhere the shape, conditions, requirements of the card



    public int askHowManyPlayers(){
        int numberofplayers = 0;
        return numberofplayers;
    }


    public void endGame(){
    }

    public void chooseFirstPlayer(){
    }

    public void refillLivingRoom(){
    }

    public void scoreBoard(){

    }

    public Player getCurrentPlayer(){
        return null;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
