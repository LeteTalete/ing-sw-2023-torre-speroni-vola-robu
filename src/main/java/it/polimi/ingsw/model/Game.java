package it.polimi.ingsw.model;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Game {
    private static Logger fileLog = LogManager.getRootLogger();
    private String endGame;
    private Player currentPlayer;
    private Player previousPlayer;
    private final String gameId;
    private static GameController gameController;
    private ArrayList<Player> players;
    private List<Player> scoreBoard;
    private int numOfPlayers;
    private LivingRoom gameBoard;
    private List<CommonGoalCard> commonGoalCards;

    public Game(String id, GameController gameC){
        this.gameId = id;
        gameController = gameC;
    }

    public void initialize() {
        fileLog.info("Initializing game " + gameId + "with players: ");
        for (Player player : players) {
            fileLog.info(player.getNickname());
        }
        /**we need the server to pass the number of players and the list of players to the gameController, somehow**/
        // create and setup board (we're assuming this all happens in the next instruction)
        this.gameBoard = new LivingRoom(numOfPlayers);
        fileLog.info("I've created a living room board!");
        startGame();
        /**once the living room is set, controller decides who's first**/
        chooseFirstPlayer();
        gameController.notifyAllPlayers(new ModelUpdate(this));
        /**the game then chooses a first player and notifies everyone**/
        String firstPlayer = getCurrentPlayer().getNickname();
        gameController.notifyOnStartTurn(firstPlayer);
        /**has a method to start a turn, which will notify each player that it's "nickname"'s turn**/

        /**has a method to change turns (it could already be implemented into Game**/

    }

    /** Method chooseFirstPlayer randomly chooses the first player and gives it the chair */
    public void chooseFirstPlayer(){
        this.currentPlayer = players.get(new Random().nextInt(numOfPlayers));
        this.currentPlayer.setChair();
    }

    /** Method nextTurn passes to the next turn by saving the current player in previousPlayer and then
     *  changing currentPlayer to the next one on the list
     *  If the current player is the last on the list then it starts back from the first */
    public void nextTurn(){
        int next = 0;
        for(int i = 0; i < numOfPlayers; i++) {
            if(players.get(i).equals(currentPlayer)) {
                next = i + 1;
                break;
            }
        }
        if(next == numOfPlayers) {
            next = 0;
        }
        previousPlayer = currentPlayer;
        currentPlayer = players.get(next);
        gameController.notifyOnModelUpdate(new ModelUpdate(this));
        gameController.notifyOnStartTurn(currentPlayer.getNickname());

    }

    /** Method scoreBoard ranks in descending order the players by their scores and then prints them */
    public void scoreBoard(ArrayList<Player> ps){
        this.scoreBoard = ps.stream().sorted(Comparator.comparing(Player::getScore).reversed()).collect(Collectors.toList());
    }

    /** Method startGame initializes CGCs, PGCs and chooses the first player */
    public void startGame(){
        generateCGC(players.size());
        generatePGC(players);
    }

    public void gameHasEnded(){
        calculateScore();
        scoreBoard(players);
        gameController.notifyOnModelUpdate(new ModelUpdate(this));
        gameController.notifyOnGameEnd();
    }

    /** Method calculateScore calculates the score of each player at the end of the game */
    public void calculateScore(){
        for ( Player player : players ){
            player.setScore(player.getMyShelf().additionalPoints() + player.getGoalCard().scorePersonalGoalCard(player.getMyShelf()));
            if (endGame != null && endGame.equals(player.getNickname())){
                player.setScore(1);
            }
        }
    }

    /** Method generateCGC generates and returns an ArrayList containing CommonGoalCard objects
     * Those will be the cards that will be used in the game
     * First it generates 2 different random numbers from 0 to 11
     * (inside the code it's from 0 to 12 because the upper bound is exclusive)
     * Then it iterates for how many cards are needed and adds the cards to the ArrayList
     */
    public void generateCGC(int numOfPlayers){
        commonGoalCards = new ArrayList<>();
        int numberOfCommonGoalCards = 2; // Change this number if you want to use more cards
        int[] idsOfTheCards = new Random().ints(0, 12).distinct().limit(numberOfCommonGoalCards).toArray();

        for ( int i = 0; i < numberOfCommonGoalCards; i++){
            CommonGoalCard dummy = new CommonGoalCard(idsOfTheCards[i]);
            commonGoalCards.add(dummy.cardType());
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
/*
    public void updateCouples(ArrayList<Position> choice){
        this.gameBoard.updateCouples(choice);
        gameController.notifyAllPlayers(new ModelUpdate(this));
    }
*/
    public void insertTiles(int columnChosen, ArrayList<Tile> tiles){
        this.getCurrentPlayer().getMyShelf().insertTiles(columnChosen, tiles);
        if ( endGame == null ) {
            if (this.getCurrentPlayer().getMyShelf().checkShelfFull()) {
                setEndGame(this.getCurrentPlayer().getNickname());
                gameController.notifyOnLastTurn(this.getCurrentPlayer().getNickname());
            }
        }
    }


    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }

    public void setPreviousPlayer(Player previousPlayerPlayer) {
        this.previousPlayer = currentPlayer;
    }
    public Player getPreviousPlayer(){
        return this.previousPlayer;
    }
    public void setNumOfPlayers(int num){
        this.numOfPlayers = num;
    }
    public LivingRoom getGameBoard(){
        return this.gameBoard;
    }
    public List<CommonGoalCard> getCommonGoalCards() { return  this.commonGoalCards; }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
    }
    public void setPlayersView(ArrayList<Player> players) {
        this.players = players;
    }
    public String getGameId(){
        return this.gameId;
    }

    public String placeTilesOnShelf(List<Tile> tilesChosen, int column) {
        return null;
    }

    public void createGameBoard(int size) {
        gameBoard = new LivingRoom(size);
    }

    public String getEndGame() {
        return endGame;
    }

    public void setEndGame(String endGame) {
        this.endGame = endGame;
    }
    public List<Player> getScoreBoard() {
        return scoreBoard;
    }
}
