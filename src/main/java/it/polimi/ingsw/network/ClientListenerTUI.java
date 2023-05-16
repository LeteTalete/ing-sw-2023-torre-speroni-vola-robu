package it.polimi.ingsw.network;

import it.polimi.ingsw.view.View;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientListenerTUI extends UnicastRemoteObject implements IClientListener {
    private transient final View view;

    public ClientListenerTUI(View currentView) throws RemoteException{
        this.view = currentView;
    }

    @Override
    public int askHowMany() throws RemoteException {
        return view.askAmountOfPlayers();
    }

    @Override
    public void sendNotification(String message) throws RemoteException {
        view.displayNotification(message);
    }

}
