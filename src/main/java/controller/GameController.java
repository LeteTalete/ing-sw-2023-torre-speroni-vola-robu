package controller;

import model.Player;
import model.Game;
import model.board.LivingRoom;
import model.enumerations.Couple;

import java.sql.Array;

public class GameController {

    private static String ViewInterface;
    private static Game game;
    private static Player player;

    public static void main() {

            game = new Game();
            int numberofplayers = game.askHowManyPlayers();

            // ask player GUI or CLI
            // This should not be here?
            // The player class does not have an attribute related to view
            // The view that the player chooses is something the client stores (?)
            // In other words there is no need to keep an array or an attribute within game or player about the view choices
            // game.startView(game.askGUIorTUI(ViewInterface));

            // create board
            // setup board
            LivingRoom board = new LivingRoom(numberofplayers);

            // initialize player
            // ask player name and add player
            // After a player connects the method game.addPlayer is called
            // It takes the array of players and asks the player his nickname
            // then it adds the nickname to the corresponding player object
            Player[] Array = new Player[numberofplayers];
            game.addPlayers( Array, player.askNickname());

            // After all players have connected and have chosen a nickname the game can start
            // startgame is given the array of player objects as a parameter
            // This allows game to iter through each player instance
            game.startGame(board, Array);




        }



}
