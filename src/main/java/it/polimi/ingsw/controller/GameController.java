package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Game;

public class GameController {

    private static String ViewInterface;
    private static Game game;

    private static Player player;

    public static void main() {

            game = new Game();
            /**this is wrong! this should be asked by the server, not by the game!**/
            int numberofplayers = game.askHowManyPlayers();

            // create and setup board (we're assuming this all happens in the next instruction)
            LivingRoom board = new LivingRoom(numberofplayers);

            /**once the living room is set, controller decides who's first and re-arrangers the
             * list of players accordingly**/


            /**has a method to start a turn, which will notify each player that it's "nickname"'s turn**/

            /**has a method to change turns (it could already be implemented into Game**/



        }



}
