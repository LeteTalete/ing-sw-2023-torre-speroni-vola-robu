package model;

import model.board.Shelf;
import model.enumerations.Tile;
import model.cards.GoalCard;
import model.cards.PersonalGoalCard;

import java.util.ArrayList;
import java.util.List;

public class Player{
    private boolean Chair;
    private String nickname;
    private boolean isPlaying;
    private Shelf shelf;
    private PersonalGoalCard personalgoalcard;
    public int score;
    private boolean endGame;

    public GoalCard getGoalCard(GoalCard card){
        return card;
    }

    public Shelf getMyShelf(){
        return shelf;
    }

    public Shelf getOtherShelf(){
    }

    public int getMyScore (){

    }

    public int getOtherScore(){
    }

    public List<Tile> chooseTile(Tile tile){
    }

    public void placeTilesOnShelf(Shelf shelf, ArrayList<Tile> tile) {

    }

    public String askNickname(){

        return nickname;
    }


}