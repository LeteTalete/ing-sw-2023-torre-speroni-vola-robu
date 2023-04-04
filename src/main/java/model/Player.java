package model;

import Exceptions.InvalidChoiceFormatException;
import model.board.LivingRoom;
import model.board.Shelf;
import model.enumerations.Tile;
import model.cards.GoalCard;
import model.cards.PersonalGoalCard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player{
//attributes
    private boolean chair;
    private String nickname;
    private boolean isPlaying;
    private Shelf shelf;
    private int personalGoalCard;
    public int score;
    private boolean endGame;
    private LivingRoom livingRoom;

    private ArrayList<Position> tilesChoosen;

//methods
    public ArrayList<Position> chooseTiles(){
        //this method will ask the player to insert the coordinates of the tiles he wants to pick up
        //if the choice is valid and he confirms this method will return an ArrayList containing the positions
        // of the tiles choosen

        ArrayList<Position> choice = new ArrayList<Position>();
        boolean flag = false;
        String user_input;
        char confirm;

        while(!flag)
        {
            if(!choice.isEmpty()) choice.clear(); //this is for when the user doesn't confirm his choice and wants to make another one
            Scanner sc= new Scanner(System.in); //System.in is a standard input stream
            System.out.print("insert coordinates: ");
            user_input = sc.nextLine();              //reads string
            sc.close();
            try
            {
               if(!checkUserInput(user_input)) throw new InvalidChoiceFormatException();
               // here i am sure that the user_input is in a correct format
                // now i can add chosen positions
               for(int i=0;i<user_input.length();i++)
               {
                   if(i%3 == 0)
                   {
                    choice.add(new Position());
                    choice.get(i/3).setX(user_input.charAt(i)-48);
                   }
                   else if((i+1)%3 != 0)
                   {
                     choice.get((i-1)/3).setY(user_input.charAt(i)-48);
                   }
               }
               if(livingRoom.checkPlayerChoice(choice))
               {
                   do {
                       sc= new Scanner(System.in); //System.in is a standard input stream
                       System.out.print("confirm? ");
                       confirm = sc.next().charAt(0);              //reads string
                       sc.close();
                       if(confirm != 'y' && confirm != 'n')
                       {
                           System.out.println("please insert y or n");
                       }
                      }
                   while (confirm != 'y' && confirm != 'n');
                   if(confirm == 'y') flag = true;
               }
               else System.out.println("This choice is not valid, please make another choice");
            }
            catch (InvalidChoiceFormatException exc)
            {
                System.out.println(exc.toString());
            }
        }
        livingRoom.setPickedCouples(choice);

        return choice;
    }

    public boolean checkUserInput(String s)
    {
        //user input should be like this: "02" or "39 45" or "54 11 64"
        //from 1 to 3 couples of int separated by a space
        //note: ASCII: '0' = 48 ... '9' = 57
        //note: 'space' = 32
        int l = s.length();

        if(l!=2 && l!=5 && l!=8) return false;
        if(l > 5)
        {
            if(s.charAt(5) != 32) return false;
            if(s.charAt(6) < 48 || s.charAt(6) > 57) return false;
            if(s.charAt(7) < 48 || s.charAt(7) > 57) return false;
        }
        if(l > 2)
        {
            if(s.charAt(2) != 32) return false;
            if(s.charAt(3) < 48 || s.charAt(3) > 57) return false;
            if(s.charAt(4) < 48 || s.charAt(4) > 57) return false;
        }
        if(s.charAt(0) < 48 || s.charAt(0) > 57) return false;
        if(s.charAt(1) < 48 || s.charAt(1) > 57) return false;
        return true;
    }

    public ArrayList<Position> chooseOrder(ArrayList<Position> tilesChosen){
        //this method asks the player to select in wich order insert tiles choosen in the shelf

        ArrayList<Position> tiles = (ArrayList<Position>) tilesChosen.clone(); //making a deep copy for safety
        Tile t;
        String order_input = null;
        boolean flag = false;
        char confirm;

        if(tiles.size()>1)
        {
            System.out.print("tiles: ");
            for(int i=1;i<=tiles.size();i++)
            {
                t = livingRoom.getCouple(tiles.get(i-1)).getTile();
                System.out.print("(" + i + ") " + t.getTileType() + "_" + t.getFigure() + "  ");
                //example: "tiles: (1) CAT_1  (2) BOOK_1  (3) CAT_3"
            }
            System.out.println();
            while(!flag)
            {
                Scanner sc= new Scanner(System.in); //System.in is a standard input stream
                System.out.print("choose order: ");
                order_input = sc.nextLine();              //reads string
                sc.close();
                try
                {
                    if(!checkOrderInput(order_input)) throw new InvalidChoiceFormatException();
                    else
                    {
                        do{
                            sc= new Scanner(System.in); //System.in is a standard input stream
                            System.out.print("confirm? ");
                            confirm = sc.next().charAt(0);              //reads string
                            sc.close();
                            if(confirm != 'y' && confirm != 'n')
                            {
                                System.out.println("please insert y or n");
                            }
                        }
                        while (confirm != 'y' && confirm != 'n');
                        if(confirm == 'y') flag = true;
                    }
                }
                catch (InvalidChoiceFormatException exc)
                {
                    System.out.println(exc.toString());
                }
            }
            //here i am sure that the input is valid and the player confirmed his choice
            //now i have to order the positions array as the player chosed
            int index = 0;
            for(int i=0; i<order_input.length(); i++)
            {
                if(order_input.charAt(i) != 32)
                {
                    tiles.set(index,tilesChosen.get(order_input.charAt(i)-48-1));
                    index++;
                }
            }

            return tiles;
        }
        else return tiles; //if player selected only one tile, order is implicit
    }

    public boolean checkOrderInput(String s)
    {
        //input should be like this: "1 2 3" or "2 1 3" or "3 1 2"
        //three different integers from 1 to 3 separated by a space
        //note: ASCII: '0' = 48 ... '9' = 57
        //note: 'space' = 32

        if(s.length()!=5) return false;
        if(s.charAt(0) < 49 || s.charAt(0) > 51) return false;
        if(s.charAt(1) != 32) return false;
        if(s.charAt(2) < 49 || s.charAt(2) > 51) return false;
        if(s.charAt(3) != 32) return false;
        if(s.charAt(4) < 49 || s.charAt(4) > 51) return false;
        if(s.charAt(0) == s.charAt(2) || s.charAt(0) == s.charAt(4) || s.charAt(2) == s.charAt(4)) return false;

        return true;
    }

    public void chooseColumn(int col){

    }

    public String askNickname(){

        return nickname;
    }

    public String getNickname(){
        return this.nickname;
    }

    public void setNickname(String nm){
        //not sure send help I can't remember
        this.nickname = new String(nm);
    }

    public int getGoalCard(){
        return this.personalGoalCard;
    }

    public void setGoalCard(int pGCard) {
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
        return this.score;
    }

    public void setMyScore (int myScore){
        this.score = myScore;
        return;
    }







}