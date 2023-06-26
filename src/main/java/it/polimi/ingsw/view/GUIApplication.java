package it.polimi.ingsw.view;

import com.sun.javafx.scene.text.GlyphList;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.structures.GameView;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;
import it.polimi.ingsw.view.ControllerGUI.BoardPlayer;
import it.polimi.ingsw.view.ControllerGUI.ConnectionPlayer;
import it.polimi.ingsw.view.ControllerGUI.ErrorMessage;
import it.polimi.ingsw.view.ControllerGUI.GenericController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class GUIApplication extends Application {
    //private static GenericController genericController;
    public static ClientGUI clientGUI;
    private static GenericController genericController;
    private static FXMLLoader loaderController;
    private static Stage stageWindow;
    private static Scene sceneWindow;

    @Override
    public void start(Stage stage) throws Exception {
        stageWindow = stage;
        Image icon = new Image(Objects.requireNonNull(GUIApplication.class.getResourceAsStream("/imgs/Icon.png")));
        stageWindow.getIcons().add(icon);
        //stageWindow.setResizable(false);
        showSceneName(SceneNames.CONNECTION);
        stageWindow.setOnCloseRequest(event -> System.exit(0));
    }

    public static Stage setUpStage(SceneNames sceneNames){
        Stage newStage = new Stage();
        newStage.setScene( setUpScene(sceneNames) );
        return newStage;
    }

    private static Scene setUpScene(SceneNames sceneNames){
        try {
            loaderController = new FXMLLoader(GUIApplication.class.getResource(sceneNames.scaneString()));
            Parent root = loaderController.load();
            Scene newScene = new Scene(root);
            genericController = loaderController.getController();
            return newScene;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    public static void updateLivingRoom(LivingRoomView livingRoomView){
        boardPlayer.setLivingRoom(livingRoomView);
    }

     */

    public static BoardPlayer getBoardPlayer(){
        System.out.println("Entrato Board!!");
        return (BoardPlayer) genericController;
    }

    public static void showSceneName(SceneNames sceneNames){
        Platform.runLater(()-> {
            sceneWindow = setUpScene(sceneNames);
            stageWindow.hide();
            if(sceneNames.equals(SceneNames.CONNECTION)){
                stageWindow.setHeight(220);
                stageWindow.setWidth(465);
                stageWindow.setTitle("Choose Connection");
            } else if(sceneNames.equals(SceneNames.USERNAME)){
                stageWindow.setHeight(400);
                stageWindow.setWidth(542);
                stageWindow.setTitle("Choose name");
            } else if(sceneNames.equals(SceneNames.NUMPLAYERS)) {
                stageWindow.setHeight(400);
                stageWindow.setWidth(542);
                stageWindow.setTitle("Choose number players");
            } else if (sceneNames.equals(SceneNames.WAITINGROOM)){
                stageWindow.setHeight(306);
                stageWindow.setWidth(636);
                stageWindow.setTitle("Waiting Room");
            } else if(sceneNames.equals(SceneNames.BOARDPLAYER)){
                stageWindow.setHeight(780);
                stageWindow.setWidth(1430);
                stageWindow.setTitle("MyShelfie");
                getBoardPlayer().setBoadPlayer(clientGUI.getGameView());
            } else if(sceneNames.equals(SceneNames.ENDGAME)){
                stageWindow.setTitle("Score End Game");
            }
            stageWindow.setScene(sceneWindow);
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

    public static void behaviorGUI(int command){
        //Settare il turno,
        //DIre al giocatore che deve ora scegliere le tile dalla LivingRoom
        //Dire al giocatore che deve scegliere l'ordine delle tile e la colonna in cui metterle
        /* 0 non è il turno, 1 devo scegliere le tile, 2 devo scegliere l'ordine delle tile e la colonna, 3 ricevere un messaggio

        0 -> setta la livingRoom
        1 -> setta la shelf
        2 -> setta i punteggi
        3 -> setta la shelf degli altri giocatori?
        4 -> mostra il messaggio che mi arriva da un giocatore

         */
        Platform.runLater(()-> {
            if(command == 0){
                //Aggiorna la LivingRoom
                getBoardPlayer().updateBoard(clientGUI.getGameView().getGameBoardView());
            }
            else if(command == 1){
                //getBoardPlayer().setLivingRoom(clientGUI.getGameView().getGameBoardView());
            }
            else if(command == 2){
                //Setta la posizione della tiles
                getBoardPlayer().setTileOrderPosition();
            }
            else if( command == 3){
                //Aggiorna la Shelf:
                PlayerView mine = clientGUI.getGameView().getPlayersView().stream().filter(
                                (p) -> p.getNickname().equals(clientGUI.getMaster().getUsername())
                ).findFirst().orElse(null);
                ShelfView shelfView = mine.getShelf();
                getBoardPlayer().updateShelfClient(shelfView);
            }
        });

    }

    public static void setMessageEntry(String sender, String message, String receiver){
        Platform.runLater( () -> getBoardPlayer().setMessageEntry(sender, message, receiver));
    }

    public static void messaggeForPlayer(String message){
        Platform.runLater(()-> {
            getBoardPlayer().setLabelTurn(message);
        });
    }

    public static void setMyScoreCGC(int id, int token){
        Platform.runLater( () -> getBoardPlayer().updateScore(id, token, true) );
    }

    public static void setPlayerScoreCGC(String player, int id, int token, boolean scoreClient){
        Platform.runLater( () -> getBoardPlayer().updateScore(id, token, true) );

    }


    public static void error(String message){
        ErrorMessage.errorMessage(stageWindow, message);
    }

    public static void main(String args[]){
        launch(args);
    }

}
