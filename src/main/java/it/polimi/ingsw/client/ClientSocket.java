package it.polimi.ingsw.client;

import it.polimi.ingsw.requests.*;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.timers.ConnectionClientTimer;
import it.polimi.ingsw.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Timer;

/**ClientSocket class used to manage the Socket connection to the server.*/

public class ClientSocket implements IClientConnection {
    /**logger to keep track of events, such as errors and info about parameters*/
    private static final Logger fileLog = LogManager.getRootLogger();

    /**master used to invoke methods of the ClientController*/
    private final ClientController master;
    /**token used to identify the client to the server*/
    private String token;
    /**ip of the server*/
    private final String ip;
    /** port of the server */
    private final int port;
    private Socket socket;
    private ObjectInputStream socketIn;
    private  ObjectOutputStream socketOut;
    /**boolean signalling whether the client is connected or not*/
    private boolean amIconnected;
    private Thread receiving;
    /**responseDecoder used to decode the responses received from the server via socket
     * @see ResponseDecoder*/
    private ResponseDecoder responseDecoder;
    /**boolean notReceivingResponse used to wait for a response from the server*/
    private boolean notReceivingResponse;
    /**boolean syn used to know whether the client has received the ping message from the server*/
    private boolean syn;
    private final int synCheckTime = 5000;
    private Timer synCheckTimer;
    /**currentView parameter used to invoke the method of the View (GUI or TUI)*/
    private View currentView;

    /**ClientSocket constructor.
     * @param ip - the server's ip address.
     * @param port - the server's port.
     * @param cController - the clientController*/
    public ClientSocket(String ip, int port, ClientController cController) throws IOException {
        this.ip = ip;
        this.port = port;
        this.master = cController;
        this.notReceivingResponse = true;
    }

    /**openStreams method creates a new socket and initializes it. Then it starts the startReceiving method*/
    public void openStreams() throws IOException {
        socket = new Socket(ip, port);
        this.socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.socketIn = new ObjectInputStream(socket.getInputStream());
        amIconnected= true;
        startReceiving();
    }

    /**startReceiving method is basically a loop in which the socket keeps listening. Each time something is read
     * from the stream, it is handled.*/
    private void startReceiving() {
        receiving = new Thread(
                () -> {
                    Response response = null;
                    do{
                        response = readResponse();
                        //if i get a response and the client controller doesn't tell me to close the connection
                        if(response != null && !master.isToClose()){
                            try{
                                response.handleResponse(responseDecoder);
                            }catch(RemoteException e){
                                fileLog.error(e.getMessage());
                            }
                        }
                    }while(response!=null);
                }
        );
        receiving.start();

    }

    /**
     * readResponse method is used to actually read from the socket stream
     * @return the response message sent by the server socket
     */
    private Response readResponse() {
        try{
            return ((Response) socketIn.readObject());
        }catch(ClassNotFoundException e){
            //if the serialization went wrong or if the class doesn't exist
            fileLog.error(e.getMessage());
        }catch (IOException e){
            fileLog.error(e);
            //if the connection is lost
            currentView.printError("Connection lost. Please try again later.");
        }
        return null;
    }

    /**startClient method is used to start the client. It opens the streams and then calls the userLogin method*/
    public void startClient() {
        try {
            openStreams();
            currentView.displayNotification("Connection successful");
            master.userLogin();
        } catch (IOException e) {
            fileLog.error(e);
            currentView.printError("Connection failed");
            currentView.askConnectionServer();
        }
    }

    /**closeStreams method is used to close the streams*/
    public void closeStreams() throws IOException {
        //closing streams
        socketIn.close();
        socketOut.close();
    }


    /**
     * setViewClient method is used to set the view (TUI or GUI) of the client
     * @param cView - the view chosen by the user (TUI or GUI)
     */
    @Override
    public void setViewClient(View cView) {
        this.currentView = cView;
    }

