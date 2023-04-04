package org.example;

import Exceptions.InvalidChoiceFormatException;
import model.Player;
import model.Position;
import model.board.LivingRoom;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest
{
    @Test
    public void testCheckUserInput()
    {
        Player p = new Player();
        assertTrue(p.checkUserInput("09"));
        assertTrue(p.checkUserInput("09 84"));
        assertTrue(p.checkUserInput("09 84 11"));
        assertFalse(p.checkUserInput(""));
        assertFalse(p.checkUserInput("0"));
        assertFalse(p.checkUserInput("ab"));
        assertFalse(p.checkUserInput("0a"));
        assertFalse(p.checkUserInput("09 "));
        assertFalse(p.checkUserInput("09 69 1B"));
        assertFalse(p.checkUserInput("09a84b11"));
        assertFalse(p.checkUserInput("09 84 11 99"));
    }

    @Test
    public void testCheckOrderInput()
    {
        Player p = new Player();

        assertTrue(p.checkOrderInput("1 2 3"));
        assertTrue(p.checkOrderInput("2 3 1"));
        assertFalse(p.checkOrderInput("1 2 "));
        assertFalse(p.checkOrderInput("1 2 4"));
        assertFalse(p.checkOrderInput(""));
        assertFalse(p.checkOrderInput("1 2 3 4"));
        assertFalse(p.checkOrderInput("123"));
        assertFalse(p.checkOrderInput(" 1 2 3"));
        assertFalse(p.checkOrderInput("a b c"));
        assertFalse(p.checkOrderInput("1 2 c"));
    }

    @Test
    public void testChooseTiles()
    {
        //this method will ask the player to insert the coordinates of the tiles he wants to pick up
        //if the choice is valid and he confirms this method will return an ArrayList containing the positions
        // of the tiles choosen

        Player p = new Player();
        LivingRoom livingRoom = new LivingRoom(4);
        ArrayList<Position> choice = new ArrayList<Position>();
        boolean flag = false;
        String user_input = "09 84 11";
        char confirm = 'y';

        while(!flag)
        {
            if(!choice.isEmpty()) choice.clear(); //this is for when the user doesn't confirm his choice and wants to make another one
            System.out.println("insert coordinates: " + user_input);
            try
            {
                if(!p.checkUserInput(user_input)) throw new InvalidChoiceFormatException();
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
                        System.out.print("confirm? " + confirm);
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
    }

}
