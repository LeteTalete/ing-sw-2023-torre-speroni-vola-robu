package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.CommandParsing;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.ClientListenerGUI;
import it.polimi.ingsw.network.ClientListenerTUI;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.stati.Status;
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
    @Override
    public void chooseConnection() {
        System.out.println("FineConnection");
    }

    @Override
    public String getConnectionType() {
        System.out.println("connection type");
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
        System.out.println("NAME:");
    }

    @Override
    public void displayNotification(String message) {
        System.out.println("displayNotification: " + message);
    }

    @Override
    public void GamerStatus(Status current) {

    }

    @Override
    public void askAmountOfPlayers() {
        GUIApplication.showSceneName(SceneNames.NUMPLAYERS);
    }

    @Override
    public void GameTitle() {

    }
    @Override
    public void setBoardStartGame(){

    }

    @Override
    public void showShelves(){

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

    }

    @Override
    public void setMyTurn(boolean b) {

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
        this.username = name;
        master.serverSavedUsername(name, b, token, first);
        if(first){
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
    public void pingSyn() {
        master.pingSyn();
    }

    @Override
    public void addToChatQueue(String message, String receiver) {

    }

    @Override
    public void hideShelves() {

    }


}
