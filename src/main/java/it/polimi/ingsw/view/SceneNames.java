package it.polimi.ingsw.view;

public enum SceneNames {
    CONNECTION("/fxml/Connection.fxml"),
    USERNAME("/fxml/UsernamePlayer.fxml"),
    WAITINGROOM("/fxml/LoadingGame.fxml"),
    NUMPLAYERS("/fxml/NumberPlayers.fxml"),
    ENDGAME("/fxml/ScoreEndGame.fxml"),
    BOARDPLAYER("/fxml/BoardPlayer.fxml");
    private final String scaneString;
    SceneNames(final String string){
        this.scaneString = string;
    }
    public String scaneString(){
        return scaneString;
    }
}
