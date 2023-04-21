package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.PersonalGoalCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameController {
    private Player currentPlayer;
    private int gameId;
    private static String ViewInterface;
    private static Game game;
    private ArrayList<Player> players;
    private int numOfPlayers;
    private LivingRoom board;
    private static Player player;//what was this for?
    //constructor
    public GameController(ArrayList<Player> playersList, int id) {
        players = playersList;
        numOfPlayers = players.size();
        gameId = id;
    }

    public void main() {
        System.out.println("I've created a game and here are the players:");
        for (Player player : players) {
            System.out.println(player.getNickname());
        }
        /**we need the server to pass the number of players and the list of players to the gameController, somehow**/
        // create and setup board (we're assuming this all happens in the next instruction)
         this.board = new LivingRoom(numOfPlayers);

        /**once the living room is set, controller decides who's first**/
        chooseFirstPlayer();


        /**has a method to start a turn, which will notify each player that it's "nickname"'s turn**/

        /**has a method to change turns (it could already be implemented into Game**/



    }

    // choose randomly a player and set is as the first one to play
    public void chooseFirstPlayer(){
        int curr = new Random().nextInt(numOfPlayers-1);
        this.currentPlayer = players.get(curr);
        //didn't the first player had a boolean like 'Chair'?
    }

    public void nextTurn(){
        int next = 0;
        for(int i = 0; i < numOfPlayers + 1; i++)
        {
            if(players.get(i).equals(currentPlayer))
            {
                next = i + 1;
                break;
            }
        }
        if(next > numOfPlayers)
        {
            next = 0;
        }
        currentPlayer = players.get(next);
    }

    public List<Player> scoreBoard(ArrayList<Player> ps){
        List<Player> ranking = new ArrayList<Player>();
        return ranking = ps.stream().sorted(Comparator.comparing(Player::getScore)).collect(Collectors.toList());
    }

    public void endGame(){
        //make one last round with the remaining players and then calculates score and shows the scoreboard
        calculateScore();
        scoreBoard(players);
    }

    public int calculateScore(){

        for(int i=0; i<numOfPlayers; i++)
        {
            Player calcNow = players.get(i);
            int PGC = calcNow.getGoalCard();
            PersonalGoalCard card = new PersonalGoalCard();
            int add = card.scorePersonalGoalCard(calcNow.getMyShelf(), PGC);
            calcNow.setScore(calcNow.getScore() + add + calcNow.getMyShelf().additionalPoints());
        }
        return 0;
    }
    public int getGameId(){
        return this.gameId;
    }
}
