package it.polimi.ingsw.server;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.network.IRemoteController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerManager extends UnicastRemoteObject implements IRemoteController {
    private static final Logger fileLog = LogManager.getRootLogger();
    private final Map<String, GameController> activeGames;
    private WaitingRoom waitingRoom;
    //network manager: to instantiate RMI and socket
    //these are all the usernames and the respective rooms
    private final Map<String, String> activeUsers;

    //constructor
    public ServerManager() throws RemoteException{
        super();
        activeGames = new HashMap<>();
        activeUsers = new HashMap<>();
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


    public synchronized void notifyAllPlayers(String id, ModelUpdate something) {
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(id))
                .forEach(e -> {
                    try {
                        ConnectionManager.get().viewListenerMap.get(e.getKey()).sendUpdatedModel(something);
                    } catch (RemoteException ex) {
                        fileLog.error(ex.getMessage());
                    }
                });
    }

    public synchronized void createWaitingRoom (String name, String token){
        Random rand = new Random();
        int id = rand.nextInt(1000);
        waitingRoom = new WaitingRoom(id, this);
        waitingRoom.addPlayerToWaitingRoom(name, token);
        activeUsers.put(token, String.valueOf(id));
    }

    @Override
    public void sendChat(String sender, String message, String receiver) throws RemoteException {
        fileLog.info("Received a chat message from "+sender+" to "+receiver+": "+message);
        String senderToken = ConnectionManager.get().namesTokens.get(sender);
        if(ConnectionManager.get().namesTokens.get(receiver)==null && !receiver.equals("all")){
            fileLog.debug("Sender token is null");
            ConnectionManager.get().getLocalView(senderToken).showTextNotification("The user you are trying to reach does not exist.");
        }
        else if(receiver.equals("all")){
            activeUsers.entrySet()
                    .stream()
                    .filter(e -> e.getValue().equals(activeUsers.get(senderToken)))
                    .forEach(e -> {
                        try {
                            ConnectionManager.get().getLocalView(e.getKey()).notifyChatMessage(sender, message, receiver);
                        } catch (RemoteException ex) {
                            fileLog.error(ex.getMessage());
                        }
                    });
        }
        else{
            String receiverToken = ConnectionManager.get().namesTokens.get(receiver);
            ConnectionManager.get().getLocalView(senderToken).notifyChatMessage("you", message, receiver);
            ConnectionManager.get().getLocalView(receiverToken).notifyChatMessage(sender, message, "you");
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

    @Override
    public void login(String name, IClientListener viewListener) throws RemoteException {
        if(ConnectionManager.get().tokenNames.containsValue(name)){
            viewListener.notifySuccessfulRegistration(name, false, null, false);
        }
        else{
            fileLog.debug("Received a login request from "+name);
            String token = generateToken();
            ConnectionManager.get().addClientView(token, name, viewListener);

            if(waitingRoom==null){
                ConnectionManager.get().viewListenerMap.get(token).notifySuccessfulRegistration(name, true, token, true);
                createWaitingRoom(name, token);
            }
            else  {
                fileLog.debug("Waiting room is not null, adding player"+name+ "to waiting room");
                ConnectionManager.get().viewListenerMap.get(token).notifySuccessfulRegistration(name, true, token, false);
                String success = putInWaitingRoom(name, token);
                if(success.equals(StaticStrings.GAME_START))
                {
                    createGame(waitingRoom.getId());
                }
            }


            if(viewListener.getTypeConnection().equals("RMI")){
                generateTokenRMI(viewListener, name);
            }
            else if(viewListener.getTypeConnection().equals("SOCKET")){
                //the cast is not necessary
                createToken((ServerSocketClientHandler) viewListener, name);
            }
        }
    }

    public void createGame(String id) {
        //i have to create the players when creating the game
        ArrayList<Player> playersReady = new ArrayList<>();
        int i=0;
        for(; i<waitingRoom.getMaxPLayers(); i++){
            playersReady.add(waitingRoom.getSinglePlayer(i));
        }
        if(i<waitingRoom.getListOfPlayers().size()){
            for(; i<waitingRoom.getListOfPlayers().size(); i++){
                String token = waitingRoom.getListOfPlayers().get(i).getTokenId();
                activeUsers.remove(token);
                ConnectionManager.get().inactiveUsers.add(token);
                try {
                    ConnectionManager.get().viewListenerMap.get(token).showTextNotification("There is no game active for you. Please disconnect and try again later");
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //now we need to notify all the players that the game is about to start
        notifyAboutGameStart(playersReady);
        GameController game = new GameController(playersReady, waitingRoom.getId(), this);
        activeGames.put(waitingRoom.getId(), game);

        fileLog.info("I created a new game with id: " + waitingRoom.getId() + "and started the game!");

        try {
            game.initialize();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        waitingRoom=null;
    }

    public void notifyAboutGameStart(ArrayList<Player> playersReady) {
        playersReady.stream().forEach(p -> {
            try {
                ConnectionManager.get().viewListenerMap.get(p.getTokenId()).notifyGameStart();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
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

    public void disconnect(String token) {
        fileLog.debug("I am disconnecting user: " + token);
        ConnectionManager.get().disconnectToken(token);
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
        ConnectionManager.get().viewListenerMap.put(token, socketClientHandler);
        ConnectionManager.get().startPingTimer(token);
        ConnectionManager.get().startSynTimer(token);
    }

    public void generateTokenRMI(IClientListener viewListener, String token) throws RemoteException {
        ConnectionManager.get().viewListenerMap.put(token, viewListener);
        ConnectionManager.get().startPingTimer(token);
        ConnectionManager.get().startSynTimer(token);
    }

    @Override
    public void sendPing(String token) throws RemoteException {
        ConnectionManager.get().setPingMap(token, true);
    }

    @Override
    public void setPlayersWaitingRoom(String token, int num) throws RemoteException {
        waitingRoom.setMaxPLayers(num);
        if(activeGames.isEmpty()){
            ConnectionManager.get().viewListenerMap.get(token).showTextNotification("Waiting room created! Waiting for other players to join...");
        }
    }


    public void notifyAboutRearrange(String token, boolean b, ArrayList<Position> tiles) {
        try {
            ConnectionManager.get().viewListenerMap.get(token).notifyRearrangeOk(b, tiles);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    public void notifyAboutColumn(String token, boolean b) {
        try {
            ConnectionManager.get().viewListenerMap.get(token).notifyColumnOk(b);
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
                        ConnectionManager.get().viewListenerMap.get(e.getKey()).notifyStartTurn(currentPlayer);
                    } catch (RemoteException ex) {
                        fileLog.error(ex.getMessage());
                    }
                });
    }

    public void notifyOnEndTurn(String token) {
        try {
            ConnectionManager.get().viewListenerMap.get(token).notifyEndTurn();
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }
    public void notifyAboutTiles(String token, boolean b, ArrayList<Position> choice) {
        try {
            ConnectionManager.get().viewListenerMap.get(token).notifyTilesOk(b, choice);
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
                        ConnectionManager.get().viewListenerMap.get(e.getKey()).notifyEndGame();
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
                        ConnectionManager.get().viewListenerMap.get(e.getKey()).notifyLastTurn(nickname);
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
                        ConnectionManager.get().viewListenerMap.get(e.getKey()).notifyOnCGC(nickname, id);
                    } catch (RemoteException ex) {
                        fileLog.error(ex.getMessage());
                    }
                });
    }

    //TODO closeGame(gameId)
}
