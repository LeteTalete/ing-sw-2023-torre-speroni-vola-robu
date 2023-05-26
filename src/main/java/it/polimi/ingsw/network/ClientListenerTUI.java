package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.client.ResponseDecoder;
import it.polimi.ingsw.notifications.CommonGoalGained;
import it.polimi.ingsw.notifications.EndTurn;
import it.polimi.ingsw.notifications.GameEnd;
import it.polimi.ingsw.notifications.LastTurn;
import it.polimi.ingsw.responses.*;
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

    @Override
    public void sendNotification(Response response) throws RemoteException {
        view.detangleMessage(response);
    }

    @Override
    public void sendUpdatedModel(ModelUpdate message) throws RemoteException {
        view.displayUpdatedModel(message);
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


    @Override
    public void notifyMoveOk(MoveOk moveOk) throws RemoteException {
        if(moveOk.isMoveOk()){
            view.nextAction();
            view.displayNotification("Move successful!");
        }
        else{
            view.displayNotification("Invalid move. Try again.");
        }
    }

    @Override
    public void notifyEndTurn(EndTurn endTurn) throws RemoteException {
        view.setMyTurn(false);
        view.displayNotification("Turn ended.");
    }

    @Override
    public void notifyGameEnd(GameEnd gameEnd) throws RemoteException {
        view.setMyTurn(false);
        view.setGameOn(false);
        view.showEndResult();
    }

    @Override
    public void notifyLastTurn(LastTurn lastTurn) throws RemoteException {
        view.displayNotification(lastTurn.getName() + "completed their Shelfie. Last round starts now!");
    }

    @Override
    public void notifyCommonGoalGained(CommonGoalGained commonGoalGained) throws RemoteException {
        view.displayNotification(commonGoalGained.getName() + " gained Common Goal Card " + commonGoalGained.getCard() + "!");
    }

}
