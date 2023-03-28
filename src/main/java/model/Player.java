package model;

import model.board.Shelf;
import model.enumerations.Tile;
import model.cards.GoalCard;
import model.cards.PersonalGoalCard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Player{
    private boolean chair;
    private String nickname;
    private boolean isPlaying;
    private Shelf shelf;
    private PersonalGoalCard personalGoalCard;
    public int score;
    private boolean endGame;

    private ArrayList<Tile> tilesChoosen;

    public GoalCard getGoalCard(){
        return this.personalGoalCard;
    }

    public void setGoalCard(PersonalGoalCard pGCard) {
        this.personalGoalCard = pGCard;
        return;
    }

    public Shelf getMyShelf(){
        return this.shelf;
    }

    public void setMyShelf(Shelf myShelf){
        this.shelf = myShelf;
        return;
    }


    public int getMyScore (){
        return score;
    }

    public void setMyScore (int myScore){
        this.score = myScore;
        return;
    }


    public ArrayList<Tile> chooseTiles(Tile tile){
    }

    public ArrayList<Tile> chooseOrder(ArrayList<Tile> tilesChosen){


    }

    public void chooseColumn(int col){

    }

    public void placeTilesOnShelf(Shelf shelf, ArrayList<Tile> tile) {

    }

    public String askNickname(){

        return nickname;
    }


}