package it.polimi.ingsw.server;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.notifications.ChatMessage;
import it.polimi.ingsw.notifications.DisconnectionNotif;
import it.polimi.ingsw.notifications.GameStart;
import it.polimi.ingsw.requests.ChatMessageRequest;
import it.polimi.ingsw.responses.LoginResponse;
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
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    public synchronized void notifyAllPlayers(String gameId, Response response) {
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
                        ConnectionManager.get().getLocalView(e.getKey()).sendUpdatedModel(something);
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
    public void sendChat(ChatMessageRequest messageRequest) throws RemoteException {
        fileLog.info("Received a chat message from "+messageRequest.getSender()+" to "+messageRequest.getReceiver()+": "+messageRequest.getMessage());
        ChatMessage message = new ChatMessage(messageRequest.getSender(), messageRequest.getReceiver(), messageRequest.getMessage());
        String senderToken = ConnectionManager.get().namesTokens.get(message.getSender());
        if(message.getReceiver().equals("all")){
            activeUsers.entrySet()
                    .stream()
                    .filter(e -> e.getValue().equals(activeUsers.get(senderToken)))
                    .forEach(e -> {
                        try {
                            ConnectionManager.get().getLocalView(e.getKey()).notifyChatMessage(message);
                        } catch (RemoteException ex) {
                            fileLog.error(ex.getMessage());
                        }
                    });
        }
        else{
            String receiverToken = ConnectionManager.get().namesTokens.get(message.getReceiver());
            ConnectionManager.get().getLocalView(receiverToken).notifyChatMessage(message);
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
            LoginResponse loginResponse = new LoginResponse(name, false, null, false);
            viewListener.notifySuccessfulRegistration(loginResponse);
        }
        else{
            String token = generateToken();
            ConnectionManager.get().addClientView(token, name, viewListener);
            String success = putInWaitingRoom(name, token);

            if(success==null) {
                LoginResponse loginResponse = new LoginResponse(name, true, token, true);
                ConnectionManager.get().getLocalView(token).notifySuccessfulRegistration(loginResponse);

            } else if(success.equals(StaticStrings.GAME_START)) {
                LoginResponse loginResponse = new LoginResponse(name, true, token, false);
                ConnectionManager.get().getLocalView(token).notifySuccessfulRegistration(loginResponse);

                //now we need to notify all the players that the game is about to start
                notifyAllPlayers(waitingRoom.getId(), new GameStart());

                //i have to create the players when creating the game
                GameController game = new GameController(waitingRoom.getListOfPlayers(), waitingRoom.getId(), this);
                activeGames.put(waitingRoom.getId(), game);

                fileLog.info("I created a new game with id: " + waitingRoom.getId() + "and started the game!");
                fileLog.info("The players are: " + waitingRoom.getListOfPlayers());

                game.initialize();
                waitingRoom=null;

            } else {
                LoginResponse loginResponse = new LoginResponse(name, true, token, false);
                ConnectionManager.get().getLocalView(token).notifySuccessfulRegistration(loginResponse);
            }
        }
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
                        ConnectionManager.get().getLocalView(e.getKey()).sendNotification(disconnectionNotif);
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
    //TODO closeGame(gameId)
}
