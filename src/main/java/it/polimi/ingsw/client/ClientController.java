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
    private IClientConnection currentConnection;
    private IClientListener listenerClient;
    private Registry registry;
    private IRemoteController remoteController;

    //constructor
    public ClientController(View currentView) {
        this.currentView = currentView;
        this.listenerClient = currentView.getListener();
        this.username = new String();
        currentView.setMaster(this);
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
        if(connectionStatus!=null){
            this.currentView.displayNotification(connectionStatus);
        }
    }

    public String setupSocket()
    {
        ClientSocket client = new ClientSocket("127.0.0.1",1420);
        try
        {
            client.startClient();
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
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
            String LoginSuccess = userLogin();
            if(LoginSuccess.equals(StaticStrings.LOGIN_OK_NEW_ROOM)){
                System.out.println(StaticStrings.LOGIN_OK_NEW_ROOM);
            }
        }catch(Exception e){
            return e.toString();
        }
        return null;
    }

    public String userLogin () {
        String name = currentView.getUsername();
        String serverResponse = currentConnection.login(name);
        //if there is a waiting room, the user has to be put there. if there is not, it will ask the player to
        //choose how many players they want for the game, but this will all be done by the server
        while(serverResponse.equals(StaticStrings.LOGIN_KO)){
            //if the username already exists, it will ask for a new one
            System.out.println("The username already exist!");
            name = currentView.getUsername();
            serverResponse = currentConnection.login(name);
        }
        setUsername(name);
        currentConnection.setName(name);
        return serverResponse;
    }

    public String getUsername() {
        return username;
    }


    public void chooseTiles(String tilesChosen) {
        System.out.println("correct "+ username);

        currentConnection.chooseTiles(username, tilesChosen);
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

    public void setUsername(String username) {
        this.username = username;
    }
}
