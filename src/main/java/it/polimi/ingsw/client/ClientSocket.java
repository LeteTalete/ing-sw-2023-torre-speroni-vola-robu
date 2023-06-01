package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.notifications.ChatMessage;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientSocket implements IClientConnection
{
    private static Logger fileLog = LogManager.getRootLogger();

    private final ClientController master;
    private String username;
    private String token;
    private String ip;
    private int port;
    private View viewClient;
    private Socket socket;
    private ObjectInputStream socketIn;
    private  ObjectOutputStream socketOut;
    private Scanner stdin;
    private boolean amIconnected;
    private Thread receiving;
    private ResponseDecoder responseDecoder;
    private boolean notReceivingResponse;


    public ClientSocket(String ip, int port, ClientController cController) throws IOException
    {
        this.ip = ip;
        this.port = port;
        this.master = cController;
        this.notReceivingResponse = true;
    }

    public void openStreams()
    {
        try
        {
            socket = new Socket(ip, port);
            this.socketOut = new ObjectOutputStream(socket.getOutputStream());
            this.socketIn = new ObjectInputStream(socket.getInputStream());
            amIconnected= true;
            startReceiving();
        }
        catch(IOException e)
        {
            fileLog.error(e.getMessage());
        }
    }

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

    public void startClient()
    {
        openStreams();
        System.out.println(">> Connection established");
        //if the connection is successful i can use socket's streams to communicate with the server
        master.userLogin();
    }

    public void closeStreams() throws IOException
    {
        //closing streams
        stdin.close();
        socketIn.close();
        socketOut.close();
    }

    @Override
    public void setName(String name) {
        this.username=name;
    }

    @Override
    public void setViewClient(View currentView)
    {
        this.viewClient = currentView;
    }

    @Override
    public synchronized void chooseTiles(String token, List<String> tilesChosen)
    {
        setReceivedResponse(true);
        request(new ChooseTilesRequest(token, tilesChosen));
        while(notReceivingResponse){
            try
            {
                this.wait();
            }
            catch (InterruptedException e)
            {
                fileLog.error(e.getMessage());
            }
        }
        fileLog.debug("I received a response on choose tiles");
    }

    @Override
    public synchronized void login(String name)
    {
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
        fileLog.debug("I received a response on login");
    }

    private void request(Request request) {
        System.out.println("I'm sending a request");
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
    public void setReceivedResponse(boolean b) {
        notReceivingResponse = b;
    }

    @Override
    public synchronized void numberOfPlayers(String name, String tokenA, int number) {
        fileLog.debug("numberOfPlayers");
        setUserToken(tokenA);
        setName(name);
        setReceivedResponse(true);
        fileLog.debug("Sending request for waiting room");
        request(new WaitingRoomRequest(tokenA, name, number));
        while(notReceivingResponse){
            try{
                //maybe this doesn't need 'this', but since it's a thread it's better to be safe
                this.wait();
            }catch (InterruptedException e){
                fileLog.error(e.getMessage());
            }
        }
        fileLog.debug("I received a response on number of player");
    }

    @Override
    public void chooseColumn(int column) {
        setReceivedResponse(true);
        request(new ColumnRequest(token, column));
        while(notReceivingResponse){
            try{
                //maybe this doesn't need 'this', but since it's a thread it's better to be safe
                this.wait();
            }catch (InterruptedException e){
                fileLog.error(e.getMessage());
            }
        }
        fileLog.debug("I received a response on choose column");
    }


    @Override
    public void setSynCheckTimer(boolean b) {

    }

    @Override
    public void close() {

    }

    public void setResponseDecoder(ResponseDecoder responseDecoder) {
        this.responseDecoder = responseDecoder;
    }

    @Override
    public boolean isConnected() {
        return amIconnected;
    }

    @Override
    public void rearrangeTiles(String userToken, List<String> multipleChoiceNumber) {
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
        fileLog.debug("I received a response on rearrange tiles");
    }

    @Override
    public void passTiles(ArrayList<Position> tilesChosen) {
        master.passTiles(tilesChosen);
    }

    @Override
    public void sendChat(ChatMessageRequest message) {
        setReceivedResponse(true);
        request(message);
    }


}
