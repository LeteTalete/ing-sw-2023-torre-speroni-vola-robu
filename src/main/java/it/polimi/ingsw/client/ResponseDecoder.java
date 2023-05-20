package it.polimi.ingsw.client;

import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.responses.*;

import java.rmi.RemoteException;

public class ResponseDecoder implements ResponseHandler {
    private final IClientListener clientListener;
    private final IClientConnection client;

    public ResponseDecoder(IClientListener clientListener, IClientConnection clientC) {
        this.clientListener = clientListener;
        this.client = clientC;
    }

    @Override
    public void handle(GetTilesResponse getTilesResponse) throws RemoteException {

    }

    @Override
    public void handle(Pinged pinged) throws RemoteException {

    }

    @Override
    public void handle(LoginResponse loginResponse) throws RemoteException {
        System.out.println("i'm about to handle reply");
        clientListener.notifySuccessfulRegistration(loginResponse.name, loginResponse.b, loginResponse.token, loginResponse.first);
        client.setReceivedResponse(false);
        synchronized (client) {
            client.notifyAll();
        }
    }

}
