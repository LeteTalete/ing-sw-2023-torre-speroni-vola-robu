package it.polimi.ingsw.view;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.CommandParsing;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.ClientListenerGUI;
import it.polimi.ingsw.network.ClientListenerTUI;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.stati.Status;
import it.polimi.ingsw.structures.GameView;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;
import it.polimi.ingsw.view.ControllerGUI.GenericController;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ClientGUI implements View {
    private ClientController master;
    private GameView gameView;
    private ClientListenerGUI listenerClient;
    private CommandParsing commPars;
    private String connectionType;
    private boolean isRunning;
    private String ServerIP;
    private LinkedList<String> chatQueue = new LinkedList<>();
    private String username;
    private boolean isStarGame = false;
    public ClientGUI(){
        try {
            this.listenerClient = new ClientListenerGUI(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        GUIApplication.clientGUI = this;
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
            this.isStarGame = true;
            GUIApplication.showSceneName(SceneNames.BOARDPLAYER);
            //GUIApplication.setBoardPlayer();
        }
        //GUIApplication.updateLivingRoom(this.gameView.getGameBoardView());
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

    @Override
    public void GamerStatus(Status current) {

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
        System.out.println("Errore: " + message);
    }

    @Override
    public void setMyTurn(int b) {

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
        System.out.println("FUNZIONA");

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

    }

    @Override
    public void askServerIP() {
        
    }

    @Override
    public String getServerIP() {
        return ServerIP;
    }


    @Override
    public void chooseColumn() {
        //todo
    }

    @Override
    public void chooseOrder(ArrayList<Position> tilesPosition) {
        //todo
    }

    @Override
    public void nextAction(int num, ArrayList<Position> tiles) {

    }

    @Override
    public void showEndResult() {
        //todo
    }


    @Override
    public void addToChatQueue(String message, String receiver) {

    }

    @Override
    public void hideShelves() {

    }

    @Override
    public void passTilesToView(ArrayList<Position> tiles) {

    }

    @Override
    public void passSyn() {
        master.onSyn();
    }


}
