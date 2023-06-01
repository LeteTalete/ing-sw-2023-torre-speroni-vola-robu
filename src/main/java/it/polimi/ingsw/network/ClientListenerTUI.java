package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.client.ResponseDecoder;
import it.polimi.ingsw.notifications.*;
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

   //this is wrong todo needs to be deleted
    @Override
    public void sendNotification(Response response) throws RemoteException {
        view.detangleMessage(response);
    }

    @Override
    public void sendUpdatedModel(ModelUpdate message) throws RemoteException {
        view.displayUpdatedModel(message);
    }

    @Override
    public void notifySuccessfulRegistration(String name, boolean b, String token, boolean first) throws RemoteException {
        if(b){
            view.displayNotification("Registration Successful!");
            view.serverSavedUsername(name, true, token, first);
        }
        else{
            view.displayNotification("Registration failed: "+name+" already exists. Try again");
            view.serverSavedUsername(name, false, token, first);
        }
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
    public void showTextNotification(String message) throws RemoteException {
        view.displayNotification(message);
    }


    @Override
    public void notifyColumnOk(boolean ok) throws RemoteException {
        if(ok){
            view.displayNotification("Choice of column successful!");
        }
        else{
            view.displayNotification("Invalid move. Try again.");
        }
    }

    @Override
    public void notifyEndTurn() throws RemoteException {
        view.setMyTurn(false);
        view.displayNotification("Turn ended.");
    }

    @Override
    public void notifyLastTurn(String firstDoneUser) throws RemoteException {
        view.displayNotification(firstDoneUser + "completed their Shelfie. Last round starts now!");
    }

    @Override
    public void notifyCommonGoalGained(CommonGoalGained commonGoalGained) throws RemoteException {
        view.displayNotification(commonGoalGained.getName() + " gained Common Goal Card " + commonGoalGained.getCard() + "!");
    }

    @Override
    public void notifyChatMessage(String sender, String message) throws RemoteException {
        view.displayChatNotification("@" + sender + ": " + message);
    }

    @Override
    public void updateModel(ModelUpdateNotification modelUpdateNotification) throws RemoteException {
        view.displayUpdatedModel(modelUpdateNotification.getUpdate());
    }

    @Override
    public void notifyRearrangeOk(boolean ok) throws RemoteException {
        if(ok){
            view.nextAction(3);
            view.displayNotification("Rearrange successful!");
        }
        else{
            view.displayNotification("Invalid move. Try again.");
        }
    }

    @Override
    public void notifyTilesOk(boolean ok) throws RemoteException {
        if(ok){
            view.nextAction(2);
            view.displayNotification("Choice of tiles successful!");
        }
        else{
            view.displayNotification("Invalid move. Try again.");
        }
    }

    @Override
    public void notifyGameStart() throws RemoteException {
        view.displayNotification("Game started!");
    }

    @Override
    public void notifyStartTurn(String currentPlayer) throws RemoteException {
        view.changeTurn(currentPlayer);
    }

    @Override
    public void notifyEndGame() throws RemoteException {
        view.setMyTurn(false);
        view.setGameOn(false);
        view.showEndResult();
    }

    @Override
    public void notifyOnCGC(String nickname, int id) throws RemoteException {
        view.displayNotification(nickname + " gained Common Goal Card " + id + "!");
    }

}
