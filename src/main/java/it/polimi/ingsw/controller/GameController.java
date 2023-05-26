package it.polimi.ingsw.controller;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.Tile;
import it.polimi.ingsw.notifications.CommonGoalGained;
import it.polimi.ingsw.notifications.EndTurn;
import it.polimi.ingsw.responses.GetOrderResponse;
import it.polimi.ingsw.responses.GetTilesResponse;
import it.polimi.ingsw.responses.MoveOk;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.server.ServerManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private static Logger fileLog = LogManager.getRootLogger();
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
        if (model.getCurrentPlayer().getMyShelf().checkEnoughSpace(choice) && this.getGameBoard().checkPlayerChoice(choice)) {
            this.choiceOfTiles = choice;
            master.notifySinglePlayer(token, new GetTilesResponse(choice));
            master.notifySinglePlayer(token, new MoveOk(true));
        }
        else{
            master.notifySinglePlayer(token, new MoveOk(false));
        }
    }

    public void rearrangeTiles(String token, List<String> order)
    {
        ArrayList<Position> tiles = (ArrayList<Position>) this.choiceOfTiles.clone();

        for(int i=0; i<order.size(); i++)
        {
            tiles.set(i,this.choiceOfTiles.get(order.get(i).charAt(0)-48-1));
        }
        this.choiceOfTiles = tiles;

        master.notifySinglePlayer(token, new GetOrderResponse(tiles, true));
        master.notifySinglePlayer(token, new MoveOk(true));
    }

    public void chooseColumn(String token, int column)
    {
        if(this.choiceOfTiles.size() <= model.getCurrentPlayer().getMyShelf().getMaxFree(column))
        {
            ArrayList<Tile> tiles = new ArrayList<>();
            for(int i=0;i<this.choiceOfTiles.size();i++)
            {
                tiles.add(model.getGameBoard().getCouple(this.choiceOfTiles.get(i)).getTile());
            }
            master.notifySinglePlayer(token, new MoveOk(true));

            updateGame(token,column,tiles);
        }
        else
        {
            master.notifySinglePlayer(token, new MoveOk(false));
        }
    }

    public void updateGame(String token, int column, ArrayList<Tile> tiles){

        insertTilesInShelf(column, tiles);
        checkCGCs();
        updateBoardCouples();

        nextTurn(token);
    }

    public void nextTurn(String token){
        if ( model.getEndGame() != null && model.getEndGame().equals(model.getCurrentPlayer().getNickname()) && model.getPreviousPlayer().getChair() ){
            model.gameHasEnded();
        } else {
            master.notifySinglePlayer(token, new EndTurn());
            model.nextTurn();
        }
    }

    public void checkCGCs(){

        for (CommonGoalCard card : model.getCommonGoalCards() ) {
            if ( card.checkConditions(model.getCurrentPlayer().getMyShelf()) == 1 ) {
                int points = card.getPoints().pop();
                model.getCurrentPlayer().setScore(points);
                master.notifyAllPlayers(gameId, new CommonGoalGained(model.getCurrentPlayer().getNickname(), card.getID()));
                fileLog.info(model.getCurrentPlayer().getNickname() + " has received " + points + " points from CGC " + card.getID());
            }
        }
    }

    public void insertTilesInShelf(int column, ArrayList<Tile> tiles){
        model.getCurrentPlayer().getMyShelf().insertTiles(column,tiles);

        if ( model.getCurrentPlayer().getMyShelf().checkShelfFull() ){
            model.setEndGame(model.getCurrentPlayer().getNickname());
        }
    }
    public void updateBoardCouples(){
        model.getGameBoard().updateCouples(this.choiceOfTiles);
        this.choiceOfTiles = null;

        if ( model.getGameBoard().checkForRefill() ){
            model.getGameBoard().refill();
        }
    }

    public void generateCGC(){
        model.generateCGC(model.getPlayers().size());
    }

    public void startGame(){
        model.startGame();
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
            fileLog.error(e.getMessage());
        }
    }

    public void notifyAllPlayers(ModelUpdate modelUpdate) {
        master.notifyAllPlayers(gameId, modelUpdate);
    }
}
