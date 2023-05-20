package it.polimi.ingsw.client;

import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.view.View;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;


public class ClientRMI implements IClientConnection, Remote, Serializable {
    private String username;
    private ClientController master;
    private final IRemoteController remoteController;
    private View viewClient;
    private String userToken;

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
            remoteController.createWaitingRoom(username, userToken, number);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void setName(String name) {
        this.username=name;
    }

    public void setViewClient(View currentView) {
        this.viewClient = currentView;
    }

    @Override
    public void chooseTiles(String token, String tilesChosen) {
        try {
            System.out.println("clientrmi tiles: "+tilesChosen+" for token "+token);
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
}