    /**chooseTiles method is used to create a request with the choice of tiles to be sent to the server.
     * After sending the request, it begins waiting for a response.
     * @param tilesChosen - the choice of tiles.
     * @param token - the token used to identify the client.*/
    @Override
    public synchronized void chooseTiles(String token, List<String> tilesChosen) {
        setReceivedResponse(true);
        request(new ChooseTilesRequest(token, tilesChosen));
        while(notReceivingResponse){
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                fileLog.error(e.getMessage());
                currentView.printError("Connection error. Please try again later.");
            }
        }
    }

    /**login method is used to create a login request and pass the username to the server. After sending the request,
     * it begins waiting for a response.
     * @param name - username chosen by the client.*/
    @Override
    public synchronized void login(String name) {
        setReceivedResponse(true);
        request(new LoginRequest(name));
        while(notReceivingResponse){
            try{
                this.wait();
            }catch (InterruptedException e){
                fileLog.error(e.getMessage());
                currentView.printError("Connection error. Please try again later.");
            }
        }
    }

    /**request method is used to write the request on the stream
     * @param request - generic request instance that needs to be sent to the server.*/
    private void request(Request request) {
        fileLog.info("I'm sending a request");
        try{
            socketOut.writeObject(request);
            socketOut.reset();
        }catch (IOException e){
            fileLog.error(e.getMessage());
            currentView.printError("Connection error. Please try again later.");
        }
    }

    /**
     * method setUserToken is used to set a token to a user
     * @param tokenA - the user token, it is used to recognize a user uniquely
     */
    @Override
    public void setUserToken(String tokenA) {
        this.token = tokenA;
    }


    /**setReceivedResponse signals whether a response has been received. If so, it stops the waits.
     * @param b - boolean used to know whether the socket has received a response or not*/
    @Override
    public void setReceivedResponse(boolean b) {
        notReceivingResponse = b;
    }

    /**numberOfPlayers method generates a request to create the waiting room.
     * @param name - username of the client.
     * @param tokenA - token used to identify the client who's sending the request.
     * @param number - number of players for the next match.*/
    @Override
    public synchronized void numberOfPlayers(String name, String tokenA, int number) {
        fileLog.debug("numberOfPlayers");
        setUserToken(tokenA);
        setReceivedResponse(true);
        request(new WaitingRoomRequest(tokenA, name, number));
        while(notReceivingResponse){
            try{
                this.wait();
            }catch (InterruptedException e){
                fileLog.error(e.getMessage());
                currentView.printError("Connection error. Please try again later.");
            }
        }
    }

    /**chooseColumn method creates a request to send the choice of column to the server.
     * @param column - number of column in which the tiles will be placed.*/
    @Override
    public synchronized void chooseColumn(int column) {
        setReceivedResponse(true);
        request(new ColumnRequest(token, column));
        while(notReceivingResponse){
            try{
                this.wait();
            }catch (InterruptedException e){
                fileLog.error(e.getMessage());
                currentView.printError("Connection error. Please try again later.");
            }
        }
    }

    @Override
    public void close() {
        try {
            closeStreams();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * setResponseDecoder method is used to set the response decoder for the client socket
     * @param responseDecoder - the response decoder
     */
    public void setResponseDecoder(ResponseDecoder responseDecoder) {
        this.responseDecoder = responseDecoder;
    }

    /**isConnected method returns a boolean signalling whether the client is connected or not*/
    @Override
    public boolean isConnected() {
        return amIconnected;
    }

    /**setConnected method is used to keep track of whether the client has lost connection to the server
     * @param b - boolean signalling whether the client is connected to the server or not*/
    @Override
    public void setConnected(boolean b) {
        amIconnected = b;
    }

    /**rearrangeTiles method generates a request to send the re-arranged tiles to the server.
     * @param userToken - token used to identify the client.
     * @param multipleChoiceNumber - list of the positions of the re-arranged tiles.*/
    @Override
    public synchronized void rearrangeTiles(String userToken, List<String> multipleChoiceNumber) {
        setReceivedResponse(true);
        request(new RearrangeTilesRequest(userToken, multipleChoiceNumber));
        while(notReceivingResponse){
            try{
                this.wait();
            }catch (InterruptedException e){
                fileLog.error(e.getMessage());
                currentView.printError("Connection error. Please try again later.");
            }
        }
    }

    /**sendChat method used to generate a chat request.
     * @param receiver - the receiver of the chat message.
     * @param username - the sender of the chat message.
     * @param toString - the text of the message.*/
    @Override
    public void sendChat(String username, String toString, String receiver) {
        setReceivedResponse(true);
        request(new ChatMessageRequest(username, toString, receiver));
    }



    /**quit method used to quit the game.
     * @param token - token used to identify the client.*/
    @Override
    public void quit(String token) {
        setReceivedResponse(true);
        request(new QuitRequest(token));
        try {
            closeStreams();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**setSyn method is used to set the value syn to true when the client received a ping from the server*/
    @Override
    public void setSyn(boolean b) {
        syn = b;
    }

    /**isSyn method is used to check whether the client has received a ping from the server*/
    @Override
    public boolean isSyn() {
        return syn;
    }

    /**method setSynCheckTimer starts a timer waiting for the ping from the server. When a ping is received, the timer
     * is reset. If the timer expires and no ping has been received by the client, it means that the server
     * is unreachable
     * @param startTimer - boolean signalling whether to start the timer or to reset it*/
    @Override
    public void setSynCheckTimer(boolean startTimer) {
        if(startTimer){
            synCheckTimer = new Timer();
            synCheckTimer.scheduleAtFixedRate(new ConnectionClientTimer(this), (synCheckTime/2)+synCheckTime, synCheckTime);
        }
        else{
            synCheckTimer.purge();
            synCheckTimer.cancel();
        }
    }

    /**method sendAck is used to send a response ack message to the server after receiving a ping*/
    @Override
    public void sendAck() {
        request(new AckPing(token));
    }

}
