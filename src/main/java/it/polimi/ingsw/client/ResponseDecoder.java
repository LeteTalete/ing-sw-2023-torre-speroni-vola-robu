package it.polimi.ingsw.client;

import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.notifications.*;
import it.polimi.ingsw.responses.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;

public class ResponseDecoder implements ResponseHandler {
    private static final Logger fileLog = LogManager.getRootLogger();

    private final IClientListener clientListener;
    private final IClientConnection client;

    /**respondeDecoder constructor.
     * @param clientC - used to invoke the methods of the clientController.
     * @param clientListener - used to invoke the methods of the clientListener*/
    public ResponseDecoder(IClientListener clientListener, IClientConnection clientC) {
        this.clientListener = clientListener;
        this.client = clientC;
    }


    /**handle method to handle the pings from the server*/
    @Override
    public void handle(Pinged pinged) throws RemoteException {
        try{
            clientListener.sendPingSyn();
        }catch (RemoteException e){
            fileLog.error(e.getMessage());
        }
    }

    /**handle method to get the login response and notify the client about it. After that, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(LoginResponse loginResponse) throws RemoteException {
        clientListener.notifySuccessfulRegistration(loginResponse.name, loginResponse.b, loginResponse.token, loginResponse.first);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    /**method to handle the gameStart notification. After that, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(GameStart gameStart) throws RemoteException {
        clientListener.setGameOn();
        client.setReceivedResponse(false);
        synchronized (client){
            client.notifyAll();
        }
    }

    /**method to handle the notification of the start of a new turn. After that, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(NotifyOnTurn notifyOnTurn) throws RemoteException {
        clientListener.changeTurn(notifyOnTurn.getCurrentPlayer());
        client.setReceivedResponse(false);
        synchronized (client){
            client.notifyAll();
        }
    }

    /**method used to handle the disconnection notification from the server (i.e. when a client has
     * disconnected and the game needs to be closed). After that, it sets the boolean of the controller
     * setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(DisconnectionNotif disconnectionNotif) throws RemoteException {
        clientListener.notifyAboutDisconnection(disconnectionNotif.getName());
        client.setReceivedResponse(false);
        synchronized (client){
            client.notifyAll();
        }
    }

    /**method used to handle the response from the server after choosing the column. After that, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(ColumnOk moveOk) throws RemoteException {
        clientListener.notifyColumnOk(moveOk.isMoveOk());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }


    /**method used to notify the end of the turn. After that, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(EndTurn endTurn) throws RemoteException {
        clientListener.notifyEndTurn();
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    /**method used to handle the notification of a game ending. After that, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(GameEnd gameEnd) throws RemoteException {
        clientListener.notifyEndGame();
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    /**method used to handle the notification of the start of the last turn. After that, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(LastTurn lastTurn) throws RemoteException {
        clientListener.notifyLastTurn(lastTurn.getName());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    /**method used to handle the notification of a common goal card gained by a player. After that, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(CommonGoalGained commonGoalGained) throws RemoteException {
        clientListener.notifyOnCGC(commonGoalGained.getName(), commonGoalGained.getCard());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    /**method used to handle an incoming chat message. After notifying about the message, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(ChatMessage chatMessage) throws RemoteException {
        clientListener.notifyChatMessage(chatMessage.getSender(), chatMessage.getMessage(), chatMessage.getReceiver());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    /**method used to handle a textNotification from the server. After that, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(TextNotification textNotification) throws RemoteException {
        clientListener.showTextNotification(textNotification.getMessage());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    /**method used to handle a model update. It calls an update of the model. After that, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(ModelUpdateNotification modelUpdateNotification) throws RemoteException {
        clientListener.updateModel(modelUpdateNotification.getUpdate());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    /**method used to handle a response from the server about a tiles re-arrange. After that, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(RearrangeOk rearrangeOk) throws RemoteException {
        clientListener.notifyRearrangeOk(rearrangeOk.isMoveOk(), rearrangeOk.getTiles());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

    /**method used to handle a response from the server about the choice of tiles. After that, it sets the boolean
     * of the controller setReceivedResponse to true, and it notifies all the waiting threads to unlock them.*/
    @Override
    public void handle(TilesOk tilesOk) throws RemoteException {
        clientListener.notifyTilesOk(tilesOk.isMoveOk(), tilesOk.getTiles());
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }


    public void handle(Response response) {
        //not used
    }
}
