package it.polimi.ingsw.structures;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PersonalGoalCard;

import java.io.Serializable;

public class PlayerView implements Serializable {

    private String nickname;
    private ShelfView shelfView;
    private PersonalGoalCard personalGoalCard;
    private final int score;
    private boolean chair;

    /**
     * Constructor PlayerView creates a new PlayerView instance. It sets the attributes of the class so that the
     * information of the player(s) can be shown on the view.
     * @param player - the player.
     */
    public PlayerView(Player player){
        this.nickname = player.getNickname();
        this.shelfView = new ShelfView(player.getMyShelf());
        this.personalGoalCard = player.getGoalCard();
        this.score = player.getScore();
        this.chair = player.getChair();
    }

    /**
     * Method getShelf returns the shelf of the player.
     * @return - the shelf of the player.
     */
    public ShelfView getShelf(){
        return this.shelfView;
    }

    /**
     * Method getNickname returns the nickname of the player.
     * @return - the nickname of the player.
     */
    public String getNickname(){
        return this.nickname;
    }

    /**
     * Method getScore returns the score of the player.
     * @return - the score of the player.
     */
    public int getScore(){
        return this.score;
    }

    /**
     * Method getPersonalGoalCard returns the personal goal card of the player.
     * @return - the personal goal card of the player.
     */
    public PersonalGoalCard getPersonalGoalCard(){
        return this.personalGoalCard;
    }

}
