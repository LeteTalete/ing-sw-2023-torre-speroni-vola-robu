package it.polimi.ingsw.view;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.CommandParsing;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.ClientListenerGUI;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.server.StaticStrings;
import it.polimi.ingsw.structures.GameView;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**class ClientGUI for the graphical interface of the game*/

public class ClientGUI implements View {
    /**logger to keep track of events, such as errors and info about parameters*/
    private static final Logger fileLog = LogManager.getRootLogger();
    private ClientController master;
    private GameView gameView;
    private ClientListenerGUI listenerClient;
    private CommandParsing commPars;
    private String connectionType;
    private boolean isRunning;
    private String ServerIP;
    private String port;
    private String username;
    private boolean isGameOn = false;
    /**tiles attribute is used to keep track of the player's re-arranged choice*/
    private ArrayList<Position> tiles;

    public ClientGUI(){
        try {
            this.listenerClient = new ClientListenerGUI(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        GUIApplication.setClientGUI(this);
    }
    public GameView getGameView(){
        return this.gameView;
    }
    /**
     * Method displayUpdatedModel sets the updated game view with the new model. It then updates the GUI and the turn.
     * @param modelUpdate - updated model.
     */
    @Override
    public void displayUpdatedModel(ModelUpdate modelUpdate){
        Platform.runLater(() -> {
            this.gameView = new GameView(modelUpdate);

            if (!this.isGameOn) {
                this.isGameOn = true;
                GUIApplication.showSceneName(SceneNames.BOARDPLAYER);
            }
            if(master.isGameOn()) {
                GUIApplication.updateLivingRoom();
                GUIApplication.updateShelfPlayer(gameView.getPlayersView());
                GUIApplication.updateShelf(gameView);
                GUIApplication.updateScore(gameView);
                turnPhase();
            }
        });
    }

    @Override
    public void chooseConnection() {
        //only on TUI
    }

    @Override
    public String getConnectionType() {
        return this.connectionType;
    }
    public void setConnectionType(String typeConnection){
        this.connectionType = typeConnection;
    }
    /**
     * method getUsername used to open the window to ask the player's username
     * */
    @Override
    public void getUsername() {
        if(!isRunning){
            GUIApplication.showSceneName(SceneNames.USERNAME);
            isRunning = true;
        }
    }
    /**
     * method displayNotification used primarily on the TUI. On GUI, since we pass notification via the listener, it
     * is not used
     * @param message - message to be displayed
     * */
    @Override
    public void displayNotification(String message) {
        fileLog.info("displayNotification: " + message);
    }

    public void setGameOn(boolean gameOn) {
        master.setGameOn(gameOn);
    }
    /**
     * method askAmountOfPlayers used to open the window to ask the player's chosen amount of players to be
     * expected in the game
     * */
    @Override
    public void askAmountOfPlayers() {
        GUIApplication.showSceneName(SceneNames.NUMPLAYERS);
    }

    @Override
    public void GameTitle() {
        //only on TUI
    }

    @Override
    public void showShelves(){
        //only on TUI
    }

    @Override
    public void showChat() {
        //only on TUI
    }

    @Override
    public void hideChat() {
        //only on TUI
    }

    @Override
    public void showCommands() {
        //only on TUI
    }

    @Override
    public void hideCommands() {
        //only on TUI
    }

    @Override
    public void showCommonGoalCards() {
        //only on TUI
    }

    @Override
    public void hideCommonGoalCards() {
        //only on TUI
    }


    @Override
    public void showBoardPlayer(PlayerView playerBoardView, LivingRoomView livingRoomView) {
        //only on TUI
    }


    @Override
    public IClientListener getListener() {
        return this.listenerClient;
    }

    /**
     * method printError used to print an error message on the GUI
     * @param message - error message to be printed
     * */
    @Override
    public void printError(String message) {
        GUIApplication.error(message);
    }

    @Override
    public void setMyTurn(int b) {
        this.master.setMyTurn(b);
    }

    @Override
    public void setMaster(ClientController clientController, CommandParsing commandParsing) {
        this.master = clientController;
        this.commPars = commandParsing;
    }

    public ClientController getMaster(){
        return this.master;
    }
    public CommandParsing getCommPars(){
        return this.commPars;
    }

    /**
     * method serverSavedUsername used to save the player's username and to pass it to the controller, along with
     * other useful parameters.
     * @param name - username chosen by the player
     * @param b - boolean signalling whether the login has been successful or not (meaning, if the username used was
     *          valid or not)
     * @param token - token used to identify the player
     * @param first - boolean signalling whether the player is the first to connect to the server or not
     * */
    @Override
    public void serverSavedUsername(String name, boolean b, String token, boolean first) {
        master.serverSavedUsername(name, b, token, first);
        if(b){
            this.username = name;
        }
        if (first) {
            askAmountOfPlayers();
        }
    }

    /**
     * Method changeTurn updates the client state of the current turn based on the name of current player.
     * @param name - the name of the player whose turn it is.
     */
    @Override
    public void changeTurn(String name) {
        master.isItMyTurn(name);
    }

    @Override
    public void askConnectionServer() {
        //only on TUI
    }

    @Override
    public String getServerIP() {
        return ServerIP;
    }

    /**
     * Method turnPhase is used to print the current turn phase.
     * If it's the player's turn, it will guide him through the commands he can use.
     * If it's not the player's turn, it will display a message saying whose turn it is.
     */
    @Override
    public void turnPhase(){
        switch (master.isMyTurn()) {
            case 0 -> {
                GUIApplication.messaggeForPlayer("It's " + gameView.getCurrentPlayerNickname() + "'s turn");
                displayNotification("It's " + gameView.getCurrentPlayerNickname() + "'s turn");
            }
            case 1 -> {
                GUIApplication.messaggeForPlayer("It's your turn: Choose the tiles");
                displayNotification(StaticStrings.YOUR_TURN);
            }
            case 2 -> {
                GUIApplication.messaggeForPlayer("It's your turn : Re-arrange the tiles or choose the column");
                displayNotification(StaticStrings.YOUR_TURN);
                displayNotification("You can now re-arrange the tiles or choose the column.");
                chooseOrder();
            }
        }
    }

    @Override
    public void chooseColumn() {
        //only on TUI
    }

    /**method chooseOrder is used to view the chosen order of the tiles*/
    @Override
    public void chooseOrder() {
        GUIApplication.setOrderTile();
    }

    /**
     * Method nextAction changes the current turn phase and passes the tiles chosen by the player to the client controller.
     * @param num - the number of the turn phase.
     * @param tiles - the tiles chosen by the player.
     */
    @Override
    public void nextAction(int num, ArrayList<Position> tiles) {
        master.nextAction(num, tiles);
    }

    /**
     * method showEndResult is used to show the end result of the game
     * */
    @Override
    public void showEndResult() {
        GUIApplication.showSceneName(SceneNames.ENDGAME);
    }


    @Override
    public void hideShelves() {
        //only on TUI
    }

    /**
     * Method passTilesToView is used to pass the tiles chosen by the player to clientTUI.
     * @param tiles - the tiles chosen by the player.
     */
    @Override
    public void passTilesToView(ArrayList<Position> tiles) {
        this.tiles = tiles;
    }

    /**
     * method passSyn is used to pass the ping received from the listener to the client controller.
     * */
    @Override
    public void passSyn() {
        master.onSyn();
    }

    @Override
    public String getName(){
        return username;
    }

    @Override
    public String getPort() {
        return port;
    }

    /**
     * method setServerIP is used to get the text from the text field relative to the Ip address
     * in the connection scene and to save it
     * @param accessibleText - is the text from the text field (i.e. server ip address)
     * */
    public void setServerIP(String accessibleText) {
        this.ServerIP = accessibleText;
    }

    public void setPort(String port){
        this.port = port;
    }
}
