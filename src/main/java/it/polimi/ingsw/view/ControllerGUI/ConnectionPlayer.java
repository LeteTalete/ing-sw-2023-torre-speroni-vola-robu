package it.polimi.ingsw.view.ControllerGUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import it.polimi.ingsw.view.GUIApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ConnectionPlayer class is the controller of the scene of the connection and login of the player
 * */

public class ConnectionPlayer extends GenericController {
    /**
     * logger to keep track of events, such as errors and info about parameters
     * */
    private static final Logger fileLog = LogManager.getRootLogger();
    /**
     * TextField attributes are the boxes in which the player can type the IP and port of the server and their username
     * */
    @FXML private TextField IP, port, textUsername;

    /**
     * ImageView attributes are the images of the buttons of the connection type (RMI or Socket)
     * */
    @FXML private ImageView imageSocket, imageRMI;
    /**
     * String typeConnection is used to keep track of the chosen connection type (RMI or Socket)
     * */
    private String typeConnection;
    /**
     * boolean activeButtonS is used to keep track of the button of the socket connection. When set to true, it means
     * the chosen connection type is socket
     * */
    private boolean activeButtonS = false;

    /**
     * boolean activeButtonR is used to keep track of the button of the RMI connection. When set to true, it means
     * the chosen connection type is RMI
     * */
    private boolean activeButtonR = false;

    /**
     * enteredButton method is used to enlarge the button when the mouse is over it
     * @param mouseEvent - the event of the mouse (in this case, entering the field of the button)
     * */
    public void enteredButton(MouseEvent mouseEvent){
        ImageView imageView = (ImageView) ((StackPane) mouseEvent.getSource()).getChildren().get(0);
        imageView.setScaleX(1.1);
        imageView.setScaleY(1.1);
        imageView.setOpacity(0.8);
    }
    /**
     * exitedButton method is used shrink the button back to its original size when the mouse exits the button
     * @param mouseEvent - the event of the mouse (in this case, exiting the field of the button)
     * */
    public void exitedButton(MouseEvent mouseEvent){
        ImageView imageView = (ImageView) ((StackPane) mouseEvent.getSource()).getChildren().get(0);
        imageView.setScaleX(1);
        imageView.setScaleY(1);
        imageView.setOpacity(1);
    }

    /**
     * exitedButtonConnection is the type of connection the player has chosen, between socket and RMI.
     * The choice is made by pressing on either pulused to store.
     * @param mouseEvent - the event of the mouse (in this case exiting the field of the button)
     * */
    public void exitedButtonConnection(MouseEvent mouseEvent){
        ImageView imageView = (ImageView) ((StackPane) mouseEvent.getSource()).getChildren().get(0);
        if(imageView.equals(imageRMI)){
            activeTypeConnection(activeButtonR, imageRMI);
        } else {
            activeTypeConnection(activeButtonS, imageSocket);
        }
    }

    /**
     * clickedButtonConnection method is used to send the type of connection that was chosen by the player,
     * with all the ip and port information written in the appropriate spaces
     * @param mouseEvent - the event of the mouse (in this case, clicking on the button)
     * */
    public void clickedButtonConnection(MouseEvent mouseEvent){
        GUIApplication.getClientGUI().setConnectionType(this.typeConnection);
        GUIApplication.getClientGUI().setServerIP(IP.getText());
        GUIApplication.getClientGUI().setPort(port.getText());
        GUIApplication.getClientGUI().getMaster().setupConnection();
    }

    /**
     * clickedButtonNumPlayers method is used to set the number of players of the game when the mouse clicks on the
     * button depicting the preferred amount of players expected in the game.
     * @param mouseEvent - the event of the mouse (in this case, clicking on the button)
     * */
    public void clickedButtonNumPlayers(MouseEvent mouseEvent){
        GUIApplication.getClientGUI().getCommPars().elaborateInput(((Label) (((StackPane) mouseEvent.getSource()).getChildren().get(1))).getText());

    }

    /**
     * clickedButtonUsername method is invoked whenever the player clicks on the 'OK' button of the username window.
     * It gets the username typed by the player and passes it to the commandParsing.
     * @param mouseEvent - the event of the mouse (in this case, clicking on the button)
     * */
    public void clickedButtonUsername(MouseEvent mouseEvent){
        fileLog.info("username read: " + textUsername.getText());
        GUIApplication.getClientGUI().getCommPars().elaborateInput(textUsername.getText());
    }

    /**
     * activeWindowSocket method is invoked whenever the player clicks on the button of the socket connection type.
     * It sets the type of connection to socket and enlarges the windows so that the player can type the port
     * and IP.
     * @param mouseEvent - the event of the mouse (in this case, clicking on the button)
     * */
    public void activeWindowSocket(MouseEvent mouseEvent){
        this.typeConnection = "SOCKET";
        activeButtonR = false;
        activeButtonS = true;
        activeTypeConnection(activeButtonS, imageSocket);
        activeTypeConnection(activeButtonR, imageRMI);

        GUIApplication.getStageWindow().setHeight(400);
    }

    /**
     * activeWindowRMI method is invoked whenever the player clicks on the button of the RMI connection type.
     * It sets the type of connection to RMI and enlarges the windows so that the player can type the port
     * and IP.
     * @param mouseEvent - the event of the mouse (in this case, clicking on the button)
     * */
    public void activeWindowRMI(MouseEvent mouseEvent){
        this.typeConnection = "RMI";
        activeButtonR = true;
        activeButtonS = false;
        activeTypeConnection(activeButtonR, imageRMI);
        activeTypeConnection(activeButtonS, imageSocket);
        GUIApplication.getStageWindow().setHeight(400);
    }

    /**
     * activeTypeConnection method is invoked when one of the connection type buttons is clicked. It enlarges the
     * button of the chosen connection type and makes it less opaque
     * */
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
