package it.polimi.ingsw.client;

import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public class ClientRMI implements IClientConnection, Remote, Serializable {
    private String username;
    private ClientController master;
    private final IRemoteController remoteController;
    private View viewClient;
    private String userToken;
    private ResponseDecoder responseDecoder;
    private boolean isConnected;

    public ClientRMI(ClientController clientHandler, IRemoteController rc) {
        this.master = clientHandler;
        this.remoteController = rc;
    }

    @Override
    public void login(String name) {
        try {
            //needs the view to implement getListener method
            remoteController.login(name, viewClient.getListener());

        } catch (RemoteException e) {
            viewClient.printError(e.getMessage());
        }
    }

    @Override
    public void setUserToken(String token) {
        this.userToken = token;
    }

    @Override
    public void setReceivedResponse(boolean b) {
        //unused in rmi
    }

    @Override
    public void numberOfPlayers(String name, String token, int number) {
        try {
            remoteController.createWaitingRoom(name, userToken, number);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseColumn(int column) {
        try {
            remoteController.selectColumn(userToken, column);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void setSynCheckTimer(boolean b) {

    }

    @Override
    public void close() {
        System.out.println(System.getProperty("line.separator") + "Quit.");
        System.exit(0);
    }

    @Override
    public void setResponseDecoder(ResponseDecoder responseDecoder) {
        this.responseDecoder = responseDecoder;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }


    @Override
    public void setName(String name) {
        this.username=name;
    }

    public void setViewClient(View currentView) {
        this.viewClient = currentView;
    }

    @Override
    public void chooseTiles(String token, List<String> tilesChosen) {
        try {
            remoteController.pickedTiles(token, tilesChosen);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return username;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
