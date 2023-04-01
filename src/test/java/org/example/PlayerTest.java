package org.example;

import model.Player;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest
{
    @Test
    void testChooseTiles()
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
}
