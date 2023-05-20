package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.server.StaticStrings;
import it.polimi.ingsw.view.ClientTUI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientListenerTUI extends UnicastRemoteObject implements IClientListener {
    private transient final ClientTUI view;

    public ClientListenerTUI(ClientTUI currentView) throws RemoteException{
        this.view = currentView;
    }

    @Override
    public int askHowMany() throws RemoteException {
        return view.askAmountOfPlayers();
    }

    //this will become a bunch of sendNotification methods that will resolve different types of messages
    //that way we can implement socket connections
    //we should probably differentiate by looking at the staticstrings: they help the client and server choose on
    //what is to be done after an action
    @Override
    public String sendNotification(String message) throws RemoteException {
        if(message.equals(StaticStrings.YOUR_TURN)){
            view.setMyTurn(true);
        }
        if(message.equals(StaticStrings.END_TURN)){
            view.setMyTurn(false);
        }
        if(message.equals(StaticStrings.GAME_START)){
            view.setGameOn(true);
        }
        view.displayNotification(message);
        //should move this method to choose tiles somewhere else?
        if(view.getMyTurn()){
            view.askForTiles();
        }
        return message;
    }

    @Override
    public void sendUpdatedModel(ModelUpdate message) throws RemoteException {
        //view.updateModel???
    }

    @Override
    public String notifySuccessfulRegistration(boolean b, String name) throws RemoteException {
        if(b==true){
            view.displayNotification("Registration Successful!");
            view.serverSavedUsername(true, name);
        }
        else{
            view.displayNotification("Registration failed: "+name+" already exists. Try again");
            view.serverSavedUsername(false, name);
        }
        return name;
    }

    public void chooseTiles(String name, String tileScelte) {

    }
}
