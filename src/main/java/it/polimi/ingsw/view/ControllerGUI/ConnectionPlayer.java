package it.polimi.ingsw.view.ControllerGUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import it.polimi.ingsw.view.GUIApplication;

public class ConnectionPlayer extends GenericController {

    @FXML private StackPane buttonConnetionOK, buttonRMI, buttonSocket, buttonNumPlayers, buttonUsername;
    @FXML private TextField IP, port, textUsername;
    @FXML private ImageView imageSocket, imageRMI;
    private String typeConnection;
    private boolean activeButtonS = false;
    private boolean activeButtonR = false;


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


    public void exitedButtonConnection(MouseEvent mouseEvent){
        ImageView imageView = (ImageView) ((StackPane) mouseEvent.getSource()).getChildren().get(0);
        if(imageView.equals(imageRMI)){
            activeTypeConnection(activeButtonR, imageRMI);
        } else {
            activeTypeConnection(activeButtonS, imageSocket);
        }
    }

    //Si attiva quando premo sui bottone ok della finestra di connessione
    public void clickedButtonConnetion(MouseEvent mouseEvent){
        //TODO Nel caso ci fossero dei problemi a lato server il codice si fotte!!(Finisce in un ciclo di errori senza uscita)
        GUIApplication.getClientGUI().setConnectionType(this.typeConnection);
        GUIApplication.getClientGUI().setServerIP(IP.getText());
        GUIApplication.getClientGUI().setPort(port.getText());
        GUIApplication.getClientGUI().getMaster().setupConnection();
    }

    //Si arriva quando il giocatore che ha sta creando la partita ha deciso quale sia il numero dei dei partecipanti
    public void clickedButtonNumPlayers(MouseEvent mouseEvent){
        GUIApplication.getClientGUI().getCommPars().elaborateInput(((Label) (((StackPane) mouseEvent.getSource()).getChildren().get(1))).getText());

    }

    //Si arriva quando il giocatore ha deciso il suo username
    public void clickedButtonUsername(MouseEvent mouseEvent){
        System.out.println(textUsername.getText());
        GUIApplication.getClientGUI().getCommPars().elaborateInput(textUsername.getText());
    }

    public void activeWindowSocket(MouseEvent mouseEvent){
        this.typeConnection = "SOCKET";
        activeButtonR = false;
        activeButtonS = true;
        activeTypeConnection(activeButtonS, imageSocket);
        activeTypeConnection(activeButtonR, imageRMI);

        GUIApplication.getStageWindow().setHeight(400);
    }
    
    public void activeWindowRMI(MouseEvent mouseEvent){
        this.typeConnection = "RMI";
        activeButtonR = true;
        activeButtonS = false;
        activeTypeConnection(activeButtonR, imageRMI);
        activeTypeConnection(activeButtonS, imageSocket);
        GUIApplication.getStageWindow().setHeight(400);
    }

    private void activeTypeConnection(boolean active, ImageView imageView){
        if(active){
            imageView.setScaleX(1.2);
            imageView.setScaleY(1.2);
            imageView.setOpacity(0.8);
        } else {
            imageView.setScaleX(1);
            imageView.setScaleY(1);
            imageView.setOpacity(1);
        }
    }

}
