package it.polimi.ingsw.server;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.network.IRemoteController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerManager extends UnicastRemoteObject implements IRemoteController {
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
            System.out.println("i will create a new waiting room for you");
            return null;
        }
        else{
            String enoughPLayers = waitingRoom.addPlayerToWaitingRoom(name, token);
            activeUsers.put(token,waitingRoom.getId());
            System.out.println("Added user "+ token + " to waiting room "+waitingRoom.getId()+" successfully!");
            if(enoughPLayers.equals(StaticStrings.GAME_START)){
                //i have to create the players when creating the game
                GameController game = new GameController(waitingRoom.getListOfPlayers(), waitingRoom.getId(), this);
                activeGames.put(waitingRoom.getId(), game);

                System.out.println("i deleted the waiting room "+waitingRoom.getId()+" and started the game!");
                System.out.println("there are currently "+activeGames.size()+" games active!");
                //now we need to notify all the players that the game is about to start

                notifyAllPlayers(waitingRoom.getId(), StaticStrings.GAME_START);
                // game.initialize();

                waitingRoom=null;
                return enoughPLayers;
            }
        return waitingRoom.getId();
        }
    }

    public synchronized void notifySinglePlayer(String token, String message) throws RemoteException {
        ConnectionManager.get().getLocalView(token).sendNotification(message);
    }

    public synchronized void notifyAllPlayers(String gameId, String something) {
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(gameId))
                .forEach(e -> {
                    try {
                        ConnectionManager.get().getLocalView(e.getKey()).sendNotification(something);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public synchronized void notifyAllPlayers(String id, ModelUpdate something) {
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(id))
                .forEach(e -> {
                    try {
                        ConnectionManager.get().getLocalView(e.getKey()).sendUpdatedModel(something);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public synchronized void createWaitingRoom (String name, String token, int howMany){
        //this needs to ask the player how many others we're waiting for
        Random rand = new Random();
        int id = rand.nextInt(1000);
        System.out.println("Im about to create waiting room " + id + " for " + howMany + " players");
        waitingRoom = new WaitingRoom(id, howMany);
        waitingRoom.addPlayerToWaitingRoom(name, token);
        System.out.println("I created waiting room " + id + " for " + howMany + " players");
        activeUsers.put(token, String.valueOf(id));
        try {
            ConnectionManager.get().getLocalView(token).sendNotification(StaticStrings.LOGIN_OK_NEW_ROOM);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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
    @Override
    public void login(String name, IClientListener viewListener) throws RemoteException {
        String dummy;
        System.out.println("I received user " + name);
        if(ConnectionManager.get().tokenNames.containsValue(name)){
            dummy = viewListener.notifySuccessfulRegistration(name,false, null, false);
        }
        else{
            String token = generateToken();
            ConnectionManager.get().addClientView(token, name, viewListener);
            System.out.println("tokenS: "+ token);
            String success = putInWaitingRoom(name, token);
            System.out.println("I'm elaborating for " + name);

            if(success==null) {

                dummy = ConnectionManager.get().getLocalView(token).notifySuccessfulRegistration(name,true, token, true);

            } else if(success.equals(StaticStrings.GAME_START)) {

                System.out.println("nameS: " + name + " tokenS: " + token);
                dummy = ConnectionManager.get().getLocalView(token).notifySuccessfulRegistration(name, true, token, false);
                dummy = ConnectionManager.get().getLocalView(token).sendNotification(StaticStrings.GAME_START);

            } else if(success.equals(StaticStrings.GAME_WAITING)){
                dummy = ConnectionManager.get().getLocalView(token).sendNotification(StaticStrings.GAME_WAITING);

            } else {
                dummy = ConnectionManager.get().getLocalView(token).notifySuccessfulRegistration(name, true, token, false);
            }
        }
    }

    @Override
    public synchronized void pickedTiles(String token, String tilesCoordinates) throws RemoteException {
        System.out.println("AAAAAASERVERMANAGER");
        System.out.println("I received user: " + token + "  and tiles coordinates: " + tilesCoordinates);

        activeGames.get(token).chooseTiles(token, tilesCoordinates);
    }

    @Override
    public synchronized void rearrangeTiles(String token, String tilesOrdered) throws RemoteException {

    }

    @Override
    public synchronized void selectColumn(String token, int column) throws RemoteException {

    }


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
                        ConnectionManager.get().getLocalView(e.getKey()).sendNotification(StaticStrings.GAME_END_DISC);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
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
    //TODO closeGame(gameId)
}
