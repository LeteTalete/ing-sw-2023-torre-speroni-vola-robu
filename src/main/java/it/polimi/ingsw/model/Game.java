package it.polimi.ingsw.model;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.Tile;
import it.polimi.ingsw.notifications.GameEnd;
import it.polimi.ingsw.notifications.LastTurn;
import it.polimi.ingsw.notifications.NotifyOnTurn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
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
    private int numOfPlayers;
    private LivingRoom gameBoard;
    private List<CommonGoalCard> commonGoalCards;

    public Game(String id, GameController gameC){
        this.gameId = id;
        gameController = gameC;
    }

    public void initialize() throws RemoteException {
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
        //todo check why this notify breaks everything: is it the modelupdate or the notify
        gameController.notifyAllPlayers(new ModelUpdate(this));
        /**the game then chooses a first player and notifies everyone**/
        String firstPlayer = getCurrentPlayer().getNickname();
        gameController.notifyAllPlayers(new NotifyOnTurn(firstPlayer));
        gameController.askTilesToPlayer(getCurrentPlayer().getTokenId());
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

        gameController.notifyAllPlayers(new ModelUpdate(this));
        gameController.notifyAllPlayers(new NotifyOnTurn(currentPlayer.getNickname()));

    }

    /** Method scoreBoard ranks in descending order the players by their scores and then prints them */
    public void scoreBoard(ArrayList<Player> ps){
        List<Player> ranking;
        ranking = ps.stream().sorted(Comparator.comparing(Player::getScore).reversed()).collect(Collectors.toList());
        for ( Player player : ranking ){
            fileLog.info( player.getNickname() + "'s score is: " + player.getScore());
        }
    }

    /** Method startGame initializes CGCs, PGCs and chooses the first player */
    public void startGame(){
        generateCGC(players.size());
        generatePGC(players);
    }

    public void gameHasEnded(){
        calculateScore();
        scoreBoard(players); // remember this scoreboard is printed only on the server
        gameController.notifyAllPlayers(new ModelUpdate(this));
        gameController.notifyAllPlayers(new GameEnd());
    }

    /** Method calculateScore calculates the score of each player at the end of the game */
    public void calculateScore(){
        for ( Player player : players ){
            player.setScore(player.getMyShelf().additionalPoints() + player.getGoalCard().scorePersonalGoalCard(player.getMyShelf()));
            if (endGame.equals(player.getNickname())){
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
                gameController.notifyAllPlayers(new LastTurn(this.getCurrentPlayer().getNickname()));
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


    public ArrayList<Position> getChoiceOfTiles(String choiceOfTiles) {
        //choiceOfTiles is the string in the correct format sent by the user to select from 1 to 3 tiles from the board
        //if those tiles can be picked, this method will return an ArrayList of the corresponding positions
        //otherwise this method will return null so the server can send to the client an Error Message

        ArrayList<Position> tilesChosen = new ArrayList<>();

        for (int i = 0; i < choiceOfTiles.length(); i++)
        {
            if (i % 3 == 0)
            {
                tilesChosen.add(new Position());
                tilesChosen.get(i / 3).setX(choiceOfTiles.charAt(i) - 48);
            }
            else if (( i + 1 ) % 3 != 0)
            {
                tilesChosen.get(( i - 1 ) / 3).setY(choiceOfTiles.charAt(i) - 48);
            }
        }

        if(!gameBoard.checkPlayerChoice(tilesChosen))
        {
            //if the tilesChosen are can't be picked, the server has to send an Error message back to the client
            //todo handle this situation
            //DEBUG
            fileLog.debug("DEBUG: those tiles can't be picked");
            tilesChosen = null;
        }

        return tilesChosen;
    }
}
