package it.polimi.ingsw.view;

import com.sun.javafx.scene.shape.ArcHelper;
import com.sun.javafx.scene.text.GlyphList;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.structures.GameView;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;
import it.polimi.ingsw.view.ControllerGUI.*;
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
import java.io.PushbackInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class GUIApplication extends Application {
    //private static GenericController genericController;
    public static ClientGUI clientGUI;
    private static GenericController genericController;
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

    private static Scene setUpScene(SceneNames sceneNames){
        try {
            FXMLLoader loaderController = new FXMLLoader(GUIApplication.class.getResource(sceneNames.scaneString()));
            Parent root = loaderController.load();
            Scene newScene = new Scene(root);
            genericController = loaderController.getController();
            return newScene;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BoardPlayer getBoardPlayer(){
        return (BoardPlayer) genericController;
    }
    private static EndGame getEndGame(){
        return (EndGame) genericController;
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
                stageWindow.setHeight(535);
                stageWindow.setWidth(610);
                stageWindow.setTitle("Score End Game");
                getEndGame().setEndGame();
            }
            stageWindow.setScene(sceneWindow);
            stageWindow.show();
            stageWindow.centerOnScreen();
        });
    }

    public static Stage getStageWindow(){
        return stageWindow;
    }


    /*
    public static void behaviorGUI(int command){
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
    */

    public static void updateLivingRoom(){
        Platform.runLater( () -> getBoardPlayer().updateBoard(clientGUI.getGameView().getGameBoardView()));
    }

    public static void updateShelf(GameView gameView){
        Platform.runLater( () -> {
            PlayerView mine = gameView.getPlayersView().stream().filter(
                    (p) -> p.getNickname().equals(clientGUI.getMaster().getUsername())
            ).findFirst().orElse(null);
            ShelfView shelfView = mine.getShelf();
            getBoardPlayer().updateShelfClient(shelfView);
        });
    }

    public static void setOrderTile(){
        Platform.runLater( () -> getBoardPlayer().setTileOrderPosition() );
    }


    public static void updateShelfPlayer(ArrayList<PlayerView> players ){
        Platform.runLater(() -> {
            ArrayList<ShelfView> shelfPlayers = new ArrayList<>();
            players.forEach( p -> {
                if(!GUIApplication.clientGUI.getName().equals(p.getNickname())){
                    shelfPlayers.add(p.getShelf());
                }
            });
            getBoardPlayer().updateShelfOthers(shelfPlayers);
        });

    }

    public static void setMessageEntry(String sender, String message, String receiver){
        Platform.runLater( () -> getBoardPlayer().setMessageEntry(sender, message, receiver));
    }

    public static void messaggeForPlayer(String message){
        Platform.runLater(()-> getBoardPlayer().setLabelTurn(message));
    }

    public static void setMyScoreCGC(int id, int token){
        Platform.runLater( () -> getBoardPlayer().updateScore(id, token, true) );
    }


    public static void error(String message){
        Platform.runLater( () -> ErrorMessage.errorMessage(stageWindow, message));
    }

    public static void main(String args[]){
        launch(args);
    }

}
