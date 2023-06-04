package it.polimi.ingsw.client;

import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.notifications.*;
import it.polimi.ingsw.responses.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;

public class ResponseDecoder implements ResponseHandler {
    private static Logger fileLog = LogManager.getRootLogger();

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
        try{
            clientListener.sendPingSyn();
        }catch (RemoteException e){
            fileLog.error(e.getMessage());
        }
    }

    @Override
    public void handle(LoginResponse loginResponse) throws RemoteException {
        clientListener.notifySuccessfulRegistration(loginResponse.name, loginResponse.b, loginResponse.token, loginResponse.first);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(GameStart gameStart) throws RemoteException {
        clientListener.setGameOn();
        client.setReceivedResponse(false);
        synchronized (client){
            client.notifyAll();
        }
    }

    @Override
    public void handle(NotifyOnTurn notifyOnTurn) throws RemoteException {
        clientListener.changeTurn(notifyOnTurn.getCurrentPlayer());
        client.setReceivedResponse(false);
        synchronized (client){
            client.notifyAll();
        }
    }

    @Override
    public void handle(DisconnectionNotif disconnectionNotif) throws RemoteException {
        clientListener.notifyAboutDisconnection(disconnectionNotif.getName());
        client.setReceivedResponse(false);
        synchronized (client){
            client.notifyAll();
        }
    }


    @Override
    public void handle(ColumnOk moveOk) throws RemoteException {
        clientListener.notifyColumnOk(moveOk.isMoveOk());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }


    @Override
    public void handle(EndTurn endTurn) throws RemoteException {
        clientListener.notifyEndTurn();
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(GameEnd gameEnd) throws RemoteException {
        clientListener.notifyEndGame();
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(LastTurn lastTurn) throws RemoteException {
        clientListener.notifyLastTurn(lastTurn.getName());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(CommonGoalGained commonGoalGained) throws RemoteException {
        clientListener.notifyOnCGC(commonGoalGained.getName(), commonGoalGained.getCard());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(ChatMessage chatMessage) throws RemoteException {
        clientListener.notifyChatMessage(chatMessage.getSender(), chatMessage.getMessage(), chatMessage.getReceiver());
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
        clientListener.updateModel(modelUpdateNotification.getUpdate());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(RearrangeOk rearrangeOk) throws RemoteException {
        clientListener.notifyRearrangeOk(rearrangeOk.isMoveOk());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    @Override
    public void handle(TilesOk tilesOk) throws RemoteException {
        clientListener.notifyTilesOk(tilesOk.isMoveOk());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }


    public void handle(Response response) {
        //not used
    }
}