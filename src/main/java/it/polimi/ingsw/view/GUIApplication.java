package it.polimi.ingsw.view;

import com.sun.javafx.scene.text.GlyphList;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.view.ControllerGUI.BoardPlayer;
import it.polimi.ingsw.view.ControllerGUI.ConnectionPlayer;
import it.polimi.ingsw.view.ControllerGUI.GenericController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class GUIApplication extends Application {
    //private static GenericController genericController;
    public static ClientGUI clientGUI;
    private static GenericController genericController;
    private static Stage stageWindow;

    @Override
    public void start(Stage stage) throws Exception {
        stageWindow = stage;
        showSceneName(SceneNames.CONNECTION);
        stageWindow.setOnCloseRequest(event -> System.exit(0));
    }


    private static void setUpStage(SceneNames sceneNames){
        try {
            FXMLLoader loader = new FXMLLoader(GUIApplication.class.getResource(sceneNames.scaneString()));
            Scene scene = new Scene(loader.load());
            Image icon = new Image(Objects.requireNonNull(GUIApplication.class.getResourceAsStream("/imgs/Icon.png")));
            genericController = loader.getController();
            //activeGenericController();
            stageWindow.getIcons().add(icon);
            stageWindow.setResizable(false); //Non modifica le dimensioni della finestra
            stageWindow.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void activeGenericController(){
        ((BoardPlayer) genericController).setBoadPlayer();
    }

    public static void showSceneName(SceneNames sceneNames){
        Platform.runLater(()-> {
            setUpStage(sceneNames);
            if(sceneNames.equals(SceneNames.CONNECTION)){
                stageWindow.setHeight(220);
                stageWindow.setTitle("Choose Connection");
            } else if(sceneNames.equals(SceneNames.USERNAME)){
                stageWindow.setTitle("Choose name");
            } else if(sceneNames.equals(SceneNames.NUMPLAYERS)) {
                stageWindow.setTitle("Choose number players");
            } else if (sceneNames.equals(SceneNames.WAITINGROOM)){
                stageWindow.setTitle("Waiting Room");
            } else if(sceneNames.equals(SceneNames.BOARDPLAYER)){
                stageWindow.setTitle("MyShelfie");
            }
            stageWindow.show();
        });
    }

    public static Stage getStageWindow(){
        return stageWindow;
    }

    public static void closeStage(){
        if(stageWindow != null){
            stageWindow.close();
        }
    }


    public static void main(String args[]){
        launch(args);
    }

}
