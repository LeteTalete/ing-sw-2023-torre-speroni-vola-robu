package model;

import model.Player;
import model.board.LivingRoom;

import java.util.ArrayList;

public class Game{
    private ArrayList<Player> players;

    public void nextTurn(){
    }

    public int calculateScore(){

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
        int numberofplayers;
        return numberofplayers;
    }


    public void endGame(){
    }

    public void chooseFirstPlayer(){
    }

    public LivingRoom showLivingRoom(){
    }

    public String askGUIorTUI(String string){

        return string;
    }

    public void matchSPacesPGoal(){
    }

    public void refillLivingRoom(){
    }

    public String startView(String string){

        return string;
    }

}
