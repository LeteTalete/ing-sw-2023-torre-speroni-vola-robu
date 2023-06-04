package it.polimi.ingsw.view;

import it.polimi.ingsw.view.ControllerGUI.ConnectionPlayer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class GUIApplication extends Application {

    private Stage stage;
    private FXMLLoader loader;

    @Override
    public void start(Stage stage) throws Exception {
        setStage(stage);
        showSceneName(SceneNames.CONNECTION);
    }

    public void setUpStage(SceneNames sceneNames){
        try{
            this.loader = new FXMLLoader(getClass().getResource(sceneNames.scaneString()));
            Scene scene = new Scene(this.loader.load());
            this.stage.setScene(scene);
            this.stage.setResizable(false); //Non modifica le dimensioni della finestra
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void showSceneName(SceneNames sceneNames){
        setUpStage(sceneNames);
        if(sceneNames.equals(SceneNames.CONNECTION)){
            //Finestra in cui il giocatore deve scegliere la ripologia di connessione
            ConnectionPlayer connectionPlayer = this.loader.getController();
            connectionPlayer.setWindowConnection(stage);
            this.stage.setHeight(220);
            this.stage.setTitle("Choose Connection");
            this.stage.show();
        } else if(sceneNames.equals(SceneNames.USERNAME)){


        }

    }

    public static void main(String args[]){
        launch(args);
    }

}
