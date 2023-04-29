package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.cards.CommonGoalCard;

import java.util.ArrayList;
import java.util.List;
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
        game.generatePGC(game.getPlayers());
        game.chooseFirstPlayer();
        System.out.println("The first player will be: " + game.getCurrentPlayer().getNickname());
        System.out.println();

        int end = 0;
        int endTurn = 0;
        while( end != players.size() - 1 ){
            System.out.println("The current player is: " + game.getCurrentPlayer().getNickname());
            System.out.println();
            endTurn = 0;

            while( endTurn != 1 ) {
                ArrayList<Position> hand = new ArrayList<>();
                game.getGameBoard().printBoard();
                System.out.println();
                hand = game.getCurrentPlayer().chooseTiles(game.getGameBoard());
                System.out.print("Tiles chosen: ");
                for (Position position : hand) {
                    System.out.print(game.getGameBoard().getCouple(position).getTile().getTileType() + " ");
                }
                System.out.println();
                System.out.println("To start over write 'undo' else to continue write anything else ");
                if ( !sc.nextLine().equals("undo") ) {
                    while (endTurn != 1) {
                        hand = game.getCurrentPlayer().choseOrder(hand, game.getGameBoard());
                        System.out.print("Order chosen: ");
                        int k = 1;
                        for (Position position : hand) {
                            System.out.print( k + ". " + game.getGameBoard().getCouple(position).getTile().getTileType() + " ");
                            k++;
                        }
                        System.out.println();
                        System.out.println("To go back and choose different tiles write 'undo' else to continue write anything else ");
                        if (sc.nextLine().equals("undo")) {
                            break;
                        }
                        while (endTurn != 1) {
                            game.getCurrentPlayer().getMyShelf().printShelf();
                            System.out.println("To go back and choose a different tiles order write 'undo' else to continue write anything else ");
                            if (sc.nextLine().equals("undo")) {
                                break;
                            } else {
                                game.getCurrentPlayer().chooseColumn(hand, game.getGameBoard());
                                game.getCurrentPlayer().getMyShelf().printShelf();
                                endTurn = 1;
                            }
                        }
                    }
                }

            }

            if ( game.getCurrentPlayer().getMyShelf().checkShelfFull() ){
                end++;
            } else if ( end != 0 ){
                end++;
            }

            for (CommonGoalCard card : game.getCommonGoalCards() ) {
                if ( card.checkConditions(game.getCurrentPlayer().getMyShelf()) == 1 ) {
                    game.getCurrentPlayer().setScore(card.getPoints().pop());
                }
            }

            game.nextTurn();
            System.out.println();
            System.out.println();
        }

        System.out.println("Calculating scores...");
        game.calculateScore();
        Player highestScore = game.getPlayers().get(0);
        for ( Player player : game.getPlayers() ){
            System.out.println( player.getNickname() + "'s score is: " + player.getScore());
            if (player.getScore() > highestScore.getScore()) {
                highestScore = player;
            }
        }
        System.out.println();
        System.out.println("The winner is: " + highestScore.getNickname());
    }

}
