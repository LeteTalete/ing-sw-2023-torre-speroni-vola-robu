package it.polimi.ingsw.client;

import it.polimi.ingsw.network.IListener;
import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.server.StaticStrings;
import it.polimi.ingsw.view.View;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ClientController {
    private View currentView;
    private String username;
    private ClientRMI currentConnection;
    private IListener listenerClient;
    private Registry registry;
    private IRemoteController remoteController;

    //constructor
    public ClientController(View currentView) {
        this.currentView = currentView;
        this.listenerClient = currentView.getListener();
        setupConnection();
    }

    public void setupConnection() {
        currentView.chooseConnection();
        String connectionStatus = "Connecting...";
        if(currentView.getConnectionType().equals("RMI")) {
            connectionStatus = setupRMI();
        }
        else if(currentView.getConnectionType().equals("SOCKET")){
            //add socket connection
        }
        if(connectionStatus!=null){
            this.currentView.displayNotification(connectionStatus);
        }
    }

    private String setupRMI() {
        try{
            this.registry = LocateRegistry.getRegistry(8089);
            this.remoteController = (IRemoteController) registry.lookup("Login");
            ClientRMI clientRMI = new ClientRMI(this, remoteController);
            this.currentConnection = clientRMI;
            clientRMI.setViewClient(currentView);
            String LoginSuccess = userLogin();
            System.out.println(LoginSuccess);
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
        this.username = name;
        return serverResponse;
    }

    public String getUsername() {
        return username;
    }


}
