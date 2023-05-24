package it.polimi.ingsw.client;

import it.polimi.ingsw.requests.ChooseTilesRequest;
import it.polimi.ingsw.requests.Request;
import it.polimi.ingsw.requests.WaitingRoomRequest;
import it.polimi.ingsw.requests.loginRequest;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientSocket implements IClientConnection
{
    private final ClientController master;
    private String username;
    private String token;
    private String ip;
    private int port;
    private View viewClient;
    private Socket socket;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private Scanner stdin;
    private Thread receiving;
    private ResponseDecoder responseDecoder;
    private boolean notReceivingResponse;


    public ClientSocket(String ip, int port, ClientController cController)
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
            startReceiving();
            //this.stdin = new Scanner(System.in);
        }
        catch(IOException e)
        {
            System.err.println(e.getMessage());
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
                                viewClient.printError(e.getMessage());
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
            throw new RuntimeException("Class does not exist: "+ e.getMessage());
        }catch (IOException e){
            System.out.println("Bad formatting");
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
    public synchronized void chooseTiles(String token, String tilesChosen)
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
                viewClient.printError(e.getMessage());
            }
        }
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
                viewClient.printError(e.getMessage());
            }
        }

    }

    private void request(Request request) {
        try{
            socketOut.writeObject(request);
            //maybe this is flush() but we need to test it out
            socketOut.reset();
        }catch (IOException e){
            viewClient.printError(e.getMessage());
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
        setUserToken(tokenA);
        setName(name);
        setReceivedResponse(true);
        request(new WaitingRoomRequest(tokenA, name, number));
        while(notReceivingResponse){
            try{
                //maybe this doesn't need 'this', but since it's a thread it's better to be safe
                this.wait();

            }catch (InterruptedException e){
                viewClient.printError(e.getMessage());
            }
        }
    }

    @Override
    public void chooseColumn(int column) {

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
        return false;
    }

}
