package it.polimi.ingsw.view;

public enum SceneNames {
    CONNECTION("/fxml/Connection.fxml"),
    USERNAME("/fxml/UsernamePlayer.fxml"),
    WAITINGROOM("/fxml/WaitingPlayers.fxml"),
    NUMPLAYERS("/fxml/NumberPlayers.fxml"),
    ENDGAME("/fxml/ScoreEndGame.fxml"),
    BOARDPLAYER("/fxml/BoardPlayer.fxml"),
    SHELFPLAYERS("/fxml/ShelfPlayers.fxml");
    private final String scaneString;
    SceneNames(final String string){
        this.scaneString = string;
    }
    public String scaneString(){
        return scaneString;
    }
}
