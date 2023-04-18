package it.polimi.ingsw.server;

import it.polimi.ingsw.client.RMIConnectionHandler;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class NetworkManagerRMI implements NetworkManager {
    private ServerManager master;
    private List<RMIConnectionHandler> RMIClients;
    public NetworkManagerRMI(ServerManager server) throws RemoteException, AlreadyBoundException {
        master = server;

        //port 0 means it will pick a random port available
        //System.setProperty("java.rmi.server.hostname","192.168.0.110");
        NetworkManager stub = (NetworkManager) UnicastRemoteObject.exportObject(this, 8089);
        Registry registry = LocateRegistry.createRegistry(8089);
        registry.bind("Login",stub);
        registry.bind("howMany", stub);
    }

    @Override
    public String registerUser(String name) {
        return master.registerUser(name);
    }

    @Override
    public String howMany(int num, String name) throws RemoteException {
        return master.createWaitingRoom(name, num);
    }


}
