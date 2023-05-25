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

    //todo these tiles need to be passed to the client's view to help the user visualize what's going on
    @Override
    public void handle(GetTilesResponse getTilesResponse) throws RemoteException {
        //clientListener.notifyTilesResponse(getTilesResponse);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
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
    public void handle(ChooseColumnResponse chooseColumnResponse) throws RemoteException {
        clientListener.notifyChooseColumnResponse(chooseColumnResponse);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(MoveOk moveOk) throws RemoteException {
        clientListener.notifyMoveOk(moveOk);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }


    public void handle(Response response) {

    }
}
