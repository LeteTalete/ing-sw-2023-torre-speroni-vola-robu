package it.polimi.ingsw.client;

import it.polimi.ingsw.server.NetworkManager;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIConnectionHandler implements ConnectionHandler {
    public RMIConnectionHandler() {
    }

    private NetworkManager stubLogin;
    private NetworkManager stubHowMany;
    private Registry registry;
    @Override
    public String setupConnection() {
        try{
            this.registry = LocateRegistry.getRegistry(8089);
            this.stubLogin = (NetworkManager) registry.lookup("Login");
            this.stubHowMany = (NetworkManager) registry.lookup("howMany");
        }catch(Exception e){
            return e.toString();
        }
        return null;
    }

    @Override
    public String Login(String name) {
        String success;
        try {
            //this fails because I can't seem to pass the clientHandler as an argument.
            //how can i solve?
            success = stubLogin.registerUser(name);
        } catch (RemoteException e) {
            return "Failed: remote exception";
        }
        return success;
    }

    @Override
    public String howMany(int num, String name){
        String success;
        try{
            success = stubHowMany.howMany(num, name);
        }catch (RemoteException e){
            return "Failed: remote exception";
        }
        return success;
    }
}
