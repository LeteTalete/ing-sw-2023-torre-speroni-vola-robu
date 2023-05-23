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

    //this will become a bunch of sendNotification methods that will resolve different types of messages
    //that way we can implement socket connections
    //we should probably differentiate by looking at the staticstrings: they help the client and server choose on
    //what is to be done after an action
    //todo fix this asap
    @Override
    public String sendNotification(String message) throws RemoteException {
        if(message.equals(StaticStrings.YOUR_TURN)){
            view.setMyTurn(true);
            view.displayNotification(message);
        }
        if(message.equals(StaticStrings.END_TURN)){
            view.setMyTurn(false);
            view.displayNotification(message);
        }
        if(message.equals(StaticStrings.GAME_START)){
            view.setGameOn(true);
            view.displayNotification(message);
        }
        if(message.equals(StaticStrings.OK)){
            view.rearrangeTiles();
        }
        if(message.equals(StaticStrings.COLUMN)){
            view.chooseColumn();
        }
        else{
            view.displayNotification(message);
        }
        //should move this method to choose tiles somewhere else?
        //todo delete this, we need to make a separate thread for reading from input, and we don't have to ask for anything here
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
    public String notifySuccessfulRegistration(String name, boolean b, String token, boolean first) throws RemoteException {
        if(b){
            view.displayNotification("Registration Successful!");
            view.serverSavedUsername(name, true, token, first);
        }
        else{
            view.displayNotification("Registration failed: "+name+" already exists. Try again");
            view.serverSavedUsername(name,false, token, first);
        }
        return name;
    }

    public void chooseTiles(String name, String tileScelte) {

    }
}
