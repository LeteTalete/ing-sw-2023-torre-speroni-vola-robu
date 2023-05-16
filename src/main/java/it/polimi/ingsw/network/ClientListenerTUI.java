package it.polimi.ingsw.network;

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

    @Override
    public void sendNotification(String message) throws RemoteException {
        if(message.equals(StaticStrings.YOUR_TURN)){
            view.setMyTurn(true);
        }
        if(message.equals(StaticStrings.END_TURN)){
            view.setMyTurn(false);
        }
        view.displayNotification(message);
    }

    public void chooseTiles(String tileScelte) {

    }
}
