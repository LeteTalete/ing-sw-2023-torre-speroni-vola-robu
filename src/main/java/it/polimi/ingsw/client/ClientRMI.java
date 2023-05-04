package it.polimi.ingsw.client;

import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.view.View;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;


public class ClientRMI implements IClientConnection, Remote, Serializable {
    private ClientController master;
    private final IRemoteController remoteController;
    private View viewClient;
    public ClientRMI(ClientController clientHandler, IRemoteController rc) {
        this.master = clientHandler;
        this.remoteController = rc;
    }

    @Override
    public String login(String name) {
        String success = null;
        try {
            //needs the view to implement getListener method
            success = remoteController.login(name, viewClient.getListener());

        } catch (RemoteException e) {
            viewClient.printError(e.getMessage());
        }
        return success;
    }



    public void setViewClient(View currentView) {
        this.viewClient = currentView;
    }
}
