package it.polimi.ingsw.view.ControllerGUI;

import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.view.GUIApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * EndGame class is the controller for the scene of the end of the game containing the scoreboard with the results
 * */

public class EndGame extends GenericController {
    /**
     * Label labelStateGame is the label that shows if the player has won or lost
     * */
    @FXML private Label labelStateGame;
    /**
     * score is the VBox containing the scoreboard
     * */
    @FXML private VBox score;

    /**
     * method setEndGame sets the scene of the end of the game by displaying "winner" or "loser" depending on the
     * game's outcome. It also creates an HBox for each player containing the position, the nickname and the score
     * calculated at the end of the game
     * */
    public void setEndGame(){

        ArrayList<PlayerView> playersSorted = new ArrayList<>();
        playersSorted.addAll(GUIApplication.getClientGUI().getGameView().getPlayersView());
        Collections.sort(playersSorted, new Comparator<PlayerView>() {
            @Override
            public int compare(PlayerView player1, PlayerView player2) {
                return Integer.compare(player2.getScore(), player1.getScore());
            }
        });

        if(playersSorted.get(0).getNickname().equals(GUIApplication.getClientGUI().getName())){
            labelStateGame.setText("WINNER");
        } else {
            labelStateGame.setText("LOSER");
        }
        for(int i = 0; i < playersSorted.size(); i++ ){
            HBox scorePlayers = (HBox) score.getChildren().get(i+1);
            ((Label) scorePlayers.getChildren().get(0)).setText(Integer.toString(i+1) + ".");
            ((Label) scorePlayers.getChildren().get(1)).setText(playersSorted.get(i).getNickname());
            ((Label) scorePlayers.getChildren().get(2)).setText(Integer.toString(playersSorted.get(i).getScore()));
        }
    }

}
