package it.polimi.ingsw.view.ControllerGUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import it.polimi.ingsw.view.GUIApplication;

public class ConnectionPlayer extends GenericController {

    @FXML private StackPane buttonConnetionOK, buttonRMI, buttonSocket, buttonNumPlayers, buttonUsername;
    @FXML private HBox IP, port;
    @FXML private TextField textUsername;
    private String typeConnection;


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

    //Si attiva quando premo sui bottone ok della finestra di connessione
    public void clickedButtonConnetion(MouseEvent mouseEvent){
        //TODO Nel caso ci fossero dei problemi a lato server il codice si fotte!!(Finisce in un ciclo di errori senza uscita)
        GUIApplication.clientGUI.setConnectionType(this.typeConnection);
        String ip = ((TextField) IP.getChildren().get(1)).getText();
        GUIApplication.clientGUI.setServerIP(ip);
        GUIApplication.clientGUI.getMaster().setupConnection();
    }

    //Si arriva quando il giocatore che ha sta creando la partita ha deciso quale sia il numero dei dei partecipanti
    public void clickedButtonNumPlayers(MouseEvent mouseEvent){
        GUIApplication.clientGUI.getCommPars().elaborateInput(((Label) (((StackPane) mouseEvent.getSource()).getChildren().get(1))).getText());

    }

    //Si arriva quando il giocatore ha deciso il suo username
    public void clickedButtonUsername(MouseEvent mouseEvent){
        System.out.println(textUsername.getText());
        GUIApplication.clientGUI.getCommPars().elaborateInput(textUsername.getText());
    }

    public void activeWindowSocket(MouseEvent mouseEvent){
        IP.setVisible(true);
        port.setVisible(true);
        this.typeConnection = "SOCKET";
        GUIApplication.getStageWindow().setHeight(400);
    }
    
    public void activeWindowRMI(MouseEvent mouseEvent){
        IP.setVisible(true);
        port.setVisible(false);
        this.typeConnection = "RMI";
        GUIApplication.getStageWindow().setHeight(400);
    }

}
