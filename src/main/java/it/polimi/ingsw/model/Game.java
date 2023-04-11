package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import it.polimi.ingsw.model.cards.PersonalGoalCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Game{
    private ArrayList<Player> players;

    private LivingRoom gameBoard;

    private int numOfPlayers;

    private Player currentPlayer;

    public void nextTurn(){
        int next = 0;
        for(int i = 0; i < numOfPlayers + 1; i++)
        {
            if(players.get(i).equals(getCurrentPlayer()))
            {
                next = i + 1;
            }
        }
        if(next > numOfPlayers)
        {
            next = 0;
        }
        setCurrentPlayer(players.get(next));
    }

    public int calculateScore(){

        for(int i=0; i<numOfPlayers; i++)
        {
            Player calcNow = players.get(i);
            int PGC = calcNow.getGoalCard();
            PersonalGoalCard card = new PersonalGoalCard();
            int add = card.scorePersonalGoalCard(calcNow.getMyShelf(), PGC);
            calcNow.score =+ add + calcNow.getMyShelf().additionalPoints();
        }
        return 0;
    }



    // addplayers should initialize every player attribute to 0
    public void addPlayers(Player newOne, String nickname){
        newOne.setNickname(nickname);
        this.players.add(newOne);
    }

    public void startGame(LivingRoom board){
        chooseFirstPlayer();
        this.gameBoard = board;

    }


    public int askHowManyPlayers(){
        int numberOfPlayers = 0;
        this.numOfPlayers= numberOfPlayers;
        return numberOfPlayers;
    }


    public void endGame(){
        //make one last round with the remaining players and then calculates score and shows the scoreboard
        calculateScore();
        scoreBoard(players);
    }

    // choose randomly a player and set is as the first one to play
    public void chooseFirstPlayer(){
        int curr = new Random().nextInt(numOfPlayers+1);
        this.currentPlayer = players.get(curr);
    }

    public List<Player> scoreBoard(ArrayList<Player> ps){
        List<Player> ranking = new ArrayList<Player>();
        return ranking = ps.stream().sorted(Comparator.comparing(Player::getMyScore)).collect(Collectors.toList());
    }

    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }


    /** Method generateCGC generates and returns an ArrayList containing CommonGoalCard objects
    * Those will be the cards that will be used in the game
    * First it generates 2 different random numbers from 0 to 11
    * (inside the code it's from 0 to 12 because the upper bound is exclusive)
    * Then it iterates for how many cards are needed and adds the cards to the ArrayList
    */
    public List<CommonGoalCard> generateCGC(){
        List<CommonGoalCard> commonGoalCards = new ArrayList<>();
        int numberOfCommonGoalCards = 2; // Change this number if you want to use more cards
        int[] idsOfTheCards = new Random().ints(0, 12).distinct().limit(numberOfCommonGoalCards).toArray();

        for ( int i = 0; i < numberOfCommonGoalCards; i++){
            CommonGoalCard dummy = new CommonGoalCard(idsOfTheCards[i]);
            commonGoalCards.add(dummy.typeGroupOrShape());
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
}
