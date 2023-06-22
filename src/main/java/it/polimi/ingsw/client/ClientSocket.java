package it.polimi.ingsw.client;

import it.polimi.ingsw.network.ConnectionClientTimer;
import it.polimi.ingsw.requests.*;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

public class ClientSocket implements IClientConnection
{
    private static final Logger fileLog = LogManager.getRootLogger();

    private final ClientController master;
    private String token;
    private final String ip;
    private final int port;
    private Socket socket;
    private ObjectInputStream socketIn;
    private  ObjectOutputStream socketOut;
    private Scanner stdin;
    private boolean amIconnected;
    private Thread receiving;
    private ResponseDecoder responseDecoder;
    private boolean notReceivingResponse;
    private boolean syn;
    private Timer checkTimer;
    private final int synCheckTime = 1000;

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
    public void openStreams() {
        try {
            socket = new Socket(ip, port);
            this.socketOut = new ObjectOutputStream(socket.getOutputStream());
            this.socketIn = new ObjectInputStream(socket.getInputStream());
            amIconnected= true;
            startReceiving();
        }
        catch(IOException e) {
            fileLog.error(e.getMessage());
        }
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

    /**readResponse method is used to actually read from the socket stream*/
    private Response readResponse() {
        try{
            return ((Response) socketIn.readObject());
        }catch(ClassNotFoundException e){
            //if the serialization went wrong or if the class doesn't exist
            fileLog.error(e.getMessage());
        }catch (IOException e){
            fileLog.error(e);
        }
        return null;
    }

    /**startClient method is used to start the client. It opens the streams and then calls the userLogin method*/
    public void startClient() {
        openStreams();
        System.out.println(">> Connection established");
        //if the connection is successful i can use socket's streams to communicate with the server
        master.userLogin();
    }

    /**closeStreams method is used to close the streams*/
    public void closeStreams() throws IOException {
        //closing streams
        stdin.close();
        socketIn.close();
        socketOut.close();
    }


    @Override
    public void setViewClient(View currentView) {
        //only for rmi
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
            }
        }
    }

    /**login method is used to create a login request and pass the username to the server. After sending the request,
     * it begins waiting for a response.
     * @param name - username chosen by the client.*/
    @Override
    public synchronized void login(String name) {
        setReceivedResponse(true);
        request(new loginRequest(name));
        while(notReceivingResponse){
            try{
                //maybe this doesn't need 'this', but since it's a thread it's better to be safe
                this.wait();
            }catch (InterruptedException e){
                fileLog.error(e.getMessage());
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
        }
    }

    @Override
    public void setUserToken(String tokenA) {
        this.token = tokenA;
    }

    @Override
    public String getToken() {
        return token;
    }

    /**setReceivedResponse signals whether a response has been received. If so, it stops the waits.*/
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
            }
        }
    }


    /**todo*/
    @Override
    public void setPing(boolean b) {
        this.syn = b;
    }

    @Override
    public void close() {
        //todo
    }

    public void setResponseDecoder(ResponseDecoder responseDecoder) {
        this.responseDecoder = responseDecoder;
    }

    @Override
    public boolean isConnected() {
        return amIconnected;
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
                //maybe this doesn't need 'this', but since it's a thread it's better to be safe
                this.wait();
            }catch (InterruptedException e){
                fileLog.error(e.getMessage());
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

    /**sendPing method generates a ping request.
     * @param token - token used to identify the client.*/
    @Override
    public void sendPing(String token) {
        setReceivedResponse(true);
        request(new PingRequest(token));
    }

    /**quit method used to quit the game.
     * @param token - token used to identify the client.*/
    @Override
    public void quit(String token) {
        setReceivedResponse(true);
        request(new QuitRequest(token));
    }

    /**setCheckTimer method resets the timer or creates one if there isn't any*/
    @Override
    public void setCheckTimer(boolean b) {
        if(b){
            checkTimer = new Timer();
            checkTimer.scheduleAtFixedRate(new ConnectionClientTimer(this), synCheckTime, synCheckTime);
        }
        else{
            checkTimer.purge();
            checkTimer.cancel();
        }
    }

    /**sets the syn boolean which signals whether the server is still reachable*/
    public boolean isSyn() {
        return syn;
    }

}
