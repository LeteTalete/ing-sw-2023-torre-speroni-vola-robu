package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.CommonGoalCard;
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
    private static GameController game;
    private ArrayList<Player> players;
    private int numOfPlayers;
    private LivingRoom gameBoard;
    private static Player player;//what was this for?
    //constructor
    public GameController(ArrayList<Player> playersList, int id) {
        players = playersList;
        numOfPlayers = players.size();
        gameId = id;
        gameBoard = new LivingRoom(players.size());
    }

    public GameController() {
    }

    public void main() {
        System.out.println("I've created a game and here are the players:");
        for (Player player : players) {
            System.out.println(player.getNickname());
        }
        /**we need the server to pass the number of players and the list of players to the gameController, somehow**/
        // create and setup board (we're assuming this all happens in the next instruction)
         this.gameBoard = new LivingRoom(numOfPlayers);

        /**once the living room is set, controller decides who's first**/
        chooseFirstPlayer();


        /**has a method to start a turn, which will notify each player that it's "nickname"'s turn**/

        /**has a method to change turns (it could already be implemented into Game**/



    }


    // choose randomly a player and set is as the first one to play
    public void chooseFirstPlayer(){
        int curr = new Random().nextInt(numOfPlayers);
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

    public void startGame(LivingRoom board){
        chooseFirstPlayer();
        this.gameBoard = board;

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
            int scorePGC = calcNow.getGoalCard().scorePersonalGoalCard(calcNow.getMyShelf());
            calcNow.setScore(scorePGC  + calcNow.getMyShelf().additionalPoints());
        }
        return 0;
    }

    /** Method generateCGC generates and returns an ArrayList containing CommonGoalCard objects
     * Those will be the cards that will be used in the game
     * First it generates 2 different random numbers from 0 to 11
     * (inside the code it's from 0 to 12 because the upper bound is exclusive)
     * Then it iterates for how many cards are needed and adds the cards to the ArrayList
     */
    public List<CommonGoalCard> generateCGC(int numOfPlayers){
        List<CommonGoalCard> commonGoalCards = new ArrayList<>();
        int numberOfCommonGoalCards = 2; // Change this number if you want to use more cards
        int[] idsOfTheCards = new Random().ints(0, 12).distinct().limit(numberOfCommonGoalCards).toArray();

        for ( int i = 0; i < numberOfCommonGoalCards; i++){
            CommonGoalCard dummy = new CommonGoalCard(idsOfTheCards[i]);
            commonGoalCards.add(dummy.typeGroupOrShape());
        }
        for ( CommonGoalCard card : commonGoalCards ) {
            if ( numOfPlayers == 2 ){
                card.getPoints().push(4);
                card.getPoints().push(8);
            } else if ( numOfPlayers == 3) {
                card.getPoints().push(4);
                card.getPoints().push(6);
                card.getPoints().push(8);
            } else if ( numOfPlayers == 4 ) {
                card.getPoints().push(2);
                card.getPoints().push(4);
                card.getPoints().push(6);
                card.getPoints().push(8);
            }
        }

        return commonGoalCards;
    }

    /** Method generatePGC generates as many ints (all random and different) as there are players.
     * Then it assigns a personal goal card to each player
     * @param players - The Arraylist of players
     */
    public void generatePGC(ArrayList<Player> players){
        int[] personalGoalCards = new Random().ints(1, 13).distinct().limit(players.size()).toArray();

        for ( int i = 0; i < players.size() ; i++){
            players.get(i).setGoalCard(personalGoalCards[i]);
        }
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }
    public void setNumOfPlayers(int num){
        this.numOfPlayers = num;
    }
    public LivingRoom getGameBoard(){
        return this.gameBoard;
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public void setPlayersView(ArrayList<Player> players) {
        this.players = players;
    }
    public int getGameId(){
        return this.gameId;
    }
}
