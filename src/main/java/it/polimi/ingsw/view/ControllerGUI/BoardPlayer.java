package it.polimi.ingsw.view.ControllerGUI;

import it.polimi.ingsw.view.GUIApplication;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class BoardPlayer extends GenericController {
    @FXML private VBox chooseTileBox;
    @FXML private HBox scorePlayer;
    @FXML private Label labelTurn;
    @FXML private ImageView chair, tileEndGame, imageCommonCard1, imageCommonCard2, imagePersonalGoalCard;
    @FXML private ChoiceBox choiceChat;
    @FXML private GridPane myShelf, livingRoom;

    private double xOffset = 0, yOffset = 0;

    public void setBoadPlayer(){
        Image PersonalGC = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgs/PersonalCards/Personal_Goals2.png")));
        Image CommonGC1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgs/CommonCards/2.jpg")));
        Image CommonGC2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgs/CommonCards/0.jpg")));
        imageCommonCard1.setImage(CommonGC1);
        imageCommonCard2.setImage(CommonGC2);
        imagePersonalGoalCard.setImage(PersonalGC);
        String[] players = {"All", "player1", "player2"}; //lo dovrò prendere dalla partita
        choiceChat.getItems().addAll(players); //Per settare la chat, il nome
        choiceChat.setValue("All"); //Per settare il valore predefinito

    }

    public void chooseColumn(MouseEvent mouseEvent){
        //ImageView ((StackPane) mouseEvent.getSource()).getChildren(1)
    }





    //Quando un giocatore prende la tileEndGame deve essere resa invisibile in quanto è stata presa dal giocatore,
    //se è stata presa dal giocatore stesso allora deve essere mostrata nello ScoreBox
    public void tileEndGameTaken(Boolean currentPlayer){
        if(currentPlayer){
            ((ImageView) scorePlayer.getChildren().get(2)).setImage(tileEndGame.getImage());
        }
        tileEndGame.setVisible(false);
    }

    //Questa funzione mi permette di gestire il trascinamento delle tile 2/3 tile per cui dovrò decidere l'ordine:
    private void setDraggingTile(ImageView imageView){
        imageView.setOnMousePressed(event -> {
            // Salva la posizione iniziale dell'immagine quando viene premuto il mouse
            xOffset = event.getSceneX(); // event.getX(); //imageView.getX();
            yOffset = event.getSceneY(); //event.getY();
        });

        imageView.setOnMouseDragged(event -> {
            // Sposta l'immagine sulla nuova posizione
            imageView.setTranslateX(event.getSceneX() - xOffset);
            imageView.setTranslateY(event.getSceneY() - yOffset);
        });

        imageView.setOnMouseReleased(event -> {
            for (Node node : chooseTileBox.getChildren()) {
                ImageView otherImageView = (ImageView) node;
                if(otherImageView.getImage() != null){
                    if (imageView != otherImageView && imageView.getBoundsInParent().intersects(otherImageView.getBoundsInParent())) {
                        // Scambia le immagini tra imageView corrente e altre immagini sovrapposte
                        Image tempImage = imageView.getImage();
                        imageView.setImage(otherImageView.getImage());
                        otherImageView.setImage(tempImage);
                        break;

                    }
                }
            }
            imageView.setTranslateX(0);
            imageView.setTranslateY(0);
        });
    }


    //Con queste due funzioni mi permettono di mettere in rilievo i bottoni quando ci passo sopra divenetando opachi
    public void activeButton(MouseEvent event){
        StackPane stackPane = (StackPane) event.getSource();
        stackPane.getChildren().get(0).setOpacity(0.4);
    }

    public void deactivateButton(MouseEvent event){
        StackPane stackPane = (StackPane) event.getSource();
        stackPane.getChildren().get(0).setOpacity(1);
    }

    //Devo poi gestire i vari comportamenti a seconda dei bottini su cui clicco:
    //...

    //Seleziona le Tile su cui clicco e se ci passo sopra diventano opache:
    public void setInnerShadowTile(ImageView tile){
        tile.setOnMouseClicked( event -> {
            if(tile.getEffect() == null){
                InnerShadow tileShadow = new InnerShadow();
                Color color = new Color(0.9548, 1.0, 0.21, 1.0);//Yellow
                tileShadow.setColor(color);
                tileShadow.setChoke(0.35);
                tileShadow.setHeight(20);
                tileShadow.setWidth(20);
                tile.setEffect(tileShadow);
            } else {
                tile.setEffect(null);
            }
        });
        tile.setOnMouseEntered( event -> {
            tile.setOpacity(0.7);
        });
        tile.setOnMouseExited( event -> {
            tile.setOpacity(1);
        });
    }
}
