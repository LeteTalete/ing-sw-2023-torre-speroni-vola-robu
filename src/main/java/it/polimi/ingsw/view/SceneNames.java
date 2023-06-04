package it.polimi.ingsw.view;

public enum SceneNames {
    CONNECTION("/fxml/Connection.fxml"),
    USERNAME("/fxml/UsernamePlayer.fxml"),
    LOADINGGAME("/fxml/UsernamePlayer.fxml"),
    NUMPLAYERS("/fxml/UsernamePlayer.fxml"),
    ENDGAME("/fxml/UsernamePlayer.fxml"),
    BOARDPLAYER("/fxml/BoardPlayer.fxml");
    private final String scaneString;
    SceneNames(final String string){
        this.scaneString = string;
    }
    public String scaneString(){
        return scaneString;
    }
}
