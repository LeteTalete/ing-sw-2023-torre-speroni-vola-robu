package it.polimi.ingsw.controller;

import it.polimi.ingsw.Exceptions.InvalidChoiceFormatException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.StaticStrings;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameController {
    private Game model;
    private String gameId;
    private ServerManager master;

    public GameController(ArrayList<Player> playersList, String id, ServerManager serverMaster) {
        model = new Game(id, this);
        model.setPlayers(playersList);
        model.setNumOfPlayers(playersList.size());
        gameId = id;
        model.createGameBoard(playersList.size());
        master = serverMaster;
    }

    public GameController(ArrayList<Player> playersList, String id) {
        model = new Game(id, this);
        model.setPlayers(playersList);
        model.setNumOfPlayers(playersList.size());
        gameId = id;
        model.createGameBoard(playersList.size());
    }

    public void initialize() throws RemoteException {
        model.initialize();
    }

    public void notifySinglePlayer(String name, String message) throws RemoteException {
        master.notifySinglePlayer(name, message);
    }

    public void notifyAllPlayers(String message) {
        master.notifyAllPlayers(gameId, message);
    }

    public void chooseTiles(String username, String userInput) throws RemoteException {

        ArrayList<Position> choice = new ArrayList<>();

        for (int i = 0; i < userInput.length(); i++) {
            if (i % 3 == 0) {
                choice.add(new Position());
                choice.get(i / 3).setX(userInput.charAt(i) - 48);
            } else if (( i + 1 ) % 3 != 0) {
                choice.get(( i - 1 ) / 3).setY(userInput.charAt(i) - 48);
            }
        }

        if (this.getGameBoard().checkPlayerChoice(choice)) {
            master.notifySinglePlayer(username, StaticStrings.OK);
        }
        // needs else with StaticString.NO in case the choice is wrong

    }


    public void generateCGC(){
        model.generateCGC(model.getPlayers().size());
    }

    public void startGame(){
        model.startGame();
    }
    public void nextTurn(){
        model.nextTurn();
    }
    public void calculateScore(){
        model.calculateScore();
    }
    public void scoreBoard(){
        model.scoreBoard(model.getPlayers());
    }
    public void setCurrentPlayer(Player currentPlayer) {
        model.setCurrentPlayer(currentPlayer);
    }
    public Player getCurrentPlayer(){
        return model.getCurrentPlayer();
    }

    public void setPreviousPlayer(Player previousPlayerPlayer) {
        model.setPreviousPlayer(model.getCurrentPlayer());
    }
    public Player getPreviousPlayer(){
        return model.getPreviousPlayer();
    }
    public void setNumOfPlayers(int num){
       model.setNumOfPlayers(num);
    }
    public LivingRoom getGameBoard(){
        return model.getGameBoard();
    }
    public List<CommonGoalCard> getCommonGoalCards() { return  model.getCommonGoalCards(); }

    public ArrayList<Player> getPlayers(){
        return model.getPlayers();
    }
    public String getGameId(){
        return this.gameId;
    }

    // Dunno what to do with those two

    /*
    public String chooseTiles(List<Tile> tilesChosen) {
        //it will do something, I guess
        return null;
    }
     */

   /*
   public String placeTilesOnShelf(List<Tile> tilesChosen, int column) {
        return null;
    }
     */
}
