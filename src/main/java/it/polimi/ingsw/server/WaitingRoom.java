package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class WaitingRoom {
    private String id;
    private int playersWaiting;
    private int maxPLayers;
    private ArrayList<Player> players;
    //constructor
    public WaitingRoom(int identifier, int max_players){
        this.id = "room-" + identifier;
        this.playersWaiting = 0;
        players = new ArrayList<Player>();
        this.maxPLayers = max_players;
    }

    public String addPlayerToWaitingRoom(String name, String token){
        System.out.println("I'm adding player "+name);
        Player p = new Player();
        p.setNickname(name);
        p.setTokenId(token);
        players.add(p);
        this.playersWaiting=this.playersWaiting+1;
        System.out.println("There are currently "+playersWaiting+" players ready to play:");
        for (Player player : players) {
            System.out.println(player.getNickname());
        }
        if(playersWaiting==maxPLayers)
        {
            System.out.println("ready to play!");
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
