package it.polimi.ingsw.client;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ClientController {
    private static Logger fileLog = LogManager.getRootLogger();
    private static String HOSTNAME = "ingsw.server.hostname";
    private View currentView;
    private boolean gameOn;
    private int myTurn;
    private String username;
    private CommandParsing commPars;
    private IClientConnection currentConnection;
    private IClientListener listenerClient;
    private ResponseDecoder responseDecoder;
    private Registry registry;
    private IRemoteController remoteController;
    private String userToken;
    private boolean toCLose;
    private boolean onlyOneTile;

    //constructor
    public ClientController(View currentView) {
        this.currentView = currentView;
        this.listenerClient = currentView.getListener();
        //not sure if we need the command parsing anyway
        this.commPars = new CommandParsing(this);
        currentView.setMaster(this, commPars);
        setupConnection();
    }

    public void setupConnection() {
        currentView.chooseConnection();
        //todo uncomment this and place SIP instead of null when initializing connections
        //currentView.askServerIP();
        //String SIP = currentView.getServerIP();
        String connectionStatus = "Connecting...";
        if(currentView.getConnectionType().equals("RMI")) {
            connectionStatus = setupRMI(System.getProperty(HOSTNAME));
        }
        else if(currentView.getConnectionType().equals("SOCKET")){
            connectionStatus = setupSocket(System.getProperty(HOSTNAME));
        }
        else if(connectionStatus!=null){
            this.currentView.displayNotification(connectionStatus);
        }
    }

    public String setupSocket(String serverIP) {
        try {
            //you have to pass 'this' to the client socket
            ClientSocket clientSocket = new ClientSocket(serverIP, 8899, this);
            this.currentConnection = clientSocket;
            clientSocket.setViewClient(currentView);
            this.responseDecoder = new ResponseDecoder(listenerClient, currentConnection);
            clientSocket.setResponseDecoder(responseDecoder);
            clientSocket.startClient();

        } catch (Exception e) {
            fileLog.error(e);
        }
        return null;
    }

    private String setupRMI(String serverIP) {
        try{
            //TODO put serverip in host field of locateregisty
            this.registry = LocateRegistry.getRegistry(serverIP,8089);
            this.remoteController = (IRemoteController) registry.lookup("Login");
            ClientRMI clientRMI = new ClientRMI(this, remoteController);
            this.currentConnection = clientRMI;
            clientRMI.setViewClient(currentView);
            this.responseDecoder = new ResponseDecoder(listenerClient, currentConnection);
            clientRMI.setResponseDecoder(responseDecoder);
            clientRMI.setConnected(true);
            userLogin();

        }catch(Exception e){
            fileLog.error(e.getMessage());
        }
        return null;
    }

    public void userLogin () {
        currentView.getUsername();
    }

    public String getUsername() {
        return username;
    }

    public void chooseTiles(List<String> tilesChosen) {
        currentConnection.chooseTiles(userToken, tilesChosen);
    }

    public boolean isGameOn() {
        return gameOn;
    }

    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
        commPars.setGameIsOn(gameOn);
    }

    public int isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(int turn) {
        this.myTurn = turn;
        commPars.setPlaying(turn);
    }

    public void serverSavedUsername(String name, boolean b, String token, boolean first) {
        if(b){
            setUserToken(token);
            fileLog.debug("i'm about the set the name "+name);
            setUsername(name);
            currentConnection.setUserToken(token);
            commPars.setFirst(first);
            setToCLose(false);
        }
        else{
            userLogin();
        }
    }

    public void setUsername(String username){ this.username = new String(username); }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public boolean isToClose() {
        return toCLose;
    }

    public void setToCLose(boolean toCLose) {
        this.toCLose = toCLose;
    }

    public void numberOfPlayers(int number) {
        if(number < 2 || number > 4){
            currentView.printError("Wrong number of players, please type 'help' for a list of commands");
        }
        else{
            currentConnection.numberOfPlayers(username, userToken, number);
        }
    }

    public void chooseColumn(int column) {
        currentConnection.chooseColumn(column);
    }

    public void close() {
        currentConnection.close();
    }

    public boolean isConnected() {
        return currentConnection.isConnected();
    }

    public void askLogin(String s) {
        currentConnection.login(s);
    }

    public void wrongCommand() {
        currentView.printError("Wrong command, please type 'help' for a list of commands");
    }

    public void isItMyTurn(String name) {
        if(name.equals(username)){
            setMyTurn(1);
        }
        else{
            setMyTurn(0);}
    }

    public void rearrangeTiles(List<String> multipleChoiceNumber) {
        currentConnection.rearrangeTiles(userToken, multipleChoiceNumber);
    }

    public void invalidNotMyTurn() {
        currentView.displayNotification("It's not your turn, yet!");
    }

    public void errorFormat() {
        currentView.printError("Wrong format, please try again or type 'help' for a list of commands");
    }

    public void nextAction(int num, ArrayList<Position> tiles) {
        if(num==2){
            currentView.passTilesToView(tiles);
            setMyTurn(2);
        }
    }

    public void gameNotStarted() {
        currentView.displayNotification("The game has not started yet, please wait for the other players to join");
    }

    public void sendChat(String receiver, String message) {
        currentConnection.sendChat(username, message, receiver);
        //currentView.addToChatQueue(message, receiver);
    }


    public void setOnlyOneTile(boolean b) {
        this.onlyOneTile= b;
    }

    public void pingSyn() {
        currentConnection.setPing(true);
    }

    public void hideShelves() {
        currentView.hideShelves();
    }
    public void showShelves(){currentView.showShelves();}

    public void showCards(){currentView.showCommonGoalCards();}

    public void hideCards() { currentView.hideCommonGoalCards(); }

    public void showCommands() {
        currentView.showCommands();
    }

    public void hideCommands() {
        currentView.hideCommands();
    }

    public void showChat() {
        currentView.showChat();
    }

    public void hideChat() {
        currentView.hideChat();
    }

    public void quit() { currentConnection.quit(userToken); }

}

