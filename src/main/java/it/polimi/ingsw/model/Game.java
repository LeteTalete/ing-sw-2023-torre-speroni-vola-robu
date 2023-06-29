package it.polimi.ingsw.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
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

    /**
     * Constructor Game creates a new Game instance. Sets the game id and the game controller.
     * @param id - the id of the game.
     * @param gameC - the game controller.
     */
    public Game(String id, GameController gameC){
        this.gameId = id;
        gameController = gameC;
    }

    /** Method startGame initializes the game and notifies the players that the game has started. */
    public void startGame() {
        fileLog.info("Initializing game " + gameId + "with players: ");
        for (Player player : players) {
            fileLog.info(player.getNickname());
        }
        initialize();
        notifyStart();
    }

    /** Method notifyStart notifies the players that the game has started. */
    private void notifyStart() {
        String firstPlayer = getCurrentPlayer().getNickname();
        gameController.notifyOnStartTurn(firstPlayer);
        gameController.notifyOnModelUpdate(new ModelUpdate(this));
    }

    /** Method chooseFirstPlayer randomly chooses the first player and gives it the chair */
    public void chooseFirstPlayer(){
        this.currentPlayer = players.get(new Random().nextInt(numOfPlayers));
        this.currentPlayer.setChair();
    }

    /**
     * Method nextTurn passes to the next turn by saving the current player in previousPlayer and then
     * changing currentPlayer to the next one on the list.
     * If the current player is the last on the list then it starts back from the first.
     */
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
        gameController.notifyOnStartTurn(currentPlayer.getNickname());
        gameController.notifyOnModelUpdate(new ModelUpdate(this));

    }


    /**
     * Method scoreBoard ranks in descending order the players by their scores and then prints them.
     * @param ps - the list of players
     */
    public void scoreBoard(ArrayList<Player> ps){
        this.scoreBoard = ps.stream().sorted(Comparator.comparing(Player::getScore).reversed()).collect(Collectors.toList());
    }

    /** Method initialize creates the board, CGCs, PGCs and chooses the first player. */
    public void initialize(){
        this.gameBoard = new LivingRoom(numOfPlayers);
        fileLog.info("I've created a living room board!");
        generateCGC(players.size());
        generatePGC(players);
        chooseFirstPlayer();
    }

    /** Method gameHasEnded calculates the score of each player and then notifies the players that the game has ended. */
    public void gameHasEnded(){
        calculateScore();
        scoreBoard(players);
        gameController.notifyOnModelUpdate(new ModelUpdate(this));
        gameController.notifyOnGameEnd();
    }

    /** Method calculateScore calculates the score of each player at the end of the game. */
    public void calculateScore(){
        for ( Player player : players ){
            player.setScore(player.getMyShelf().additionalPoints() + player.getGoalCard().scorePersonalGoalCard(player.getMyShelf()));
            if (endGame != null && endGame.equals(player.getNickname())){
                player.setScore(1);
            }
        }
    }


    /**
     * Method generateCGC generates and returns an ArrayList containing CommonGoalCard objects.
     * First it generates 2 different random numbers from 0 to cardNodes (the number of card nodes in the JSON file).
     * Then it iterates for how many cards are needed, adds the cards to commonGoalCards and adds the points to the cards
     * depending on the number of players.
     * @param numOfPlayers - the number of players in the current game
     */
    public void generateCGC(int numOfPlayers){
        commonGoalCards = new ArrayList<>();
        int numberOfCommonGoalCards = 2; // Change this number if you want to use more cards
        int cardNodes = getNumberOfCardNodes();
        int[] idsOfTheCards = new Random().ints(0, cardNodes).distinct().limit(numberOfCommonGoalCards).toArray();

        for ( int i = 0; i < numberOfCommonGoalCards; i++){
            CommonGoalCard dummy = new CommonGoalCard(idsOfTheCards[i]);
            commonGoalCards.add(dummy.cardType());
        }
        for ( CommonGoalCard card : commonGoalCards ) {
            if ( numOfPlayers == 2 ){
                card.getPoints().push(0);
                card.getPoints().push(4);
                card.getPoints().push(8);
            } else if ( numOfPlayers == 3) {
                card.getPoints().push(0);
                card.getPoints().push(4);
                card.getPoints().push(6);
                card.getPoints().push(8);
            } else if ( numOfPlayers == 4 ) {
                card.getPoints().push(0);
                card.getPoints().push(2);
                card.getPoints().push(4);
                card.getPoints().push(6);
                card.getPoints().push(8);
            }
        }
    }

    /** Method getNumberOfCardNodes returns the number of card nodes in the JSON file containing the CGCs. */
    public int getNumberOfCardNodes(){

        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = Game.class.getClassLoader().getResourceAsStream("JSON/CommonGoalCards.json");
        try {
            JsonNode rootNode = mapper.readTree(inputStream);
            return rootNode.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method generatePGC generates as many ints (all random and different) as there are players.
     * Then it assigns a personal goal card to each player
     * @param players - The Arraylist of players
     */
    public void generatePGC(ArrayList<Player> players){
        int[] personalGoalCards = new Random().ints(1, 13).distinct().limit(players.size()).toArray();

        for ( int i = 0; i < players.size() ; i++){
            players.get(i).setGoalCard(personalGoalCards[i]);
        }
    }

    /**
     * Method insertTiles inserts the tiles in the shelf of the current player.
     * If the shelf is full and none of the players has already taken the endGame token, it sets the endGame token
     * to the nickname of the current player and notifies all players that the last round has started.
     * @param columnChosen - The column chosen by the player.
     * @param tiles - The tiles to be inserted.
     */
    public void insertTiles(int columnChosen, ArrayList<Tile> tiles){
        this.getCurrentPlayer().getMyShelf().insertTiles(columnChosen, tiles);

        if ( endGame == null ) {
            if (this.getCurrentPlayer().getMyShelf().checkShelfFull()) {
                setEndGame(this.getCurrentPlayer().getNickname());
                gameController.setLastRound();
            }
        }
    }

    /**
     * Method setCurrentPlayer sets the currentPlayer to the player passed as a parameter. Used for testing purposes.
     * @param cPlayer - The player to be set as the currentPlayer.
     */
    public void setCurrentPlayer(Player cPlayer) {
        currentPlayer = cPlayer;
    }

    /**
     * Method getCurrentPlayer returns the currentPlayer.
     * @return - The currentPlayer.
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * Method getPreviousPlayer returns the previousPlayer.
     * @return - The previousPlayer.
     */
    public Player getPreviousPlayer(){
        return this.previousPlayer;
    }

    /**
     * Method getGameBoard returns the gameBoard.
     * @return - The gameBoard.
     */
    public LivingRoom getGameBoard(){
        return this.gameBoard;
    }

    /**
     * Method getCommonGoalCards returns the commonGoalCards.
     * @return - The commonGoalCards.
     */
    public List<CommonGoalCard> getCommonGoalCards() { return  this.commonGoalCards; }

    /**
     * Method getPlayers returns the list of players.
     * @return - The list of players.
     */
    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    /**
     * Method setNumOfPlayers sets the number of players.
     * @param num - The number of players.
     */
    public void setNumOfPlayers(int num){
        this.numOfPlayers = num;
    }

    /**
     * Method setPlayers sets the list of players.
     * @param players - The list of players.
     */
    public void setPlayers(ArrayList<Player> players){
        this.players = players;
    }

    /**
     * Method getGameId returns the gameId.
     * @return - The gameId.
     */
    public String getGameId(){
        return this.gameId;
    }

    /**
     * Method getEndGame returns the endGame token which is the nickname of the player who took it.
     * @return - The endGame token.
     */
    public String getEndGame() {
        return endGame;
    }

    /**
     * Method setEndGame sets the endGame token to the nickname of the player who took it.
     * @param endGame - The endGame token.
     */
    public void setEndGame(String endGame) {
        this.endGame = endGame;
    }

    /**
     * Method getScoreBoard returns the scoreBoard.
     * @return - The scoreBoard.
     */
    public List<Player> getScoreBoard() {
        return scoreBoard;
    }

}
