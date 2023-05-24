package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.client.ResponseDecoder;
import it.polimi.ingsw.responses.LoginResponse;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.server.StaticStrings;
import it.polimi.ingsw.view.ClientTUI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientListenerTUI extends UnicastRemoteObject implements IClientListener {
    private transient final ClientTUI view;
    private ResponseDecoder responseDecoder;

    public ClientListenerTUI(ClientTUI currentView) throws RemoteException{
        this.view = currentView;
    }

    //this will become a bunch of sendNotification methods that will resolve different types of messages
    //that way we can implement socket connections
    //we should probably differentiate by looking at the staticstrings: they help the client and server choose on
    //what is to be done after an action
    //todo fix this asap: instead of a list of if clauses, we can display the notification and have the server call the right
    //method immediately
    @Override
    public void sendNotification(Response response) throws RemoteException {
        view.detangleMessage(response);
        /*if(message.equals(StaticStrings.END_TURN)){
            view.setMyTurn(false);
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
        */
    }

    @Override
    public void sendUpdatedModel(ModelUpdate message) throws RemoteException {
        //view.updateModel???
    }

    @Override
    public void notifySuccessfulRegistration(LoginResponse loginResponse) throws RemoteException {
        if(loginResponse.b){
            view.displayNotification("Registration Successful!");
            view.serverSavedUsername(loginResponse.name, true, loginResponse.token, loginResponse.first);
        }
        else{
            view.displayNotification("Registration failed: "+loginResponse.name+" already exists. Try again");
            view.serverSavedUsername(loginResponse.name, false, loginResponse.token, loginResponse.first);
        }
    }

    @Override
    public void setClientTurn() throws RemoteException{
        view.setMyTurn(true);
    }

    @Override
    public void setGameOn() throws RemoteException {
        view.writeText(StaticStrings.GAME_START);
        view.setGameOn(true);
    }

    @Override
    public void changeTurn(String name) throws RemoteException {
        view.changeTurn(name);
    }

    @Override
    public void showTextNotification(String waitingRoomCreated) {
        view.displayNotification(waitingRoomCreated);
    }

    public void chooseTiles(String name, String tileScelte) {

    }

}
