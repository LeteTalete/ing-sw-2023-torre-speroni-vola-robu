package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.server.StaticStrings;
import it.polimi.ingsw.view.ClientGUI;
import it.polimi.ingsw.view.ClientTUI;
import it.polimi.ingsw.view.GUIApplication;
import it.polimi.ingsw.view.SceneNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientListenerGUI extends UnicastRemoteObject implements IClientListener {
    private static Logger fileLog = LogManager.getRootLogger();

    private String connectionType = "RMI";
    private transient final ClientGUI view;
    private String token;
    public ClientListenerGUI(ClientGUI currentView) throws RemoteException {
        this.view = currentView;
    }

    @Override
    public String getTypeConnection() throws RemoteException {
        return connectionType;
    }

    @Override
    public void sendUpdatedModel(ModelUpdate message) throws RemoteException {
        view.displayUpdatedModel(message);
    }

    @Override
    public void notifySuccessfulRegistration(String name, boolean b, String token, boolean first) throws RemoteException {
        if(b) {
            view.displayNotification("Registration Successful!");
            setToken(token);
            if (!first){
                view.displayNotification("Waiting for other players to join...");
            }
            view.serverSavedUsername(name, true, token, first);
        }
        else{
            view.displayNotification("Registration failed: "+name+" already exists. Try again");
            view.serverSavedUsername(name, false, token, first);
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void setGameOn() throws RemoteException {
        //view.writeText(StaticStrings.GAME_START);
        //view.printCommands();
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
        view.setMyTurn(0);
        view.displayNotification("Turn ended.");
    }

    @Override
    public void notifyLastTurn(String firstDoneUser) throws RemoteException {
        view.displayNotification(firstDoneUser + "completed their Shelfie. Last round starts now!");
    }

    @Override
    public void notifyChatMessage(String sender, String message, String receiver) throws RemoteException {
        //view.displayChatNotification("@"+sender + " to " + receiver +": " + message);

    }

    @Override
    public void updateModel(ModelUpdate modelUpdate) throws RemoteException {
        view.displayUpdatedModel(modelUpdate);
    }

    @Override
    public void notifyRearrangeOk(boolean ok, ArrayList<Position> tiles) throws RemoteException {
        if(ok){
            view.nextAction(3, tiles);
            view.displayNotification("Rearrange successful!");
        }
        else{
            view.displayNotification("Invalid move. Try again.");
        }
    }

    @Override
    public void notifyTilesOk(boolean ok, ArrayList<Position> tiles) throws RemoteException {
        if(ok){
            view.displayNotification("Choice of tiles successful!");
            view.nextAction(2, tiles);
        }
        else{
            view.displayNotification("Invalid move. Try again.");
        }
    }

    @Override
    public void notifyGameStart() throws RemoteException {
        setGameOn();
    }

    @Override
    public void notifyStartTurn(String currentPlayer) throws RemoteException {
        view.changeTurn(currentPlayer);
    }

    @Override
    public void notifyEndGame() throws RemoteException {
        view.setMyTurn(0);
        view.setGameOn(false);
        view.showEndResult();
    }

    @Override
    public void notifyOnCGC(String nickname, int id) throws RemoteException {
        view.displayNotification(nickname + " gained Common Goal Card " + id + "!");
    }

    @Override
    public void notifyAboutDisconnection(String disconnectedUser) throws RemoteException {
        view.displayNotification(disconnectedUser + " disconnected. The game is now over.");
    }

    @Override
    public void sendPingSyn() throws RemoteException {
        view.pingSyn();
    }

    public String getToken() {
        return token;
    }
}
