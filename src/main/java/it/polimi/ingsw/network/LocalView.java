package it.polimi.ingsw.network;

import it.polimi.ingsw.server.ConnectionManager;

import java.io.Serializable;
import java.rmi.RemoteException;

//proxy pattern
public class LocalView implements Serializable {
    private final ConnectionManager connectionManager;
    private final IListener viewListener;
    private final String username;

    public LocalView(IListener viewL, String name){
        this.connectionManager = ConnectionManager.get();
        this.viewListener = viewL;
        this.username = name;
    }

    public String getUsername() {return username;}

    public void processLogin(String message){
        try{
            viewListener.processLogin(message);
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

    public void sendNotification(String gameStart) {
        viewListener.notification(gameStart);
    }
}
