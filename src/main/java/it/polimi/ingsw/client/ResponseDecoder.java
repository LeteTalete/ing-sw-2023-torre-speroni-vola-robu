package it.polimi.ingsw.client;

import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.notifications.DisconnectionNotif;
import it.polimi.ingsw.notifications.GameStart;
import it.polimi.ingsw.notifications.NotifyOnTurn;
import it.polimi.ingsw.responses.*;

import java.rmi.RemoteException;

public class ResponseDecoder implements ResponseHandler {
    private final IClientListener clientListener;
    private final IClientConnection client;

    public ResponseDecoder(IClientListener clientListener, IClientConnection clientC) {
        this.clientListener = clientListener;
        this.client = clientC;
    }

    @Override
    public void handle(GetTilesResponse getTilesResponse) throws RemoteException {
    //todo
    }

    @Override
    public void handle(Pinged pinged) throws RemoteException {

    }

    @Override
    public void handle(LoginResponse loginResponse) throws RemoteException {
        clientListener.notifySuccessfulRegistration(loginResponse);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(GameStart gameStart) throws RemoteException {
        clientListener.setGameOn();
        synchronized (client){
            client.notifyAll();
        }
    }

    @Override
    public void handle(NotifyOnTurn notifyOnTurn) throws RemoteException {
        clientListener.changeTurn(notifyOnTurn.getCurrentPlayer());
        synchronized (client){
            client.notifyAll();
        }
    }

    @Override
    public void handle(DisconnectionNotif disconnectionNotif) throws RemoteException {

    }

    @Override
    public void handle(GetOrderResponse getOrderResponse) throws RemoteException {
        //todo
    }


    public void handle(Response response) {

    }
}
