package it.polimi.ingsw.controller;

import it.polimi.ingsw.Exceptions.InvalidChoiceFormatException;
import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.Tile;
import it.polimi.ingsw.responses.GetTilesResponse;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.server.ServerManager;
import it.polimi.ingsw.server.StaticStrings;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameController {
    private Game model;
    private String gameId;
    private ArrayList<Position> choiceOfTiles;
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

    public void notifySinglePlayer(String token, Response response) throws RemoteException {
        master.notifySinglePlayer(token, response);
    }

    public void notifyAllPlayers(Response response) {
        master.notifyAllPlayers(gameId, response);
    }
/*
    public void notifyAllPlayers(ModelUpdate message) {
        master.notifyAllPlayers(gameId, message);
    }
*/

    public void chooseTiles(String token, List<String> userInput) throws RemoteException
    {
        ArrayList<Position> choice = new ArrayList<>();

        for(String s : userInput)
        {
            choice.add(new Position(s.charAt(0)-48,s.charAt(1)-48));
        }

        if (this.getGameBoard().checkPlayerChoice(choice)) {
            this.choiceOfTiles = choice;
            master.notifySinglePlayer(token, new GetTilesResponse(choice, true));
        }
        else{
            master.notifySinglePlayer(token, new GetTilesResponse(choice, true));
        }

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

    //todo
    public void chooseColumn(String token, int column) {

    }

    public ArrayList<Position> getChoiceOfTiles() {
        return choiceOfTiles;
    }

    public void setChoiceOfTiles(String choiceOfTiles) {
        this.choiceOfTiles = model.getChoiceOfTiles(choiceOfTiles);
    }


    public void askTilesToPlayer(String tokenId) {
        try {
            master.askTilesToPlayer(tokenId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void rearrangeTiles(String token, List<String> tilesOrdered) {
    }
}
