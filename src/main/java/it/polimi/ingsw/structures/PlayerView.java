package it.polimi.ingsw.structures;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;

public class PlayerView implements Serializable {

    private String nickname;
    private ShelfView shelfView;
    private int personalGoalCard;
    public int score;

    public PlayerView(Player player){
        this.nickname = player.getNickname();
        this.shelfView = new ShelfView(player.getMyShelf());
        this.personalGoalCard = player.getGoalCard();
        this.score = player.getMyScore();
    }

    public ShelfView getShelf(){
        return this.shelfView;
    }

    public String getNickname(){
        return this.nickname;
    }
    public int getScore(){
        return this.score;
    }

    public int getPersonalGoalCard(){
        return this.personalGoalCard;
    }




}
