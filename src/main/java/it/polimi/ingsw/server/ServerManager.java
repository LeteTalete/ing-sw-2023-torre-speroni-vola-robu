package it.polimi.ingsw.server;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.ClientListenerTUI;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.notifications.DisconnectionNotif;
import it.polimi.ingsw.responses.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerManager extends UnicastRemoteObject implements IRemoteController {
    private static Logger fileLog = LogManager.getRootLogger();

    private Map<String, GameController> activeGames;
    private WaitingRoom waitingRoom;
    //network manager: to instantiate RMI and socket
    //these are all the usernames and the respective rooms
    private Map<String, String> activeUsers;

    //constructor
    public ServerManager() throws RemoteException{
        super();
        activeGames = new HashMap<String, GameController>();
        activeUsers = new HashMap<String,String>();
    }

    public synchronized String putInWaitingRoom(String name, String token) throws RemoteException {
        if(waitingRoom==null){
            fileLog.info("No waiting room found... I'm going to create one");
            return null;
        }
        else{
            String enoughPLayers = waitingRoom.addPlayerToWaitingRoom(name, token);
            activeUsers.put(token,waitingRoom.getId());
            fileLog.info("Added user "+ token + " to waiting room "+waitingRoom.getId()+" successfully!");
            if(enoughPLayers.equals(StaticStrings.GAME_START)){
                return enoughPLayers;
            }
        return waitingRoom.getId();
        }
    }

    public void notifySinglePlayer(String token, Response response){
        try {
            ConnectionManager.get().getLocalView(token).sendNotification(response);
            fileLog.debug("i'm notifying single player");
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    public synchronized void notifyAllPlayers(String gameId, Response response) {
        fileLog.debug("i'm about to notify all players");
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(gameId))
                .forEach(e -> {
                    try {
                        ConnectionManager.get().getLocalView(e.getKey()).sendNotification(response);
                    } catch (RemoteException ex) {
                        fileLog.error(ex.getMessage());
                    }
                });
    }

    public synchronized void notifyAllPlayers(String id, ModelUpdate something) {
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(id))
                .forEach(e -> {
                    try {
                        ConnectionManager.get().getViewProxy(e.getKey()).sendUpdatedModel(something);
                    } catch (RemoteException ex) {
                        fileLog.error(ex.getMessage());
                    }
                });
    }

    public synchronized void createWaitingRoom (String name, String token, int howMany){
        Random rand = new Random();
        int id = rand.nextInt(1000);
        waitingRoom = new WaitingRoom(id, howMany);
        waitingRoom.addPlayerToWaitingRoom(name, token);
        activeUsers.put(token, String.valueOf(id));
        try{
            ConnectionManager.get().getLocalView(token).showTextNotification("I created a waiting room. Waiting for other players to join...");
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    @Override
    public void sendChat(String sender, String message, String receiver) throws RemoteException {
        fileLog.info("Received a chat message from "+sender+" to "+receiver+": "+message);
        String senderToken = ConnectionManager.get().namesTokens.get(sender);
        if(receiver.equals("all")){
            activeUsers.entrySet()
                    .stream()
                    .filter(e -> e.getValue().equals(activeUsers.get(senderToken)))
                    .forEach(e -> {
                        try {
                            ConnectionManager.get().getLocalView(e.getKey()).notifyChatMessage(sender, message);
                        } catch (RemoteException ex) {
                            fileLog.error(ex.getMessage());
                        }
                    });
        }
        else{
            String receiverToken = ConnectionManager.get().namesTokens.get(receiver);
            ConnectionManager.get().getLocalView(receiverToken).notifyChatMessage(sender, message);
        }

    }


    public synchronized void periodicPing (){
        //this has to send a ping to all the players in all the active games
        //if it does not catch a single pong from one player, it shuts down the entire game
        //if the game is shut down, the server will notify all the players about it
    }

    public synchronized String generateToken(){
        Random rand = new Random();
        int id = rand.nextInt(1000);
        String token = String.valueOf(id);
        return token;
    }

    //this should be fixed further: all of these return strings, so that the server doesn't
    //wait on them, but they should be fixed with threads
    //todo check on the alreadyexists attributes
    @Override
    public void login(String name, IClientListener viewListener) throws RemoteException {
        if(ConnectionManager.get().tokenNames.containsValue(name)){
            viewListener.notifySuccessfulRegistration(name, false, null, false);
        }
        else{
            String token = generateToken();
            ConnectionManager.get().addClientView(token, name, viewListener);
            String success = putInWaitingRoom(name, token);
            if(viewListener instanceof ClientListenerTUI /*|| viewListener instanceof ClientListenerGui*/){
                generateTokenRMI(viewListener, name);
            }
            else if(viewListener instanceof ServerSocketClientHandler){
                //the cast is not necessary
                createToken((ServerSocketClientHandler) viewListener, name);
            }
            if(success==null) {
                ConnectionManager.get().getViewProxy(token).notifySuccessfulRegistration(name, true, token, true);

            } else if(success.equals(StaticStrings.GAME_START)) {
                ConnectionManager.get().getViewProxy(token).notifySuccessfulRegistration(name, true,token, false);

                //now we need to notify all the players that the game is about to start
                notifyAboutGameStart(waitingRoom.getId());

                //i have to create the players when creating the game
                GameController game = new GameController(waitingRoom.getListOfPlayers(), waitingRoom.getId(), this);
                activeGames.put(waitingRoom.getId(), game);

                fileLog.info("I created a new game with id: " + waitingRoom.getId() + "and started the game!");
                fileLog.info("The players are: " + waitingRoom.getListOfPlayers());

                game.initialize();
                waitingRoom=null;

            } else {
                ConnectionManager.get().getViewProxy(token).notifySuccessfulRegistration(name, true, token, false);
            }
        }
    }

    private void notifyAboutGameStart(String id) {
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(id))
                .forEach(e -> {
                    try {
                        ConnectionManager.get().getViewProxy(e.getKey()).notifyGameStart();
                    } catch (RemoteException ex) {
                        fileLog.error(ex.getMessage());
                    }
                });
    }


    @Override
    public void pickedTiles(String token, List<String> tilesCoordinates) throws RemoteException {
        fileLog.info("I received user: " + token + "  and tiles coordinates: " + tilesCoordinates);
        activeGames.get(activeUsers.get(token)).chooseTiles(token, tilesCoordinates);
        fileLog.info("Finished picking tiles: " + tilesCoordinates + " for token: " + token );
    }

    @Override
    public synchronized void rearrangeTiles(String token, List<String> tilesOrdered) throws RemoteException {
        activeGames.get(activeUsers.get(token)).rearrangeTiles(token, tilesOrdered);
    }

    @Override
    public synchronized void selectColumn(String token, int column) throws RemoteException {
        activeGames.get(activeUsers.get(token)).chooseColumn(token, column);
    }

//todo fix
    public void disconnect(String token) {
        ConnectionManager.get().disconnectToken(token);
        String gameId = activeGames.get(token).getGameId();
        //could be put in its own method notifyAllExcept
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(gameId))
                .filter(e -> !e.getKey().equals(token))
                .forEach(e -> {
                    try {
                        DisconnectionNotif disconnectionNotif = new DisconnectionNotif(ConnectionManager.get().getNameToken(token));
                        ConnectionManager.get().getViewProxy(e.getKey()).sendNotification(disconnectionNotif);
                    } catch (RemoteException ex) {
                        fileLog.error(ex.getMessage());
                    }
                });
    }

    public String getTokenFromHandler(ServerSocketClientHandler serverSocketClientHandler) {
        for(String token : ConnectionManager.get().viewListenerMap.keySet()){
            if(ConnectionManager.get().viewListenerMap.get(token).equals(serverSocketClientHandler)){
                return token;
            }
        }
        return "error!";
    }

    public void createToken(ServerSocketClientHandler socketClientHandler, String token) {
        ViewProxy viewProxy = new ViewProxy(socketClientHandler, token);
        ConnectionManager.get().viewsProxy.put(token, viewProxy);
        ConnectionManager.get().viewListenerMap.put(token, socketClientHandler);
    }

    public void generateTokenRMI(IClientListener viewListener, String token) throws RemoteException {
        ConnectionManager.get().viewsProxy.put(token, new ViewProxy(viewListener, token));
    }
//todo??
    public void notifyAboutRearrange(String token, boolean b) {
        try {
            ConnectionManager.get().getViewProxy(token).notifyRearrangeOk(b);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    public void notifyAboutColumn(String token, boolean b) {
        try {
            ConnectionManager.get().getViewProxy(token).notifyColumnOk(b);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    public void notifyOnStartTurn(String gameId, String currentPlayer) {
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(gameId))
                .forEach(e -> {
                    try {
                        ConnectionManager.get().getViewProxy(e.getKey()).notifyStartTurn(currentPlayer);
                    } catch (RemoteException ex) {
                        fileLog.error(ex.getMessage());
                    }
                });
    }

    public void notifyOnEndTurn(String token) {
        try {
            ConnectionManager.get().getViewProxy(token).notifyEndTurn();
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }
//todo check if this method actually needs to pass an array of positions
    public void notifyAboutTiles(String token, boolean b, ArrayList<Position> choice) {
        try {
            ConnectionManager.get().getViewProxy(token).notifyTilesOk(b);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    public void notifyOnEndGame(String gameId) {
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(gameId))
                .forEach(e -> {
                    try {
                        ConnectionManager.get().getViewProxy(e.getKey()).notifyEndGame();
                    } catch (RemoteException ex) {
                        fileLog.error(ex.getMessage());
                    }
                });
    }

    public void notifyOnLastTurn(String gameId, String nickname) {
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(gameId))
                .forEach(e -> {
                    try {
                        ConnectionManager.get().getViewProxy(e.getKey()).notifyLastTurn(nickname);
                    } catch (RemoteException ex) {
                        fileLog.error(ex.getMessage());
                    }
                });
    }

    public void notifyOnCGC(String gameId, String nickname, int id) {
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(gameId))
                .forEach(e -> {
                    try {
                        ConnectionManager.get().getViewProxy(e.getKey()).notifyOnCGC(nickname, id);
                    } catch (RemoteException ex) {
                        fileLog.error(ex.getMessage());
                    }
                });
    }

    //TODO closeGame(gameId)
}
