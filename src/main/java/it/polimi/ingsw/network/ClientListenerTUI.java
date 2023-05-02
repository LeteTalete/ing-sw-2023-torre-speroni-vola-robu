package it.polimi.ingsw.network;

import it.polimi.ingsw.view.View;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientListenerTUI extends UnicastRemoteObject implements IListener {
    private transient final View view;

    public ClientListenerTUI(View currentView) throws RemoteException{
        this.view = currentView;
    }

    @Override
    public void processLogin(String result) throws RemoteException {
        System.out.println("processLogin: " + result);
        //method to notify the view of the result
    }

    @Override
    public int askHowMany() throws RemoteException {
        return view.askAmountOfPlayers();
    }

    @Override
    public void notification(String gameStart) {
        view.displayNotification(gameStart);
    }
}
