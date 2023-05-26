package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class WaitingRoom {
    private static Logger fileLog = LogManager.getRootLogger();
    private String id;
    private int playersWaiting;
    private int maxPLayers;
    private ArrayList<Player> players;
    //constructor
    public WaitingRoom(int identifier, int max_players){
        this.id = String.valueOf(identifier);
        this.playersWaiting = 0;
        players = new ArrayList<Player>();
        this.maxPLayers = max_players;
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
            System.out.println(player.getNickname());
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

    public ArrayList<Player> getListOfPlayers() {
        return this.players;
    }
}
