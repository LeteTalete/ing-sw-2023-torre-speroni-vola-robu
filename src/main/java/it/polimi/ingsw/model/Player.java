package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.cards.PersonalGoalCard;

import java.io.Serializable;

public class Player implements Serializable {
//attributes
    private boolean chair;
    private String nickname;
    private String tokenId;
    private Shelf shelf;
    private PersonalGoalCard personalGoalCard;
    private int score = 0;

    /** Constructor for the player class. It creates a new shelf for the player. */
    public Player(){
        this.shelf = new Shelf();
    }

    /**
     * Method getNickname returns the nickname of the player.
     * @return - the nickname of the player.
     */
    public String getNickname(){
        return this.nickname;
    }

    /**
     * Method setNickname sets the nickname of the player.
     * @param nm - the nickname of the player.
     */
    public void setNickname(String nm){
        this.nickname = new String(nm);
    }

    /**
     * Method getGoalCard returns the personal goal card of the player.
     * @return - the personal goal card of the player.
     */
    public PersonalGoalCard getGoalCard(){
        return this.personalGoalCard;
    }

    /**
     * Method setGoalCard sets the personal goal card of the player.
     * @param pGCard - the personal goal card of the player.
     */
    public void setGoalCard(int pGCard) {
        this.personalGoalCard = new PersonalGoalCard(pGCard);
    }

    /**
     * Method getMyShelf returns the shelf of the player.
     * @return - the shelf of the player.
     */
    public Shelf getMyShelf(){
        return this.shelf;
    }

    /**
     * Method setMyShelf sets the shelf of the player.
     * @param myShelf - the shelf of the player.
     */
    public void setMyShelf(Shelf myShelf){
        this.shelf = myShelf;
    }

    /** Method setChair sets the chair of the player to true. */
    public void setChair() {
        this.chair = true;
    }

    /**
     * Method getChair returns the chair of the player.
     * @return - the chair of the player.
     */
    public boolean getChair() {
        return this.chair;
    }

    /**
     * Method getScore returns the score of the player.
     * @return - the score of the player.
     */
    public int getScore (){
        return this.score;
    }

    /**
     * Method setScore adds new points to the score of the player.
     * @param points - the points to add to the score of the player.
     */
    public void setScore (int points){
        this.score += points;
    }

    /**
     * Method getTokenId returns the token id of the player.
     * @return - the token id of the player.
     */
    public String getTokenId() {
        return tokenId;
    }

    /**
     * Method setTokenId sets the token id of the player.
     * @param tokenId - the token id of the player.
     */
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}