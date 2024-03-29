package it.polimi.ingsw.network;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.server.StaticStrings;
import it.polimi.ingsw.view.ClientTUI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**this class is used by the server to communicate with the client via RMI (it's used so that the server can invoke
 * these methods, which then invoke the client's view methods)*/

public class ClientListenerTUI extends UnicastRemoteObject implements IClientListener {
    private String connectionType = "RMI";
    /**view parameter is used to invoke the methods of the TUI*/
    private transient final ClientTUI view;
    /**token used to identify the client*/
    private String token;

    /**ClientListenerTUI constructor.
     * @param currentView - used to invoke the methods of the view.*/
    public ClientListenerTUI(ClientTUI currentView) throws RemoteException{
        this.view = currentView;
    }

    @Override
    public String getTypeConnection() throws RemoteException {
        return connectionType;
    }

    /**notifySuccessfulRegistration method is used to notify about the success (or failure) of the client's login.
     * @param name - username of the client.
     * @param token - token used to identify the client.
     * @param b - boolean signalling the success or failure of the login procedure.
     * @param first - boolean used to signal whether the player needs to create a waiting room or not.*/
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
            view.printError("Registration failed: "+name+" already exists. Try again");
            view.serverSavedUsername(name, false, token, first);
        }
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    /**method onSyn to receive the syn/ping from the server*/
    @Override
    public void onSyn() throws RemoteException {
        view.passSyn();
    }

    @Override
    public String getToken() {
        return token;
    }

    /**method setGameOn is used when the game starts to grant the user permission to make moves*/
    @Override
    public void setGameOn() throws RemoteException {
        view.writeText(StaticStrings.GAME_START);
        view.setGameOn(true);
    }

    /**showTextNotificationMethod is used to display a generic text notification on the view.
     * @param message - containst the text notification.*/
    @Override
    public void showTextNotification(String message) throws RemoteException {
        view.displayNotification(message);
    }

    /**method notifyColumnsOk used to notify about the success or failure of the column choice.
     * @param ok - boolean signalling the success or failure of the move.*/
    @Override
    public void notifyColumnOk(boolean ok) throws RemoteException {
        if(ok){
            view.displayNotification("Choice of column successful!");
        }
        else{
            view.printError("Invalid move. Try again.");
        }
    }

    /**method notifyEndTurn used to notify the player about the end of their turn*/
    @Override
    public void notifyEndTurn() throws RemoteException {
        view.displayNotification("Turn ended.");
    }

    /**method notifyLastTurn used to notify the players about the start of the last turn.
     * @param firstDoneUser - username of the player who first completed their shelf.*/
    @Override
    public void notifyLastTurn(String firstDoneUser) throws RemoteException {
        if(firstDoneUser.equals(view.getName())){
            view.displayNotification("You completed your Shelfie! Last round starts now.");
        } else view.displayNotification(firstDoneUser + " completed their Shelfie. Last round starts now!");
    }

    /**method notifyChatMessage used to display a chat message.
     * @param message - text of the chat message.
     * @param receiver - receiver of the message.
     * @param sender - sender of the message.*/
    @Override
    public void notifyChatMessage(String sender, String message, String receiver) throws RemoteException {
        view.displayChatNotification("@"+sender + " to " + receiver +": " + message);

    }

    /**method sendUpdatedModel to send the model update to the view.
     * @param modelUpdate - contains the model update*/
    @Override
    public void updateModel(ModelUpdate modelUpdate) throws RemoteException {
        view.displayUpdatedModel(modelUpdate);
    }

    /**method notifyRearrangeOk used to notify the player about the success or failure of the re-arrange.
     * @param ok - boolean signalling the success or failure of the move.
     * @param tiles - contains the re-arranged tiles so that the player can view them.*/
    @Override
    public void notifyRearrangeOk(boolean ok, ArrayList<Position> tiles) throws RemoteException {
        if(ok){
            view.nextAction(2, tiles);
            view.refreshBoard();
            view.displayNotification("Rearrange successful!");
        }
        else{
            view.displayNotification("Invalid move. Try again.");
        }
    }

    /**method notifyTilesOk used to notify the player whether the choice of tiles was a success or a failure.
     * @param ok - integer signaling whether the move was successful or not (0 = success, 1 = tiles not adjacent,
     *           2 = tiles not in the same row/column, 3 = tiles not from the edge, 4 = not enough space in shelf,
     *           5 = the tiles is not pickable).
     * @param tiles - contains the tiles chosen by the client.*/
    @Override
    public void notifyTilesOk(int ok, ArrayList<Position> tiles) throws RemoteException {
        if(ok==0){
            view.nextAction(2, tiles);
            view.refreshBoard();
        }
        else{
            if(ok==1){
                view.displayNotification("Invalid move: all tiles need to be adjacent! Try again.");
            }
            else if(ok == 2){
                view.displayNotification("Invalid move: all tiles need to be in the same row or column! Try again.");
            }
            else if(ok == 3){
                view.displayNotification("Invalid move: all tiles need to have at least one side free! Try again.");
            }
            else if(ok==4) {
                view.displayNotification("Invalid move: not enough space in your Shelfie! Try again.");
            }
            else view.displayNotification("Invalid move: the tile chosen is not pickable!");
        }
    }

    /**method notifyGameStart used to notify about the start of the game*/
    @Override
    public void notifyGameStart() throws RemoteException {
        setGameOn();
    }

    /**method notifyStartTurn used to notify about the start of a turn.
     * @param currentPlayer - username of the current player.*/
    @Override
    public void notifyStartTurn(String currentPlayer) throws RemoteException {
        view.changeTurn(currentPlayer);
    }

    /**method notifyEndGame notifies about the end of a match.*/
    @Override
    public void notifyEndGame() throws RemoteException {
        view.setMyTurn(0);
        view.setGameOn(false);
        view.showEndResult();
    }

    /**method showWaitingRoomNotification is used to display a notification relative to the waiting room*/
    @Override
    public void showWaitingRoomNotification(String message) throws RemoteException {
        view.displayNotification(message);
    }


    /**method notifyOnCGC notifies about the gain of a common goal card.
     * @param nickname - username of the player who won the card.
     * @param id - id of the common goal card.*/
    @Override
    public void notifyOnCGC(String nickname, int id, int points) throws RemoteException {
        if(nickname.equals(view.getName())){
            view.displayNotification("You gained " + points + " points from Common Goal Card " + id + "!");
        } else view.displayNotification(nickname + " gained " + points + " points from Common Goal Card " + id + "!");
    }

    /**method notifyAboutDisconnection notifies about the disconnection of a user.
     * @param disconnectedUser - username of the disconnected player.*/
    @Override
    public void notifyAboutDisconnection(String disconnectedUser) throws RemoteException {
        view.displayNotification(disconnectedUser + " disconnected. The game is now over.");
        view.setGameOn(false);
    }


}