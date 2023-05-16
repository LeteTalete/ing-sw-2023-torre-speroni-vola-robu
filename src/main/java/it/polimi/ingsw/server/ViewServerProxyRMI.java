package it.polimi.ingsw.server;

import it.polimi.ingsw.network.IClientListener;

import java.io.Serializable;
import java.rmi.RemoteException;

//proxy pattern
public class ViewServerProxyRMI implements Serializable {
    private final ConnectionManager connectionManager;
    private final IClientListener viewListener;
    private final String username;

    public ViewServerProxyRMI(IClientListener viewL, String name){
        this.connectionManager = ConnectionManager.get();
        this.viewListener = viewL;
        this.username = name;
    }

    public String getUsername() {return username;}

    public void processLogin(String message){
        try{
            viewListener.sendNotification(message);
        }catch(RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    public int askHowMany() {
        int number;
        try {
            number = viewListener.askHowMany();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return number;
    }

    public void sendNotification(String message){
        try {
            viewListener.sendNotification(message);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
