package it.polimi.ingsw.client;

import it.polimi.ingsw.view.View;

import java.io.IOException;


public class ClientHandler {
    private View currentView;
    private ConnectionHandler currentConnection;
    //constructor
    public ClientHandler(View currentView) {
        this.currentView = currentView;
        setupConnection();
    }
    public void setupConnection() {
        currentView.chooseConnection();
        if(currentView.getConnectionType().equals("RMI")) {
            this.currentConnection = new RMIConnectionHandler();
        }
        else if(currentView.getConnectionType().equals("SOCKET")){
            //add socket conneciton
        }
        String connectionStatus = this.currentConnection.setupConnection();
        if(connectionStatus==null){
            this.currentView.displayNotification(userLogin());
        }
        else{
            this.currentView.displayNotification(connectionStatus);
        }
    }

    public int askHowMany(){
        return currentView.askAmountOfPlayers();
    }

    public String userLogin () {
        String name = currentView.getUsername();
        String serverResponse = currentConnection.Login(name);
        //if there is a waiting room, the user has to be put there. if there is not, it will ask the player to
        //choose how many players they want for the game
        if(serverResponse.equals("Already exists")){
            //if the username already exists, it will ask for a new one
            System.out.println("The username already exist!");
            return userLogin();
        }
        else if(serverResponse.equals("Creating new")) {
            //ask the player how many players to wait
            int n = askHowMany();
            serverResponse = currentConnection.howMany(n, name);
            return serverResponse;
        }
        return serverResponse;
    }

}
