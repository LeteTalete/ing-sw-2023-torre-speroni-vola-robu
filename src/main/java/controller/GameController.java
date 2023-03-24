package controller;

import model.Player;
import model.Game;

public class GameController {

    private String ViewInterface;
    private Game game;
    private Player player;

    public GameController() {
        game = new Game();

        // ask player GUI or CLI
        // I suppose in the end the player will be presented with 2 buttons to choose from
        // I don't know if the ViewInterface (string variable) is needed but for now if
        // the player is going to choose (e.g. by writing CLI) then I believe passing this information
        // through a variable might be a start
        game.startView(game.askGUIorTUI(ViewInterface));

        // initialize game
        // Question: Since startGame should initialize the board shouldn't the number of players be known beforehand?
        //           The number of tiles on the board depends on the number of players playing, we can't setup the board
        // I suppose in the end the game will be started AFTER all the players have been connected
        //
        // Two scenarios?
        // 1. After the player connects he is presented a screen saying waiting for players to connect
        // 2. After the player connects he is presented the board and his shelfie (all empty)
        //    and is asked to wait for players
        // In the 2. it might be better to initialize the board, render it through view but without the setup
        //
        // Note: Perhaps a second method is needed for less confusion
        //       One method should be about the SETUP and another for the START itself of the game
        // SETUP: game.setupGAME(); or even game.initializeGame();
        // START: game.startGame();
        game.startGame();


        // initialize player
        player = new Player();

        // ask player name and add player
        game.addPlayers(player.askNickname());

        // create board
        // setup board
        // create shelfie
        // assign shelfie
        // start game
        // For those see "initialize game"




    }

    // while(!endgame)
    // ask player which tile | ( up to
    // check tile            |         3 times )
    // ask player tile order
    // check tile
    // ask if player is sure
    // insert tiles inside shelfie
    // if shelfie is full endgame = 1
    // else new turn
}

