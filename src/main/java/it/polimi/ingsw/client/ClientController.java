package it.polimi.ingsw.client;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

/**the ClientController class manages the client: it re-directs requests between the view, the responseDecoder,
 * the commandParsing and the network.*/

public class ClientController {
    /**logger to keep track of events, such as errors and info about parameters*/
    private static final Logger fileLog = LogManager.getRootLogger();
    /**currentView parameter used by ClientController to call methods of the View (GUI or TUI)*/
    private final View currentView;
    /**boolean signalling whether a game is active or not*/
    private boolean gameOn;
    /**int myTurn is used to keep track of the state of the player (0 = not my turn, 1 = choosing tiles,
     * 3 = choosing column or rearranging tiles)*/
    private int myTurn;
    private String username;
    private final CommandParsing commPars;
    /**currentConnection attribute to call the method of the connection (RMI or socket)*/
    private IClientConnection currentConnection;
    /**listenerClient used to assign a listener to the connection
     * @see IClientListener*/
    private final IClientListener listenerClient;
    /**responseDecoder used to decode the responses received from the server via socket
     * @see ResponseDecoder*/
    private ResponseDecoder responseDecoder;
    private Registry registry;
    /**remoteController is the RMI register to allow the client to call the methods of the server*/
    private IRemoteController remoteController;
    /**token assigned by the server to the client*/
    private String userToken;
    /**boolean toClose signalling whether the connection needs to be closed*/
    private boolean toCLose;

    /**constructor ClientController sets a new ClientController and initializes the view, the listener,
     * the command parsing and the connection (RMI or Socket). It also creates a new instance of CommandParsing
     * @param currentView - the view of the client.
     * */
    public ClientController(View currentView) {
        this.currentView = currentView;
        this.listenerClient = currentView.getListener();
        this.commPars = new CommandParsing(this);
        currentView.setMaster(this, commPars);
        this.currentView.chooseConnection();
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username){ this.username = new String(username); }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    /**this boolean signals whether the connection needs to be closed or not
     * @return boolean - signals if the connection needs to be closed*/
    public boolean isToClose() {
        return toCLose;
    }
    public void setToCLose(boolean toCLose) {
        this.toCLose = toCLose;
    }

