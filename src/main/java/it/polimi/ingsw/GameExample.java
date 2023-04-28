package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

import java.util.ArrayList;
import java.util.Scanner;

public class GameExample {

    public static void main( String[] args ) {

        System.out.println("Choose number of players (2/3/4): ");
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        sc.nextLine();

        ArrayList<Player> players = new ArrayList<>();
        for ( int i = 0; i < a; i++){
            Player player = new Player();
            System.out.println("Player " + i + " choose a nickname: ");
            player.setNickname(sc.nextLine());
            players.add(player);
        }

        GameController game = new GameController(players, 0);
        game.generateCGC(players.size());
        game.generatePGC(players);
        game.chooseFirstPlayer();
        System.out.println("The first player will be: " + game.getCurrentPlayer().getNickname());

        int undo = 0;
        int end = 0;
        int endTurn = 0;
        while( end != players.size() - 1 ){
            System.out.println("The current player is: " + game.getCurrentPlayer().getNickname());
            System.out.println();
            endTurn = 0;
            undo = 0;

            while( endTurn != 1 ){
                ArrayList<Position> hand = new ArrayList<>();
                game.getGameBoard().printBoard();
                System.out.println();
                hand = game.getCurrentPlayer().chooseTiles(game.getGameBoard());
                System.out.print("Tiles chosen: ");
                for ( Position position : hand ){
                    System.out.print(game.getGameBoard().getCouple(position).getTile().getTileType() + " ");
                }
                System.out.println();
                System.out.println("To start over write 'undo' else to continue write anything else ");
                if ( sc.nextLine().equals("undo") ) {
                    undo = 1;
                }
                    while (undo == 0 && endTurn != 1) {
                        hand = game.getCurrentPlayer().choseOrder(hand, game.getGameBoard());
                        System.out.print("Order chosen: ");
                        int k = 1;
                        for ( Position position : hand ){
                            System.out.print( k + ". " + game.getGameBoard().getCouple(position).getTile().getTileType() + " ");
                            k++;
                        }
                        System.out.println();
                        System.out.println("To go back and choose different tiles write 'undo' else to continue write anything else ");
                        if ( sc.nextLine().equals("undo") ) {
                            undo = 1;
                        }
                        while ( undo == 0 && endTurn != 1) {
                            game.getCurrentPlayer().getMyShelf().printShelf();
                            System.out.println("To go back and choose a different tiles order write 'undo' else to continue write anything else ");
                            if ( !sc.nextLine().equals("undo") ) {
                                game.getCurrentPlayer().chooseColumn(hand, game.getGameBoard());
                                game.getCurrentPlayer().getMyShelf().printShelf();
                                endTurn = 1;
                            } else {
                                break;
                            }
                        }
                    }
                undo = 0;
            }

            if ( game.getCurrentPlayer().getMyShelf().checkShelfFull() ){
                end++;
            } else if ( end != 0 ){
                end++;
            }

            game.nextTurn();
            System.out.println();
            System.out.println();

        }

    }

}
