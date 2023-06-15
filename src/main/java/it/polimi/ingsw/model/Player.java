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


    public Player(){
        this.shelf = new Shelf();
    }

    public String getNickname(){
        return this.nickname;
    }

    public void setNickname(String nm){
        this.nickname = new String(nm);
    }

    public PersonalGoalCard getGoalCard(){
        return this.personalGoalCard;
    }

    public void setGoalCard(int pGCard) {
        this.personalGoalCard = new PersonalGoalCard(pGCard);
    }

    public Shelf getMyShelf(){
        return this.shelf;
    }

    public void setMyShelf(Shelf myShelf){
        this.shelf = myShelf;
    }
    public void setChair() {
        this.chair = true;
    }
    public boolean getChair() {
        return this.chair;
    }
    public int getScore (){
        return this.score;
    }

    public void setScore (int myScore){
        this.score += myScore;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}