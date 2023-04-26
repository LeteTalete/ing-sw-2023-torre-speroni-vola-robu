package it.polimi.ingsw.structures;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PersonalGoalCard;

import java.io.Serializable;

public class PlayerView implements Serializable {

    private String nickname;
    private ShelfView shelfView;
    private PersonalGoalCard personalGoalCard;
    public int score;

    public PlayerView(Player player){
        this.nickname = player.getNickname();
        this.shelfView = new ShelfView(player.getMyShelf());
        this.personalGoalCard = player.getGoalCard();
        this.score = player.getScore();
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

    public PersonalGoalCard getPersonalGoalCard(){
        return this.personalGoalCard;
    }


}
