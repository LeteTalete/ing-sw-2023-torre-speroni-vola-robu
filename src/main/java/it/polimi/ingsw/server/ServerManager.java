package it.polimi.ingsw.server;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.network.ServerSocketClientHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerManager extends UnicastRemoteObject implements IRemoteController {
    private static final Logger fileLog = LogManager.getRootLogger();
    private final Map<String, GameController> activeGames;
    private WaitingRoom waitingRoom;
    private final Map<String, String> activeUsers;

    /**ServerManager constructor*/
    public ServerManager() throws RemoteException{
        super();
        activeGames = new HashMap<>();
        activeUsers = new HashMap<>();
    }

    /**method setPlayersWaitingRoom to set a maximum number of players to a waiting room.
     * @param token - token to notify the client about the correct creation of the waiting room.
     * @param num - number of players expected to be in the next match.*/
    @Override
    public void setPlayersWaitingRoom(String token, int num) throws RemoteException {
        waitingRoom.setMaxPLayers(num);
        if(activeGames.isEmpty()){
            ConnectionManager.get().viewListenerMap.get(token).showTextNotification("Waiting room created! Waiting for other players to join...");
        }
    }

    /**putInWaitingRoom method is used to place the players inside the waiting room.
     * @param name - name of the player.
     * @param token - token used to identify the player.*/
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

    /**createWaitingRoom method to create a waiting room and assign it an id.
     * @param token - token of the first player to be placed in the waiting room.
     * @param name - username of the first player to be placed in the waiting room.*/
    public synchronized void createWaitingRoom (String name, String token){
        Random rand = new Random();
        int id = rand.nextInt(1000);
        waitingRoom = new WaitingRoom(id, this);
        waitingRoom.addPlayerToWaitingRoom(name, token);
        activeUsers.put(token, String.valueOf(id));
    }

    /**method sendChat used to send a message to either all the players or to a single one.
     * @param receiver - receiver of the message.
     * @param sender - sender of the message.
     * @param message - the text of the message.*/
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

    /**generateToken methods create a random token to assign to a player*/
    public synchronized String generateToken(){
        Random rand = new Random();
        int id = rand.nextInt(1000);
        String token = String.valueOf(id);
        return token;
    }

    /**login method used to log a client in. It then notifies the client about the success or the failure of
     * the login procedure.
     * @param name - username of the client.
     * @param viewListener - listener of the client, which will be used from now on to send them notifications
     * and responses.*/
    @Override
    public void login(String name, IClientListener viewListener) throws RemoteException {
        if(ConnectionManager.get().tokenNames.containsValue(name)){
            viewListener.notifySuccessfulRegistration(name, false, null, false);
        }
        else{
            String token = generateToken();
            ConnectionManager.get().addClientView(token, name, viewListener);

            if(waitingRoom==null){
                ConnectionManager.get().viewListenerMap.get(token).notifySuccessfulRegistration(name, true, token, true);
                createWaitingRoom(name, token);
            }
            else  {
                ConnectionManager.get().viewListenerMap.get(token).notifySuccessfulRegistration(name, true, token, false);
                String success = putInWaitingRoom(name, token);
                if(success.equals(StaticStrings.GAME_START))
                {
                    createGame(waitingRoom.getId());
                }
            }


            if(viewListener.getTypeConnection().equals("RMI")){
                generateTokenRMI(viewListener, token);
            }
            else if(viewListener.getTypeConnection().equals("SOCKET")){
                //the cast is not necessary
                createToken((ServerSocketClientHandler) viewListener, token);
            }
        }
    }

    /**method createGame used to create the game. It adds the players to the match. If there are still users waiting,
     * it notifies them that there is no game active (since there can only be one active at a time.
     * @param id - identification of the room.*/
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

        game.getModel().startGame();

        waitingRoom=null;
    }

    /**method used to pass the choice of tiles to the gameController.
     * @param token - token of the client who made the move.
     * @param tilesCoordinates - coordinates of the tiles chosen by the client.*/
    @Override
    public void pickedTiles(String token, List<String> tilesCoordinates) throws RemoteException {
        fileLog.info("I received user: " + token + "  and tiles coordinates: " + tilesCoordinates);
        activeGames.get(activeUsers.get(token)).chooseTiles(token, tilesCoordinates);
        fileLog.info("Finished picking tiles: " + tilesCoordinates + " for token: " + token );
    }

    /**method used to pass the choice of re-arranged tiles to the gameController.
     * @param token - token of the client who made the move.
     * @param tilesOrdered - order of the tiles re-arranged by the client.*/
    @Override
    public synchronized void rearrangeTiles(String token, List<String> tilesOrdered) throws RemoteException {
        activeGames.get(activeUsers.get(token)).rearrangeTiles(token, tilesOrdered);
    }

    /**method used to pass the choice of column to the gameController.
     * @param token - token of the client who made the move.
     * @param column - column chosen by the client.*/
    @Override
    public synchronized void selectColumn(String token, int column) throws RemoteException {
        activeGames.get(activeUsers.get(token)).chooseColumn(token, column);
    }

    /**disconnect method to disconnect a user.
     * @param token - token to identify the disconnecting client.*/
    public void disconnect(String token) {
        ConnectionManager.get().disconnectToken(token);
        ConnectionManager.get().stopSynTimer(token);
        ConnectionManager.get().stopAckTimer(token);
    }

    @Override
    public void sendAck(String userToken) throws RemoteException {
        ConnectionManager.get().setAck(userToken, true);
    }

    /**method used to get the token from a serverSocketClientHandler, mainly used disconnect.
     * @param serverSocketClientHandler - the listener of the client.*/
    public String getTokenFromHandler(ServerSocketClientHandler serverSocketClientHandler) {
        for(String token : ConnectionManager.get().viewListenerMap.keySet()){
            if(ConnectionManager.get().viewListenerMap.get(token).equals(serverSocketClientHandler)){
                return token;
            }
        }
        return "error!";
    }

    /**createToken is used to add the token and the socket handler of a client to the map of all the client
     * listeners. It also starts the timers.
     * @param socketClientHandler - the listener of the client.
     * @param token - the token to identify the client*/
    public void createToken(ServerSocketClientHandler socketClientHandler, String token) {
        ConnectionManager.get().viewListenerMap.put(token, socketClientHandler);
        ConnectionManager.get().getAckMap().put(token, false);
        ConnectionManager.get().startSynTimer(token);
        ConnectionManager.get().startAckTimer(token);
    }

    /**createToken is used to add the token and the viewListener of a client to the map of all the client
     * listeners. It also starts the timers.
     * @param viewListener - the listener of the client.
     * @param token - the token to identify the client*/
    public void generateTokenRMI(IClientListener viewListener, String token) throws RemoteException {
        ConnectionManager.get().viewListenerMap.put(token, viewListener);
        ConnectionManager.get().getAckMap().put(token, false);
        ConnectionManager.get().startSynTimer(token);
        ConnectionManager.get().startAckTimer(token);
    }

    /**method used to notify all the players of a room about the start of the game.
     * @param playersReady - list of players to be notified.*/
    public void notifyAboutGameStart(ArrayList<Player> playersReady) {
        playersReady.stream().forEach(p -> {
            try {
                ConnectionManager.get().viewListenerMap.get(p.getTokenId()).notifyGameStart();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }


    /**method notifyAboutRearrange used to notify the client about a successful (or failed) re-arrange.
     * @param token - token to identify the client.
     * @param b - boolean signaling whether the move was successful or not.
     * @param tiles - list of tiles re-arranged, so that the client can view them.*/
    public void notifyAboutRearrange(String token, boolean b, ArrayList<Position> tiles) {
        try {
            ConnectionManager.get().viewListenerMap.get(token).notifyRearrangeOk(b, tiles);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    /**notifyAboutColumn method used to notify the client about a successful (or failed) choice of column.
     * @param b - boolean signaling whether the move was succesful or not.
     * @param token - token used to identify the client.*/
    public void notifyAboutColumn(String token, boolean b) {
        try {
            ConnectionManager.get().viewListenerMap.get(token).notifyColumnOk(b);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    /**notifyOnStartTurn method to notify the players about the start of a new turn.
     * @param currentPlayer - the username of the current player.
     * @param gameId - id of the room.*/
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

    /**notifyOnEndTurn method used to notify a single player about the end of their turn.
     * @param token - token used to identify the player.*/
    public void notifyOnEndTurn(String token) {
        try {
            ConnectionManager.get().viewListenerMap.get(token).notifyEndTurn();
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    /**notifyAboutTiles method used to notify a player about a successful (or failed) choice of tiles.
     * @param b - integer signaling whether the move was successful or not (0 = success, 1 = tiles not adjacent, 2 =
     *          tiles not in the same row/column, 3 = tiles not from the edge, 4 = not enough space in shelf).
     * @param token - token used to identify the client.
     * @param choice - choice of tiles passed to the client so that they can view them.*/
    public void notifyAboutTiles(String token, int b, ArrayList<Position> choice) {
        try {
            ConnectionManager.get().viewListenerMap.get(token).notifyTilesOk(b, choice);
        } catch (RemoteException e) {
            fileLog.error(e.getMessage());
        }
    }

    /**method used to identify the players about the end of a match.
     * @param gameId - id of the room.*/
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

    /**method used to notify the players about the beginning of the last turn.
     * @param gameId - id of the room.
     * @param nickname - username of the player who first completed their shelf.*/
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

    /**method used to notify the players about someone getting a common goal card.
     * @param gameId - id of the room.
     * @param nickname - username of the players who won the card.
     * @param id - id of the common goal card gained.*/
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


    /**method notifyAllPlayers is used to send a model update to all the players in a match.
     * @param id - id of the match.
     * @param something - contains the model update*/
    public synchronized void notifyAllPlayers(String id, ModelUpdate something) {
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(id))
                .forEach(e -> {
                    try {
                        ConnectionManager.get().viewListenerMap.get(e.getKey()).updateModel(something);
                    } catch (RemoteException ex) {
                        fileLog.error(ex.getMessage());
                    }
                });
    }

    //TODO closeGame(gameId)


}
