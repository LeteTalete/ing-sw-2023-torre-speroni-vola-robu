package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class WaitingRoom {
    private int id;
    private int playersWaiting;
    private int maxPLayers;
    private ArrayList<Player> players;
    //constructor
    public WaitingRoom(int identifier, int max_players){
        this.id = identifier;
        this.playersWaiting = 0;
        players = new ArrayList<Player>();
        this.maxPLayers = max_players;
    }

    public String addPlayerToWaitingRoom(String name){
        Player p = new Player();
        p.setNickname(name);
        players.add(p);
        this.playersWaiting=this.playersWaiting+1;
        System.out.println("There are currently "+playersWaiting+" players ready to play:");
        for (Player player : players) {
            System.out.println(player.getNickname());
        }
        if(playersWaiting==maxPLayers)
        {
            System.out.println("ready to play!");
            return "Ready";
        }
        return "Waiting";
    }

    public int getId(){
        return this.id;
    }

    public ArrayList<Player> getListOfPlayers() {
        return this.players;
    }
}