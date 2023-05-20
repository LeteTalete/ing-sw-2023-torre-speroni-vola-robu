package it.polimi.ingsw.client;

import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.server.StaticStrings;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ClientController {
    private View currentView;
    private boolean gameOn;
    private boolean myTurn;
    private String username;
    private CommandParsing commPars;
    private IClientConnection currentConnection;
    private IClientListener listenerClient;
    private Registry registry;
    private IRemoteController remoteController;
    private String userToken;

    //constructor
    public ClientController(View currentView) {
        this.currentView = currentView;
        this.listenerClient = currentView.getListener();
        this.username = new String();
        //not sure if we need the command parsing anyway
        this.commPars = new CommandParsing();
        currentView.setMaster(this, commPars);
        setupConnection();
    }

    public void setupConnection() {
        currentView.chooseConnection();
        String connectionStatus = "Connecting...";
        if(currentView.getConnectionType().equals("RMI")) {
            connectionStatus = setupRMI();
        }
        else if(currentView.getConnectionType().equals("SOCKET")){
            connectionStatus = setupSocket();
        }
        else if(connectionStatus!=null){
            this.currentView.displayNotification(connectionStatus);
        }
    }

    public String setupSocket()
    {
        try
        {
            //you have to pass 'this' to the client socket
            ClientSocket clientSocket = new ClientSocket("127.0.0.1",1420);
            this.currentConnection = clientSocket;
            clientSocket.setViewClient(currentView);
            clientSocket.startClient();
            userLogin();
            //deleted the if clause to check the login response, since the server should already notify the users about it
        }
        catch (Exception e)
        {
            return e.toString();
        }

        return null;
    }

    private String setupRMI() {
        try{
            this.registry = LocateRegistry.getRegistry(8089);
            this.remoteController = (IRemoteController) registry.lookup("Login");
            ClientRMI clientRMI = new ClientRMI(this, remoteController);
            this.currentConnection = clientRMI;
            clientRMI.setViewClient(currentView);
            userLogin();

        }catch(Exception e){
            return e.toString();
        }
        return null;
    }

    public void userLogin () {
        String name = currentView.getUsername();
        currentConnection.login(name);
    }

    public String getUsername() {
        return username;
    }


    public void chooseTiles(String tilesChosen) {
        currentConnection.chooseTiles(userToken, tilesChosen);
    }

    public boolean isGameOn() {
        return gameOn;
    }

    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public void serverSavedUsername(boolean b, String token) {
        if(b){
            setUserToken(token);
            currentConnection.setUserToken(token);
        }
        else{
            userLogin();
        }
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
