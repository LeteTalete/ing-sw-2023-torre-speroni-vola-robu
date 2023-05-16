package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
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

    public synchronized String putInWaitingRoom(String name){
        if(waitingRoom==null){
            System.out.println("i will create a new waiting room for you");
            return null;
        }
        else{
            String enoughPLayers = waitingRoom.addPlayerToWaitingRoom(name);
            activeUsers.put(name,waitingRoom.getId());
            System.out.println("Added user "+ name + " to waiting room "+waitingRoom.getId()+" successfully!");
            if(enoughPLayers.equals(StaticStrings.GAME_START)){
                //i have to create the players when creating the game
                GameController game = new GameController(waitingRoom.getListOfPlayers(), waitingRoom.getId());
                activeGames.put(waitingRoom.getId(), game);
                //
                System.out.println("i deleted the waiting room "+waitingRoom.getId()+" and started the game!");
                System.out.println("there are currently "+activeGames.size()+" games active!");
                //now we need to notify all the players that the game is about to start
                SendNotifToPlayers(waitingRoom.getId(), StaticStrings.GAME_START);

                game.initialize();
                waitingRoom=null;
                return enoughPLayers;
            }
        return waitingRoom.getId();
        }
    }

    private void SendNotifToPlayers(String id, String something) {
        activeUsers.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(id))
                .forEach(e -> ConnectionManager.get().getLocalView(e.getKey()).sendNotification(something));
    }

    public synchronized String createWaitingRoom (String name, int howMany){
        //this needs to ask the player how many others we're waiting for
        Random rand = new Random();
        int id = rand.nextInt(1000);
        waitingRoom = new WaitingRoom(id, howMany);
        waitingRoom.addPlayerToWaitingRoom(name);
        System.out.println("I created waiting room " + id + " for " + howMany + " players");
        return waitingRoom.getId();
    }

    public synchronized void periodicPing (){
        //this has to send a ping to all the players in all the active games
        //if it does not catch a single pong from one player, it shuts down the entire game
        //if the game is shut down, the server will notify all the players about it
    }

    @Override
    public String login(String name, IClientListener viewListener) throws RemoteException {
        String success = ConnectionManager.get().addClientView(name, viewListener);
        if(success.equals(StaticStrings.LOGIN_KO)){
            return success;
        }
        ConnectionManager.get().getLocalView(name).processLogin("Server registered you as a user");
        String response = putInWaitingRoom(name);
        if(response==null){
            int num = ConnectionManager.get().getLocalView(name).askHowMany();
            response = createWaitingRoom(name, num);
            activeUsers.put(name, response);
            return StaticStrings.LOGIN_OK_NEW_ROOM;
        }
        if(response.equals(StaticStrings.GAME_START))
        {
            return response;
        }
        return StaticStrings.WAITING_4_START;
    }

    @Override
    public void pickedTiles(String username, String tilesCoordinates) throws RemoteException {

    }

    @Override
    public void rearrangeTiles(String username, String tilesOrdered) throws RemoteException {

    }

    @Override
    public void selectColumn(String username, int column) throws RemoteException {

    }

}
