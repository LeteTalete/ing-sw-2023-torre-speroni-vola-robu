package it.polimi.ingsw.view;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.CommandParsing;
import it.polimi.ingsw.model.board.Couple;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.ClientListenerGUI;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.server.StaticStrings;
import it.polimi.ingsw.structures.GameView;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;
import it.polimi.ingsw.view.ControllerGUI.BoardPlayer;
import it.polimi.ingsw.view.ControllerGUI.ConnectionPlayer;
import it.polimi.ingsw.view.ControllerGUI.ErrorMessage;
import it.polimi.ingsw.view.ControllerGUI.GenericController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class ClientGUI implements View {
    private static final Logger fileLog = LogManager.getRootLogger();
    private ClientController master;
    private BoardPlayer boardPlayer;
    private GameView gameView;
    private ClientListenerGUI listenerClient;
    private CommandParsing commPars;
    private String connectionType;
    private boolean isRunning;
    private String ServerIP;
    private String port;
    private LinkedList<String> chatQueue = new LinkedList<>();
    private String username;
    private boolean isStarGame = false;
    private ArrayList<Position> tiles;

    public ClientGUI(){
        try {
            this.listenerClient = new ClientListenerGUI(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        GUIApplication.setClientGUI(this);
    }
    public GameView getGameView(){
        return this.gameView;
    }
    @Override
    public void displayUpdatedModel(ModelUpdate modelUpdate){
        //todo check this
        this.gameView = new GameView(modelUpdate);
        //
        if (!this.isStarGame) {
            //Caricamento partita: settare la board
            this.isStarGame = true;
            GUIApplication.showSceneName(SceneNames.BOARDPLAYER);
            //GUIApplication.behaviorGUI(0);
            //GUIApplication.getBoardPlayer().setBoadPlayer(gameView);
        }
        GUIApplication.updateLivingRoom(); // behaviorGUI(0);
        GUIApplication.updateShelfPlayer(gameView.getPlayersView());
        GUIApplication.updateShelf(gameView);
        GUIApplication.updateScore(gameView);
        turnPhase();
        //GUIApplication.getBoardPlayer().setLivingRoom(gameView.getGameBoardView());
    }

    public void setBoardPlayer(BoardPlayer boardPlayerController){
        boardPlayer = boardPlayerController;
    }
    @Override
    public void chooseConnection() {
        System.out.println("Connessione");//lasciare vuoto
    }

    @Override
    public String getConnectionType() {
        return this.connectionType;
    }
    public void setConnectionType(String typeConnection){
        this.connectionType = typeConnection;
    }
    @Override
    public void getUsername() {
        if(!isRunning){
            GUIApplication.showSceneName(SceneNames.USERNAME);
            isRunning = true;
        }
    }

    @Override
    public void displayNotification(String message) {
        System.out.println("displayNotification: " + message);
    }

    public void setGameOn(boolean gameOn) {
        master.setGameOn(gameOn);
    }

    @Override
    public void askAmountOfPlayers() {
        GUIApplication.showSceneName(SceneNames.NUMPLAYERS);
    }

    @Override
    public void GameTitle() {

    }

    @Override
    public void showShelves(){

    }

    @Override
    public void showChat() {

    }

    @Override
    public void hideChat() {

    }

    @Override
    public void showCommands() {

    }

    @Override
    public void hideCommands() {

    }

    @Override
    public void showCommonGoalCards() {

    }

    @Override
    public void hideCommonGoalCards() {

    }

    @Override
    public void showLivingRoom(LivingRoomView livingRoomView) {

    }

    @Override
    public void showBoardPlayer(PlayerView playerBoardView, LivingRoomView livingRoomView) {

    }

    @Override
    public void showPersonalGoalCard() {

    }

    @Override
    public void showBoard(LivingRoomView livingRoomView) {

    }


    @Override
    public IClientListener getListener() {
        return this.listenerClient;
    }


    @Override
    public void printError(String message) {
        GUIApplication.error(message);
        System.out.println("Errore: " + message);
    }

    @Override
    public void setMyTurn(int b) {
        this.master.setMyTurn(b);
    }

    @Override
    public void startRun() {


    }

    @Override
    public void setMaster(ClientController clientController, CommandParsing commandParsing) {
        this.master = clientController;
        this.commPars = commandParsing;
    }

    public ClientController getMaster(){
        return this.master;
    }
    public CommandParsing getCommPars(){
        return this.commPars;
    }

    @Override
    public void askForTiles() {

    }

    @Override
    public void serverSavedUsername(String name, boolean b, String token, boolean first) {
        master.serverSavedUsername(name, b, token, first);
        if(b){
            this.username = name;
        }
        if (first) {
            askAmountOfPlayers();
        }
    }

    @Override
    public void running() {

    }

    @Override
    public void printCommands() {

    }

    @Override
    public void changeTurn(String name) {
        master.isItMyTurn(name);
    }

    @Override
    public void askServerIP() {

    }

    @Override
    public String getServerIP() {
        return ServerIP;
    }

    @Override
    public void turnPhase(){
        //Devo mostrare sulla GUI di chi è il turno
        switch (master.isMyTurn()) {
            case 0 -> {
                GUIApplication.messaggeForPlayer("It's " + gameView.getCurrentPlayerNickname() + "'s turn");
                displayNotification("It's " + gameView.getCurrentPlayerNickname() + "'s turn");
            }
            case 1 -> {
                GUIApplication.messaggeForPlayer("It's your turn: Choose the tiles"); //GUIApplication.showWhoseIsTurn("It's your turn: Choose the tiles");
                displayNotification(StaticStrings.YOUR_TURN);

                askForTiles();
            }
            case 2 -> {
                GUIApplication.messaggeForPlayer("It's your turn : Re-arrange the tiles or choose the column"); //GUIApplication.showWhoseIsTurn("It's your turn : Re-arrange the tiles or choose the column");
                displayNotification(StaticStrings.YOUR_TURN);
                displayNotification("You can now re-arrange the tiles or choose the column.");
                chooseOrder();
            }
        }
    }

    @Override
    public void chooseColumn() {
        //GUIApplication.updateShelf(gameView);// behaviorGUI(3);

    }

    @Override
    public void chooseOrder() {
        GUIApplication.setOrderTile(); // behaviorGUI(2);
    }

    @Override
    public void nextAction(int num, ArrayList<Position> tiles) {
        master.nextAction(num, tiles);
    }

    @Override
    public void showEndResult() {
        GUIApplication.showSceneName(SceneNames.ENDGAME);
    }


    @Override
    public void addToChatQueue(String message, String receiver) {

    }

    @Override
    public void hideShelves() {

    }

    @Override
    public void passTilesToView(ArrayList<Position> tiles) {
        this.tiles = tiles;
    }

    @Override
    public void passSyn() {
        master.onSyn();
    }

    @Override
    public String getName(){
        return username;
    }

    @Override
    public void askPort() {

    }

    @Override
    public String getPort() {
        return port;
    }

    public void setServerIP(String accessibleText) {
        this.ServerIP = accessibleText;
    }

    public void setPort(String port){
        this.port = port;
    }
}
