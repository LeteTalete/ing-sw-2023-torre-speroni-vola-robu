package it.polimi.ingsw.view.ControllerGUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import it.polimi.ingsw.view.GUIApplication;

import java.io.IOException;

public class ConnectionPlayer extends GenericController {

    @FXML private StackPane buttonConnetionOK, buttonRMI, buttonSocket, buttonNumPlayers, buttonUsername;
    @FXML private HBox IP, port;
    @FXML private TextField textIP, textPort, textUsername, textNumPlayers;
    @FXML private Label errorMessageNumP, errorConnection, labelSearchPlayers;
    @FXML private ProgressBar barLoading;
    @FXML private Stage windowConnection;

    //Quando passo sopra ai bottoni essi diventano un pò più grandi e quando ci esco ritornano alla dimensione originale
    public void enteredButton(MouseEvent mouseEvent){
        ImageView imageView = (ImageView) ((StackPane) mouseEvent.getSource()).getChildren().get(0);
        imageView.setScaleX(1.1);
        imageView.setScaleY(1.1);
        imageView.setOpacity(0.8);
    }

    public void exitedButton(MouseEvent mouseEvent){
        ImageView imageView = (ImageView) ((StackPane) mouseEvent.getSource()).getChildren().get(0);
        imageView.setScaleX(1);
        imageView.setScaleY(1);
        imageView.setOpacity(1);
    }

    //Si attiva quando premo sul bottone buttonConnetionOK e deve attivare la connessione con il server
    public void clickedButtonConnetionOK(MouseEvent mouseEvent){
        }

    //Si attiva quando il giocatore che sta creando la partita ha deciso quale sia il numero dei partecipanti
    public void clickedButtonNumPlayers(MouseEvent mouseEvent){

    }

    //Si arriva quando il giocatore ha deciso il suo username
    public void clickedButtonUsername(MouseEvent mouseEvent){

    }

    public void setWindowConnection(Stage stage){
        this.windowConnection = stage;
    }

    public void activeWindowSocket(MouseEvent mouseEvent) throws IOException {
        IP.setVisible(true);
        port.setVisible(true);
    }
    
    public void activeWindowRMI(MouseEvent mouseEvent){
        IP.setVisible(true);
        port.setVisible(false);
        windowConnection.setHeight(390);
    }



}
