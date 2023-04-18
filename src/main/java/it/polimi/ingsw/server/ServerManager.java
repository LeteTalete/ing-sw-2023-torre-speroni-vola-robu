package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class ServerManager {
    private HashSet<GameController> activeGames;
    private WaitingRoom waitingRoom;
    //network manager: to instantiate RMI and socket
    private NetworkManagerRMI networkManager;
    //active users probably also need some kind of connection so that we can ping them
    private ArrayList<Player> activeUsers;
    private ServerSocket serverSocket;

    //constructor
    public ServerManager(){
        activeGames = new HashSet<GameController>();
        try {
            networkManager = new NetworkManagerRMI(this);
            activeUsers = new ArrayList<Player>();
        } catch (RemoteException e) {
            //port error
            System.err.println("Remote exception: " + e.toString());
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            //name conflict in registry
            System.err.println("Already bound exception: " + e.toString());
            e.printStackTrace();
        }
    }
    public String registerUser(String name) {
        System.out.println("Here are the active users:");
        for (Player playr : activeUsers) {
            System.out.println(playr.getNickname());
        }
        for (Player plt : activeUsers){
            if(plt.getNickname().equals(name)){
                return "Already exists";
            }
        }
        //should probably add some kind of connection reference to each client
        Player newPlayer = new Player();
        newPlayer.setNickname(name);
        activeUsers.add(newPlayer);
        System.out.println("User " + name + " added successfully");
        String response = putInWaitingRoom(name);
        if(response.equals("Ready")){
            return "Starting game...";
        }
        else return response;
    }

    public String putInWaitingRoom(String name){
        if(waitingRoom==null){
            System.out.println("i will create a new waiting room for you");
            return "Creating new";
        }
        else{
            String enoughPLayers = waitingRoom.addPlayerToWaitingRoom(name);
            System.out.println("Added user "+ name + " to waiting room "+waitingRoom.getId()+" successfully!");
            if(enoughPLayers.equals("Ready")){
                GameController game = new GameController(waitingRoom.getListOfPlayers(), waitingRoom.getId());
                activeGames.add(game);
                System.out.println("i deleted the waiting room "+waitingRoom.getId()+" and started the game!");
                System.out.println("there are currently "+activeGames.size()+" games active!");
                game.main();
                waitingRoom=null;
                return enoughPLayers;
            }
        return "Success!";
        }
    }
    public String askHowMany(int num, String name) {
        return createWaitingRoom(name, num);
    }

    public String createWaitingRoom (String name, int howMany){
        //this needs to ask the player how many others we're waiting for
        Random rand = new Random();
        int id = rand.nextInt(1000);
        waitingRoom = new WaitingRoom(id, howMany);
        waitingRoom.addPlayerToWaitingRoom(name);
        return "Done, I've created a waiting room!";
    }

    public void periodicPing (){
        //this has to send a ping to all the players in all the active games
        //if it does not catch a single pong from one player, it shuts down the entire game
        //if the game is shut down, the server will notify all of the players about it
    }

}
