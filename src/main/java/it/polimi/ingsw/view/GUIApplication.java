package it.polimi.ingsw.view;

import com.sun.javafx.scene.text.GlyphList;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.structures.GameView;
import it.polimi.ingsw.structures.LivingRoomView;
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
    private static FXMLLoader loaderController;
    private static BoardPlayer boardPlayer;
    private static Stage stageWindow;
    private static Scene sceneWindow;

    @Override
    public void start(Stage stage) throws Exception {
        stageWindow = stage;
        Image icon = new Image(Objects.requireNonNull(GUIApplication.class.getResourceAsStream("/imgs/Icon.png")));
        stageWindow.getIcons().add(icon);
        stageWindow.setResizable(false);
        showSceneName(SceneNames.CONNECTION);
        stageWindow.setOnCloseRequest(event -> System.exit(0));
    }


    private static void setUpScene(SceneNames sceneNames){
        try {
            loaderController = new FXMLLoader(GUIApplication.class.getResource(sceneNames.scaneString()));
            Parent root = loaderController.load();
            sceneWindow = new Scene(root);
            //Image icon = new Image(Objects.requireNonNull(GUIApplication.class.getResourceAsStream("/imgs/Icon.png")));
            //activeGenericController();
            //stageWindow.getIcons().add(icon);
            //stageWindow.setResizable(false); //Non modifica le dimensioni della finestra
            stageWindow.setHeight(root.prefHeight(-1));
            stageWindow.setWidth( root.prefWidth(-1));
            stageWindow.setScene(sceneWindow);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateLivingRoom(LivingRoomView livingRoomView){
        boardPlayer.setLivingRoom(livingRoomView);
    }

    public static void setBoardPlayer(){
        boardPlayer = loaderController.getController();
        boardPlayer.setBoadPlayer(clientGUI.getGameView());
        updateLivingRoom(clientGUI.getGameView().getGameBoardView());
    }

    public static void showSceneName(SceneNames sceneNames){
        Platform.runLater(()-> {
            setUpScene(sceneNames);
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
                setBoardPlayer();
            }
            stageWindow.show();
            stageWindow.centerOnScreen();
        });
    }
    public static Scene getSceneWindow(){
        return sceneWindow;
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
