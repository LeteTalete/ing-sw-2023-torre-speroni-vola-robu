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

public class EndGame extends GenericController {
    @FXML private Label labelStateGame;
    @FXML private VBox score;

    public void setEndGame(){

        ArrayList<PlayerView> playersSorted = new ArrayList<>();
        playersSorted.addAll(GUIApplication.clientGUI.getGameView().getPlayersView());
        Collections.sort(playersSorted, new Comparator<PlayerView>() {
            @Override
            public int compare(PlayerView player1, PlayerView player2) {
                return Integer.compare(player2.getScore(), player1.getScore());
            }
        });

        if(playersSorted.get(0).getNickname().equals(GUIApplication.clientGUI.getName())){
            labelStateGame.setText("WINNER");
        } else {
            labelStateGame.setText("LOSER");
        }
        for(int i = 0; i < playersSorted.size(); i++ ){
            HBox scorePlayers = (HBox) score.getChildren().get(i+1);
            ((Label) scorePlayers.getChildren().get(0)).setText(Integer.toString(i+1));
            ((Label) scorePlayers.getChildren().get(1)).setText(playersSorted.get(i).getNickname());
            ((Label) scorePlayers.getChildren().get(2)).setText(Integer.toString(playersSorted.get(i).getScore()));
        }
    }

}
