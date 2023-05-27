package it.polimi.ingsw.client;

import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.notifications.*;
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
        client.passTiles(getTilesResponse.getTilesChosen());
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
    public void handle(MoveOk moveOk) throws RemoteException {
        clientListener.notifyMoveOk(moveOk);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(GetOrderResponse getOrderResponse) throws RemoteException {
        //todo
    }

    @Override
    public void handle(EndTurn endTurn) throws RemoteException {
        clientListener.notifyEndTurn(endTurn);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(GameEnd gameEnd) throws RemoteException {
        clientListener.notifyGameEnd(gameEnd);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(LastTurn lastTurn) throws RemoteException {
        clientListener.notifyLastTurn(lastTurn);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(CommonGoalGained commonGoalGained) throws RemoteException {
        clientListener.notifyCommonGoalGained(commonGoalGained);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(ChatMessage chatMessage) throws RemoteException {
        clientListener.notifyChatMessage(chatMessage);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(TextNotification textNotification) throws RemoteException {
        clientListener.showTextNotification(textNotification.getMessage());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(ModelUpdateNotification modelUpdateNotification) throws RemoteException {
        clientListener.updateModel(modelUpdateNotification);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }


    public void handle(Response response) {

    }
}
