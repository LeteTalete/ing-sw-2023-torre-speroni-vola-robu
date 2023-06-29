package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.view.ClientGUI;
import it.polimi.ingsw.view.GUIApplication;
import it.polimi.ingsw.view.SceneNames;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientListenerGUI extends UnicastRemoteObject implements IClientListener {
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
    public void notifySuccessfulRegistration(String name, boolean b, String token, boolean first) throws RemoteException {
        if(b) {
            view.printError("Registration Successful!");
            setToken(token);
            if (!first){
                showWaitingRoomNotification("Waiting for other players to join...");
            }
            view.serverSavedUsername(name, true, token, first);
        }
        else{
            view.printError("Registration failed: "+name+" already exists. Try again");
            view.serverSavedUsername(name, false, token, first);
        }
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void onSyn() throws RemoteException {
        view.passSyn();
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setGameOn() throws RemoteException {
        view.setGameOn(true);
    }

    @Override
    public void showTextNotification(String message) throws RemoteException {
        view.displayNotification(message);
    }


    @Override
    public void notifyColumnOk(boolean ok) throws RemoteException {
        if(!ok) view.printError("Invalid move. Try again.");
    }

    @Override
    public void notifyEndTurn() throws RemoteException {
        view.printError("Turn ended.");
    }

    @Override
    public void notifyLastTurn(String firstDoneUser) throws RemoteException {
        if(firstDoneUser.equals(view.getName())){
            view.printError("You gained completed your Shelfie!\nLast round starts now.");
        }
        view.printError(firstDoneUser + "completed their Shelfie.\nLast round starts now!");
    }

    @Override
    public void notifyChatMessage(String sender, String message, String receiver) throws RemoteException {
        GUIApplication.setMessageEntry(sender, message, receiver);
    }

    @Override
    public void updateModel(ModelUpdate modelUpdate) throws RemoteException {
        view.displayUpdatedModel(modelUpdate);
    }

    @Override
    public void notifyRearrangeOk(boolean ok, ArrayList<Position> tiles) throws RemoteException {
        if(ok){
            view.nextAction(3, tiles);
            view.turnPhase();
        }
        else{
            view.printError("Invalid move. Try again.");
        }
    }

    @Override
    public void notifyTilesOk(int ok, ArrayList<Position> tiles) throws RemoteException {
        if(ok==0){
            view.nextAction(2, tiles);
            view.turnPhase();
        }
        else{
            if(ok==1){
                view.printError("Invalid move:\nall tiles need to be adjacent!\nTry again.");
            }
            else if(ok == 2){
                view.printError("Invalid move:\nall tiles need to be in the same row or column!\nTry again.");
            }
            else if(ok == 3){
                view.printError("Invalid move:\nall tiles need to have at least one side free!\nTry again.");
            }
            else view.printError("Invalid move:\nnot enough space in your Shelfie!\nTry again.");
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
    public void showWaitingRoomNotification(String message) throws RemoteException {
        GUIApplication.showSceneName(SceneNames.WAITINGROOM);
        System.out.println("WATINGROOM: " + message);
    }

    @Override
    public void notifyOnCGC(String nickname, int id, int points) throws RemoteException {
        if(nickname.equals(view.getName())){
            view.printError("You gained " + points + " points from Common Goal Card " + id + "!");
            GUIApplication.setScorePlayer(points);
        }
        else{
            view.printError(nickname + " gained " + points + " points from Common Goal Card " + id + "!");
        }
    }

    @Override
    public void notifyAboutDisconnection(String disconnectedUser) throws RemoteException {
        view.printError(disconnectedUser + " disconnected. The game is now over.");
    }
}
