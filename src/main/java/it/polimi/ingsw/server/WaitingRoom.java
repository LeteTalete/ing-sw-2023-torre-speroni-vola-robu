package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class WaitingRoom {
    private static Logger fileLog = LogManager.getRootLogger();
    private String id;
    private ServerManager master;
    private int playersWaiting;
    private int maxPLayers;
    private ArrayList<Player> players;
    private ArrayList<Player> overflowPlayers;
    //constructor
    public WaitingRoom(int identifier, ServerManager serverManager){
        this.id = String.valueOf(identifier);
        this.playersWaiting = 0;
        players = new ArrayList<Player>();
        this.master=serverManager;
        overflowPlayers = new ArrayList<Player>();
    }

    public String addPlayerToWaitingRoom(String name, String token){
        fileLog.info("Adding player "+name+" to waiting room "+this.id);
        Player p = new Player();
        p.setNickname(name);
        p.setTokenId(token);
        players.add(p);
        this.playersWaiting=this.playersWaiting+1;
        fileLog.info("There are currently "+playersWaiting+" players ready to play:");
        for (Player player : players) {
            fileLog.info(player.getNickname());
        }
        if(playersWaiting==maxPLayers)
        {
            fileLog.info("Enough players to start the game!");
            return StaticStrings.GAME_START;
        }
        return StaticStrings.GAME_WAITING;
    }

    public String getId(){
        return this.id;
    }
    public int getMaxPLayers(){
        return this.maxPLayers;
    }

    public ArrayList<Player> getListOfPlayers() {
        return this.players;
    }
    public Player getSinglePlayer(int i){
        return this.players.get(i);
    }

    public void setMaxPLayers(int maxPLayers) {
        this.maxPLayers = maxPLayers;
        if(playersWaiting>=maxPLayers)
        {
            fileLog.info("Enough players to start the game!");
            master.createGame(id);
            //return StaticStrings.GAME_START;
        }
        else if(playersWaiting>maxPLayers)
        {
            //return StaticStrings.GAME_START;
        }
    }
}
