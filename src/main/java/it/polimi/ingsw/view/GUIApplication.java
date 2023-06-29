package it.polimi.ingsw.view;

import it.polimi.ingsw.structures.GameView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;
import it.polimi.ingsw.view.ControllerGUI.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GUIApplication extends Application {
    private static ClientGUI clientGUI;
    private static GenericController genericController;
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
                stageWindow.setHeight(320);
                stageWindow.setWidth(636);
                stageWindow.setTitle("Waiting Room");
            } else if(sceneNames.equals(SceneNames.BOARDPLAYER)){
                stageWindow.setHeight(780);
                stageWindow.setWidth(1440);
                stageWindow.setTitle("MyShelfie");
                stageWindow.setResizable(true);
                getBoardPlayer().setBoardPlayer(clientGUI.getGameView());
            } else if(sceneNames.equals(SceneNames.ENDGAME)){
                stageWindow.setHeight(535);
                stageWindow.setWidth(610);
                stageWindow.setTitle("Score End Game");
                getEndGame().setEndGame();
                stageWindow.setResizable(false);
            }
            stageWindow.setScene(sceneWindow);
            stageWindow.show();
            stageWindow.centerOnScreen();
        });
    }

    public static Stage getStageWindow(){
        return stageWindow;
    }

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

    public static void setScorePlayer(int token){
        Platform.runLater( () -> getBoardPlayer().setScoreCGC(token) );
    }

    public static void updateScore(GameView gameView){
        Platform.runLater( () -> {
            String name = gameView.getEndGame();
            if( name != null){
                if(name.equals(GUIApplication.clientGUI.getName())){
                    getBoardPlayer().setScoreCGC(1);
                } else {
                    getBoardPlayer().setEmptyTileEndGame();
                }
            }
            getBoardPlayer().setToken(gameView);
        });
    }

    public static void error(String message){
        Platform.runLater( () -> ErrorMessage.errorMessage(stageWindow, message));
    }

    public static ClientGUI getClientGUI (){
        return clientGUI;
    }

    public static void setClientGUI(ClientGUI clientView){
        clientGUI = clientView;
    }

    public static void main(String args[]){
        launch(args);
    }

}
