package it.polimi.ingsw.controller;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.cards.CommonGoalCard;
import it.polimi.ingsw.server.ServerManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {
    private static Logger fileLog = LogManager.getRootLogger();
    private Game model;
    private String gameId;
    private ArrayList<Position> choiceOfTiles;
    private ServerManager master;
    private Map<Integer,Integer> cardsClaimed;

    /**
     * Constructor GameController creates a new GameController instance and initializes the game model.
     * @param playersList - list of players that are playing the game.
     * @param id - id of the game.
     * @param serverMaster - serverManager that manages the server.
     */
    public GameController(ArrayList<Player> playersList, String id, ServerManager serverMaster) {
        model = new Game(id, this);
        model.setPlayers(playersList);
        model.setNumOfPlayers(playersList.size());
        gameId = id;
        master = serverMaster;
        cardsClaimed = new HashMap<>();
    }

    /**
     * Method chooseTiles saves the choice of tiles made by the player if it's valid and notifies the player, otherwise
     * it doesn't save the choice and notifies the player that the choice is invalid.
     * @param token - token that identifies the player.
     * @param userInput - list of strings that represent the positions of the tiles chosen by the player.
     */
    public void chooseTiles(String token, List<String> userInput)
    {
        ArrayList<Position> choice = new ArrayList<>();
        boolean valid = true;

        //userInput: [5,4] [1,1] [6,4]
        //extracting positions from the input
        for(String sub : userInput)
        {
            String[] pos = sub.split(","); //[5] [4]
            Position p = new Position(Integer.parseInt(pos[0]),Integer.parseInt(pos[1])); //(5,4)
            choice.add(p);
            if(p.getX()>=model.getGameBoard().getBoard().length || p.getY()>=model.getGameBoard().getBoard()[0].length) valid = false;
        }

        if (valid && model.getCurrentPlayer().getMyShelf().checkEnoughSpace(choice) && this.model.getGameBoard().checkPlayerChoice(choice))
        {
            this.choiceOfTiles = choice;
            master.notifyAboutTiles(token, 0, choice);
        }
        else
        {
            if(!model.getCurrentPlayer().getMyShelf().checkEnoughSpace(choice)){
                master.notifyAboutTiles(token, 4, choice);
            }
            else master.notifyAboutTiles(token, model.getGameBoard().getErrorTilesCode(), choice);
        }
    }

    /**
     * Method rearrangeTiles saves the rearrangement of the tiles made by the player if it's valid and notifies the player,
     * otherwise it doesn't save the rearrangement and notifies the player that the rearrangement is invalid.
     * @param token - token that identifies the player.
     * @param order - list of strings that represent the new order of the tiles chosen by the player.
     */
    public void rearrangeTiles(String token, List<String> order)
    {
        boolean valid = true;

        if(this.choiceOfTiles == null || this.choiceOfTiles.size() != order.size()) valid = false;

        //checking for admissible values
        for(int i=0;i<order.size() && valid;i++)
        {
            if(Integer.parseInt(order.get(i))>this.choiceOfTiles.size()) valid = false;
        }

        //checking for duplicates
        for(int i=0; i< order.size()-1 && valid;i++)
        {
            for(int j=i+1; j< order.size() && valid;j++)
            {
                if(order.get(i).equals(order.get(j))) valid = false;
            }
        }

        if(valid)
        {
            ArrayList<Position> tiles = new ArrayList<>(this.choiceOfTiles);

            for(int i=0; i<order.size(); i++)
            {
                tiles.set(i,this.choiceOfTiles.get(order.get(i).charAt(0)-48-1));
            }
            this.choiceOfTiles = tiles;
            master.notifyAboutRearrange(token, true, tiles);
        }
        else
        {
            master.notifyAboutRearrange(token, false, null);
        }
    }

    /**
     * Method chooseColumn inserts the tiles chosen by the player in the column chosen by the player if it's valid and
     * notifies the player, otherwise it doesn't insert the tiles and notifies the player that the choice is invalid.
     * If the choice is valid it also triggers the update of the game.
     * @param token - token that identifies the player.
     * @param column - integer that represents the column chosen by the player.
     */
    public void chooseColumn(String token, int column) {
        if(this.choiceOfTiles != null && this.choiceOfTiles.size() <= model.getCurrentPlayer().getMyShelf().getMaxFree(column)) {

            ArrayList<Tile> tiles = new ArrayList<>();

            for(int i=0;i<this.choiceOfTiles.size();i++) {
                tiles.add(model.getGameBoard().getCouple(this.choiceOfTiles.get(i)).getTile());
            }
            model.insertTiles(column, tiles);

            fileLog.debug("i'm in choose column and i'm about to notify player: "+token+" about the move ok");

            updateGame();
            master.notifyAboutColumn(token, true);
            master.notifyOnEndTurn(token);
        }
        else {
            master.notifyAboutColumn(token, false);
        }
    }

    /** Method updateGame updates the game by checking the CGCs, updating the board couples and changing the current player. */
    public void updateGame(){
        checkCGCs();
        updateBoardCouples();

        nextTurn();
        send();
    }

    /** Method nextTurn changes the current player and if the game has ended it notifies the players. */
    public void nextTurn(){
        if ( model.getEndGame() != null && model.getPreviousPlayer().getChair() ){
            model.gameHasEnded();
        } else {
            model.nextTurn();
        }
    }

    /**
     * Method checkCGCs checks if the current player has completed any CGC and if so it gives him the points, then it
     * notifies all players.
     */
    public void checkCGCs(){

        int i = 0;
        for (CommonGoalCard card : model.getCommonGoalCards() ) {
            i++;
            if ( card.checkConditions(model.getCurrentPlayer().getMyShelf()) == 1 ) {
                int points = card.getPoints().pop();
                model.getCurrentPlayer().setScore(points);
                cardsClaimed.put(i, points);
                fileLog.info(model.getCurrentPlayer().getNickname() + " has received " + points + " points from CGC " + card.getID());
            }
        }
    }

    public void send(){
        if ( cardsClaimed.size() > 0 ) {
            for (Integer key : cardsClaimed.keySet()) {
                master.notifyOnCGC(gameId, model.getPreviousPlayer().getNickname(), key, cardsClaimed.get(key));
            }
        }
        cardsClaimed.clear();
    }

    /**
     * Method updateBoardCouples updates the couples on the board by removing the tiles that have been chosen by the player.
     * It then checks if the board needs to be refilled and if so it refills it.
     */
    public void updateBoardCouples(){
        model.getGameBoard().updateCouples(this.choiceOfTiles);
        this.choiceOfTiles = null;

        if ( model.getGameBoard().checkForRefill() ){
            model.getGameBoard().refill();
        }
    }

    /**
     * Method getModel returns the model of the game.
     * @return - model of the game.
     */
    public Game getModel(){
        return model;
    }

    /**
     * Method notifyOnStartTurn notifies the players that it's the turn of the current player.
     * @param currentPlayer - nickname of the current player.
     */
    public void notifyOnStartTurn(String currentPlayer) {
        master.notifyOnStartTurn(gameId, currentPlayer);
    }

    /**
     * Method notifyOnModelUpdate notifies the players that the model has been updated.
     * @param modelUpdate - updated model.
     */
    public void notifyOnModelUpdate(ModelUpdate modelUpdate) {
        master.notifyAllPlayers(gameId, modelUpdate);
    }

    /** Method notifyOnEndTurn notifies the players that the last turn has ended. */
    public void notifyOnGameEnd() {
        master.notifyOnEndGame(gameId);
    }

    /**
     * Method notifyOnLastTurn notifies the players that the last turn has started.
     * @param nickname - nickname of the player that has completed their shelf.
     */
    public void notifyOnLastTurn(String nickname) {
        master.notifyOnLastTurn(gameId, nickname);
    }

    /**
     * Method setChoiceOfTiles is used only for testing.
     * @param choiceOfTiles - tiles chosen by the player.
     */
    public void setChoiceOfTiles(ArrayList<Position> choiceOfTiles)
    {
        this.choiceOfTiles = choiceOfTiles;
    }

    /**
     * Method getChoiceOfTiles is used only for testing.
     * @return - tiles chosen by the player.
     */
    public ArrayList<Position> getChoiceOfTiles()
    {
        return this.choiceOfTiles;
    }
}
