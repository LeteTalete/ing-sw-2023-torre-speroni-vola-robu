package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.server.StaticStrings;
import it.polimi.ingsw.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class ClientController {
    private static Logger fileLog = LogManager.getRootLogger();

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

    //constructor
    public ClientController(View currentView) {
        this.currentView = currentView;
        this.listenerClient = currentView.getListener();
        this.username = new String();
        //not sure if we need the command parsing anyway
        this.commPars = new CommandParsing(this);
        currentView.setMaster(this, commPars);
        setupConnection();
    }

    public void setupConnection() {
        currentView.chooseConnection();
        //todo uncomment this and place SIP in stead of null when initializing connections
        //currentView.askServerIP();
        //String SIP = currentView.getServerIP();
        String connectionStatus = "Connecting...";
        if(currentView.getConnectionType().equals("RMI")) {
            connectionStatus = setupRMI(null);
        }
        else if(currentView.getConnectionType().equals("SOCKET")){
            connectionStatus = setupSocket(null);
        }
        else if(connectionStatus!=null){
            this.currentView.displayNotification(connectionStatus);
        }
    }

    public String setupSocket(String serverIP) {
        try {
            //you have to pass 'this' to the client socket
            ClientSocket clientSocket = new ClientSocket("127.0.0.1", 1420, this);
            this.currentConnection = clientSocket;
            clientSocket.setViewClient(currentView);
            this.responseDecoder = new ResponseDecoder(listenerClient, currentConnection);
            clientSocket.setResponseDecoder(responseDecoder);
            clientSocket.startClient();

            //deleted the if clause to check the login response, since the server should already notify the users about it
        } catch (Exception e) {
            fileLog.error(e.getMessage());
        }
        return null;
    }

    private String setupRMI(String serverIP) {
        try{
            //TODO put serverip in host field of locateregisty
            this.registry = LocateRegistry.getRegistry(8089);
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

    public void setMyTurn(boolean turn) {
        if(turn)
            this.myTurn = 1;
        else
            this.myTurn = 0;
        commPars.setPlaying(turn);
    }

    public void serverSavedUsername(String name, boolean b, String token) {
        if(b){
            setUserToken(token);
            setUsername(name);
            currentConnection.setUserToken(token);
        }
        else{
            userLogin();
        }
    }

    public void setUsername(String username){ this.username = username; }

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

    public void wake() {
        currentConnection.setSynCheckTimer(true);
    }

    public void close() {
        currentConnection.close();
    }

    public IClientConnection getCurrentConnection() {
        return currentConnection;
    }

    public void detangleMessage(Response response) throws RemoteException {
        response.handleResponse(responseDecoder);
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

    public void printCommands() {
        currentView.printCommands();
    }

    public void isItMyTurn(String name) {
        if(name.equals(username)){
            setMyTurn(true);
            currentView.displayNotification(StaticStrings.YOUR_TURN);
            currentView.askForTiles();
        }
        else{
            setMyTurn(false);
            currentView.displayNotification("It's " + name + "'s turn");
        }
    }

    public void rearrangeTiles(List<String> multipleChoiceNumber) {
        currentConnection.rearrangeTiles(userToken, multipleChoiceNumber);
    }

    public void invalidNotMyTurn() {
        currentView.displayNotification("It's not your turn, yet!");
    }

    public void passTiles(ArrayList<Position> tilesChosen) {
        if(tilesChosen.size()==1){
            myTurn = 3;
            currentView.chooseColumn();
        }
        else{
            myTurn = 2;
            currentView.displayNotification("You can now re-arrange the tiles or choose the column. Here are the commands:");
            //todo it should show commands format, not show the request
            currentView.chooseColumn();
            currentView.chooseOrder();
            //todo shows commands for these two actions
        }
    }

    public void errorFormat() {
        currentView.printError("Wrong format, please try again or type 'help' for a list of commands");
    }
}