    /**this boolean is used on the client's side to know if a game has started or not
     * @return boolean to know if the game has started*/
    public boolean isGameOn() {
        return gameOn;
    }
    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
        commPars.setGameIsOn(gameOn);
    }

    public boolean isConnected() {
        return currentConnection.isConnected();
    }


    /**this int is used on the client's side to know at which stage of the turn they are playing
     * @return 0 when it's not the client's turn, 1 when they are choosing tiles, 2 if they are either
     * choosing a column or re-arranging the tiles*/
    public int isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(int turn) {
        this.myTurn = turn;
        commPars.setPlaying(turn);
    }

    /**method setupConnection sets up the connection of the client, choosing between RMI and Socket.
     * To initialize the connection, it asks the server IP and then it calls the method setupRMI or setupSocket.
     * */
    public void setupConnection() {
        String SIP = currentView.getServerIP();
        int port = 0;
        try {
            port = Integer.parseInt(currentView.getPort());
            if(currentView.getConnectionType().equals("RMI")) {
                setupRMI(SIP, port);
            }
            else if(currentView.getConnectionType().equals("SOCKET")){
                setupSocket(SIP, port);
            }
        }
        catch(Exception e) {
            currentView.printError("Server not found. Please try again.");
            currentView.askConnectionServer();
        }

    }


    /**method setupSocket sets up the socket connection of the client. It creates a new instance of ClientSocket
     * and it sets the view of the client and the response decoder.
     * @param serverIP - the IP of the server caught from the view.
     * @param port - the port of the server caught from the view.
     * */
    public void setupSocket(String serverIP, int port) {
        try {
            ClientSocket clientSocket = new ClientSocket(serverIP, port, this);//8899
            this.currentConnection = clientSocket;
            this.responseDecoder = new ResponseDecoder(listenerClient, currentConnection);
            clientSocket.setResponseDecoder(responseDecoder);
            clientSocket.setViewClient(currentView);
            clientSocket.setConnected(true);
            clientSocket.startClient();

        }catch(NullPointerException n){
            currentView.printError("Server not found. Please try again.");
            //setupConnection();
            currentView.askConnectionServer();
        }
        catch (Exception e) {
            fileLog.error(e);
        }
    }

    /**method setupRMI sets up the RMI connection of the client. It creates a new instance of ClientRMI
     * and it sets the view of the client and the response decoder.
     * @param serverIP - the IP of the server caught from the view.
     * */
    private void setupRMI(String serverIP, int port) {
        try{
            this.registry = LocateRegistry.getRegistry(serverIP,port); //8089
            this.remoteController = (IRemoteController) registry.lookup("Login");
            ClientRMI clientRMI = new ClientRMI(remoteController);
            this.currentConnection = clientRMI;
            clientRMI.setViewClient(currentView);
            this.responseDecoder = new ResponseDecoder(listenerClient, currentConnection);
            clientRMI.setResponseDecoder(responseDecoder);
            clientRMI.setConnected(true);
            userLogin();

        }catch(UnknownHostException b){
            currentView.printError("Unknown Host. Please try again.");
            //setupConnection();
            currentView.askConnectionServer();
        }catch (RemoteException e){
            currentView.printError("RemoteException. Please try again.");
            //setupConnection();
            currentView.askConnectionServer();
        }
        catch(Exception e){
            fileLog.error(e);
        }
    }

    /**method userLogin asks the username to the client's View
     * */
    public void userLogin () {
        currentView.getUsername();
    }

    /**method to pass the chosen tiles from the view to the connection. It also passes the user's token
     * @param tilesChosen - the list of tiles chosen by the user.
     * */
    public void chooseTiles(List<String> tilesChosen) {
        currentConnection.chooseTiles(userToken, tilesChosen);
    }

    /**method serverSavedUsername takes the reply of the server after the login and saves useful information for the
     * future such as the token. If the login was not successful, the client will be asked to choose a new name.
     * @param name - the name with which the client has logged in
     * @param b - boolean which signals whether the username is valid or not.
     * @param token - token assigned by the server to the client. It will be used for future requests to the server.
     * @param first - signals whether the client is the first player logging on, in which case they will need
     * to create a waiting room.*/
    public void serverSavedUsername(String name, boolean b, String token, boolean first) {
        if(b){
            setUserToken(token);
            setUsername(name);
            currentConnection.setUserToken(token);
            commPars.setFirst(first);
            setToCLose(false);
            currentConnection.setSynCheckTimer(true);
        }
        else{
            userLogin();
        }
    }


    /**method numberOfPlayers checks whether the input from the view is valid. If it is, it sends the number
     * of players to the server so that it will create a waiting room.
     * @param number - number of players of the next match, passed by the view.*/
    public void numberOfPlayers(int number) {
        if(number < 2 || number > 4){
            currentView.printError("Wrong number of players, please type 'help' for a list of commands");
        }
        else{
            currentConnection.numberOfPlayers(username, userToken, number);
        }
    }

    /**method chooseColumn sends the column chosen by the user to the server.
     * @param column - the column chosen by the user.*/
    public void chooseColumn(int column) {
        currentConnection.chooseColumn(column);
    }

    /**method close closes the connection with the server.*/
    public void close() {
        currentConnection.close();
    }

    /**askLogin method is invoked by the ResponseDecoder to send the username to the server.
     * @param s - it's the username chosen by the client.*/
    public void askLogin(String s) {
        currentConnection.login(s);
    }

    /**this method sets the turn parameter to 1 if the name of the current player is the same as the
     * client's username.
     * @param name - name of the current player passed by the server.*/
    public void isItMyTurn(String name) {
        if(name.equals(username)){
            setMyTurn(1);
        }
        else{
            setMyTurn(0);}
    }

    /**method rearrangeTiles passes the tiles re-arranged to the server.
     * @param multipleChoiceNumber - list of re-arranged tiles passed by the view*/
    public void rearrangeTiles(List<String> multipleChoiceNumber) {
        currentConnection.rearrangeTiles(userToken, multipleChoiceNumber);
    }

    /**nextAction method used to change the state of the turn parameter from one to two when the choice of tiles
     * has been deemed successful by the server.
     * @param num - the stage at which the turn of the client is.
     * @param tiles - tiles passed from the server to the view so that the client can see them.*/
    public void nextAction(int num, ArrayList<Position> tiles) {
        if(num==2){
            currentView.passTilesToView(tiles);
            setMyTurn(2);
        }
    }

    public void wrongCommand() {
        currentView.printError("Wrong command, please type 'help' for a list of commands");
    }

    public void invalidNotMyTurn() {
        currentView.printError("It's not your turn, yet!");
    }

    /**
     * method errorFormat used to print an error message when the format of the input is wrong.
     * */
    public void errorFormat() {
        currentView.printError("Wrong format, please try again or type 'help' for a list of commands");
    }

    /**
     * method errorNoSelection used to print an error message when the user has not typed anything after the command.
     * @param s - string which signals which error message to print (about selection of tiles, column or re-arranging).
     * */
    public void errorNoSelection(String s) {
        if (s.equals("selection")) {
            currentView.printError("Please select one to three tiles");
        }
        if (s.equals("order")) {
            currentView.printError("Please select the tiles to rearrange");
        }
        if (s.equals("column")){
            currentView.printError("Please select one to three tiles first");
        }
        if (s.equals("choose")){
            currentView.printError("Please select the column");
        }
    }

    public void wrongNumber() {
        currentView.printError("Number of players not valid; please try again.");
    }

    /**
     * method gameNotStarted used to notify the client that the game has not started yet and therefore
     * they cannot type commands
     * */
    public void gameNotStarted() {
        currentView.displayNotification("The game has not started yet, please wait for the other players to join");
    }

    /**sendChat method passes a message to the server.
     * @param receiver - the receiver of the chat message.
     * @param message - text of the chat message.*/
    public void sendChat(String receiver, String message) {
        currentConnection.sendChat(username, message, receiver);
        //currentView.addToChatQueue(message, receiver);
    }

    public void hideShelves() {
        currentView.hideShelves();
    }
    public void showShelves(){currentView.showShelves();}

    public void showCards(){currentView.showCommonGoalCards();}

    public void hideCards() { currentView.hideCommonGoalCards(); }

    public void showCommands() {
        currentView.showCommands();
    }

    public void hideCommands() {
        currentView.hideCommands();
    }

    public void showChat() {
        currentView.showChat();
    }

    public void hideChat() {
        currentView.hideChat();
    }

    /**quit method to quit the game*/
    public void quit()
    {
        currentConnection.quit(userToken);
        System.exit(0);
    }

    public void onSyn() {
        currentConnection.setSyn(true);
    }

    public View getCurrentView() {
        return currentView;
    }

}

